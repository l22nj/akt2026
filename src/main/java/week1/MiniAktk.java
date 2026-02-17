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

class AvaldisPuu {
    String info;
    char muutuja;
    int väärtus;
    AvaldisPuu v;
    AvaldisPuu p;

    AvaldisPuu(String tehe) {
        this.info = tehe;
        this.muutuja = ' ';
        this.väärtus = -1;
    }

    AvaldisPuu(char muutuja) {
        this.info = "muutuja";
        this.muutuja = muutuja;
        this.väärtus = -1;
    }

    AvaldisPuu(int väärtus) {
        this.info = "täisarv";
        this.muutuja = ' ';
        this.väärtus = väärtus;
    }

    int väärtustaPuu(Map<Character, Integer> muutujad) {
        int vasak = v.väärtustaPuu(muutujad);
        int parem = p.väärtustaPuu(muutujad);
        try {
            switch (info) {
                case "täisarv":
                    return väärtus;
                case "muutuja":
                    return muutujad.get(muutuja);
                case "liida":
                    return vasak + parem;
                case "lahuta":
                    return vasak - parem;
                default:
                    throw new IllegalArgumentException("Vigane avaldis!");
            }
        }
        catch(Exception e) {
            throw new IllegalArgumentException("Vigane avaldis!");
        }
    }
}

public class MiniAktk {

    // viimase commiti peaks parandama kui harjutuse valmis saan!
    static AvaldisPuu looAvaldisPuu(String avaldis) {
        Deque<String> lekseemid = new ArrayDeque<>();
        String puhver = "";
        char[] avaldisMassiiv = avaldis.toCharArray();
        // Vaja arvestada ka hash-iga
        // Targem viis arvude/muutujatega arvestamiseks?
        for (char täht : avaldisMassiiv) {
            if (täht == '+') {
                lekseemid.push(puhver);
                puhver = "";
                lekseemid.push("liida");
            }
            else if (täht == '-') {
                lekseemid.push(puhver);
                puhver = "";
                lekseemid.push("-");
            } else {
                // Siin pole vist vaja arvulisust testida, sellega tegeleb AvaldisPuu, lihtsalt
                // kõik puhvrisse? See pole ka hea
                if (Character.isLetter(täht) && puhver = "") {

                }
            }
        }
    }
    static int väärtusta(String avaldis) {
        return 0;
    }

    static void töötleRida(String rida, Map<Character, Integer> muutujad) {
        if (rida.charAt(0) == ' ') {
            rida = rida.strip();
            if (rida.length() == 0) {
                return;
            }
            if (rida.charAt(0) == '#') {
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
        Map<Character, Integer> muutujad = new HashMap<>();
        for (String rida : read) {
            töötleRida(rida, muutujad);
        }
    }

    // Muutujad peaks salvestama sõnastikku skoobis töötleFail
    // Kas funktsioon töötleRida peaks saama sõnastiku viida argumendina?

    static void main(String[] args) throws IOException {
        for (String arg : args) {
            MiniAktk.töötleFail(arg);
        }
    }
}
