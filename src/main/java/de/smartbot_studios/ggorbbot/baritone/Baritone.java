package de.smartbot_studios.ggorbbot.baritone;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import de.smartbot_studios.ggorbbot.utils.javautils.PositionUtils;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.path.pathutils.Position;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

public class Baritone {

    public static boolean checkForBaritone() {
        File baritone = new File(Minecraft.getMinecraft().mcDataDir + "/mods/baritone-standalone-forge-1.2.15.jar");
        return baritone.exists();
    }

    public static void installBaritone() {
        try {
            URL baritoneGitUrl = new URL("https://github.com/cabaletta/baritone/releases/download/v1.2.15/baritone-standalone-forge-1.2.15.jar");
            InputStream inputStream = baritoneGitUrl.openStream();
            Files.copy(inputStream, Paths.get(Minecraft.getMinecraft().mcDataDir + "/mods/baritone-standalone-forge-1.2.15.jar"), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void goTo(int x, int z) {
        Minecraft.getMinecraft().player.sendChatMessage("#goto " + x + " " + z);
        BlockPos pos = Minecraft.getMinecraft().player.getPosition();
        System.out.println(pos.toString());
        while (!(PositionUtils.getDistance(new Position(pos.getX(), 0, pos.getZ()), new Position(x, 0, z)) <= 1.5)) {
            pos = Minecraft.getMinecraft().player.getPosition();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
