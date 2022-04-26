package de.smartbot_studios.ggorbbot.utils.minecraftutils;

import com.google.gson.JsonObject;

import net.minecraft.client.Minecraft;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Inventory {
    public static void quickMoveFromSlot(int slot) {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.openContainer.windowId, slot, 0, ClickType.QUICK_MOVE, Minecraft.getMinecraft().player);
        delay(250);
    }

    public static boolean checkForEmptySlot() {
        boolean toReturn = false;

        ContainerChest containerChest = (ContainerChest) Minecraft.getMinecraft().player.openContainer;

        for(int i = 0; i < containerChest.getLowerChestInventory().getSizeInventory(); i++) {
            if(Minecraft.getMinecraft().player.openContainer.getInventory().get(i).isEmpty()) {
                toReturn = true;
                break;
            }
        }

        return toReturn;
    }

    public static void drop() {
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, 44, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        delay(200);
        Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.inventoryContainer.windowId, -999, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
        delay(200);
    }
    public static void transferItemFromTo(int slotToGetFrom, int targetSlot, int amount) {
        delay(50);

        Slot slot = Minecraft.getMinecraft().player.openContainer.getSlot(slotToGetFrom);

        if (slot.getStack().getCount() == amount || amount == 0) {
            log("getting stack");
            Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.openContainer.windowId, slotToGetFrom, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
            delay(50);
            log("putting stack into target slot");
            Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.openContainer.windowId, targetSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
            delay(125);
        } else if (slot.getStack().getCount() / 2 == amount) {
            log("splitting stack");
            Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.openContainer.windowId, slotToGetFrom, 1, ClickType.PICKUP, Minecraft.getMinecraft().player);
            delay(125);
            log("putting half into target slot");
            Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.openContainer.windowId, targetSlot, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
            delay(125);
        } else {
            log("getting stack");
            Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.openContainer.windowId, slotToGetFrom, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
            delay(125);

            for (int i = 0; i < amount; i++) {
                log("putting item " + (i + 1) + " into target slot");
                Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.openContainer.windowId, targetSlot, 1, ClickType.PICKUP, Minecraft.getMinecraft().player);
                delay(125);
            }

            log("putting rest back");
            Minecraft.getMinecraft().playerController.windowClick(Minecraft.getMinecraft().player.openContainer.windowId, slotToGetFrom, 0, ClickType.PICKUP, Minecraft.getMinecraft().player);
            delay(125);
        }
    }

    public static int getSlotWithItem(JsonObject object) {
        String displayname = object.get("displayname").getAsString();
        int amount = object.get("amount").getAsInt();
        int id = object.get("id").getAsInt();
        int subid = object.get("subid").getAsInt();
        String enchantments = object.get("enchantments").getAsString();
        String lore = object.get("lore").getAsString();

        int slot = -1;

        ContainerChest containerChest = (ContainerChest) Minecraft.getMinecraft().player.openContainer;

        for(int i = 0; i < containerChest.getLowerChestInventory().getSizeInventory(); i++) {
            if(!containerChest.getLowerChestInventory().getStackInSlot(i).isEmpty()) {
                ItemStack itemStack = containerChest.getLowerChestInventory().getStackInSlot(i);

                String itemStackLore;
                try {
                    itemStackLore = itemStack.getTagCompound().getCompoundTag("display").getTag("Lore").toString();
                } catch (Exception e) {
                    itemStackLore = "";
                }

                if (displayname.equals(itemStack.getDisplayName()) &&
                        amount <= itemStack.getCount() &&
                        id == Item.getIdFromItem(itemStack.getItem()) &&
                        subid == itemStack.getMetadata() &&
                        enchantments.equals(itemStack.getEnchantmentTagList().toString()) &&
                        lore.equals(itemStackLore)) {
                    System.out.println(i);
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }

    public static int getSlotWithItemInWorkbench(int objectId, JsonObject object) {
        String displayname = object.get(objectId + "displayname").getAsString();
        int amount = object.get(objectId + "amount").getAsInt();
        int id = object.get(objectId + "id").getAsInt();
        int subid = object.get(objectId + "subid").getAsInt();
        String lore = object.get(objectId + "lore").getAsString();

        int slot = -1;

        ContainerWorkbench containerWorkbench = (ContainerWorkbench) Minecraft.getMinecraft().player.openContainer;

        for(int i = 0; i < containerWorkbench.getInventory().size(); i++) {
            if(!containerWorkbench.getSlot(i).getStack().isEmpty()) {
                ItemStack itemStack = containerWorkbench.getSlot(i).getStack();

                String itemStackLore;
                try {
                    itemStackLore = itemStack.getTagCompound().getCompoundTag("display").getTag("Lore").toString();
                } catch (Exception e) {
                    itemStackLore = "";
                }

                if (displayname.equals(itemStack.getDisplayName()) &&
                        amount <= itemStack.getCount() &&
                        id == Item.getIdFromItem(itemStack.getItem()) &&
                        subid == itemStack.getMetadata() &&
                        lore.equals(itemStackLore)) {
                    System.out.println(i);
                    slot = i;
                    break;
                }
            }
        }
        return slot;
    }

    public static int getIdFromItemInHand() {
        return Item.getIdFromItem(Minecraft.getMinecraft().player.getHeldItemMainhand().getItem());
    }

    public static int getSubIdFromItemInHand() {
        return Minecraft.getMinecraft().player.getHeldItemMainhand().getMetadata();
    }

    public static int getAmountOfItemInHand() {
        return Minecraft.getMinecraft().player.getHeldItemMainhand().getCount();
    }

    public static String getLoreOfItemInHand() {
        String lore;
        try {
            lore = Minecraft.getMinecraft().player.getHeldItemMainhand().getTagCompound().getCompoundTag("display").getTag("Lore").toString();
        } catch (NullPointerException e) {
            lore = "";
        }
        return lore;
    }

    public static String getEnchantmentsOfItemInHand() {
        return Minecraft.getMinecraft().player.getHeldItemMainhand().getEnchantmentTagList().toString();
    }

    public static String getDisplayNameOfItemInHand() {
        return Minecraft.getMinecraft().player.getHeldItemMainhand().getDisplayName();
    }

    private static void delay(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void log(String text) {
        System.out.println(text);
    }
}

