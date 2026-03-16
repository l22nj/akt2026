package week4.baselangs.rnd;

import com.google.common.collect.Sets;
import week4.baselangs.rnd.ast.*;

import java.util.*;

public class RndMaster {
    // Nüüd tagastada kõik võimalikud tulemusväärtused hulgana.
    public static Set<Integer> evalNondeterministic(RndNode node) {
        return switch (node) {
            case RndNum num -> Set.of(num.value());
            case RndFlip flip -> Sets.union(
                    evalNondeterministic(flip.left()),
                    evalNondeterministic(flip.right())
            );
            case RndNeg neg -> {
                var result = new HashSet<Integer>();
                for (var value : evalNondeterministic(neg.expr())) {
                    result.add(-value);
                }
                yield result;
            }
            case RndAdd add -> {
                var result = new HashSet<Integer>();
                var leftValues = evalNondeterministic(add.left());
                var rightValues = evalNondeterministic(add.right());
                for (var left : leftValues) {
                    for (var right : rightValues) {
                        result.add(left + right);
                    }
                }
                yield result;
            }
        };
    }

    // Tagastada avaldise võimalike tulemuste tõenäosusjaotus.
    public static Map<Integer, Double> evalDistribution(RndNode node) {
        throw new UnsupportedOperationException();
    }
}
