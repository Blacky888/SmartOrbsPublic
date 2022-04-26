package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.javautils.Color;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Chest;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonLayer;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonPosition;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.CustomGuiScreen;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;

public class ChestsContainedGui extends CustomGuiScreen {

    private Scrollbar scrollbar = new Scrollbar(20);

    private List<Chest> chests = new LinkedList<>();

    private String home;

    private GGOrbBot gGOrbBot;
    public ChestsContainedGui(GGOrbBot gGOrbBot, String home) {
        this.gGOrbBot = gGOrbBot;
        this.home = home;

        gGOrbBot.chestConfig.getConfig().getAsJsonArray("chests").forEach(element -> {
            if(element.getAsJsonObject().get("home").getAsString().equals(home)) chests.add(Chest.getFromString(element.getAsJsonObject().get("chest").getAsString()));
        });
    }

    @Override
    public void initGui() {
        int x = this.width / 2;
        int y = this.height / 2;

        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x - 40, y + 95, 80, 20, "Zurück", Color.DARKGRAY.getColor(), Color.LIGHTGRAY.getColor(), Color.LIGHTBLUE.getColor()) {
            @Override
            public void performAction() {
                Minecraft.getMinecraft().displayGuiScreen(new HomeEditorGui(gGOrbBot));
            }
        });

        int yPos = y - 100;
        for (int i = 0; i < chests.size(); i++) {
            int chest = i;
            this.buttonList.add(new Button(ButtonLayer.UNDER, ButtonPosition.MOVABLE, x + 102, yPos + 25, 18, 18, "", "labymod/addons/GGOrbBot/images/delete_cross.png") {
                @Override
                public void performAction() {
                    Minecraft.getMinecraft().displayGuiScreen(new DelChestGui(gGOrbBot, chests.get(chest)));
                }
            });
            yPos += 20;
        }

        this.scrollbar.setPosition(x + 205, y - 80, x + 210, y + 80);
        this.scrollbar.setSpeed(10);
    }

    @Override
    public void drawScreen() {
        int x = this.width / 2;
        int y = this.height / 2;


        double yPos = y - 70 + this.scrollbar.getScrollY();
        for (Chest chest : chests) {
            drawEntry(x, (int)yPos, chest.toString());
            yPos += 20;
        }

        drawOverlayBackground(0, y - 90);
        drawOverlayBackground(y + 90, this.height);

        this.buttonList.forEach(button -> {
            if(button instanceof Button) {
                ((Button) button).setDistance((int) scrollbar.getScrollY());
            }
        });

        this.scrollbar.update(chests.size());
        this.scrollbar.draw();

        drawTitle(x, y - 110);
    }

    private void drawEntry(int x, int y, String chest) {
        drawRect(x - 100, y + 15, x + 100, y - 5, ModColor.toRGB(128, 128, 128, 120));
        LabyMod.getInstance().getDrawUtils().drawRectBorder(x - 100, y + 14, x + 100, y - 5, ModColor.toRGB(64, 64, 64, 120), 1);
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), chest, x, y, ModColor.toRGB(255, 255, 255, 120));
    }

    private void drawTitle(int x, int y) {
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Das Home \"" + home + "\" beinhaltet folgende Kisten:", x, y, ModColor.toRGB(255, 255, 255, 120));
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.CLICKED);
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);

        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.DRAGGING);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        this.scrollbar.mouseAction(mouseX, mouseY, Scrollbar.EnumMouseAction.RELEASED);
        super.mouseReleased(mouseX, mouseY, state);
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();

        this.scrollbar.mouseInput();
    }
}
