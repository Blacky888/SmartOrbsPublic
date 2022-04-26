package de.smartbot_studios.ggorbbot.utils.minecraftutils.path;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Player;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.path.pathutils.KeyState;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.path.pathutils.Position;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.MoverType;

public class Path {

    private Position lastpos;
    private List<State> path = new LinkedList<>();

    private boolean enabled;
    private boolean jumping;
    private long lastjump;
    private int delay;

    private GGOrbBot gGOrbBot;
    public Path(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;
    }

    /**
     * @param delay every (delay) seconds the path will save
     */
    public void init(int delay) {
        this.delay = delay;
        lastjump = System.currentTimeMillis();
    }

    public void run() {
        if(enabled) stop();
        try {
            Thread.sleep(delay * 2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        path.forEach(element -> {
            KeyState keyState = element.getKeyState();
            Vec3d vec3d = element.getVec3d();

            Minecraft.getMinecraft().player.rotationPitch = keyState.getPitch();
            Minecraft.getMinecraft().player.rotationYaw = keyState.getYaw();
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode(), keyState.isSneak());
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode(), keyState.isSprint());

            if(vec3d.y > 0) {
                if(System.currentTimeMillis() - lastjump >= 333 && !jumping) {
                    Player.jump();
                    jumping = true;
                    lastjump = System.currentTimeMillis();
                }
            } else {
                jumping = false;
            }

            Minecraft.getMinecraft().player.move(MoverType.PLAYER, vec3d.x, 0, vec3d.z);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressWarnings("resource")
	public void start() {
        this.enabled = true;
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        lastpos = new Position(player.posX, player.posY, player.posZ);
        File file = new File(gGOrbBot.configsPath + "test.path");
        try {
            new BufferedWriter(new FileWriter(file, false)).write("delay: " + delay);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        while (enabled) {
            save();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() {
        this.enabled = false;
    }

    private void save() {
        boolean sneaking, sprinting;
        float pitch, yaw;

        sneaking = Minecraft.getMinecraft().gameSettings.keyBindSneak.isKeyDown();
        sprinting = Minecraft.getMinecraft().gameSettings.keyBindSprint.isKeyDown();
        pitch = Minecraft.getMinecraft().player.rotationPitch;
        yaw = Minecraft.getMinecraft().player.rotationYaw;

        EntityPlayerSP player = Minecraft.getMinecraft().player;
        double x, y, z;
        x = player.posX;
        y = player.posY;
        z = player.posZ;

        File file = new File(gGOrbBot.configsPath + "test.path");
        try {
            file.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            bufferedWriter.write(new State(new KeyState(sneaking, sprinting, pitch, yaw), new Vec3d(x - lastpos.getX(), y - lastpos.getY(), z - lastpos.getZ())).toString());
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        path.add(new State(new KeyState(sneaking, sprinting, pitch, yaw), new Vec3d(x - lastpos.getX(), y - lastpos.getY(), z - lastpos.getZ())));
        lastpos = new Position(player.posX, player.posY, player.posZ);
    }
}