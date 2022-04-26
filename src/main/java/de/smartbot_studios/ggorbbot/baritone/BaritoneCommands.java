package de.smartbot_studios.ggorbbot.baritone;

public enum BaritoneCommands {
    CONFIG("#set "),
    DISABLEBREAK("allowBreak false"),
    ENABLESPRINT("allowSprint true"),
    DISABLEPLACE("allowPlace false"),
    DISABLEFREELOOK("freeLook false"),
    DISABLESPRINTASCENDS("sprintAscends false"),
    ENABLEPARKOUR("allowParkour true");

    private String value;

    BaritoneCommands(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }
}
