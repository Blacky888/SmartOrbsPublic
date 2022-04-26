package de.smartbot_studios.ggorbbot.utils.hotkeyutils;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public abstract class SimpleHotKey {

    private int keycode = 0;
    private int cooldown;
    private long lastCheck = 0;

    public SimpleHotKey() {
    }

    public abstract void execute();

    public void check() {
        if(Minecraft.getMinecraft().currentScreen == null) {
            if (isPressed()) {
                if (System.currentTimeMillis() - lastCheck >= cooldown) {
                    lastCheck = System.currentTimeMillis();
                    execute();
                }
            }
        }
    }

    private boolean isPressed () {
        if(keycode < 0) return false;
        return Keyboard.isKeyDown(keycode);
    }

    public SimpleHotKey setKeycode(int newKeycode) {
        this.keycode = newKeycode;
        return this;
    }

    public SimpleHotKey setCooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }
}
