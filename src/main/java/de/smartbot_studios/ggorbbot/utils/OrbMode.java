package de.smartbot_studios.ggorbbot.utils;

public enum OrbMode {

    CHESTMODE("Auf Kisten zugreifen"),
    AFKPOSMODE("Von Afk Position aus Sammeln und Abgeben"),
    AFKAURAMODE("Von Afk Position aus Mobs töten und Drop Abgeben");

    private String value;

    OrbMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
