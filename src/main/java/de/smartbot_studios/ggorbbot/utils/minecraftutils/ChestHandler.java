package de.smartbot_studios.ggorbbot.utils.minecraftutils;

import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.JsonElement;

import de.smartbot_studios.ggorbbot.GGOrbBot;

public class ChestHandler {

    GGOrbBot gGOrbBot;
    public ChestHandler(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;
    }

    public void deleteChest(Chest chest) {
        AtomicReference<JsonElement> toRemove = new AtomicReference<>();
        gGOrbBot.chestConfig.getConfig().getAsJsonArray("chests").forEach(element -> {
            if(element.getAsJsonObject().get("chest").getAsString().equals(chest.toString())) toRemove.set(element);
        });
        gGOrbBot.chestConfig.getConfig().getAsJsonArray("chests").remove(toRemove.get());
        gGOrbBot.chestConfig.saveConfig();
    }
}
