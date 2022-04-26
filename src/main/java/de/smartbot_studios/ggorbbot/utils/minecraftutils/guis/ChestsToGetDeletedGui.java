package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.JsonElement;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.javautils.Color;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Chest;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonLayer;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonPosition;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

public class ChestsToGetDeletedGui extends GuiScreen {

    private String home;

    private GGOrbBot gGOrbBot;

    private Scrollbar scrollbar = new Scrollbar(20);

    private List<Chest> chests = new LinkedList<>();

    public ChestsToGetDeletedGui(GGOrbBot gGOrbBot, String home) {
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

        this.scrollbar.setPosition(x + 205, y - 80, x + 210, y + 80);
        this.scrollbar.setSpeed(10);

        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x - 75, y + 95, 70, 20, "Ja", Color.BLACK.getColor(), Color.GRAY.getColor(), Color.DARKGREEN.getColor(), Color.GREEN.getColor(), Color.GREEN.getColor()) {
            @Override
            public void performAction() {
                AtomicReference<JsonElement> toRemove = new AtomicReference<>();
                gGOrbBot.chestHomeConfig.getConfig().getAsJsonArray("homes").forEach(element -> {
                    if(element.getAsString().equals(home)) toRemove.set(element);
                });
                gGOrbBot.chestHomeConfig.getConfig().getAsJsonArray("homes").remove(toRemove.get());
                gGOrbBot.chestHomeConfig.saveConfig();

                List<JsonElement> chestsToRemove = new LinkedList<>();
                gGOrbBot.chestConfig.getConfig().getAsJsonArray("chests").forEach(element -> {
                    if(element.getAsJsonObject().get("home").getAsString().equals(home)) chestsToRemove.add(element);
                });
                chestsToRemove.forEach(element -> {
                    gGOrbBot.chestConfig.getConfig().getAsJsonArray("chests").remove(element);
                });
                gGOrbBot.chestConfig.saveConfig();
                Minecraft.getMinecraft().displayGuiScreen(new HomeEditorGui(gGOrbBot));
            }
        });
        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x + 5, y + 95, 70, 20, "Nein", Color.BLACK.getColor(), Color.GRAY.getColor(), Color.DARKRED.getColor(), Color.RED.getColor(), Color.RED.getColor()) {
            @Override
            public void performAction() {
                Minecraft.getMinecraft().displayGuiScreen(new HomeEditorGui(gGOrbBot));
            }
        });

        super.initGui();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int x = this.width / 2;
        int y = this.height / 2;

        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(10);

        drawTitle(x, y - 110);

        double yPos = y - 80 + this.scrollbar.getScrollY() + 10;
        for (Chest chest : chests) {
            drawEntry(x, yPos, chest);
            yPos += 20;
        }
        drawAmount(x, y);

        drawTopAndTitle(x, y);
        this.scrollbar.update(chests.size());
        this.scrollbar.draw();
        drawButtonsAndBottom(y, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button){
        buttonList.stream().filter(button::equals).forEach(b -> {
            if(b instanceof Button) {
                ((Button) b).performAction();
            }
        });
    }

    private void drawEntry(int x, double y, Chest chest) {
        drawRect(x - 100, (int)y + 15, x + 100, (int)y - 5, Color.GRAY.getColor());
        LabyMod.getInstance().getDrawUtils().drawRectBorder(x - 100, y + 14, x + 100, y - 5, Color.BLACK.getColor(), 1);
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), chest.toString(), x, (int)y, Color.WHITE.getColor());
    }

    private void drawTopAndTitle(int x, int y) {
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(0, y - 90);
        drawTitle(x, y - 110);
    }

    private void drawBottomBackground(int y) {
        LabyMod.getInstance().getDrawUtils().drawOverlayBackground(y + 90, this.height);
    }

    private void drawTitle(int x, int y) {
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Folgende Kisten werden gelöscht", x, y, Color.RED.getColor());
    }

    private void drawAmount(int x, int y) {
        y -= 5;
        drawRect(x + 120, y - 70, x + 180, y - 40, Color.GRAY.getColor());
        LabyMod.getInstance().getDrawUtils().drawRectBorder(x + 120, y - 70, x + 180, y - 40, Color.BLACK.getColor(), 1);
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Gesamt: ", x + 150, y - 65, Color.WHITE.getColor());
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), String.valueOf(chests.size()), x + 150, y - 55, Color.RED.getColor());
    }

    private void drawButtonsAndBottom(int y, int mouseX, int mouseY, float partialTicks) {
        this.buttonList.forEach(button -> {
            if(button instanceof de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button) {
                if(((de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button) button).getButtonLayer() == ButtonLayer.UNDER) {
                    button.drawButton(mc, mouseX, mouseY, partialTicks);
                }
            }
        });

        drawBottomBackground(y);

        this.buttonList.forEach(button -> {
            if(button instanceof de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button) {
                if(((Button) button).getButtonLayer() == ButtonLayer.ABOVE) {
                    button.drawButton(mc, mouseX, mouseY, partialTicks);
                }
            }
        });
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
