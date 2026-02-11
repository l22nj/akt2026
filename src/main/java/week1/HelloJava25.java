package week1;

import java.util.List;

public class HelloJava25 {

    static void main() { // Java 25
        List<Object> objs = List.of("Tere", false, "Java", 25, "hüüumärk"); // Java 9
        System.out.println(objs);

        int answer = objs.stream() // Java 8
                .mapToInt(HelloJava25::convert)
                .sum();
        System.out.println(answer);
    }

    private static int convert(Object obj) {
        return switch (obj) { // Java 16
            case String str -> // Java 21
                switch (str) { // Java 16
                    case "Tere" -> -8;
                    case "Java" -> 21;
                    default -> str.length();
                };
            case Integer i -> i; // Java 21
            case Boolean _ -> -4; // Java 22
            default -> 0;
        };
    }
}
