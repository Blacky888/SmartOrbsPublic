package de.smartbot_studios.ggorbbot.listeners;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.MsgConfig;
import de.smartbot_studios.ggorbbot.utils.commandutils.CommandHandler;
import net.labymod.api.events.MessageSendEvent;
import net.labymod.main.LabyMod;

public class SendMessageListener implements MessageSendEvent {

    GGOrbBot gGOrbBot;
    public SendMessageListener(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;
    }

    @Override
    public boolean onSend(String s) {
        @SuppressWarnings("unused")
		String[] args = s.split(" ");
        if(new CommandHandler(gGOrbBot).execute(s)) {
            return true;
        } else if (s.startsWith(gGOrbBot.cmdprefix)) {

            LabyMod.getInstance().displayMessageInChat(gGOrbBot.prefix + MsgConfig.COMMAND_NOTFOUND.get());
            return true;
        } else if(s.startsWith("test")) {
            return true;
        }
        return false;
    }
}
