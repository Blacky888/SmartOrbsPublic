package de.smartbot_studios.ggorbbot.utils.minecraftutils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import de.smartbot_studios.ggorbbot.GGOrbBot;
import de.smartbot_studios.ggorbbot.baritone.Baritone;
import de.smartbot_studios.ggorbbot.utils.javautils.PositionUtils;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.path.newpathutils.Paths;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.path.pathutils.Position;
import net.labymod.main.LabyMod;
import net.minecraft.block.BlockChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class Player {

    private static Entity orbHandler;

    public static void goTo(BlockPos blockPos) {
        goTo(blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    public static void goTo(int x, int y, int z) {
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        Position position1 = new Position(player.posX, player.posY, player.posZ);
        Position position2 = new Position(x, y, z);
        while (PositionUtils.getDistance(position1, position2) > 1.3) {
            KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), true);
            lookAt(x, y + 2, z);
            position1 = new Position(player.posX, player.posY, player.posZ);
            System.out.println(PositionUtils.getDistance(position1, position2));
            delay(50);
        }
        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode(), false);
    }

    public static void sendNulls(String prefix, List<String> nulls) {
        LabyMod.getInstance().displayMessageInChat(prefix + "Folgende Werte wurden nicht gesetzt!");
        nulls.forEach(element -> {
            LabyMod.getInstance().displayMessageInChat(prefix + "- " + element);
        });
    }

    public static void clearInventory() {
        for (int i = 9; i < 45; i++) {
            if (!Minecraft.getMinecraft().player.openContainer.getSlot(i).getStack().isEmpty() && !Minecraft.getMinecraft().player.openContainer.getSlot(i).getStack().getItem().equals(Item.getItemById(276))) {
                Inventory.transferItemFromTo(i, -999, 0);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getNextSlotInChestWithItem() {
        int toReturn = -1;
        ContainerChest containerChest = (ContainerChest) Minecraft.getMinecraft().player.openContainer;

        int size = containerChest.getLowerChestInventory().getSizeInventory();

        for(int i = 0; i < size; i++) {
            if(!containerChest.getLowerChestInventory().getStackInSlot(i).isEmpty()) {
                toReturn = i;
                break;
            }
        }
        return toReturn;
    }

    public static void emptyChest() {
        try {
            Thread.sleep(pingDelay10());//500
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        while (!inventoryFull()) {
            if(!chestEmpty()) {
                Inventory.quickMoveFromSlot(getNextSlotInChestWithItem());
                int delay = 250 + random.nextInt(250);
                System.out.println(delay);
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

    public static boolean chestEmpty() {
        boolean toReturn = true;

        ContainerChest containerChest = (ContainerChest) Minecraft.getMinecraft().player.openContainer;

        for (int i = 0; i < containerChest.getLowerChestInventory().getSizeInventory(); i++) {
            if (!containerChest.getLowerChestInventory().getStackInSlot(i).isEmpty()) toReturn = false;
        }
        return toReturn;
    }

    public static boolean inventoryFull() {
        boolean toReturn = true;

        int size = Minecraft.getMinecraft().player.openContainer.getInventory().size();
        if(size == 46) {
            for (int i = 9; i < size - 1; i++) {
                if (Minecraft.getMinecraft().player.openContainer.getSlot(i).getStack().isEmpty())
                    toReturn = false;
            }
        } else {
            for(int i = size - 36; i < size; i++) {
                if (Minecraft.getMinecraft().player.openContainer.getSlot(i).getStack().isEmpty())
                    toReturn = false;
            }
        }

        return toReturn;
    }

    public static boolean chestOpened() {
        return Minecraft.getMinecraft().player.openContainer instanceof ContainerChest;
    }

    public static void setPosition(float yaw, float pitch){
        double xpos = Minecraft.getMinecraft().player.prevPosX; /*xpos*/
        double ypos = Minecraft.getMinecraft().player.prevPosY; /*xpos*/
        double zpos = Minecraft.getMinecraft().player.prevPosZ; /*zpos*/
        Minecraft.getMinecraft().player.setPositionAndRotation(xpos,ypos,zpos,yaw,pitch);
    }

    public static void lookAtBlock(BlockPos pos) {
		if (pos == null)
			return;
		EntityPlayerSP p = Minecraft.getMinecraft().player;
		double x = pos.getX() - (p.posX - 0.5d), z = pos.getZ() - (p.posZ - 0.5d);
		double yaw = Math.toDegrees(Math.atan(z / x));
		yaw += x < 0 ? 90 : -90;
		double y = Math.sqrt((x * x) + (z * z));
		double pitch = Math.toDegrees(Math.atan((pos.getY() - (p.posY + 1)) / y));
		setPosition((float) yaw, (float) -pitch);
	}


    public static void lookAt(Integer x, Integer y, Integer z) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Minecraft minecraft = Minecraft.getMinecraft();
                EntityPlayerSP player = minecraft.player;
                double dx = x - player.posX + 0.5;
                double dy = y - player.posY - 1;
                double dz = z - player.posZ + 0.5;

                double dlen = Math.sqrt(dx * dx + dy * dy + dz * dz);
                dx /= dlen;
                dy /= dlen;
                dz /= dlen;

                double pitch = Math.asin(dy);
                double yaw = Math.atan2(dz, dx);
                pitch = pitch * 180.0D / Math.PI;
                yaw = yaw * 180.0D / Math.PI;
                yaw -= 90.0D;

                while (yaw > 360) yaw -= 360;
                while (yaw < -360) yaw += 360;

                player.rotationPitch = -((float) pitch);
                player.rotationYaw = (float) yaw;

                while (Minecraft.getMinecraft().player.rotationYaw != yaw) delay(100);
                while (Minecraft.getMinecraft().player.rotationPitch != pitch) delay(100);
            }
        });
        thread.start();
    }

    public static void unmodifiedlookAt(double x, double y, double z) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Minecraft minecraft = Minecraft.getMinecraft();
                EntityPlayerSP player = minecraft.player;
                double dx = x - player.posX;
                double dy = y - player.posY - 1.3;
                double dz = z - player.posZ;

                double dlen = Math.sqrt(dx * dx + dy * dy + dz * dz);
                dx /= dlen;
                dy /= dlen;
                dz /= dlen;

                double pitch = Math.asin(dy);
                double yaw = Math.atan2(dz, dx);
                pitch = pitch * 180.0D / Math.PI;
                yaw = yaw * 180.0D / Math.PI;
                yaw -= 90.0D;

                while (yaw > 360) yaw -= 360;
                while (yaw < -360) yaw += 360;

                player.rotationPitch = -((float) pitch);
                player.rotationYaw = (float) yaw;

                while (Minecraft.getMinecraft().player.rotationYaw != yaw) delay(100);
                while (Minecraft.getMinecraft().player.rotationPitch != pitch) delay(100);
            }
        });
        thread.start();
    }

    public static void lookAt(double x, double y, double z) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Minecraft minecraft = Minecraft.getMinecraft();
                EntityPlayerSP player = minecraft.player;
                double dx = x - player.posX + 0.5;
                double dy = y - player.posY - 1;
                double dz = z - player.posZ + 0.5;

                double dlen = Math.sqrt(dx * dx + dy * dy + dz * dz);
                dx /= dlen;
                dy /= dlen;
                dz /= dlen;

                double pitch = Math.asin(dy);
                double yaw = Math.atan2(dz, dx);
                pitch = pitch * 180.0D / Math.PI;
                yaw = yaw * 180.0D / Math.PI;
                yaw -= 90.0D;

                while (yaw > 360) yaw -= 360;
                while (yaw < -360) yaw += 360;

                player.rotationPitch = -((float) pitch);
                player.rotationYaw = (float) yaw;

                while (Minecraft.getMinecraft().player.rotationYaw != yaw) delay(100);
                while (Minecraft.getMinecraft().player.rotationPitch != pitch) delay(100);
            }
        });
        thread.start();
    }

    public static BlockPos getBlockLookingAt() {
        BlockPos blockPos = null;
        RayTraceResult result = Minecraft.getMinecraft().objectMouseOver;
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            blockPos = result.getBlockPos();
        }
        return blockPos;
    }

    public static boolean lookingAtChest() {
        boolean lookingAtChest = false;
        RayTraceResult result = Minecraft.getMinecraft().objectMouseOver;
        if (result != null && result.typeOfHit == RayTraceResult.Type.BLOCK) {
            if (Minecraft.getMinecraft().world.getBlockState(result.getBlockPos()).getBlock() instanceof BlockChest) {
                lookingAtChest = true;
            }
        }
        return lookingAtChest;
    }

//    public static void openChest() {
//        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), true);
//
//        for(int i = 0; i < 4; i++) {
//            if (!(Minecraft.getMinecraft().player.openContainer instanceof ContainerChest)) {
//                System.out.println("no chest found");
//                try {
//                    Thread.sleep(250);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        KeyBinding.setKeyBindState(Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode(), false);
//    }

	public static void openChest(BlockPos blockpos1) {
        for (int i = 1; i <= 4; i++) {
            System.out.println("try " + i);

            lookAtBlock(blockpos1);
            delay(pingDelay5());

            CPacketPlayerTryUseItemOnBlock packetPlayerTryUseItemOnBlock = new CPacketPlayerTryUseItemOnBlock(blockpos1,
                    EnumFacing.DOWN, EnumHand.MAIN_HAND, (float) blockpos1.getX(), (float) blockpos1.getY(),
                    (float) blockpos1.getZ());
            if (Minecraft.getMinecraft().getConnection() != null)
                Minecraft.getMinecraft().getConnection().sendPacket(packetPlayerTryUseItemOnBlock);
            delay(3000);
            if (chestOpened()) return;

        }
    }

	public static void openOrbSeller(BlockPos blockpos1) {
        for (int i = 1; i <= 4; i++) {
            System.out.println("try " + i);

            lookAtBlock(blockpos1);
            delay(pingDelay5());
            if(orbHandler != null) {
                Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
                Minecraft.getMinecraft().getConnection().sendPacket(new CPacketUseEntity(orbHandler, EnumHand.MAIN_HAND));
            }

//		new CPacketUseEntity(Minecraft.getMinecraft().pointedEntity,EnumHand.MAIN_HAND);
//
//		CPacketUseEntity packetPlayerTryUseItemOnBlock = new CPacketUseEntity(Minecraft.getMinecraft().pointedEntity);;
//		if (Minecraft.getMinecraft().getConnection() != null)
//			Minecraft.getMinecraft().getConnection().sendPacket(packetPlayerTryUseItemOnBlock);
            delay(3000);
            if (chestOpened()) return;

        }
        System.out.println("Cant open Orbguy");
    }

    public static boolean inventoryEmpty() {
        boolean toReturn = true;
        for (int i = 9; i < 45; i++) {
            if (!Minecraft.getMinecraft().player.openContainer.getSlot(i).getStack().isEmpty()) {
                toReturn = false;
            }
        }
        return toReturn;
    }

    public static void closeScreen() {
        Minecraft.getMinecraft().player.closeScreen();

        while (!(Minecraft.getMinecraft().player.openContainer instanceof ContainerPlayer)) delay(pingDelay10());
    }

//    public static void openChest(double x, double y, double z) {
//        while (!chestOpened()) {
//            lookAt(x, y, z);
//            delay(100);
//            openChest();
//        }
//    }

    public static int getPing() {
    	return Minecraft.getMinecraft().player.connection.getPlayerInfo(Minecraft.getMinecraft().player.getUniqueID()).getResponseTime();
    }

    public static int pingDelay10() {
    	return Minecraft.getMinecraft().player.connection.getPlayerInfo(Minecraft.getMinecraft().player.getUniqueID()).getResponseTime()*10;

    }

    public static int pingDelay5() {
    	return Minecraft.getMinecraft().player.connection.getPlayerInfo(Minecraft.getMinecraft().player.getUniqueID()).getResponseTime()*5;

    }

    public static void jump() {
        Minecraft.getMinecraft().player.jump();
    }

    private static void goToOrbHandler(GGOrbBot ggOrbBot, boolean pH) {
        if(!ggOrbBot.enabled) return;

        if(pH) {
            ggOrbBot.movementHandler.execTp("/p h");
        }

        BlockPos pos = Minecraft.getMinecraft().player.getPosition();
        //Go to orbhandler
        ggOrbBot.movementHandler.execTp("/warp orbs");
        System.out.println("await");
        delay(200);
        System.out.println("await1");
        delay(Player.pingDelay10());
        if(!Baritone.checkForBaritone()) {
            System.out.println("Target reached");

            ggOrbBot.movementHandler.execPath(Paths.randomOHPath(), Minecraft.getMinecraft().player);

            delay(200);


            System.out.println("finished walking");

            delay(Player.pingDelay10());
        } else {
            Minecraft.getMinecraft().player.sendChatMessage("#goto 169 -42");
            System.out.println(pos.toString());
            while (!(PositionUtils.getDistance(new Position(pos.getX(), 0, pos.getZ()), new Position(169, 0, -42)) <= 1.5)) {
                pos = Minecraft.getMinecraft().player.getPosition();
                delay(1000);
            }

        }
        if(!orbhandlerThere()) Player.jump();
        delay(Player.pingDelay5());
        if(!orbhandlerThere()) goToOrbHandler(ggOrbBot, true);
    }

    private static boolean orbhandlerThere() {
        AtomicBoolean toReturn = new AtomicBoolean(false);
        Minecraft.getMinecraft().world.getLoadedEntityList().forEach(entity -> {
            if(entity.getName().equals("§6Händler")) {
                orbHandler = entity;
                toReturn.set(true);
                System.out.println(new BlockPos(entity.posX, entity.posY,entity.posZ));}

        });
        return toReturn.get();
    }

    public static void bringToOrbHandler(GGOrbBot ggOrbBot) {
        goToOrbHandler(ggOrbBot, false);

        if(!ggOrbBot.enabled) return;
        Player.openOrbSeller (new BlockPos(172, 27, -42));
//        Player.openChest(new BlockPos(172, 26.5, -42));//170.5

        if(!ggOrbBot.enabled) return;
        ArrayList<Item> itemChecked = new ArrayList<>();
        itemChecked.clear();
        System.out.println("itemChecked cleared");
        for (int i = 54; i < 90; i++) {//Exchange items for Orbs
            System.out.println(i);
            if (!Player.chestOpened()) {
                System.out.println("open chest " + i);
                Player.openOrbSeller (new BlockPos(172, 27, -42));
            }

            //todo blocked slots vom verkaufsprozess ausschließen, diese vorher getten (die mit schwertern)

            if(!ggOrbBot.enabled) return;
            Slot slot = Minecraft.getMinecraft().player.openContainer.getSlot(i);
            if (!slot.getStack().isEmpty()) {
                if(!itemChecked.contains(slot.getStack().getItem())) {
                    itemChecked.add(slot.getStack().getItem());
                    Inventory.quickMoveFromSlot(slot.slotNumber);
                    int ping = Minecraft.getMinecraft().player.connection.getPlayerInfo(Minecraft.getMinecraft().player.getUniqueID()).getResponseTime();
                    delay(ping > 10 ? 10 * ping : 250);//*25

                    ContainerChest containerChest = (ContainerChest) Minecraft.getMinecraft().player.openContainer;
                    if (containerChest.getLowerChestInventory().getName().contains("§6Orbs - Verkauf")) {
                        Inventory.quickMoveFromSlot(15);
                        delay(400);//900
                        System.out.println("close chest " + i);
                        Player.closeScreen();
                    }
                }}
        }

        if(!ggOrbBot.enabled) return;
        delay(500);
        System.out.println("stop: close chest!");
        Player.closeScreen();

        //go to mulleimer
        if(!Player.inventoryEmpty()) {
            if(!ggOrbBot.enabled) return;
            delay(4000);
            ggOrbBot.movementHandler.execTp(ggOrbBot.mullEimerHome);
            delay(100);

            Player.clearInventory();
            delay(7000);
        }
    }

    public static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
