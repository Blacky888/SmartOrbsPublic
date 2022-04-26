package de.smartbot_studios.ggorbbot.utils.minecraftutils.path.newpathutils;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedList;

public class MovementHandler {

    private Point target;

    private final LinkedList<Object> queue = new LinkedList<>();

    private boolean allowSprinting;
    private boolean clear;
    private boolean finished;

    private boolean deactivateWalking;
    private boolean teleportCanceled;
    private boolean teleportSent;
    private boolean teleported;
    private double distance;
    private long lastSent = System.currentTimeMillis();
    private int tpsdelay;

    private final GGOrbBot ggOrbBot;

    public MovementHandler(GGOrbBot ggOrbBot, int tpsdelay) {
        this.ggOrbBot = ggOrbBot;

        this.distance = 0.5;
    }

    public MovementHandler allowSprinting(boolean v) {
        this.allowSprinting = v;
        distance = v ? 1 : 0.5;
        return this;
    }

    public void onReceive(String msg) {
        if (msg.equals("Laufende Teleportierung abgebrochen.") || msg.startsWith("Fehler: Zeit bis zum nächsten Teleport:") || msg.equals("Bitte unterlasse das Spammen von Commands!") || msg.equals("[GrieferGames] Du konntest nicht teleportiert werden.")) {
            System.out.println("teleport canceled");
            teleportCanceled = true;
        }
    }

    public void execPath(Path path, EntityPlayerSP entityPlayer) {
        path.build();
        boolean finished = false;
        while (!finished) {

            if (target == null) target = Point.fromPlayer(Minecraft.getMinecraft().player);

            Player.unmodifiedlookAt(target.getX(), entityPlayer.posY + 1.3, target.getY());
            Vec2d v = Vec2d.fromPoints(Point.fromPlayer(entityPlayer), target);
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
            if (allowSprinting) {
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode(), true);
            }
            if (v.getLength() < distance) {
                if (path.hasNext()) {
                    target = path.getNextPoint();
                } else finished = true;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode(), false);
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
    }

    public void execTp(String tp) {
        teleported = false;
        BlockPos b = Minecraft.getMinecraft().player.getPosition();
        while (!teleported) {
            if(teleportCanceled) teleportSent = false;
            if(!teleportSent && System.currentTimeMillis() - lastSent >= tpsdelay) {
                ggOrbBot.chatQueue.add(tp);
                teleportSent = true;
                lastSent = System.currentTimeMillis();
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
            if(!blockposMatch(b, Minecraft.getMinecraft().player.getPosition())) teleported = true;
        }
        teleported = false;
    }

    private boolean blockposMatch(BlockPos b, BlockPos b1) {
        return b.getX() == b1.getX() && b.getY() == b1.getY() && b.getZ() == b1.getZ();
    }

    public void setDelay(int delay) {
        this.tpsdelay = delay;
    }
}
