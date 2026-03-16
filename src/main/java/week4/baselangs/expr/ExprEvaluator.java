package week4.baselangs.expr;

import com.google.common.collect.Sets;
import week4.baselangs.expr.ast.*;

import java.util.HashSet;
import java.util.Set;

public class ExprEvaluator {

    // Esiteks implementeeri ExprNode alamklassides eval meetodid õigesti.
    // Nende testimiseks on ExprEvaluatorTest-is testEvalNodeMethod.

    // Mõnikord aga me ei taha/saa muuta AST klasse.
    // Seega vaatame mitmeid alternatiive, kuidas AST-i töödelda.

    // Klassikaline viis on kasutada instanceof-i.

    /**
     * Väärtustab avaldise.
     */
    public static int evalInstanceof(ExprNode node) {
        // Paranda siin üks vigane juht.
        if (node instanceof ExprNum num) {
            return num.value();
        } else if (node instanceof ExprNeg neg) {
            return -evalInstanceof(neg.expr());
        } else if (node instanceof ExprAdd add) {
            return evalInstanceof(add.left()) + evalInstanceof(add.right());
        } else if (node instanceof ExprDiv div) {
            return evalInstanceof(div.numerator()) / evalInstanceof(div.denominator());
        } else {
            // Siia ei peaks kunagi jõudma (v.a. null), aga kompilaator seda ei tea.
            throw new IllegalArgumentException();
        }
    }
    // (Tegelikult oleme siin kasutanud juba Java 16 instanceof mustri võimalust. Ilma selleta oleks veel kohmakam.)
    // Seejärel proovi evalInstanceof meetodis ka IntelliJ soovitusi rakendada.


    // Kui neid soovitusi omavahel kombineerida, siis saame kasutada uuemaid Java 21 võimalusi:
    // kirjed (record-id), mustrisobitus (pattern matching) ja kinnised (sealed) liidesed/klassid.

    /**
     * Väärtustab avaldise.
     * Kasutab uuemaid Java 21 võimalusi.
     */
    public static int eval(ExprNode node) {
        // Paranda siin ka vigane juht.
        return switch (node) {
            // Kirjete puhul saab kasutada mustrisobitust, et panna vastavad väljad kohe lokaalsetesse muutujatesse.
            case ExprNum(int value) -> value;
            case ExprNeg(ExprNode expr) -> -eval(expr);
            case ExprAdd(ExprNode left, ExprNode right) -> eval(left) + eval(right);
            case ExprDiv(ExprNode numerator, ExprNode denominator) -> eval(numerator) / eval(denominator);
            case null -> throw new IllegalArgumentException("");
            // Kuna ExprNode on kinnine, siis kompilaator teab, et default juhtu pole vaja.
        };
    }

    // Nagu näha, siis need võimalused oluliselt lihtsustavad avaldispuude defineerimist ja nende väärtustamist.


    /**
     * Kogub kokku kõik tippudes esinevad arvud.
     */
    public static Set<Integer> getAllValues(ExprNode node) {
        return switch (node) {
            case ExprNum exprNum -> Set.of(exprNum.value());
            case ExprNeg exprNeg -> getAllValues(exprNeg.expr());
            case ExprAdd exprAdd -> Sets.union(getAllValues(exprAdd.left()), getAllValues(exprAdd.right()));
            case ExprDiv exprDiv -> Sets.union(getAllValues(exprDiv.numerator()), getAllValues(exprDiv.denominator()));
            case null -> throw new IllegalArgumentException("");
        };
    }


    /**
     * Kogub kokku kõik tippudes esinevad arvud.
     * Väldib koodi kordusi kasutades abiklassi isendit ja imperatiivset stiili.
     */
    public static Set<Integer> getAllValuesImperative(ExprNode node) {
        ValueCollector valueCollector = new ValueCollector();
        valueCollector.addAllValues(node);
        return valueCollector.result;
    }

    private static class ValueCollector {
        private final Set<Integer> result = new HashSet<>();

        private void addAllValues(ExprNode node) {
            switch (node) {
                case ExprNum(int value) -> result.add(value);
                case ExprNeg(ExprNode expr) -> addAllValues(expr);
                case ExprAdd(ExprNode left, ExprNode right) -> {
                    addAllValues(left);
                    addAllValues(right);
                }
                case ExprDiv(ExprNode numerator, ExprNode denominator) -> {
                    addAllValues(numerator);
                    addAllValues(denominator);
                }
            }
        }
    }
}
