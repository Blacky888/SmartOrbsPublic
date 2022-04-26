package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils;

import net.labymod.main.LabyMod;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public abstract class CustomGuiScreen extends GuiScreen {

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(10);
        drawUnderButtons(mouseX, mouseY, partialTicks);
        drawScreen();
        drawAboveButtons(mouseX, mouseY, partialTicks);
    }

    public abstract void drawScreen();

    /**
     * @param y the start of the overlay
     * @param y1 the end of the overlay
     */
    public static void drawOverlayBackground(int y, int y1) {
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(y, y1);
    }

    private void drawUnderButtons(int mouseX, int mouseY, float partialTicks) {
        this.buttonList.forEach(button -> {
            if(button instanceof Button) {
                if(((Button) button).getButtonLayer() == ButtonLayer.UNDER) {
                    button.drawButton(mc, mouseX, mouseY, partialTicks);
                }
            }
        });
    }

    private void drawAboveButtons(int mouseX, int mouseY, float partialTicks) {
        this.buttonList.forEach(button -> {
            if (button instanceof Button) {
                if (((Button) button).getButtonLayer() == ButtonLayer.ABOVE) {
                    button.drawButton(mc, mouseX, mouseY, partialTicks);
                }
            }
        });
    }

    @Override
    protected void actionPerformed(GuiButton button){
        this.buttonList.stream().filter(button::equals).forEach(b -> {
            if(b instanceof Button) {
                ((Button) b).performAction();
            }
        });
    }
}
