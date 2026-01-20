package cat.gencat.agaur.hexastock.model.Strategy;

import cat.gencat.agaur.hexastock.model.Lot;
import cat.gencat.agaur.hexastock.model.SellResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class LIFOSellStrategy implements SellStrategy {

    @Override
    public SellResult sell(List<Lot> lots, int quantity, BigDecimal sellPrice) {

        Objects.requireNonNull(lots);
        if (quantity <= 0) throw new IllegalArgumentException("Quantity can't be negative");

        int remaining = quantity;
        BigDecimal proceeds = BigDecimal.ZERO;
        BigDecimal costBasis = BigDecimal.ZERO;

        // LIFO: recorrido inverso
        List<Lot> copy = new ArrayList<>(lots);
        Collections.reverse(copy);

        for (Lot lot : copy) {
            if (remaining <= 0) break;
            int available = lot.getRemaining();
            if (available <= 0) continue;

            int taken = Math.min(available, remaining);
            lot.reduce(taken);

            proceeds = proceeds.add(sellPrice.multiply(BigDecimal.valueOf(taken)));
            costBasis = costBasis.add(lot.getUnitPrice().multiply(BigDecimal.valueOf(taken)));

            remaining -= taken;
        }

        BigDecimal profit = proceeds.subtract(costBasis);
        return new SellResult(proceeds, costBasis, profit);
    }
}