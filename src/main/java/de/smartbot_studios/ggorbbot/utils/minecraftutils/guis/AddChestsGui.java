package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonLayer;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonPosition;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class AddChestsGui extends GuiScreen {

    private Scrollbar scrollbar = new Scrollbar(20);

    private List<String> homes = new LinkedList<>();

    private GGOrbBot gGOrbBot;
    public AddChestsGui(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;

        gGOrbBot.chestHomeConfig.getConfig().getAsJsonArray("homes").forEach(element -> {
            homes.add(element.getAsString());
        });
    }

    @Override
    public void initGui() {
        int x = this.width / 2;
        int y = this.height / 2;

        this.scrollbar.setPosition(x + 205, y - 80, x + 210, y + 80);
        this.scrollbar.setSpeed(10);

        y -= 100;
        x += 102;

        for(int i = 0; i < homes.size(); i++) {
            int home = i;
            this.buttonList.add(new Button(ButtonLayer.UNDER, ButtonPosition.MOVABLE, x, y + 25, 18, 18, "", "labymod/addons/GGOrbBot/images/accept_hook.png") {
                @Override
                public void performAction() {
                    gGOrbBot.selectedHome = homes.get(home);
                    Minecraft.getMinecraft().displayGuiScreen(null);
                    gGOrbBot.pickingChests = true;
                }
            });
            y += 20;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int x = this.width / 2;
        int y = this.height / 2;

        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(10);

        double yPos = y - 70 + this.scrollbar.getScrollY();
        for (String home : homes) {
            drawEntry(x, (int)yPos, home);
            yPos += 20;
        }

        this.buttonList.forEach(button -> {
            if(button instanceof Button) {
                ((Button) button).setDistance((int) this.scrollbar.getScrollY());
            }
        });

        this.scrollbar.update(homes.size());
        this.scrollbar.draw();

        this.buttonList.forEach(button -> {
            if (button instanceof Button) {
                if (((Button) button).getButtonLayer() == ButtonLayer.UNDER) {
                    button.drawButton(mc, mouseX, mouseY, partialTicks);
                }
            }
        });
        drawTopAndTitle(x, y);
        drawBottomBackground(y);
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
        buttonList.stream().filter(button::equals).forEach(b -> {
            if(b instanceof Button) {
                ((Button) b).performAction();
            }
        });
    }

    private void drawTopAndTitle(int x, int y) {
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(0, y - 90);
        drawTitle(x, y - 110);
    }

    private void drawBottomBackground(int y) {
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(y + 90, this.height);
    }

    private void drawTitle(int x, int y) {
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Wähle einen Homepunkt aus!", x, y, ModColor.toRGB(255, 255, 255, 120));
    }

    private void drawEntry(int x, int y, String home) {
        drawRect(x - 100, y + 15, x + 100, y - 5, ModColor.toRGB(128, 128, 128, 120));
        LabyMod.getInstance().getDrawUtils().drawRectBorder(x - 100, y + 14, x + 100, y - 5, ModColor.toRGB(64, 64, 64, 120), 1);
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), home, x, y, ModColor.toRGB(255, 255, 255, 120));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);

        super.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        this.scrollbar.mouseInput();
    }

    @Override
    public void onGuiClosed() {
        gGOrbBot.pickingChests = false;
        super.onGuiClosed();
    }
}
