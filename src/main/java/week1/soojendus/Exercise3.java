package week1.soojendus;

import java.util.*;

public class Exercise3 {

    /**
     * Defineeri meetod eval, mis väärtustab etteantud avaldise.
     * @param str on plussidega eraldatud arvude jada, näiteks "5 + 35+  10".
     * @return arvude summa, antud näide puhul 50.
     */
    public static int eval(String str) {
        String[] arvudSõnedena = str.split("\\+");
        int summa = 0;
        for (String arvSõnena : arvudSõnedena) {
            int arv = Integer.parseInt(arvSõnena.strip());
            summa += arv;
        }
        return summa;
    }

    /**
     * Tuletame lihtsalt meelde Java List ja Map andmestruktuurid!
     * Selle ülesanne puhul võiks tegelikult tüüpide ja main meetodi põhjal aru saada, mida tegema peaks...
     *
     * @param list sõnedest, kus on vaheldumisi nimi ja arv (sõne kujul). Võib eeldada, et pikkus on paarisarv.
     * @return listile vastav map nimedest arvudesse.
     */
    public static Map<String, Integer> createMap(List<String> list) {
        Map<String, Integer> tulemus = new HashMap<>();
        for (int i = 0; i < list.size(); ++i) {
            if (i % 2 == 0) {
                tulemus.put(list.get(i), Integer.parseInt(list.get(i+1)));
            }
        }
        return tulemus;
    }

    static void main() {
        System.out.println(eval("2+2"));
        Map<String, Integer> ageMap = createMap(Arrays.asList("Carmen", "17", "Jürgen", "44", "Tarmo", "10", "Mari", "83"));
        System.out.println(ageMap.get("Carmen")); // vastus: 17
        System.out.println(ageMap.get("Tarmo"));  // vastu: 10
    }
}
