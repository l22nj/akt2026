package week2;

import week2.intro.RegexUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextAnalyzer {
    String text;

    // Sõned, mis lõppevad tähtedega "ed" või mõni täht veel.
    // (Ülesanne lehel on pikemalt seletatud!)
    public static final String RE1 = ".*(ed)(.|)$";

    // Paaritu pikkusega sõned.
    public static final String RE2 = "^(..)*.$";

    // Sõned, mille esimene ja viimane täht on sama!
    public static final String RE3 = "^(.)((.*\\1)|)$";

    // Sõned, mis ülesanne nimede tingimustele vastavad.
    public static final String NAME = "([A-Z][a-z]* [A-Z][a-z]*)";

    // Sõned, mis ülesanne numbri tingimustele vastavad.
    public static final String NUMBER = "((\\d{3,4}(-|\\s)\\d{3,4})|\\d{4,8})";

    static String saaPuhasNumber(String nr) {
        return nr.replaceAll("-|\\s", "");
    }

    public TextAnalyzer(String text) {
        this.text = text;
    }

    public Map<String, String> getPhoneNumbers() {
        Pattern pattern = Pattern.compile(NAME + ".*?" + NUMBER);
        Matcher matcher = pattern.matcher(text);
        Map<String, String> tulemus = new HashMap<>();
        if (!matcher.find()) return tulemus;
        do {
            String[] paar = matcher.group().replaceAll(pattern.pattern(),"$1;$2").split(";");
            tulemus.put(paar[0], saaPuhasNumber(paar[1]));
        } while (matcher.find(matcher.start()+1));
        return tulemus;
    }

    // Isikukoodi ei tunne ära
    public String anonymize() {
        return text.replaceAll(NAME, "<nimi>").replaceAll(NUMBER, "<telefoninumber>");
    }


    static void main() {

        String input =
                """
                        Mina olen Kalle Kulbok ja mu telefoninumber on 5556 4272.
                        Mina olen Peeter Peet ja mu telefoninumber on 5234 567.
                        Mari Maasikas siin, mu number on 6723 3434. Tere, olen Jaan Jubin numbriga 45631643.""";

        TextAnalyzer ta = new TextAnalyzer(input);
        Map<String, String> phoneBook = ta.getPhoneNumbers();
        System.out.println(phoneBook.get("Peeter Peet")); // peab väljastama 5234567
        System.out.println(phoneBook.get("Jaan Jubin"));  // peab väljastama 45631643

        System.out.println(ta.anonymize());

        /* peab väljastama:
            Mina olen <nimi> ja mu telefoninumber on <telefoninumber>.
            Mina olen <nimi> ja mu telefoninumber on <telefoninumber>.
            <nimi> siin, mu number on <telefoninumber>. Tere, olen <nimi> numbriga <telefoninumber>.
        */
    }
}
