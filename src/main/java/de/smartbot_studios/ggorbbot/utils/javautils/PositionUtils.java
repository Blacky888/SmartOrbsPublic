package de.smartbot_studios.ggorbbot.utils.javautils;

import de.smartbot_studios.ggorbbot.utils.minecraftutils.path.pathutils.Position;

public class PositionUtils {

    public static double getDistance(Position position1, Position position2) {
        double dx, dy, dz;
        dx = position2.getX() - position1.getX();
        dy = position2.getY() - position1.getY();
        dz = position2.getZ() - position1.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
