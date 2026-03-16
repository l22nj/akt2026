package week4.baselangs.bool;

import week4.baselangs.bool.ast.*;

import java.util.Set;

public class BoolEvaluator {

    // Väärtustada tõeväärtusavaldis, kui ette antud on tõeste muutujate hulk.
    public static boolean eval(BoolNode node, Set<Character> tv) {
        return switch (node) {
            case BoolVar var -> tv.contains(var.name());
            case BoolNot not -> !eval(not.exp(), tv);
            case BoolOr or -> eval(or.left(), tv) || eval(or.right(), tv);
            case BoolImp imp -> !eval(imp.antedecent(), tv) || eval(imp.consequent(), tv);
        };
    }
}
