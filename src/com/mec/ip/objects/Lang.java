package com.mec.ip.objects;

import java.util.Locale;

public class Lang {

    private String name;
    private Locale locale;
    private int index;

    public Lang(int index, String name, Locale locale) {
        this.name = name;
        this.locale = locale;
        this.index = index;
    }

    public Locale getLocale() {
        return locale;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return name;
    }
}
