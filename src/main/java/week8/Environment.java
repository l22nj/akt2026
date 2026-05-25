package week8;

import java.util.*;

public class Environment<T> {
    private final Deque<Map<String, T>> scopes = new ArrayDeque<>();
    /**
     * Esialgu peaks olemas olema globaalne skoop, kuhu saab muutujaid deklareerida enne ühtegi skoopi (plokki) sisenemist.
     */
    public Environment() {
        enterBlock();
    }

    private Map<String, T> scope(String variable) {
        for (Map<String, T> scope : scopes) {
            if (scope.containsKey(variable)) return scope;
        }
        return scopes.getFirst();
    }

    /**
     * Deklareerib praeguses skoobis uue muutuja.
     */
    public void declare(String variable) {
        scopes.getFirst().put(variable, null);
    }

    /**
     * Omistab muutujale uue väärtuse kõige sisemises skoobis, kus see muutuja deklareeritud on.
     */
    public void assign(String variable, T value) {
        if (scope(variable).containsKey(variable)) scope(variable).put(variable, value);
        else throw new RuntimeException("Variable " + variable + " not found.");
    }

    /**
     * Deklareerib praeguses skoobis uue muutuja ja omistab sellele väärtuse.
     */
    public void declareAssign(String variable, T value) {
        declare(variable);
        assign(variable, value);
    }

    /**
     * Tagastab muutuja praeguse väärtuse kõige sisemises skoobis, kus see muutuja deklareeritud on.
     * Deklareerimata või väärtustamata muutujate korral peaks tagastama {@code null}.
     */
    public T get(String variable) {
        return scope(variable).getOrDefault(variable, null);
    }

    /**
     * Tähistab uude skoopi (plokki) sisenemist.
     * Uues skoobis võib üle deklareerida välimiste välimise skoobi muutujaid.
     */
    public void enterBlock() {
        scopes.addFirst(new HashMap<>());
    }

    /**
     * Tähistab praegusest skoobist (plokist) väljumist.
     * Unustama peaks kõik sisemises skoobis deklareeritud muutujad.
     */
    public void exitBlock() {
        scopes.removeFirst();
    }
}
