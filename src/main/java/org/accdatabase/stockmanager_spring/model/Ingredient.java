package org.accdatabase.stockmanager_spring.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.accdatabase.stockmanager_spring.model.MoveType.inComing;
import static org.accdatabase.stockmanager_spring.model.MoveType.outComing;


@AllArgsConstructor@NoArgsConstructor@Getter@Setter
public class Ingredient {
    private String ingredientId;
    private String name;
    private LocalDateTime updateDatetime;
    private List<Price> prices = new ArrayList<>();
    private List<StockMove> stockMoves = new ArrayList<>();


    public Price getPriceAtDate(LocalDate date){
        return this.prices.stream().filter(price -> price.getDate().equals(date)).findAny().orElseThrow(()-> new RuntimeException(" date fopla"));
    }
    public Price getPriceAtDate() {
        return this.prices.stream()
                .max(Comparator.comparing(Price::getDate))
                .orElse(null); // or throw an exception if empty
    }


    public StockMove getStockQuantityAt(LocalDateTime date){
        List<StockMove> incomingStockMoves = stockMoves.stream().filter(stockMove -> stockMove.getMoveType().equals(MoveType.inComing) & stockMove.getMoveDate() == date).toList();
        List<StockMove> outComingStockMoves = stockMoves.stream().filter(stockMove -> stockMove.getMoveType().equals(outComing) & stockMove.getMoveDate() == date).toList();
        double incomingStockIngredient = incomingStockMoves.stream().mapToDouble(stockmove -> stockmove.getQuantity()).sum();
        double outcomingStockIngredient = outComingStockMoves.stream().mapToDouble(stockmove -> stockmove.getQuantity()).sum();
        double totalStockIngredient = incomingStockIngredient - outcomingStockIngredient;
        StockMove stockQuantity = new StockMove();
        stockQuantity.setQuantity(totalStockIngredient);
        stockQuantity.setMoveDate(date);

        return stockQuantity;
    }
    public StockMove getStockQuantityAt(){
        List<StockMove> incomingStockMoves = stockMoves.stream().filter(stockMove -> stockMove.getMoveType().equals(MoveType.inComing) & stockMove.getMoveDate() == LocalDateTime.now()).toList();
        List<StockMove> outComingStockMoves = stockMoves.stream().filter(stockMove -> stockMove.getMoveType().equals(outComing) & stockMove.getMoveDate() == LocalDateTime.now()).toList();
        double incomingStockIngredient = incomingStockMoves.stream().mapToDouble(stockmove -> stockmove.getQuantity()).sum();
        double outcomingStockIngredient = outComingStockMoves.stream().mapToDouble(stockmove -> stockmove.getQuantity()).sum();
        double totalStockIngredient = incomingStockIngredient - outcomingStockIngredient;
        StockMove stockQuantity = new StockMove();
        stockQuantity.setQuantity(totalStockIngredient);
        //stockQuantity.setMoveDate();
       // stockQuantity.setUnit(this.getPriceAtDate().getUnit());
        //System.out.println("stock :" + stockQuantity);

        return stockQuantity;
    }

    public Double getAvailableQuantityAt(LocalDateTime datetime) {
        System.out.println("stockmoves : "+stockMoves );
        List<StockMove> stockMovementsBeforeToday = stockMoves.stream()
                .filter(stockMovement ->
                        stockMovement.getMoveDate().isBefore(datetime)
                                || stockMovement.getMoveDate().equals(datetime))
                .toList();
        double quantity = 0;
        for (StockMove stockMovement : stockMovementsBeforeToday) {
            if (inComing.equals(stockMovement.getMoveType())) {
                quantity += stockMovement.getQuantity();
            } else if (outComing.equals(stockMovement.getMoveType())) {
                quantity -= stockMovement.getQuantity();
            }
        }
        return quantity;
    } 



    @Override
    public String toString() {
        return
                " -Id :'" + ingredientId + '\n' +
                        "name : " + name + '\n' +
                        "updateDatetime : " + updateDatetime +
                        "\nprices=" + prices ;
    }

    public List<Price> addPrices(List<Price> prices) {
        prices.forEach(price -> price.setIngredient(this));
        if (getPrices() == null || getPrices().isEmpty()) {
            setPrices(prices);
            return prices;
        }
        getPrices().addAll(prices);
        return getPrices();
    }

    public List<StockMove> addStockMovements(List<StockMove> stockMovements) {
        stockMovements.forEach(stockMovement -> stockMovement.setIngredient(this));
        if (getStockMoves() == null || getStockMoves().isEmpty()) {
            setStockMoves(stockMovements);
            return stockMovements;
        }
        getStockMoves().addAll(stockMovements);
        return getStockMoves();
    }
}
