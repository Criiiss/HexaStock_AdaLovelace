package cat.gencat.agaur.hexastock.model.Strategy;

import cat.gencat.agaur.hexastock.model.Lot;
import cat.gencat.agaur.hexastock.model.LotSelectionPolicy;
import cat.gencat.agaur.hexastock.model.SellResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HIFOSellStrategy implements SellStrategy {

    @Override
    public SellResult sell(List<Lot> lots, int quantity, BigDecimal sellPrice) {
        Objects.requireNonNull(lots);

        int remainingToSell = quantity;
        BigDecimal proceeds = BigDecimal.ZERO;
        BigDecimal costBasis = BigDecimal.ZERO;

        // HIFO: orden descendiente precio

        List<Lot> copy = new ArrayList<>(lots);
        copy.sort(Comparator.comparing(Lot::getUnitPrice).reversed()
                .thenComparing(Lot::getPurchasedAt));

        for (Lot lot : copy) {
            if (remainingToSell <= 0) break;
            int available = lot.getRemaining();
            if (available <= 0) continue;

            int taken = Math.min(available, remainingToSell);
            lot.reduce(taken);

            proceeds = proceeds.add(sellPrice.multiply(BigDecimal.valueOf(taken)));
            costBasis = costBasis.add(lot.getUnitPrice().multiply(BigDecimal.valueOf(taken)));

            remainingToSell -= taken;
        }

        BigDecimal profit = proceeds.subtract(costBasis);
        return new SellResult(proceeds, costBasis, profit);
    }
}
