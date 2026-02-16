package week1;

import java.io.IOException;

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

    static void töötleRida(String rida) {

    }

    static void töötleFail(String failinimi) throws IOException {
        if (!Abifunktsioonid.kasAktkFail(failinimi)) {
            throw new IllegalArgumentException("Argumendiks peab olema .aktk fail");
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
