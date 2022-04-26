package de.smartbot_studios.ggorbbot.commands;

import com.google.gson.JsonObject;
import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.MsgConfig;
import de.smartbot_studios.ggorbbot.utils.commandutils.Command;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Player;
import net.labymod.main.LabyMod;
import net.minecraft.util.math.BlockPos;

public class SetAuraPosCommand extends Command {

    private final GGOrbBot ggOrbBot;
    public SetAuraPosCommand(GGOrbBot ggOrbBot) {
        super("setaurapos");
        this.ggOrbBot = ggOrbBot;
    }

    @Override
    public void execute(String[] args) {

        BlockPos pos = Player.getBlockLookingAt();
        if(pos == null) return;

        JsonObject data = new JsonObject();
        data.addProperty("x", pos.getX());
        data.addProperty("y", pos.getY());
        data.addProperty("z", pos.getZ());

        ggOrbBot.getConfig().add("aurapos", data);
        ggOrbBot.auraPos = pos;
        ggOrbBot.afkAuraModeRequireNotNull.requireNotNull("Aura-Pos", pos);
        ggOrbBot.baritoneAuraModeRequireNotNull.requireNotNull("Aura-Pos", pos);
        
        LabyMod.getInstance().displayMessageInChat("Aura Pos setted");

    }
}
