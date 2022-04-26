package de.smartbot_studios.ggorbbot.listeners;

import static net.minecraft.client.renderer.RenderGlobal.renderFilledBox;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class RenderListener {

    private Minecraft mc;
    private GGOrbBot gGOrbBot;

    public RenderListener(GGOrbBot gGOrbBot) {
        this.gGOrbBot = gGOrbBot;
        mc = Minecraft.getMinecraft();
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(gGOrbBot.pickingChests) {
            gGOrbBot.chestsToBeAdded.forEach(pos -> {
                drawBox(mc.player, pos, event.getPartialTicks(), 25, 250, 32, 60);
            });

            gGOrbBot.existingChests.forEach(pos -> {
                drawBox(mc.player, pos, event.getPartialTicks(), 56, 132, 255, 120);
            });
        }
    }

    private void drawBox(EntityPlayer player, BlockPos blockpos, float partialTicks, int r, int g, int b, int a) {
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.glLineWidth(2.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        IBlockState iblockstate = mc.world.getBlockState(blockpos);

        if (iblockstate.getMaterial() != Material.AIR && mc.world.getWorldBorder().contains(blockpos)) {
            double d3 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTicks;
            double d4 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTicks;
            double d5 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTicks;

            renderFilledBox(iblockstate.getSelectedBoundingBox(mc.world, blockpos).grow(0.0020000000949949026D).offset(-d3, -d4, -d5), r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F);
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
}