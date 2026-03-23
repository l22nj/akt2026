package week4;

import week3.FiniteAutomaton;
import week4.regex.RegexParser;
import week4.regex.ast.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.util.Collections.*;

public class Grep {
    /*
     * main meetodit ei ole vaja muuta.
     *
     * See meetod on siin vaid selleks, et anda käesolevale  harjutusele veidi
     * realistlikum kontekst. Aga tegelikult on see vaid mäng -- see programm ei
     * pretendeeri päeva kasulikuima programmi tiitlile. Päris elus kasuta päris grep-i.
     */
    static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2) {
            System.err.println(
                    """
                            Programm vajab vähemalt ühte argumenti: regulaaravaldist.
                            Teiseks argumendiks võib anda failinime (kui see puudub, siis loetakse tekst standardsisendist).
                            Failinime andmisel eeldatakse, et tegemist on UTF-8 kodeeringus tekstifailiga.
                            Rohkem argumente programm ei aktsepteeri.
                            """
            );
            System.exit(1);
        }

        RegexNode regex = RegexParser.parse(args[0]);
        FiniteAutomaton automaton = determinize(regexToFiniteAutomaton(regex));

        InputStream inputStream;
        if (args.length == 2) {
            inputStream = Files.newInputStream(Paths.get(args[1]));
        } else {
            inputStream = System.in;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            // kuva ekraanile need read, mis vastavad antud regulaaravaldisele/automaadile
            String line;
            while ((line = reader.readLine()) != null) {
                if (automaton.accepts(line)) {
                    System.out.println(line);
                }
            }
        }
    }

    /*
     * See meetod peab loenguslaididel toodud konstruktsiooni põhjal koostama ja tagastama
     * etteantud regulaaravaldisele vastava mittedetermineeritud lõpliku automaadi.
     * Selle meetodi korrektne implementeerimine on antud ülesande juures kõige tähtsam.
     *
     * (Sa võid selle meetodi implementeerimiseks kasutada abimeetodeid ja ka abiklasse,
     * aga ära muuda meetodi signatuuri, sest automaattestid eeldavad just sellise signatuuri
     * olemasolu.)
     *
     * (Selle ülesande juures pole põhjust kasutada vahetulemuste salvestamiseks klassivälju,
     * aga kui sa seda siiski teed, siis kontrolli, et see meetod töötab korrektselt ka siis,
     * kui teda kutsutakse välja mitu korda järjest.)
     */
    public static FiniteAutomaton regexToFiniteAutomaton(RegexNode regex) {
        return switch (regex) {
            case Alternation alternation -> {
                FiniteAutomaton v = regexToFiniteAutomaton(alternation.left());
                FiniteAutomaton p = regexToFiniteAutomaton(alternation.right());
                FiniteAutomaton rööbitus = rööbitaAutomaadid(v,p);
                yield tagastaTulemus(rööbitus);
            }
            case Concatenation concatenation -> {
                FiniteAutomaton v = regexToFiniteAutomaton(concatenation.left());
                FiniteAutomaton p = regexToFiniteAutomaton(concatenation.right());
                FiniteAutomaton summa = liidaAutomaadid(v,p);
                yield tagastaTulemus(summa);
            }
            case Epsilon epsilon -> {
                yield tagastaTulemus(null);
            }
            case Letter letter -> {
                FiniteAutomaton täht = looTähtAutomaat(letter.symbol());
                yield tagastaTulemus(täht);
            }
            case Repetition repetition -> {
                FiniteAutomaton laps = regexToFiniteAutomaton(repetition.child());
                laps.addTransition(0, null, -1);
                laps.addTransition(-1, null, 0);
                yield tagastaTulemus(laps);
            }
        };
    }

    private static FiniteAutomaton looTähtAutomaat(char symbol) {
        FiniteAutomaton tulemus = new FiniteAutomaton();
        tulemus.addState(0);
        tulemus.addState(-1);
        tulemus.setStartState(0);
        tulemus.addAcceptingState(-1);
        tulemus.addTransition(0, symbol, -1);
        return tulemus;
    }

    private static FiniteAutomaton rööbitaAutomaadid(FiniteAutomaton v, FiniteAutomaton p) {
        FiniteAutomaton tulemus = new FiniteAutomaton();
        FiniteAutomaton uus_v = nihutaOlekuidRööbitusVasak(v, p);
        FiniteAutomaton uus_p = nihutaOlekuidRööbitusParem(v, p);

        lisaAndmed(uus_v, tulemus);
        lisaAndmed(uus_p, tulemus);

        tulemus.setStartState(0);
        tulemus.addAcceptingState(-1);

        return tulemus;
    }

    private static FiniteAutomaton liidaAutomaadid(FiniteAutomaton v, FiniteAutomaton p) {
        FiniteAutomaton tulemus = new FiniteAutomaton();
        FiniteAutomaton uus_v = nihutaOlekuidSummaVasak(v, p);
        FiniteAutomaton uus_p = nihutaOlekuidSummaParem(v, p);

        lisaAndmed(uus_v, tulemus);
        lisaAndmed(uus_p, tulemus);

        tulemus.setStartState(0);
        tulemus.addAcceptingState(-1);

        return tulemus;
    }

    private static void lisaAndmed(FiniteAutomaton automaat, FiniteAutomaton tulemus) {
        for (int olek : automaat.getStates()) {
            tulemus.addState(olek);
            for (Character süm : automaat.getOutgoingLabels(olek)) {
                for (int siht : automaat.getDestinations(olek, süm)) {
                    tulemus.addState(siht);
                    tulemus.addTransition(olek, süm, siht);
                }
            }
        }
    }

    private static FiniteAutomaton nihutaOlekuidRööbitusParem(FiniteAutomaton v, FiniteAutomaton p) {
        return null;
    }

    private static FiniteAutomaton nihutaOlekuidRööbitusVasak(FiniteAutomaton v, FiniteAutomaton p) {
        return null;
    }

    private static FiniteAutomaton nihutaOlekuidSummaParem(FiniteAutomaton v, FiniteAutomaton p) {
        return null;
    }

    private static FiniteAutomaton nihutaOlekuidSummaVasak(FiniteAutomaton v, FiniteAutomaton p) {
        return null;
    }

    private static FiniteAutomaton normeeriOlekud(FiniteAutomaton automaat) {
        FiniteAutomaton tulemus = new FiniteAutomaton();
        int n = tulemus.getStates().size();

        for (int olek : automaat.getStates()) {
            int uus_olek = olek > -1 ? olek + 1 : n;
            tulemus.addState(uus_olek);
            for (Character süm : automaat.getOutgoingLabels(olek)) {
                for (int siht : automaat.getDestinations(olek, süm)) {
                    int uus_siht = siht > -1 ? siht + 1 : n;
                    tulemus.addState(uus_siht);
                    tulemus.addTransition(uus_olek, süm, uus_siht);
                }
            }
        }

        tulemus.setStartState(1);
        tulemus.addAcceptingState(n);

        return tulemus;
    }

    private static FiniteAutomaton tagastaTulemus(FiniteAutomaton automaat) {
        FiniteAutomaton tulemus = new FiniteAutomaton();
        tulemus.addState(0);
        tulemus.addState(-1);
        tulemus.setStartState(0);
        tulemus.addAcceptingState(-1);

        if (Objects.isNull(automaat)) {
            tulemus.addTransition(0, null, -1);
            return tulemus;
        }

        FiniteAutomaton uus_automaat = normeeriOlekud(automaat);
        lisaAndmed(uus_automaat, tulemus);
        int n = uus_automaat.getStates().size();

        tulemus.addTransition(0, null, 1);
        tulemus.addTransition(n, null, -1);

        return tulemus;
    }

    /**
     * See meetod peab looma etteantud NFA-le vastava DFA, st. etteantud
     * automaat tuleb determineerida.
     * Kui sa seda ei jõua teha, siis jäta see meetod nii, nagu ta on.
     */
    public static FiniteAutomaton determinize(FiniteAutomaton nfa) {
        return nfa;
    }
}
