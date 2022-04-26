package de.smartbot_studios.ggorbbot.utils.minecraftutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.smartbot_studios.ggorbbot.utils.javautils.ToStringHelper;

public class Chest {

    private boolean empty;

    private final int x;
    private final int y;
    private final int z;

    public Chest(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public static Chest getFromString(String toCheck) {
        String regex = "(Chest=\\{x=)[-]?[0-9]+(,)\\s(y=)[-]?[0-9]+(,)\\s(z=)[-]?[0-9]+(})";

        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(toCheck);

        if(!matcher.find()) return null;

        regex = "[-]?[0-9]+";
        pattern = Pattern.compile(regex, Pattern.MULTILINE);
        matcher = pattern.matcher(toCheck);

        int i = 0;

        int returnX = 0;
        int returnY = 0;
        int returnZ = 0;

        while (matcher.find()) {
            switch (i) {
                case 0: {
                    returnX = Integer.parseInt(matcher.group());
                    i++;
                    break;
                }
                case 1: {
                    returnY = Integer.parseInt(matcher.group());
                    i++;
                    break;
                }
                case 2: {
                    returnZ = Integer.parseInt(matcher.group());
                    break;
                }
            }
        }
        return new Chest(returnX, returnY, returnZ);
    }

    public String toString() {
        return new ToStringHelper().withName(this.getClass().getSimpleName()).addProperty("x", x).addProperty("y", y).addProperty("z", z).toString();
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
