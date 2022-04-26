package de.smartbot_studios.ggorbbot.listeners;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.utils.runnables.ChestRunnable;
import de.smartbot_studios.ggorbbot.utils.runnables.KammerRunnable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class TickListener {

    GGOrbBot gGOrbBot;
    public TickListener(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;
    }
    
    
    private Thread process;
    private boolean isProcessing = false;
    private boolean isChatting = false;
    
	public BlockPos targetPos =null;

	@SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent clientTickEvent) {
	    Minecraft.getMinecraft().gameSettings.pauseOnLostFocus = false;

        gGOrbBot.hotKeyHandler.executeHotkeys();
        if(gGOrbBot.enabled) {
            if(Minecraft.getMinecraft().gameSettings.pauseOnLostFocus) Minecraft.getMinecraft().gameSettings.pauseOnLostFocus = false;
        }

        if(!gGOrbBot.enabled && gGOrbBot.orbMode != null) {
            gGOrbBot.enabled = true;
        }

        if(gGOrbBot.enabled && !isProcessing && gGOrbBot.orbMode != null) {
            isProcessing = true;
            System.out.println("mode: " + gGOrbBot.orbMode);
            switch (gGOrbBot.orbMode) {
                case CHESTMODE: {
                    process = new Thread(new ChestRunnable(gGOrbBot));
                    break;
                }
                case AFKPOSMODE: {
                    process = new Thread(new KammerRunnable(gGOrbBot, false));
                    break;
                }
                case AFKAURAMODE: {
                    process = new Thread(new KammerRunnable(gGOrbBot, true));
                    break;
                }
            }
            process.start();
            System.out.println("process started");
        } else if(!gGOrbBot.enabled && isProcessing) {
            isProcessing = false;
            new Thread(() -> {
                process.interrupt();
                delay(2500);
                process.stop();

                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindSprint.getKeyCode(), false);
                KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
            }).start();
        }
        if(!gGOrbBot.chatQueue.isEmpty() && !isChatting) {
            isChatting = true;
            new Thread(() -> {
                while (!gGOrbBot.chatQueue.isEmpty()) {
                    Minecraft.getMinecraft().player.sendChatMessage(gGOrbBot.chatQueue.poll());
                    delay(1500);
                }
                isChatting = false;
            }).start();
        }
    }

    private void delay (int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
