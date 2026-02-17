package week1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

class Abifunktsioonid {
    static Boolean kasLõpebSellega(String sõne, String lõpp) {
        return sõne.substring(sõne.length() - lõpp.length()).equals(lõpp);
    }
    public static Boolean kasAktkFail(String failinimi) {
        return kasLõpebSellega(failinimi, ".aktk");
    }
}

public class MiniAktk {

    // viimase commiti peaks parandama kui harjutuse valmis saan!
    static int väärtustaAvaldis(String avaldis, Map<String, Integer> muutujad) {
        char[] avaldisMassiiv = avaldis.strip().toCharArray();
        List<String> lekseemid = new ArrayList<>();
        String puhver = "";
        // Hiljem vaatan, kas puhvri sees on tühikuid - siis vigane avaldis
        for (char ch : avaldisMassiiv) {
            switch (ch) {
                case '+':
                    lekseemid.add(puhver.strip());
                    lekseemid.add("+");
                    puhver = "";
                    break;
                case '-':
                    lekseemid.add(puhver.strip());
                    lekseemid.add("-");
                    puhver = "";
                    break;
                default:
                    puhver += ch;
                    break;
            }
        }
        int tulemus = 0;
        try {
            tulemus = Integer.parseInt(lekseemid.getFirst());
        } catch (Exception e) {
            tulemus = muutujad.get(lekseemid.getFirst());
        }
        if (lekseemid.size() == 1) {
            return tulemus;
        }
        for (int i = 1; i < lekseemid.size() / 2; ++i) {
            int parem = 0;
            try {
                parem = Integer.parseInt(lekseemid.get(2*i + 0));
            } catch (Exception e) {
                parem = muutujad.get(lekseemid.get(2*i + 0));
            }
            String tehe = lekseemid.get(2*i - 1);
            if (tehe.equals("+")) {
                tulemus += parem;
            } else if (tehe.equals("-")) {
                tulemus -= parem;
            }
        }
        return tulemus;
    }
    static int väärtusta(String avaldis) {
        return 0;
    }

    static void töötleRida(String rida, Map<Character, Integer> muutujad) {
        int räsiIndeks = rida.indexOf('#');
        rida = rida.substring(0, räsiIndeks);
        if (rida.charAt(0) == ' ') {
            if (rida.strip().length() == 0) {
                return;
            }
            throw new IllegalArgumentException("Lause alguses ei tohi olla tühikuid! Rida: " + rida);
        }
        if (rida.substring(0, 6).equals("print ")) {
            System.out.println(väärtusta(rida.substring(6)));
        }
    }

    static void töötleFail(String failinimi) throws IOException {
        if (!Abifunktsioonid.kasAktkFail(failinimi)) {
            throw new IllegalArgumentException("Argumendiks peab olema .aktk fail");
        }
        var read = Files.readAllLines(new File(failinimi).toPath());
        Map<String, Integer> muutujad = new HashMap<>();
        for (String rida : read) {
//            töötleRida(rida, muutujad);
        }
    }

    // Muutujad peaks salvestama sõnastikku skoobis töötleFail
    // Kas funktsioon töötleRida peaks saama sõnastiku viida argumendina?

    static void main(String[] args) throws IOException {
        Map<String, Integer> muutujad = new HashMap<>();
        System.out.println(väärtustaAvaldis("1-2+7+0", muutujad));
        for (String arg : args) {
            MiniAktk.töötleFail(arg);
        }
    }
}
