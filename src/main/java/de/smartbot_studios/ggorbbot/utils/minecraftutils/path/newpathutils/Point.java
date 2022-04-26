package de.smartbot_studios.ggorbbot.utils.minecraftutils.path.newpathutils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class Point {

    private final double x;
    private final double y;
    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Vec2d toVec() {
        return new Vec2d(this.x, this.y);
    }

    public static Point fromPlayer(EntityPlayer entityPlayer) {
        return new Point(entityPlayer.posX, entityPlayer.posZ);
    }
}
