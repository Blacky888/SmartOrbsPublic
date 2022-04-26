package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.javautils.Color;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonLayer;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonPosition;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.CustomGuiScreen;
import net.labymod.gui.elements.ModTextField;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;

public class AddHomeGui extends CustomGuiScreen {

    private ModTextField home;

    private boolean error = false;
    private boolean error1 = false;
    private boolean error2 = false;

    GGOrbBot gGOrbBot;
    public AddHomeGui(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;
    }

    @Override
    public void initGui() {
        int x = this.width / 2;
        int y = this.height / 2;

        home = new ModTextField(-1, LabyMod.getInstance().getDrawUtils().getFontRenderer(), x - 75, y - 10, 150, 20);
        home.setMaxStringLength(40);

        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x - 75, y + 95, 70, 20, "Bestätigen", Color.BLACK.getColor(), Color.GRAY.getColor(), Color.DARKGREEN.getColor(), Color.GREEN.getColor(), Color.GREEN.getColor()) {
            @Override
            public void performAction() {
                error = false;
                error1 = false;
                error2 = false;
                String input = home.getText();

                AtomicBoolean notExisting = new AtomicBoolean(true);
                gGOrbBot.chestHomeConfig.getConfig().get("homes").getAsJsonArray().forEach(element -> {
                    if(element.getAsString().equals(input)) {
                        notExisting.set(false);
                    }
                });
                if(notExisting.get()) {
                    if(home.getText().equals("")) {
                        error1 = true;
                    } else {
                        System.out.println(input);

                        String phRegex = "(/p h [\\w]+)";
                        String homeRegex = "(/home )[\\w]+";
                        Pattern pattern = Pattern.compile(phRegex);
                        Matcher matcher = pattern.matcher(input);
                        pattern = Pattern.compile(homeRegex);
                        Matcher matcher1 = pattern.matcher(input);

                        if(!matcher.find() && !matcher1.find()) {
                            error2 = true;
                            return;
                        }

                        gGOrbBot.chestHomeConfig.getConfig().getAsJsonArray("homes").add(input);
                        gGOrbBot.chestHomeConfig.saveConfig();

                        Minecraft.getMinecraft().displayGuiScreen(new HomeEditorGui(gGOrbBot));
                    }
                } else {
                    error = true;
                }
            }
        });
        this.buttonList.add(new Button(ButtonLayer.ABOVE, ButtonPosition.ATTACHED, x + 5, y + 95, 70, 20, "Abbrechen", Color.BLACK.getColor(), Color.GRAY.getColor(), Color.DARKRED.getColor(), Color.RED.getColor(), Color.RED.getColor()) {
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

        LabyMod.getInstance().getDrawUtils().drawAutoDimmedBackground(10);
        drawErrors(x, y - 30);
        home.drawTextBox();
        drawOverlayBackground(0, y - 90);
        drawTitle(x, y - 110);
        drawOverlayBackground(y + 90, this.height);
    }

    private void drawTitle(int x, int y) {
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Gib deinen Homepunkt an!", x, y, ModColor.toRGB(255, 255, 255, 120));
    }

    private void drawErrors(int x, int y) {
        if(error) {
            drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Dieses Home existiert bereits!", x, y, Color.RED.getColor());
        }
        if(error1) {
            drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Das Home darf nicht leer sein!", x, y, Color.RED.getColor());
        }
        if(error2) {
            drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "/p h … oder /home …", x, y, Color.RED.getColor());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.home.mouseClicked(mouseX, mouseY, mouseButton);

        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        this.home.textboxKeyTyped(typedChar, keyCode);

        super.keyTyped(typedChar, keyCode);
    }
}
