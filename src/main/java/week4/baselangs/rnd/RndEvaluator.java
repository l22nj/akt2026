package week4.baselangs.rnd;

import week4.baselangs.rnd.ast.*;

import java.util.function.BooleanSupplier;

public class RndEvaluator {
    // Väärtusta antud avaldist vasakult paremale, kasutades ettantud münti.
    public static int eval(RndNode node, BooleanSupplier coin) {
        return switch (node) {
            case RndNum num -> num.value();
            case RndNeg neg -> -eval(neg.expr(), coin);
            case RndAdd add -> eval(add.left(), coin) + eval(add.right(), coin);
            case RndFlip flip -> eval(coin.getAsBoolean()
                    ? flip.left()
                    : flip.right(),
                    coin);
        };
    }
}
