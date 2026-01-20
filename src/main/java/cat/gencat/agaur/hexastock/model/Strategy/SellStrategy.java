package cat.gencat.agaur.hexastock.model.Strategy;

import cat.gencat.agaur.hexastock.model.Lot;
import cat.gencat.agaur.hexastock.model.SellResult;
import java.util.List;


public interface SellStrategy {
    SellResult sell(List<Lot> lots, int quantityToSell);
}
