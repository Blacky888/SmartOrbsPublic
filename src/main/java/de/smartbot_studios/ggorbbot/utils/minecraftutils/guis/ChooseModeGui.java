package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.MsgConfig;
import de.smartbot_studios.ggorbbot.baritone.Baritone;
import de.smartbot_studios.ggorbbot.utils.OrbMode;
import de.smartbot_studios.ggorbbot.utils.javautils.Color;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Player;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.Button;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonLayer;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.ButtonPosition;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils.CustomGuiScreen;
import net.labymod.gui.elements.Scrollbar;
import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;

public class ChooseModeGui extends CustomGuiScreen {

    private Scrollbar scrollbar = new Scrollbar(20);

    private GGOrbBot gGOrbBot;
    public ChooseModeGui(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;
    }

    @Override
    public void initGui() {
        int x = this.width / 2;
        int y = this.height / 2;

        this.scrollbar.setPosition(x + 205, y - 80, x + 210, y + 80);
        this.scrollbar.setSpeed(10);

        int yPos = y - 100;
        for (OrbMode mode : OrbMode.values()) {
            this.buttonList.add(new Button(ButtonLayer.UNDER, ButtonPosition.MOVABLE, x - 100, yPos + 25, 200, 20, mode.getValue(), Color.GRAY.getColor(), Color.LIGHTGRAY.getColor(), Color.LIGHTBLUE.getColor()) {
                @Override
                public void performAction() {
                    boolean canContinue = true;
                    switch (mode) {
                        case CHESTMODE: {
                            if(!Baritone.checkForBaritone()) {
                                if (!gGOrbBot.chestModeRequireNotNull.check()) {
                                    Player.sendNulls(gGOrbBot.prefix, gGOrbBot.chestModeRequireNotNull.getNulls());
                                    canContinue = false;
                                }
                            } else {
                                if (!gGOrbBot.baritoneChestRequireNotNull.check()) {
                                    Player.sendNulls(gGOrbBot.prefix, gGOrbBot.baritoneChestRequireNotNull.getNulls());
                                    canContinue = false;
                                }
                            }
                            break;
                        }
                        case AFKPOSMODE: {
                            if(!Baritone.checkForBaritone()) {
                                if (!gGOrbBot.afkModeRequireNotNull.check()) {
                                    Player.sendNulls(gGOrbBot.prefix, gGOrbBot.afkModeRequireNotNull.getNulls());
                                    canContinue = false;
                                }
                            } else {
                                if (!gGOrbBot.baritoneAfkRequireNotNull.check()) {
                                    Player.sendNulls(gGOrbBot.prefix, gGOrbBot.baritoneAfkRequireNotNull.getNulls());
                                    canContinue = false;
                                }
                            }
                            break;
                        }
                        case AFKAURAMODE:
                            if(!Baritone.checkForBaritone()) {
                                if (!gGOrbBot.afkAuraModeRequireNotNull.check()) {
                                    Player.sendNulls(gGOrbBot.prefix, gGOrbBot.afkAuraModeRequireNotNull.getNulls());
                                    canContinue = false;
                                }
                            } else {
                                if (!gGOrbBot.baritoneAfkRequireNotNull.check()) {
                                    Player.sendNulls(gGOrbBot.prefix, gGOrbBot.afkModeRequireNotNull.getNulls());
                                    canContinue = false;
                                }
                            }
                            break;
                    }

                    if(!canContinue) {
                        Minecraft.getMinecraft().displayGuiScreen(null);
                        return;
                    }
                    gGOrbBot.orbMode = mode;
                    Minecraft.getMinecraft().displayGuiScreen(null);
                    LabyMod.getInstance().displayMessageInChat(gGOrbBot.prefix + MsgConfig.GGORBBOTMODE_ACTIVATED.get());
                }
            });
            yPos += 20;
        }
    }

    @Override
    public void drawScreen() {
        int x = this.width / 2;
        int y = this.height / 2;

        drawOverlayBackground(0, y - 90);
        this.scrollbar.draw();
        drawTitle(x, y - 110);
        drawOverlayBackground(y + 90, this.height);
    }

    private void drawTitle(int x, int y) {
        drawCenteredString(LabyMod.getInstance().getDrawUtils().getFontRenderer(), "Wähle eine Option aus!", x, y, ModColor.toRGB(255, 255, 255, 120));
    }
}
