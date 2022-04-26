package de.smartbot_studios.ggorbbot.utils.commandutils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.commands.SetAuraPosCommand;

public class CommandHandler {
    private List<Command> commandList = new ArrayList<>();

    GGOrbBot gGOrbBot;
    public CommandHandler(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;
        addCommand(new SetAuraPosCommand(gGOrbBot));
    }

    public void addCommand(Command command) {
        this.commandList.add(command);
    }

    public boolean execute(String text) {
        if(!text.startsWith(gGOrbBot.cmdprefix)) {
            return false;
        }
        text = text.substring(gGOrbBot.cmdprefix.length()+1);
        String[] arguments = text.split(" ");
        for(Command command : this.commandList) {

            if(command.getName().equalsIgnoreCase(arguments[0])) {

                String[] args = Arrays.copyOfRange(arguments, 1, arguments.length);
                command.execute(args);
                return true;
            }
        }
        return false;
    }
}
