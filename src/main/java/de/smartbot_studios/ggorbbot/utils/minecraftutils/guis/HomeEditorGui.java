package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.javautils.Color;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonLayer;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonPosition;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class HomeEditorGui extends GuiScreen {

    private Scrollbar scrollbar = new Scrollbar(20);

    private List<String> homes = new LinkedList<>();

    private GGOrbBot gGOrbBot;
    public HomeEditorGui(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;

        gGOrbBot.chestHomeConfig.getConfig().getAsJsonArray("homes").forEach(element -> homes.add(element.getAsString()));
    }

    @Override
    public void initGui() {
        int x = this.width / 2;
        int y = this.height / 2;

        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x - 40, y + 95, 80, 20, "Add Home", Color.GRAY.getColor(), Color.LIGHTGRAY.getColor(), Color.LIGHTBLUE.getColor()) {
            @Override
            public void performAction() {
                Minecraft.getMinecraft().displayGuiScreen(new AddHomeGui(gGOrbBot));
            }
        });

        this.scrollbar.setPosition(x + 205, y - 80, x + 210, y + 80);
        this.scrollbar.setSpeed(10);

        int yPos = y - 100;
        for(int i = 0; i < homes.size(); i++) {
            int home = i;

            this.buttonList.add(new Button(ButtonLayer.UNDER, ButtonPosition.MOVABLE, x - 100, yPos + 25, 200, 20, homes.get(home), Color.GRAY.getColor(), Color.LIGHTGRAY.getColor(), Color.LIGHTBLUE.getColor()) {
                @Override
                public void performAction() {
                    Minecraft.getMinecraft().displayGuiScreen(new ChestsContainedGui(gGOrbBot, homes.get(home)));
                }
            });
            this.buttonList.add(new Button(ButtonLayer.UNDER, ButtonPosition.MOVABLE, x + 102, yPos + 25, 18, 18, "", "labymod/addons/GGOrbBot/images/delete_cross.png") {
                @Override
                public void performAction() {
                    Minecraft.getMinecraft().displayGuiScreen(new DelHomeGui(gGOrbBot, homes.get(home)));
                }
            });
            yPos += 20;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int x = this.width / 2;
        int y = this.height / 2;

        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(10);

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
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Deine HomePunkte", x, y, ModColor.toRGB(255, 255, 255, 120));
    }

    @SuppressWarnings("unused")
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
}
