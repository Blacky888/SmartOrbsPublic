package de.smartbot_studios.ggorbbot.utils.hotkeyutils;

import java.util.ArrayList;
import java.util.List;

import de.smartbot_studios.ggorbbot.GGOrbBot;

public class HotKeyHandler {

    private List<SimpleHotKey> simpleHotKeys = new ArrayList<>();

    public HotKeyHandler() {
    }

    public void addHotkey(SimpleHotKey simpleHotKey) {
        this.simpleHotKeys.remove(simpleHotKey);
        this.simpleHotKeys.add(simpleHotKey);
    }

    public void executeHotkeys() {
       if(GGOrbBot.R) simpleHotKeys.forEach(SimpleHotKey::check);
    }
}
