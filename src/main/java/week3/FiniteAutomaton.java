package week3;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

record Transition(int from, Character l, int to) {}

public class FiniteAutomaton extends AbstractAutomaton {
    int startState;
    Set<Integer> states = new HashSet<>();
    Set<Integer> acceptingStates = new HashSet<>();
    Set<Transition> trs = new HashSet<>();

    @Override
    public void addState(int state) {
        states.add(state);
    }

    @Override
    public void setStartState(int state) {
        if (!states.contains(state)) {
            throw new IllegalArgumentException("startState saab valida ainult juba olemasolevate olekute hulgast");
        }
        startState = state;
    }

    @Override
    public void addAcceptingState(int state) {
        if (!states.contains(state)) {
            throw new IllegalArgumentException("acceptingState saab valida ainult juba olemasolevate olekute hulgast");
        }
        acceptingStates.add(state);
    }

    @Override
    public void addTransition(int fromState, Character label, int toState) {
        if (!states.contains(fromState) || !states.contains(toState)) {
            throw new IllegalArgumentException("ülemineku saab defineerida vaid juba olemasolevate olekute vahel");
        }
        trs.add(new Transition(fromState, label, toState));
    }

    @Override
    public Set<Integer> getStates() {
        return states;
    }

    @Override
    public Integer getStartState() {
        return startState;
    }

    @Override
    public Set<Integer> getAcceptingStates() {
        return acceptingStates;
    }

    @Override
    public Set<Character> getOutgoingLabels(int state) {
        if (!getStates().contains(state)) {
            throw new IllegalArgumentException("etteantud olekut pole automaadis");
        }
        Set<Character> outgoing = new HashSet<>();
        for (Transition tr : trs) {
            if (tr.from() == state) {
                outgoing.add(tr.l());
            }
        }
        return outgoing;
    }

    @Override
    public Set<Integer> getDestinations(int state, Character label) {
        if (!getStates().contains(state)) {
            throw new IllegalArgumentException("etteantud olekut pole automaadis");
        }
        Set<Integer> destinations = new HashSet<>();
        if (label == null) destinations.add(state);
        for (Transition tr : trs) {
            if (!(tr.from() == state)) continue;
            if (label != null && label.equals(tr.l()) ||
                    label == null && tr.l() == null) {
                destinations.add(tr.to());
            }
        }
        return destinations;
    }

    @Override
    public boolean accepts(String input) {
        Set<Integer> currentStates = leiaEpsSulund(Set.of(getStartState()));
        Set<Integer> nextStates = new HashSet<>();

        for (int i = 0; i < input.length(); ++i) {
            nextStates.clear();
            for (Integer state : currentStates) {
                nextStates.addAll(leiaEpsSulund(getDestinations(state, input.charAt(i))));
            }
            currentStates.clear();
            currentStates.addAll(nextStates);
        }
        currentStates.retainAll(getAcceptingStates());
        return !currentStates.isEmpty();
    }

    Set<Integer> leiaEpsSulund(Set<Integer> states) {
        Set<Integer> sulund = new HashSet<>(states);
        Deque<Integer> pinu = new ArrayDeque<>();
        for (Integer state : states) {
            pinu.push(state);
        }
        while (!pinu.isEmpty()) {
            for (Integer state : getDestinations(pinu.pop(), null)) {
                if (!sulund.contains(state)) {
                    pinu.push(state);
                    sulund.add(state);
                }
            }
        }
        return sulund;
    }

    /**
     * Seda meetodit ei hinnata ja seda ei pea muutma, aga läbikukkunud testide korral
     * antakse sulle automaadi kirjelduseks just selle meetodi tagastusväärtus.
     */
    @Override
    public String toString() {
        return super.toString();
    }

    static void main() throws IOException {
        FiniteAutomaton fa = new FiniteAutomaton();

        fa.addState(0);
        fa.addState(1);
        fa.addState(2);

        fa.addTransition(0, 'b', 0);
        fa.addTransition(0, 'c', 2);
        fa.addTransition(2, 'a', 1);
        fa.addTransition(1, 'd', 0);
        fa.addTransition(0, null, 1);

        fa.setStartState(0);
        fa.addAcceptingState(1);

        System.out.println(fa.accepts("cadbbbca")); // true
        System.out.println(fa.accepts("abc"));      // false
        System.out.println(fa.accepts(""));         // true

        // Pead ise veenduda, et toString töötab...
        System.out.println(fa);
        fa.renderPngFile(Paths.get("graphs", "auto.png"));
    }
}
