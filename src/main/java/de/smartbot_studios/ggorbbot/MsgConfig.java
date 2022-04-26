package de.smartbot_studios.ggorbbot;

public enum MsgConfig {
    COMMAND_NOTFOUND("§cDieser Command wurde nicht gefunden!"),
    GGORBBOTMODE_ACTIVATED("GGOrbBotMode §aaktiviert§f!"),
    GGORBBOTBMODE_DEACTIVATED("GGOrbBotMode §cdeaktiviert§f!"),
    CHESTS_ADDED("Deine Auswahl an Kisten wurde der Liste hinzugefügt!"),
    BARITONE_INITIALIZED("Baritone initialisiert!"),
    ORBHANDLERHOME_SET("Die OrbHändler - Koordinaten wurden erfolgreich abgespeichert!");


    private final String value;

    MsgConfig(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
