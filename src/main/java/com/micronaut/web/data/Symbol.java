package com.micronaut.web.data;

public class Symbol {

    private String value;

    public Symbol() {

    }

    public Symbol(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Symbol) {
            Symbol thatSymbol = (Symbol) obj;
            return this.value.equals(thatSymbol.getValue());
        }
        return false;
    }

    @Override
    public String toString() {
        return value;
    }
}
