package week10.Kala;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static week6.kalaparser.KalaNode.*;

public sealed interface KalaNode permits KalaIdent, KalaList, KalaNull {

    int sum(Map<String, Integer> env);

    static KalaNode mkNull() {
        return new KalaNull();
    }

    static KalaNode mkIdent(String name) {
        return new KalaIdent(name);
    }

    static KalaNode mkList(List<KalaNode> args) {
        return new KalaList(args);
    }

    static KalaNode mkList(KalaNode... args) {
        return mkList(Arrays.asList(args));
    }


    // Siit edasi on siis lahendus ette antud, et selle peale mitte jälle liiga palju aega kulutada.
    // Nende peidetud "sisemiste" klasside asemel võid kodutöös teha täiesti eraldi avalikud klassid!

    record KalaIdent(String name) implements KalaNode {

        @Override
        public String toString() {
            return name;
        }

        @Override
        public int sum(Map<String, Integer> env) {
            return env.get(name);
        }
    }

    record KalaList(List<KalaNode> args) implements KalaNode {

        @Override
        public String toString() {
            StringJoiner joiner = new StringJoiner(", ", "(", ")");
            for (KalaNode arg : args) joiner.add(arg.toString());
            return joiner.toString();
        }

        @Override
        public int sum(Map<String, Integer> env) {
            return args.stream().mapToInt(n -> n.sum(env)).sum();
        }
    }

    record KalaNull() implements KalaNode {

        @Override
        public String toString() {
            return "NULL";
        }

        @Override
        public int sum(Map<String, Integer> env) {
            return 0;
        }
    }
}
