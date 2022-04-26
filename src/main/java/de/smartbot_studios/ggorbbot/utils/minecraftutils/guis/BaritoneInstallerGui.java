package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis;

import de.smartbot_studios.ggorbbot.baritone.Baritone;
import de.smartbot_studios.ggorbbot.utils.javautils.Color;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonLayer;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonPosition;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.CustomGuiScreen;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;

public class BaritoneInstallerGui extends CustomGuiScreen {

    @Override
    public void initGui() {
        int x = this.width / 2;
        int y = this.height / 2;

        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x - 125, y + 95, 120, 20, "Installieren", Color.BLACK.getColor(), Color.GRAY.getColor(), Color.DARKGREEN.getColor(), Color.GREEN.getColor(), Color.GREEN.getColor()) {
            @Override
            public void performAction() {
                Baritone.installBaritone();
                Minecraft.getMinecraft().shutdown();
            }
        });
        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x + 5, y + 95, 120, 20, "Ohne fortfahren", Color.BLACK.getColor(), Color.GRAY.getColor(), Color.DARKRED.getColor(), Color.RED.getColor(), Color.RED.getColor()) {
            @Override
            public void performAction() {
                Minecraft.getMinecraft().displayGuiScreen(null);
            }
        });
    }

    @Override
    public void drawScreen() {
        int x = this.width / 2;
        int y = this.height / 2;

        drawOverlayBackground(0, y - 90);
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(),  "Baritone installieren", x, y - 110, Color.WHITE.getColor());
        drawText(y - 80, "Die Mod Baritone wurde nicht installiert. Baritone ermöglicht eine");
        drawText(y - 60, "schnellere Umsetzung des Verkaufsvorgangs. Die Installation erfordert");
        drawText(y - 40, "einen Neustart. Zum Installieren wähle \"Installieren\", zum");
        drawText(y - 20, "Fortfahren ohne Baritone wähle \"Ohne fortfahren\"");
        drawOverlayBackground(y + 90, this.height);
    }

    private void drawText(int y, String text) {
        drawString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), text, 50, y, Color.RED.getColor());
    }
}
