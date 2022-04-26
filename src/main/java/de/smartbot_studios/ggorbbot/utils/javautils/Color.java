package de.smartbot_studios.ggorbbot.utils.javautils;

import net.labymod.utils.ModColor;

public enum Color {

    BLACK(ModColor.toRGB(0, 0, 0, 120)),

    LIGHTGRAY(ModColor.toRGB(150, 150, 150, 120)),
    GRAY(ModColor.toRGB(110, 110, 110, 120)),
    DARKGRAY(ModColor.toRGB(70, 70, 70, 120)),

    LIGHTBLUE(ModColor.toRGB(145, 184, 242, 120)),
    BLUE(ModColor.toRGB(45, 106, 196, 120)),
    DARKBLUE(ModColor.toRGB(0, 63, 158, 120)),

    LIGHTGREEN(ModColor.toRGB(31, 255, 61, 120)),
    GREEN(ModColor.toRGB(0, 171, 23, 120)),
    DARKGREEN(ModColor.toRGB(0, 117, 16, 120)),

    LIGHTRED(ModColor.toRGB(255, 41, 25, 120)),
    RED(ModColor.toRGB(191, 13, 0, 120)),
    DARKRED(ModColor.toRGB(150, 10, 0, 120)),

    LIGHTYELLOW(ModColor.toRGB(255, 241, 46, 120)),
    YELLOW(ModColor.toRGB(217, 202, 0, 120)),
    DARKYELLOW(ModColor.toRGB(189, 176, 0, 120)),

    WHITE(ModColor.toRGB(255, 255, 255, 120));

    private int color;

    Color(int color) {
        this.color = color;
    }

    public int getColor() {
        return color;
    }
}
