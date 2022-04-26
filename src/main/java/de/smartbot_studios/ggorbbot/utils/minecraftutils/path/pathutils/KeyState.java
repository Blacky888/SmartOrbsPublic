package de.smartbot_studios.ggorbbot.utils.minecraftutils.path.pathutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.smartbot_studios.ggorbbot.utils.javautils.ToStringHelper;

public class KeyState {

    private final boolean sneak, sprint;
    private final float pitch, yaw;
    public KeyState(boolean sneak, boolean sprint, float pitch, float yaw) {
        this.sneak = sneak;
        this.sprint = sprint;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public boolean isSneak() {
        return sneak;
    }

    public boolean isSprint() {
        return sprint;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public static KeyState fromString(String input) {
        String regex = "(KeyState=\\{sprint=)(true)?(false)?(, pitch=)(-)?[0-9]+(.)[0-9]+(, sneak=)(true)?(false)?(, yaw=)(-)?[0-9]+(.)[0-9]+(})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if(!matcher.find()) return null;

        boolean sprint, sneak;
        float pitch, yaw;

        regex = "(sprint=)((true)|(false))";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        if(!matcher.find()) return null;
        sprint = Boolean.parseBoolean(matcher.group().replace("sprint=", ""));

        regex = "(sneak=)((true)|(false))";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        if(!matcher.find()) return null;
        sneak = Boolean.parseBoolean(matcher.group().replace("sneak=", ""));

        regex = "(pitch=)(-)?[0-9]+(.)[0-9]+";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        if(!matcher.find()) return null;
        pitch = Float.parseFloat(matcher.group().replace("pitch=", ""));

        regex = "(yaw=)(-)?[0-9]+(.)[0-9]+";
        pattern = Pattern.compile(regex);
        matcher = pattern.matcher(input);
        if(!matcher.find()) return null;
        yaw = Float.parseFloat(matcher.group().replace("yaw=", ""));
        
        return new KeyState(sneak, sprint, pitch, yaw);
    }

    public String toString() {
        return new ToStringHelper().withName(this.getClass().getSimpleName()).addProperty("sneak", sneak).addProperty("sprint", sprint).addProperty("pitch", pitch).addProperty("yaw", yaw).toString();
    }
}
