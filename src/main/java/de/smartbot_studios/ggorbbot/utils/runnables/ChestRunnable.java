package de.smartbot_studios.ggorbbot.utils.runnables;

import com.google.gson.JsonArray;
import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Chest;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Player;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class ChestRunnable implements Runnable {

    private final GGOrbBot ggOrbBot;

    public ChestRunnable(GGOrbBot ggOrbBot) {
        this.ggOrbBot = ggOrbBot;
        System.out.println("chest thread initialized");
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("trial: " + System.currentTimeMillis());
            if (!ggOrbBot.enabled) return;
            Map<String, LinkedList<Chest>> list = new LinkedHashMap<>();
            JsonArray homes = ggOrbBot.chestHomeConfig.getConfig().getAsJsonArray("homes");

            homes.forEach(element -> {
                String home = element.getAsString();

                LinkedList<Chest> chests = new LinkedList<>();
                JsonArray chestArray = ggOrbBot.chestConfig.getConfig().getAsJsonArray("chests");
                chestArray.forEach(element1 -> {
                    if (!ggOrbBot.enabled) return;
                    if (element1.getAsJsonObject().get("home").getAsString().equals(home)) {
                        chests.add(Chest.getFromString(element1.getAsJsonObject().get("chest").getAsString()));
                    }
                });

                list.put(home, chests);
            });

            delay(3000);

            if (!ggOrbBot.enabled) return;
            list.forEach((home, chests) -> {
                ggOrbBot.movementHandler.execTp(home);

                while (!chests.isEmpty()) {
                    if (!ggOrbBot.enabled) break;
                    Chest chest = chests.poll();
                    if (chest != null) {
                        boolean repeat = true;

                        while (repeat) {
                            if (!ggOrbBot.enabled) break;
                            if (!Player.inventoryFull()) {

                                Player.openChest(new BlockPos(chest.getX(), chest.getY(), chest.getZ()));
                                Player.emptyChest();
                                if (Player.chestEmpty()) repeat = false;
                                Player.closeScreen();

                                delay(Player.pingDelay5());
                            } else {
                                Player.bringToOrbHandler(ggOrbBot);
                                ggOrbBot.movementHandler.execTp(home);
                            }
                        }
                    }
                }
            });
            if (!ggOrbBot.enabled) break;
            Player.bringToOrbHandler(ggOrbBot);

            if (!ggOrbBot.enabled) break;

            if (ggOrbBot.systemShutdown) ggOrbBot.closeMc = true;

            if (ggOrbBot.closeMc) {
                System.out.println("All Chests empty closing Minecraft");
                delay(1000);
                Minecraft.getMinecraft().shutdown();
            }
            if (ggOrbBot.systemShutdown) {
                System.out.println("All Chests empty Shutdown System");
                delay(1000);
                String shutdownCmd = "shutdown -f";
                try {
                    @SuppressWarnings("unused")
                    Process child = Runtime.getRuntime().exec(shutdownCmd);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("All Chests empty waiting " + ggOrbBot.waitTimer + " minutes");
            delay(ggOrbBot.waitTimer * 60000);
            System.out.println("Waiting time is over Start exchange Items to Orbs");
        }
    }

    private void delay(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}