package de.smartbot_studios.ggorbbot.listeners;

import org.lwjgl.input.Mouse;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Player;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MouseClickListener {

    private GGOrbBot gGOrbBot;
    public MouseClickListener(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onMouse(MouseEvent mouseEvent) {
        switch (mouseEvent.getButton()) {
            case 0: {
                if(gGOrbBot.pickingChests && Player.getBlockLookingAt() != null) {
                    if(!Mouse.isButtonDown(mouseEvent.getButton())) return;
                    mouseEvent.setCanceled(true);
                    if(!Player.lookingAtChest()) return;
                    if(gGOrbBot.chestsToBeAdded.contains(Player.getBlockLookingAt())) {
                        gGOrbBot.chestsToBeAdded.remove(Player.getBlockLookingAt());
                        return;
                    }
                    if(gGOrbBot.existingChests.contains(Player.getBlockLookingAt())) return;
                    gGOrbBot.chestsToBeAdded.add(Player.getBlockLookingAt());
                }
                break;
            }
        }
    }
}

