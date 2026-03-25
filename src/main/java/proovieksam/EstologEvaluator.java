package proovieksam;

import proovieksam.ast.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.NoSuchElementException;

import static proovieksam.ast.EstologNode.*;

public class EstologEvaluator {

    HashMap<String, Boolean> muutujad = new HashMap<>();

    public static boolean eval(EstologProg prog) {
        EstologEvaluator estologEvaluator = new EstologEvaluator();
        return estologEvaluator.evalNode(prog);
    }

    private boolean evalNode(EstologNode node) {
        return switch (node) {
            case EstologLiteraal(boolean value) -> value;

            case EstologKui(EstologNode kui, EstologNode siis, EstologNode muidu) ->
                    evalNode(kui) ? evalNode(siis) : evalNode(muidu);

            case EstologBinOp estologBinOp -> switch (estologBinOp) {
                case EstologJa(EstologNode left, EstologNode right) ->
                        evalNode(left) && evalNode(right);
                case EstologVoi(EstologNode left, EstologNode right) ->
                        evalNode(left) || evalNode(right);
                case EstologVordus(EstologNode left, EstologNode right) ->
                        evalNode(left) == evalNode(right);
            };

            case EstologMuutuja(String nimi) -> {
                try {
                    yield muutujad.get(nimi);
                } catch (Exception e) {
                    throw new NoSuchElementException("seda muutujat pole defineeritud");
                }
            }

            case EstologDef(String nimi, EstologNode avaldis) -> {
                muutujad.put(nimi, evalNode(avaldis));
                yield false;
            }

            case EstologProg(EstologNode avaldis, java.util.List<EstologDef> defs) -> {
                for (var def : defs) {
                    evalNode(def);
                }
                yield evalNode(avaldis);
            }
        };
    }

    static void main() {
        EstologProg prog = prog(
                kui(vordus(var("x"), var("y")), var("a"), var("b")),

                def("x", lit(false)),
                def("y", lit(true)),
                def("a", ja(var("x"), var("y"))),
                def("b", voi(var("x"), var("y")))
        );

        System.out.println(prog);
        System.out.println(eval(prog));
    }
}
