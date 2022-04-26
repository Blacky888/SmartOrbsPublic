package de.smartbot_studios.ggorbbot.utils.runnables;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Player;
import net.minecraft.client.Minecraft;

public class KammerRunnable implements Runnable {

    private final GGOrbBot ggOrbBot;
    private final boolean aura;
    public KammerRunnable(GGOrbBot ggOrbBot, boolean aura) {
        this.ggOrbBot = ggOrbBot;
        this.aura = aura;
        System.out.println("kammer thread initialized");
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("trial: " + System.currentTimeMillis());
            if (!ggOrbBot.enabled) break;
            ggOrbBot.movementHandler.execTp(ggOrbBot.afkHome);

            while (!Player.inventoryFull()) {
                if (!ggOrbBot.enabled) break;
                if(aura) {
                    ggOrbBot.killAura.exec(Minecraft.getMinecraft().player);
                }
                Player.pingDelay10();
            }

            if(!ggOrbBot.enabled) {
                break;
            }
            Player.bringToOrbHandler(ggOrbBot);
        }
    }
}
