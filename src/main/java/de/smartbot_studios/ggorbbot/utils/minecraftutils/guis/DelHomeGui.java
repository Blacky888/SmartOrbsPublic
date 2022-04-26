package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.javautils.Color;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonLayer;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonPosition;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class DelHomeGui extends GuiScreen {

    private GGOrbBot gGOrbBot;

    private String home;

    public DelHomeGui(GGOrbBot gGOrbBot, String home) {
        this.gGOrbBot = gGOrbBot;
        this.home = home;
    }

    @Override
    public void initGui() {
        int x = this.width / 2;
        int y = this.height / 2;

        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x - 75, y + 95, 70, 20, "Ja", Color.BLACK.getColor(), Color.GRAY.getColor(), Color.DARKGREEN.getColor(), Color.GREEN.getColor(), Color.GREEN.getColor()) {
            @Override
            public void performAction() {
                Minecraft.getMinecraft().displayGuiScreen(new ChestsToGetDeletedGui(gGOrbBot, home));
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
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int x = this.width / 2;
        int y = this.height / 2;

        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(10);

        drawTopBackground(y);

        drawRect(x - 100, y + 10, x + 100, y - 10, ModColor.toRGB(128, 128, 128, 120));
        LabyMod.getInstance().getDrawUtils().drawRectBorder(x - 100, y + 9, x + 100, y - 10, ModColor.toRGB(64, 64, 64, 120), 1);
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), home, x, y - 5, ModColor.toRGB(255, 255, 255, 120));

        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Möchtest du dieses Home wirklich löschen?", x, y - 25, Color.RED.getColor());

        drawButtonsAndBottom(y, mouseX, mouseY, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button){
        buttonList.stream().filter(button::equals).forEach(b -> {
            if(b instanceof Button) {
                ((Button) b).performAction();
            }
        });
    }

    private void drawTopBackground(int y) {
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(0, y - 90);
    }

    private void drawBottomBackground(int y) {
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(y + 90, this.height);
    }

    private void drawButtonsAndBottom(int y, int mouseX, int mouseY, float partialTicks) {
        this.buttonList.forEach(button -> {
            if(button instanceof Button) {
                if(((Button) button).getButtonLayer() == ButtonLayer.UNDER) {
                    button.drawButton(mc, mouseX, mouseY, partialTicks);
                }
            }
        });

        drawBottomBackground(y);

        this.buttonList.forEach(button -> {
            if(button instanceof Button) {
                if(((Button) button).getButtonLayer() == ButtonLayer.ABOVE) {
                    button.drawButton(mc, mouseX, mouseY, partialTicks);
                }
            }
        });
    }
}
