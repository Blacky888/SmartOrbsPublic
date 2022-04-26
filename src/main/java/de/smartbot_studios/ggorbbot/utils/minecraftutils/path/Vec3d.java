package de.smartbot_studios.ggorbbot.utils.minecraftutils.path;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.smartbot_studios.ggorbbot.utils.javautils.ToStringHelper;

public class Vec3d extends net.minecraft.util.math.Vec3d {
    public Vec3d(double xIn, double yIn, double zIn) {
        super(xIn, yIn, zIn);
    }

    public static Vec3d fromString(String input) {
        String regex = "(Vec3d=\\{x=)(-)?[0-9]+(.)[0-9]+(, y=)(-)?[0-9]+(.)[0-9]+(, z=)(-)?[0-9]+(.)[0-9]+(})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if(!matcher.find()) return null;

        double x, y, z;

        regex = "(x=)(-)?[0-9]+(.)[0-9]+";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        if(!matcher.find()) return null;
        x = Double.parseDouble(matcher.group().replace("x=", ""));

        regex = "(y=)(-)?[0-9]+(.)[0-9]+";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        if(!matcher.find()) return null;
        y = Double.parseDouble(matcher.group().replace("y=", ""));

        regex = "(z=)(-)?[0-9]+(.)[0-9]+";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        if(!matcher.find()) return null;
        z = Double.parseDouble(matcher.group().replace("z=", ""));

        return new Vec3d(x, y, z);
    }

    public String toString() {
        return new ToStringHelper().withName(this.getClass().getSimpleName()).addProperty("x", x).addProperty("y", y).addProperty("z", z).toString();
    }
}
