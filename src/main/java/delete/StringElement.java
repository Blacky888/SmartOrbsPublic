package delete;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import net.labymod.api.LabyModAddon;
import net.labymod.core.LabyModCore;
import net.labymod.gui.elements.ModTextField;
import net.labymod.main.LabyMod;
import net.labymod.main.ModTextures;
import net.labymod.main.lang.LanguageManager;
import net.labymod.settings.LabyModModuleEditorGui;
import net.labymod.settings.PreviewRenderer;
import net.labymod.settings.elements.ControlElement;
import net.labymod.settings.elements.SettingsElement;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;

public class StringElement
extends ControlElement {
    private String currentValue, defaultValue;
    public ModTextField textField;
    private String attributeName;
    private boolean hoverExpandButton = false;
    private List<BiConsumer<Object, String>> callbacks = new ArrayList<>();
    private Object index;
    
    public StringElement() {
    	super("§4", null);
        textField = new ModTextField(-2, LabyModCore.getMinecraft().getFontRenderer(), 0, 0, getObjectWidth() - 5, 20);
        textField.setMaxStringLength(500);
        updateValue();
        textField.setCursorPositionEnd();
        textField.setFocused(false);
        callbacks.add((i, $) -> currentValue = $);
        setSettingEnabled(true);
    }
    
    public StringElement name(String name) { setDisplayName(name); return this; }
	public StringElement icon(Object o) {iconData = (o instanceof Material ? new IconData((Material) o) : (o instanceof IconData ? (IconData) o : new IconData((String)o)));return this;}
    public StringElement description(String desc) { setDescriptionText(desc); return this; }
	public StringElement subSettings(SettingsElement... subs) {subSettings.getElements().clear();Arrays.stream(subs).forEach(subSettings::add);return this;}
    public StringElement defaultValue(String defaultValue) {this.defaultValue = defaultValue;updateValue();return this;}
    public StringElement index(Object index) {this.index = index;return this;}
    public StringElement config(LabyModAddon addon, String attributeName) {
    	if(attributeName.equals(this.attributeName))
    		return this;
    	this.attributeName = attributeName;
        currentValue = !addon.getConfig().has(attributeName) ? defaultValue : addon.getConfig().get(attributeName).getAsString();
        callbacks.add((i, accepted) -> {
            addon.getConfig().addProperty(attributeName, accepted);
            addon.saveConfig();
            addon.loadConfig();
        });
    	return this;
    }
    
    @Override
    public String toString() {
    	return "StringElement(" + value() + ")";
    }
    
    public Object getIndex() {
    	return index;
    }
    
    private void updateValue() {
    	if(currentValue == null) currentValue = defaultValue;
        textField.setText(currentValue == null ? (defaultValue == null ? "" : defaultValue) : currentValue);
    }

    @Override
    public void draw(int x, int y, int maxX, int maxY, int mouseX, int mouseY) {
        super.draw(x, y, maxX, maxY, mouseX, mouseY);
        int width = getObjectWidth() - 5;
        if (textField == null) {
            return;
        }
        textField.xPosition = maxX - width - 2;
        textField.yPosition = y + 1;
        textField.drawTextBox();
        LabyMod.getInstance().getDrawUtils().drawRectangle(x - 1, y, x, maxY, ModColor.toRGB(120, 120, 120, 120));
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(ModTextures.BUTTON_EXPAND);
        hoverExpandButton = mouseX > maxX - getObjectWidth() - 12 && mouseX < maxX - getObjectWidth() - 7 + 8 && mouseY > y + 1 && mouseY < y + 1 + 8;
        LabyMod.getInstance().getDrawUtils().drawTexture(maxX - getObjectWidth() - 7, y + 1, 0.0, hoverExpandButton ? 130.0 : 0.0, 256.0, 128.0, 8.0, 8.0);
    }

    @Override
    public void unfocus(int mouseX, int mouseY, int mouseButton) {
        super.unfocus(mouseX, mouseY, mouseButton);
        if (hoverExpandButton) {
            hoverExpandButton = false;
            Minecraft.getMinecraft().displayGuiScreen(new ExpandedStringElementGui(textField, Minecraft.getMinecraft().currentScreen, new Consumer<ModTextField>(){

                @Override
                public void accept(ModTextField accepted) {
                	textField.setText(accepted.getText());
                    textField.setFocused(true);
                    textField.setCursorPosition(accepted.getCursorPosition());
                    textField.setSelectionPos(accepted.getSelectionEnd());
                    callbacks.forEach($ -> $.accept(index, accepted.getText()));
                }
            }));
        }
        textField.setFocused(false);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, 0);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (textField.textboxKeyTyped(typedChar, keyCode)) {
            callbacks.forEach($ -> $.accept(index, textField.getText()));
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        textField.updateCursorCounter();
    }

    public StringElement maxLength(int maxLength) {
        textField.setMaxStringLength(maxLength);
        return this;
    }

    public StringElement addCallback(BiConsumer<Object, String> callback) {
        callbacks.add(callback);
        return this;
    }

    @Override
    public int getObjectWidth() {
        return 85;
    }
    
    public String value() {
        return currentValue == null ? "" : currentValue;
    }
    
    public void setCurrentValue(String value) {
    	currentValue = value;
    	updateValue();
    	callbacks.forEach(c -> c.accept(index, currentValue));
    }

    public StringElement show(List<SettingsElement> parentList) {
    	if(parentList == null)
    		return this;
    	
    	int c = -1;
    	for(int i = 0; i < parentList.size(); i++)
    		if(parentList.get(i) instanceof PlaceholderElement && ((PlaceholderElement)parentList.get(i)).getParent().equals(this))
    			c = i;
    	
    	if(c != -1)
    		parentList.set(c, this);
    	
    	return this;
    }
    
    public boolean hidden(List<SettingsElement> parentList) {
    	for(int i = 0; i < parentList.size(); i++)
    		if(parentList.get(i) instanceof PlaceholderElement && ((PlaceholderElement)parentList.get(i)).getParent().equals(this))
    			return true;
    	return false;
    }
    
    public StringElement setHidden(List<SettingsElement> parentList, boolean hidden) {
		return hidden ? hide(parentList) : show(parentList);
	}
    
	public StringElement hide(List<SettingsElement> parentList) {
		if(parentList == null || !parentList.contains(this))
			return this;
		
		parentList.set(parentList.indexOf(this), new PlaceholderElement(this));
		
		return this;
	}
    
    public class ExpandedStringElementGui
    extends GuiScreen {
        private GuiScreen backgroundScreen;
        private Consumer<ModTextField> callback;
        private ModTextField preField;
        private ModTextField expandedField;

        public ExpandedStringElementGui(ModTextField preField, GuiScreen backgroundScreen, Consumer<ModTextField> callback) {
            this.backgroundScreen = backgroundScreen;
            this.callback = callback;
            this.preField = preField;
        }

        public void initGui() {
            super.initGui();
            backgroundScreen.width = width;
            backgroundScreen.height = height;
            if (backgroundScreen instanceof LabyModModuleEditorGui) {
                PreviewRenderer.getInstance().init(ExpandedStringElementGui.class);
            }
            expandedField = new ModTextField(0, LabyModCore.getMinecraft().getFontRenderer(), width / 2 - 150, height / 4 + 45, 300, 20);
            expandedField.setMaxStringLength(preField.getMaxStringLength());
            expandedField.setFocused(true);
            expandedField.setText(preField.getText());
            expandedField.setCursorPosition(preField.getCursorPosition());
            expandedField.setSelectionPos(preField.getSelectionEnd());
            buttonList.add(new GuiButton(1, width / 2 - 50, height / 4 + 85, 100, 20, LanguageManager.translate("button_done")));
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            backgroundScreen.drawScreen(mouseX, mouseY, partialTicks);
            ExpandedStringElementGui.drawRect(0, 0, width, height, Integer.MIN_VALUE);
            ExpandedStringElementGui.drawRect((width / 2 - 165), (height / 4 + 35), (width / 2 + 165), (height / 4 + 120), Integer.MIN_VALUE);
            expandedField.drawTextBox();
            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            expandedField.mouseClicked(mouseX, mouseY, mouseButton);
            callback.accept(expandedField);
        }

        protected void keyTyped(char typedChar, int keyCode) throws IOException {
            if (keyCode == 1) {
                Minecraft.getMinecraft().displayGuiScreen(backgroundScreen);
            }
            if (expandedField.textboxKeyTyped(typedChar, keyCode)) {
                callback.accept(expandedField);
            }
        }

        public void updateScreen() {
            backgroundScreen.updateScreen();
            expandedField.updateCursorCounter();
        }

        protected void actionPerformed(GuiButton button) throws IOException {
            super.actionPerformed(button);
            if (button.id == 1) {
                Minecraft.getMinecraft().displayGuiScreen(backgroundScreen);
            }
        }

        public GuiScreen getBackgroundScreen() {
            return backgroundScreen;
        }
    }
}
 