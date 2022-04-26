package de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.guiutils;

import net.labymod.main.LabyMod;
import net.labymod.utils.ModColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public abstract class Button extends GuiButton {

    private String texturePath;

    private int startY;
    private int width;
    private int height;
    private int borderColor;
    private int backgroundColor;
    private int hoveredColor;
    private int textColor = 0;
    private int textHoveredColor = 0;
    private int distance;

    private String displayString;

    private ButtonLayer buttonLayer;
    
    private ButtonPosition buttonPosition;

    /**
     * @param buttonLayer whether the button should be above or under the background
     * @param x the x of the button
     * @param y the y of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param displayString the displayString of the button
     * @param borderColor the borderColor of the button
     * @param backgroundColor the backgroundColor of the button
     * @param hoveredColor the color of the button when it's hovered
     * @param buttonPosition whether the Button can be moved
     */
    public Button(ButtonLayer buttonLayer, ButtonPosition buttonPosition, int x, int y, int width, int height, String displayString, int borderColor, int backgroundColor, int hoveredColor) {
        super(0, x, y, width, height, displayString);
        this.width = width;
        this.height = height;
        this.displayString = displayString;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.hoveredColor = hoveredColor;
        this.texturePath = null;
        this.buttonLayer = buttonLayer;
        this.buttonPosition = buttonPosition;
        this.startY = y;
    }

    /**
     * @param buttonLayer whether the button should be above or under the background
     * @param x the this.x of the button
     * @param y the this.y of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param displayString the displayString of the button
     * @param borderColor the borderColor of the button
     * @param backgroundColor the backgroundColor of the button
     * @param hoveredColor the color of the button when it's hovered
     * @param textColor the color of the text
     * @param textHoveredColor the color of the text when it's hovered
     * @param buttonPosition whether the Button can be moved
     */
    public Button(ButtonLayer buttonLayer, ButtonPosition buttonPosition, int x, int y, int width, int height, String displayString, int borderColor, int backgroundColor, int hoveredColor, int textColor, int textHoveredColor) {
        super(0, x, y, width, height, displayString);
        this.width = width;
        this.height = height;
        this.displayString = displayString;
        this.borderColor = borderColor;
        this.backgroundColor = backgroundColor;
        this.hoveredColor = hoveredColor;
        this.texturePath = null;
        this.buttonLayer = buttonLayer;
        this.textColor = textColor;
        this.textHoveredColor = textHoveredColor;
        this.buttonPosition = buttonPosition;
        this.startY = y;
    }

    /**
     * @param buttonLayer whether the button should be above or under the background
     * @param x the this.x of the button
     * @param y the this.y of the button
     * @param width the width of the button
     * @param height the height of the button
     * @param texturePath the path to the texture
     * @param buttonPosition whether the Button can be moved
     */
    public Button(ButtonLayer buttonLayer, ButtonPosition buttonPosition, int x, int y, int width, int height, String displayString, String texturePath) {
        super(0, x, y, width, height, displayString);
        this.width = width;
        this.height = height;
        this.displayString = displayString;
        this.texturePath = texturePath;
        this.buttonLayer = buttonLayer;
        this.buttonPosition = buttonPosition;
        this.startY = y;
    }

    public abstract void performAction();

    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partial) {
        GlStateManager.color(100, 100, 100, 100);

        if(buttonPosition == ButtonPosition.MOVABLE) this.y = this.startY + this.distance;

        if(!this.visible)
            return;

        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

        if(this.texturePath != null) {
            mc.getTextureManager().bindTexture(new ResourceLocation(this.texturePath));
            drawModalRectWithCustomSizedTexture(this.x, this.y, 0, 0, this.width, this.height, this.width, this.height);
            if(hovered) {
                drawRect(this.x, this.y, this.x + this.width, this.y + this.height, ModColor.toRGB(64, 64, 64, 60));
            }
        } else {
            if(hovered) {
                if(hoveredColor == 0) {
                    drawRect(this.x, this.y, x + this.width, this.y + this.height, ModColor.toRGB(64, 64, 64, 60));
                } else {
                    drawRect(this.x, this.y, x + this.width, this.y + this.height, hoveredColor);
                }
            } else {
                drawRect(this.x, this.y, x + this.width, this.y + this.height, backgroundColor);
            }
        }

        LabyMod.getInstance().getDrawUtils().drawRectBorder(this.x, this.y,this.x + this.width, this.y + this.height, borderColor, 1.0D);

        this.mouseDragged(mc, mouseX, mouseY);

        if(textColor != 0) {
            if(hovered) {
                this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 6) / 2, textHoveredColor);
            } else {
                this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 6) / 2, textColor);
            }
        } else {
            int color = 14737632;
            if (this.packedFGColour != 0)
                color = packedFGColour;
            else if (!this.enabled)
                color = 10526880;
            else if (this.hovered)
                color = 16777120;
            this.drawCenteredString(mc.fontRenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 6) / 2, color);
        }
    }

    public ButtonLayer getButtonLayer() {
        return buttonLayer;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}
