package cat.gencat.agaur.hexastock.model.Strategy;

import cat.gencat.agaur.hexastock.model.Lot;
import cat.gencat.agaur.hexastock.model.LotSelectionPolicy;
import cat.gencat.agaur.hexastock.model.SellResult;

import java.math.BigDecimal;
import java.util.List;


public interface SellStrategy {
    SellResult sell(List<Lot> lots, int quantity, BigDecimal sellPrice);
}
