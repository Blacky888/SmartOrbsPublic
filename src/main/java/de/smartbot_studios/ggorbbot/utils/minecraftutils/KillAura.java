package de.smartbot_studios.ggorbbot.utils.minecraftutils;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class KillAura {

    private int lookCount = 0;
    private int hitCount = 0;
    private boolean active;
    private EntityLiving target;

    private final GGOrbBot ggOrbBot;
    public KillAura(GGOrbBot ggOrbBot) {
        this.ggOrbBot = ggOrbBot;
    }

    public void exec(EntityPlayerSP entityPlayer) {
        if (target != null) {
            check();
            if (target.isEntityAlive()) {
                if (lookCount >= 5) {
                    HashMap<Double, EntityLiving> entities = new HashMap<>();
                    entityPlayer.world.getLoadedEntityList().forEach(entity -> {
                        if (entity instanceof EntityLiving) {
                            double distance = entity.getDistance(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
                            if (distance < 5) {
                                entities.put(distance, (EntityLiving) entity);
                            }
                        }
                    });
                    if (!entities.isEmpty()) {
                        Stream<Map.Entry<Double, EntityLiving>> entitiesSorted = entities.entrySet().stream().sorted(
                                Map.Entry.comparingByKey()
                        );
                        targetEntity(entitiesSorted.findFirst().get().getValue());
                    }
                    Player.unmodifiedlookAt(target.posX, target.posY, target.posZ);
                    lookCount = 0;
                }
                if (hitCount >= 20 / entityPlayer.getAttributeMap().getAttributeInstanceByName("generic.attackSpeed").getAttributeValue() + 1) {
                    hitCount = 0;
                    if (entityPlayer.getDistance(target) <= 5) {
                        Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                        Minecraft.getMinecraft().playerController.attackEntity(entityPlayer, target);
                    }
                }
            } else target = null;
        } else {
            HashMap<Double, EntityLiving> entities = new HashMap<>();
            entityPlayer.world.getLoadedEntityList().forEach(entity -> {
                if (entity instanceof EntityLiving) {
                    double distance = entity.getDistance(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
                    if (distance < 5) {
                        entities.put(distance, (EntityLiving) entity);
                    }
                }
            });
            if (!entities.isEmpty()) {
                Stream<Map.Entry<Double, EntityLiving>> entitiesSorted = entities.entrySet().stream().sorted(
                        Map.Entry.comparingByKey()
                );
                targetEntity(entitiesSorted.findFirst().get().getValue());
            }
        }
        lookCount++;
        hitCount++;
    }

    private void targetEntity(EntityLiving entityLiving) {
        this.target = entityLiving;
    }

    public void check() {

        ItemStack is = Minecraft.getMinecraft().player.getHeldItemMainhand();

        boolean find = false;
        if(!is.getItem().equals(Item.getItemById(276)) && ggOrbBot.auraPos != null) {
            Player.openChest(ggOrbBot.auraPos);
            for (int i = 0; i < Minecraft.getMinecraft().player.openContainer.getInventory().size() - 36; i++) {
                ItemStack stack = Minecraft.getMinecraft().player.openContainer.getSlot(i).getStack();

                if(stack.getItemDamage() < stack.getItem().getMaxDamage(stack) - 10) {
                    Inventory.transferItemFromTo(i, 36, 0);
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Minecraft.getMinecraft().player.inventory.currentItem = 1;

                    Minecraft.getMinecraft().player.closeScreen();

                    find = true;

                    break;
                }
            }
        }
        if(is.getItem().equals(Item.getItemById(276))) find = true;

        if(!find) ggOrbBot.enabled = false;
    }
}
