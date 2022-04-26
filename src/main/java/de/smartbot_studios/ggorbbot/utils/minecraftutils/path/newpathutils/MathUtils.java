package de.smartbot_studios.ggorbbot.utils.minecraftutils.path.newpathutils;

public class MathUtils {

    public static int getPrefix(double in) {
        if(in >= 0) return 1;
        else return -1;
    }

    public static double round(double value, int places) {
        if(places == 0) places = 1;
        places = Math.abs(places);

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
