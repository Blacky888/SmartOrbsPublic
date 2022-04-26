package de.smartbot_studios.ggorbbot.listeners;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import net.labymod.api.events.MessageReceiveEvent;

import java.io.InputStream;

public class ReceiveMessageListener implements MessageReceiveEvent {

    GGOrbBot gGOrbBot;
    public ReceiveMessageListener(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;
    }

    @Override
    public boolean onReceive(String s, String s1) {
        @SuppressWarnings("unused")
        String[] args = s1.split(" ");

        gGOrbBot.movementHandler.onReceive(s);
        return false;
    }
}
