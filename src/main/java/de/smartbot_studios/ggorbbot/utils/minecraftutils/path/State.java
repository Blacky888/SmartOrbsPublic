package de.smartbot_studios.ggorbbot.utils.minecraftutils.path;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.smartbot_studios.ggorbbot.utils.javautils.ToStringHelper;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.path.pathutils.KeyState;

public class State {

    private final KeyState keyState;
    private final Vec3d vec3d;
    public State(KeyState keyState, Vec3d vec3d) {
        this.keyState = keyState;
        this.vec3d = vec3d;
    }

    public KeyState getKeyState() {
        return keyState;
    }

    public Vec3d getVec3d() {
        return vec3d;
    }

    public static State fromString(String input) {
        String regex = "(State=\\{keystate=KeyState=\\{sprint=)(true)?(false)?(, pitch=)(-)?[0-9]+(.)[0-9]+(, sneak=)(true)?(false)?(, yaw=)(-)?[0-9]+(.)[0-9]+(}, vec3d=Vec3d=\\{x=)(-)?[0-9]+(.)[0-9]+(, y=)(-)?[0-9]+(.)[0-9]+(, z=)(-)?[0-9]+(.)[0-9]+(}})";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if(!matcher.find()) return null;

        return new State(KeyState.fromString(input), Vec3d.fromString(input));
    }

    public String toString() {
        return new ToStringHelper().withName(this.getClass().getSimpleName()).addProperty("keystate", keyState.toString()).addProperty("vec3d", vec3d.toString()).toString();
    }
}
