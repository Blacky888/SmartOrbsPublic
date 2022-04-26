package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.javautils.Color;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Chest;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonLayer;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonPosition;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.CustomGuiScreen;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;

public class DelChestGui extends CustomGuiScreen {

    private Chest chest;

    private GGOrbBot gGOrbBot;

    public DelChestGui(GGOrbBot gGOrbBot, Chest chest) {
        this.gGOrbBot = gGOrbBot;
        this.chest = chest;
    }

    @Override
    public void initGui() {
        int x = this.width / 2;
        int y = this.height / 2;

        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x - 75, y + 95, 70, 20, "Ja", Color.BLACK.getColor(), Color.GRAY.getColor(), Color.DARKGREEN.getColor(), Color.GREEN.getColor(), Color.GREEN.getColor()) {
            @Override
            public void performAction() {
                gGOrbBot.chestHandler.deleteChest(chest);
                Minecraft.getMinecraft().displayGuiScreen(new HomeEditorGui(gGOrbBot));
            }
        });
        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x + 5, y + 95, 70, 20, "Nein", Color.BLACK.getColor(), Color.GRAY.getColor(), Color.DARKRED.getColor(), Color.RED.getColor(), Color.RED.getColor()) {
            @Override
            public void performAction() {
                Minecraft.getMinecraft().displayGuiScreen(new HomeEditorGui(gGOrbBot));
            }
        });
    }

    @Override
    public void drawScreen() {
        int x = this.width / 2;
        int y = this.height / 2;

        drawOverlayBackground(0, y - 90);

        drawRect(x - 100, y + 10, x + 100, y - 10, ModColor.toRGB(128, 128, 128, 120));
        LabyMod.getInstance().getDrawUtils().drawRectBorder(x - 100, y + 9, x + 100, y - 10, ModColor.toRGB(64, 64, 64, 120), 1);
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), chest.toString(), x, y - 5, ModColor.toRGB(255, 255, 255, 120));

        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Möchtest du diese Kiste wirklich löschen?", x, y - 25, Color.RED.getColor());

        drawOverlayBackground(y + 90, this.height);
    }
}
