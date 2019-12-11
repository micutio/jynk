package com.github.micutio.jynk.parsing;

import com.github.micutio.jynk.RuntimeError;
import com.github.micutio.jynk.lexing.Token;

import java.util.HashMap;
import java.util.Map;

public class Environment {

    final Environment enclosing;

    private final Map<String, Object> values = new HashMap<>();

    Environment() {
        enclosing = null;
    }

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }

        if (enclosing != null) return enclosing.get(name);

        throw new RuntimeError(name, "Undefined variable '" + name.lexeme + "'.");
    }

    /**
     * Assign a variable of this or any enclosing scope the given value.
     * Innermost scopes have priority to allow for shadowing.
     * @param name Name of the variable
     * @param value New value of the variable
     */
    void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        }

        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeError(name, "Undefinde variable '" + name.lexeme + "'.");
    }
}
