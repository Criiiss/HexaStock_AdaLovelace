package cat.gencat.agaur.hexastock.model.Strategy;

import cat.gencat.agaur.hexastock.model.Lot;
import cat.gencat.agaur.hexastock.model.LotSelectionPolicy;
import cat.gencat.agaur.hexastock.model.SellResult;
import cat.gencat.agaur.hexastock.model.exception.ConflictQuantityException;

import java.math.BigDecimal;
import java.util.List;

public class FIFOSellStrategy implements SellStrategy {

    @Override
    public SellResult sell(List<Lot> lots, int quantity, BigDecimal sellPrice) {
        int available = lots.stream().mapToInt(Lot::getRemaining).sum();

        int remainingToSell = quantity;
        BigDecimal costBasis = BigDecimal.ZERO;

        for (var lot : lots) {
            if (remainingToSell <= 0) break;

            int sharesSoldFromLot = Math.min(lot.getRemaining(), remainingToSell);
            BigDecimal lotCostBasis = lot.getUnitPrice().multiply(BigDecimal.valueOf(sharesSoldFromLot));

            costBasis = costBasis.add(lotCostBasis);
            lot.reduce(sharesSoldFromLot);
            remainingToSell -= sharesSoldFromLot;
        }

        // Remove lots with zero remaining after selling
        lots.removeIf(Lot::isEmpty);

        BigDecimal proceeds = sellPrice.multiply(BigDecimal.valueOf(quantity));
        BigDecimal profit = proceeds.subtract(costBasis);

        return new SellResult(proceeds, costBasis, profit);
    }

    //factory clas private

}