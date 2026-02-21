package week2.intro;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class RegexExercise {

    // sõnad "kalad" või "jalad".
    public static final String RE1 = "kalad|jalad";

    // Viietähelised sõned, mis lõppevad tähtedega "alad".
    public static final String RE2 = ".alad";

    // color ja colour
    public static final String RE3 = "colo(u|)r";

    // jaha, jahaaaaaaaaa!
    public static final String RE4 = "j(a+)h(a+)";

    // binaarsõned
    public static final String RE5 = "^[01]*$";

    // eelviimane täht on "a"
    public static final String RE6 = "^[ab]*a[ab]$";

    // tagasiviited
    public static final String RE7 = "^(..)\\1$";

    // nimede asendamine: regulaaravaldis
    public static final String RE8 = "([A-Z][a-z]+)\s([A-Z][a-z]+)";
    // millega asendada? (Util.replace'i teine argument)
    public static final String RP8 = "$2, $1";

    // Eemaldada sulud!
    public static final String RE9 = " \\(.*?\\)";
    // Siin asendatakse regexiga sobituvad juppid tühja sõnega.



    // Testimise meetod, et saaks natuke debuugida, muidu on ka automaattestid.

    static void main() throws IOException {
        List<String> strings = Files.readAllLines(Paths.get("inputs", "regex.txt"));
        printLines(RegexUtils.grep(RE1, strings));
    }

    private static void printLines(List<String> strings) {
        for (String s : strings) System.out.println(s);
    }

}
