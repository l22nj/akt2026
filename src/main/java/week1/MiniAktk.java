package week1;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class MiniAktk {
    static int väärtusta(String avaldis, Map<String, Integer> muutujad) {
        char[] avaldisMassiiv = avaldis.strip().toCharArray();
        List<String> lekseemid = new ArrayList<>();
        String puhver = "";
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
        lekseemid.add(puhver.strip());
        int tulemus = 0;
        try {
            tulemus = Integer.parseInt(lekseemid.getFirst());
        } catch (Exception e) {
            tulemus = muutujad.get(lekseemid.getFirst());
        }
        if (lekseemid.size() == 1) {
            return tulemus;
        }
        for (int i = 1; i < (lekseemid.size()+1) / 2; ++i) {
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

    static void töötleRida(String rida, Map<String, Integer> muutujad) {
        int räsiIndeks = rida.indexOf('#');
        if (räsiIndeks == 0) {
            return;
        }
        if (räsiIndeks != -1) {
            rida = rida.substring(0, räsiIndeks);
        }
        if (rida.strip().length() == 0) {
            return;
        }
        if (rida.charAt(0) == ' ') {
            throw new IllegalArgumentException("Lause alguses ei tohi olla tühikuid! Rida: " + rida);
        }
        else if (rida.substring(1).strip().charAt(0) == '=') {
            int võrdusIndeks = rida.indexOf('=');
            if (!Character.isLetter(rida.charAt(0))) {
                throw new IllegalArgumentException("Vigane rida://");
            }
            muutujad.put(rida.substring(0,1), väärtusta(rida.substring(võrdusIndeks + 1), muutujad));
        }
        else if (rida.substring(0, 6).equals("print ")) {
            System.out.println(väärtusta(rida.substring(6), muutujad));
        }
        else {
            throw new IllegalArgumentException("Vigane rida://");
        }
    }

    static void töötleFail(String failinimi) throws IOException {
//        if (failinimi.substring(failinimi.length() - 5).equals(".aktk")) {
//            throw new IllegalArgumentException("Argumendiks peab olema .aktk fail");
//        }
        var read = Files.readAllLines(Paths.get(failinimi));
        Map<String, Integer> muutujad = new HashMap<>();
        for (String rida : read) {
            töötleRida(rida, muutujad);
        }
    }

    static void main(String[] args) throws IOException {
        MiniAktk.töötleFail(args[0]);
    }
}
