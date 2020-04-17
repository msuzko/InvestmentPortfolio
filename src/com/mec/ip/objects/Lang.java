package com.mec.ip.objects;

import java.util.Locale;

public class Lang {

    private String name;
    private Locale locale;
    private int index;

    public Lang(int index, Locale locale) {
        this.locale = locale;
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
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
