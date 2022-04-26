package de.smartbot_studios.ggorbbot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import de.smartbot_studios.ggorbbot.baritone.Baritone;
import de.smartbot_studios.ggorbbot.baritone.BaritoneCommands;
import de.smartbot_studios.ggorbbot.listeners.*;
import de.smartbot_studios.ggorbbot.utils.OrbMode;
import de.smartbot_studios.ggorbbot.utils.hotkeyutils.HotKeyHandler;
import de.smartbot_studios.ggorbbot.utils.hotkeyutils.SimpleHotKey;
import de.smartbot_studios.ggorbbot.utils.javautils.Config;
import de.smartbot_studios.ggorbbot.utils.javautils.RequireNotNull;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.Chest;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.ChestHandler;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.KillAura;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.AddChestsGui;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.BaritoneInstallerGui;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.ChooseModeGui;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.guis.HomeEditorGui;
import de.smartbot_studios.ggorbbot.utils.minecraftutils.path.newpathutils.MovementHandler;
import delete.EnumElement;
import net.labymod.api.LabyModAddon;
import net.labymod.main.LabyMod;
import net.labymod.settings.elements.*;
import net.labymod.utils.Consumer;
import net.labymod.utils.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

import java.util.LinkedList;
import java.util.List;

public class GGOrbBot extends LabyModAddon {

    //constant System values
    public final String prefix = "§8[" +
            "§c§lG" +
            "§6§lG" +
            "§e§lO" +
            "§a§lR" +
            "§b§lB" +
            "§9§lB" +
            "§c§lO" +
            "§2§lT" +
            "§8]§f ";

    public final String configsPath = Minecraft.getMinecraft().mcDataDir + "/LabyMod/addons-1.12/GGOrbBot/configs/";
    public MovementHandler movementHandler = new MovementHandler(this, 5000);
    public KillAura killAura = new KillAura(this);

    public BlockPos auraPos;

    //public values
    public HotKeyHandler hotKeyHandler = new HotKeyHandler();

    public RequireNotNull chestModeRequireNotNull = new RequireNotNull();
    public RequireNotNull afkModeRequireNotNull = new RequireNotNull();
    public RequireNotNull afkAuraModeRequireNotNull = new RequireNotNull();
    public RequireNotNull baritoneAuraModeRequireNotNull = new RequireNotNull();
    public RequireNotNull baritoneChestRequireNotNull = new RequireNotNull();
    public RequireNotNull baritoneAfkRequireNotNull = new RequireNotNull();

    private SimpleHotKey toggleGGOrbBotModeSimpleHotKey;
    private SimpleHotKey addChestSimpleHotkey;
    private SimpleHotKey openListHomesHotKey;

    public Config chestConfig;
    public Config chestHomeConfig;

    public ChestHandler chestHandler = new ChestHandler(this);

    public LinkedList<String> chatQueue = new LinkedList<>();
    public LinkedList<BlockPos> chestsToBeAdded = new LinkedList<>();
    public LinkedList<BlockPos> existingChests = new LinkedList<>();

    public OrbMode orbMode;

    public boolean enabled;
    public boolean pickingChests;
    public boolean closeMc = false;
    public boolean systemShutdown = false;
    
    public String cmdprefix;
    public String selectedHome;
    public Integer waitTimer = 30;
    public String orbHandlerHome;
    public String mullEimerHome;
    public String afkHome;

    //private values
    private int toggleGGOrbBotModeKeyCode;
    private int addChestKeyCode;
    private int listHomesKeyCode;

	//    Licese boolean 
	public static  boolean R = true;

    @Override
    public void onEnable() {
        enabled = false;
        getApi().registerForgeListener(movementHandler);
        getApi().registerForgeListener(new TickListener(this));
        getApi().registerForgeListener(new RenderListener(this));
        getApi().registerForgeListener(new MouseClickListener(this));
        getApi().getEventManager().register(new SendMessageListener(this));
        getApi().getEventManager().register(new ReceiveMessageListener(this));
        getApi().getEventManager().registerOnJoin(serverData -> {
        	if(serverData.getIp().toLowerCase().contains("griefergames.net")) {
                if (!Baritone.checkForBaritone()) {
                    Minecraft.getMinecraft().displayGuiScreen(new BaritoneInstallerGui());
                } else setupBaritone();
            }
        });

        loadConfigs();
        registerHotkeys();

        chestModeRequireNotNull.requireNotNull("OrbHändler-Home", orbHandlerHome);
        chestModeRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
        afkModeRequireNotNull.requireNotNull("Afk-Home", afkHome);
        afkModeRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
        baritoneChestRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
        baritoneAfkRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
        baritoneAfkRequireNotNull.requireNotNull("Afk-Home", afkHome);
        afkAuraModeRequireNotNull.requireNotNull("Afk-Home", afkHome);
        afkAuraModeRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
        afkAuraModeRequireNotNull.requireNotNull("Aura-Pos", auraPos);
    }

    @Override
    public void loadConfig() {
        this.cmdprefix = getConfig().has("cmdprefix") ? getConfig().get("cmdprefix").getAsString() : ".so";

        //HotKeyKeyCodes
        toggleGGOrbBotModeKeyCode = getConfig().has("toggleGGOrbBotmodekeycode") ? getConfig().get("toggleGGOrbBotmodekeycode").getAsInt() : 0;
        toggleGGOrbBotModeSimpleHotKey.setKeycode(toggleGGOrbBotModeKeyCode);
        hotKeyHandler.addHotkey(toggleGGOrbBotModeSimpleHotKey);
        addChestKeyCode = getConfig().has("addchestkeycode") ? getConfig().get("addchestkeycode").getAsInt() : 0;
        addChestSimpleHotkey.setKeycode(addChestKeyCode);
        hotKeyHandler.addHotkey(addChestSimpleHotkey);
        listHomesKeyCode = getConfig().has("listhomeskeycode") ? getConfig().get("listhomeskeycode").getAsInt() : 0;
        openListHomesHotKey.setKeycode(listHomesKeyCode);
        hotKeyHandler.addHotkey(openListHomesHotKey);

        //homes
        orbHandlerHome = getConfig().has("orbhandlerhome") ? getConfig().get("orbhandlerhome").getAsString() : null;
        chestModeRequireNotNull.requireNotNull("OrbHändler-Home", orbHandlerHome);
        mullEimerHome = getConfig().has("mulleimerhome") ? getConfig().get("mulleimerhome").getAsString() : null;
        chestModeRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
        baritoneChestRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
        baritoneAuraModeRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
        baritoneAfkRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
        afkModeRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
        afkHome = getConfig().has("afkhome") ? getConfig().get("afkhome").getAsString() : null;
        afkModeRequireNotNull.requireNotNull("Afk-Home", afkHome);
        baritoneAfkRequireNotNull.requireNotNull("Afk-Home", afkHome);
        new EnumElement("aaa");

        if(getConfig().has("aurapos")) {
            JsonObject data = getConfig().getAsJsonObject("aurapos");

            auraPos = new BlockPos(data.get("x").getAsInt(), data.get("y").getAsInt(), data.get("z").getAsInt());
        }
        afkAuraModeRequireNotNull.requireNotNull("Aura-Pos", auraPos);

        //Timers
        waitTimer = getConfig().has("waittimer") ? getConfig().get("waittimer").getAsInt() : 30;
        if(getConfig().has("teleporttimer")) movementHandler.setDelay(getConfig().get("teleporttimer").getAsInt());
        
        
        //booleans
        systemShutdown = getConfig().has("systemshutdown") ? getConfig().get("systemshutdown").getAsBoolean() : false;
        closeMc = getConfig().has("mcshutdown") ? getConfig().get("mcshutdown").getAsBoolean() : false;

    }

    @Override
    protected void fillSettings(List<SettingsElement> list) {
//        StringElement cmdPrefixElement = new StringElement("Commandprefix", new ControlElement.IconData(Material.COMMAND), cmdprefix, s -> {
//            cmdprefix = s;
//            getConfig().addProperty("cmdprefix", cmdprefix);
//            saveConfig();
//        });
//        getSubSettings().add(cmdPrefixElement);

        initializeHotKeys();

        initializeHomes();
        
        initializeTimers();
        
        initializeChestModeBools();
    }

    private void initializeHotKeys() {
        ListContainerElement hotkeyListElement = new ListContainerElement("Hotkeys", new ControlElement.IconData("labymod/addons/GGOrbBot/images/hotkeys.png"));
        KeyElement toggleGGOrbBotModeKeyElement = new KeyElement("GGOrbBotMode Auswahl (On/Off)", new ControlElement.IconData(Material.LEVER), toggleGGOrbBotModeKeyCode, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                toggleGGOrbBotModeKeyCode = integer;
                getConfig().addProperty("toggleGGOrbBotmodekeycode", integer);
                saveConfig();
                
                toggleGGOrbBotModeSimpleHotKey.setKeycode(toggleGGOrbBotModeKeyCode);
                hotKeyHandler.addHotkey(toggleGGOrbBotModeSimpleHotKey);
            }
        });
        hotkeyListElement.getSubSettings().add(toggleGGOrbBotModeKeyElement);
        
        KeyElement openListHomesKeyElement = new KeyElement("Homes/Ph Menü", new ControlElement.IconData(Material.ENDER_CHEST), listHomesKeyCode, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                listHomesKeyCode = integer;
                getConfig().addProperty("listhomeskeycode", integer);
                saveConfig();
                openListHomesHotKey.setKeycode(listHomesKeyCode);
                hotKeyHandler.addHotkey(openListHomesHotKey);
            }
        });
        hotkeyListElement.getSubSettings().add(openListHomesKeyElement);
        getSubSettings().add(hotkeyListElement);
       
        KeyElement addChestKeyElement = new KeyElement("Kisten hinzufügen", new ControlElement.IconData(Material.CHEST), addChestKeyCode, new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                addChestKeyCode = integer;
                getConfig().addProperty("addchestkeycode", integer);
                saveConfig();
                addChestSimpleHotkey.setKeycode(addChestKeyCode);
                hotKeyHandler.addHotkey(addChestSimpleHotkey);
            }
        });
        hotkeyListElement.getSubSettings().add(addChestKeyElement);
    }

    private void initializeHomes() {
        ListContainerElement homes = new ListContainerElement("Home's/Ph's", new ControlElement.IconData("labymod/addons/GGOrbBot/images/home.png"));
        homes.getSubSettings().add(new StringElement("OrbHändler", new ControlElement.IconData(Material.EMERALD), orbHandlerHome, s -> {
            orbHandlerHome = s;
            chestModeRequireNotNull.requireNotNull("OrbHändler-Home", orbHandlerHome);
            afkModeRequireNotNull.requireNotNull("OrbHändler-Home", orbHandlerHome);
            afkAuraModeRequireNotNull.requireNotNull("OrbHändler-Home", orbHandlerHome);
            getConfig().addProperty("orbhandlerhome", s);
            saveConfig();
        }));
        homes.getSubSettings().add(new StringElement("Mülleimer", new ControlElement.IconData(Material.BUCKET), mullEimerHome, s -> {
            mullEimerHome = s;
            afkModeRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
            chestModeRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
            baritoneChestRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
            baritoneAfkRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
            baritoneAuraModeRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
            afkAuraModeRequireNotNull.requireNotNull("Mülleimer-Home", mullEimerHome);
            getConfig().addProperty("mulleimerhome", s);
            saveConfig();
        }));
        homes.getSubSettings().add(new StringElement("AFK-Farm-Home", new ControlElement.IconData(Material.WATCH), afkHome, s -> {
            afkHome = s;
            afkModeRequireNotNull.requireNotNull("Afk-Home", afkHome);
            baritoneAfkRequireNotNull.requireNotNull("Afk-Home", afkHome);
            afkAuraModeRequireNotNull.requireNotNull("Afk-Home", afkHome);
            baritoneAuraModeRequireNotNull.requireNotNull("Afk-Home", afkHome);
            getConfig().addProperty("afkhome", afkHome);
            saveConfig();
        }));
        getSubSettings().add(homes);
    }
    
    private void initializeTimers() {
        ListContainerElement timers = new ListContainerElement("Timer's", new ControlElement.IconData("labymod/addons/GGOrbBot/images/uhr.png"));

        timers.getSubSettings().add( new NumberElement("Wait X minutes after last Chest", new ControlElement.IconData(Material.WATCH), waitTimer).addCallback(accepted -> {

               waitTimer = accepted;

               getConfig().addProperty("waittimer", accepted);
               saveConfig();
               System.out.println("Saved");

        }));

        timers.getSubSettings().add(new NumberElement("Wait X seconds after last teleport", new ControlElement.IconData(Material.WATCH)).setMinValue(5).setMaxValue(60).addCallback(accepted -> {

            accepted *= 1000;
            movementHandler.setDelay(accepted);

            getConfig().addProperty("teleporttimer", accepted);
            saveConfig();
            System.out.println("saved");

        }));
        getSubSettings().add(timers);
    }
    
    private void initializeChestModeBools() {
        ListContainerElement bools = new ListContainerElement("Chestmode Settings", new ControlElement.IconData("labymod/addons/GGOrbBot/images/chest.png"));
   
        bools.getSubSettings().add(new BooleanElement( "Shutdown after last Chest" /* Display name */, new ControlElement.IconData( Material.LEVER ), new Consumer<Boolean>() {
            @Override
            public void accept( Boolean accepted ) {
                systemShutdown = accepted;
                if (accepted)closeMc = false;
                getConfig().addProperty("systemshutdown", accepted);
                saveConfig();
                
            }
        } /* Change listener */, systemShutdown /* current value */ ) );
        
        bools.getSubSettings().add(new BooleanElement( "Close Minecraft after last Chest" /* Display name */, new ControlElement.IconData( Material.LEVER ), new Consumer<Boolean>() {
            @Override
            public void accept( Boolean accepted ) {
            	
            	closeMc = accepted;
            	
            	if (accepted)systemShutdown = false;
            	
            	getConfig().addProperty("mcshutdown", accepted);
                saveConfig();
            	
            }
        } /* Change listener */, closeMc /* current value */ ) );
         
           
        	
        getSubSettings().add(bools);
    }
    

    private void registerHotkeys() {
        hotKeyHandler.addHotkey(new SimpleHotKey() {
            @Override
            public void execute() {
                /*
                if(killAura.isActive()) {
                    killAura.deActivate();
                } else killAura.activate();
                */
                //movementHandler.allowSprinting(true).setPath(Paths.p1());

                /*
                Minecraft.getMinecraft().player.world.getLoadedEntityList().forEach(entity -> {
                    if(entity instanceof AbstractClientPlayer) {
                        System.out.println(entity.getName() + " " + ((AbstractClientPlayer) entity).inventory.getCurrentItem().getDisplayName());
                    }
                });
                 */
            }
        }.setCooldown(1000).setKeycode(Keyboard.KEY_H));

        toggleGGOrbBotModeSimpleHotKey = new SimpleHotKey() {
            @Override
            public void execute() {
                if(enabled) {
                    enabled = false;
                    orbMode = null;
                    LabyMod.getInstance().displayMessageInChat(prefix + MsgConfig.GGORBBOTBMODE_DEACTIVATED.get());
                } else {
                    Minecraft.getMinecraft().displayGuiScreen(new ChooseModeGui(GGOrbBot.this));
                }
            }
        }.setCooldown(500).setKeycode(toggleGGOrbBotModeKeyCode);
        hotKeyHandler.addHotkey(toggleGGOrbBotModeSimpleHotKey);

        addChestSimpleHotkey = new SimpleHotKey() {
            @Override
            public void execute() {
                if (!pickingChests) {
                    existingChests.clear();
                    JsonArray chests = chestConfig.getConfig().getAsJsonArray("chests");
                    chests.forEach(element -> {
                        Chest chest = Chest.getFromString(element.getAsJsonObject().get("chest").getAsString());
                        if (chest != null) {
                            existingChests.add(new BlockPos(chest.getX(), chest.getY(), chest.getZ()));
                        }
                    });
                    Minecraft.getMinecraft().displayGuiScreen(new AddChestsGui(GGOrbBot.this));
                } else {
                    pickingChests = false;
                    chestsToBeAdded.forEach(pos -> {
                        Chest chest = new Chest(pos.getX(), pos.getY(), pos.getZ());

                        JsonObject toReturn = new JsonObject();
                        toReturn.addProperty("chest", chest.toString());
                        toReturn.addProperty("home", selectedHome);
                        chestConfig.getConfig().getAsJsonArray("chests").remove(toReturn);
                        chestConfig.getConfig().getAsJsonArray("chests").add(toReturn);
                    });
                    chestsToBeAdded.clear();
                    chestConfig.saveConfig();
                    LabyMod.getInstance().displayMessageInChat(prefix + MsgConfig.CHESTS_ADDED.get());
                }
            }
        }.setCooldown(500).setKeycode(addChestKeyCode);
        hotKeyHandler.addHotkey(addChestSimpleHotkey);

        openListHomesHotKey = new SimpleHotKey() {
            @Override
            public void execute() {
                Minecraft.getMinecraft().displayGuiScreen(new HomeEditorGui(GGOrbBot.this));
            }
        }.setCooldown(500).setKeycode(listHomesKeyCode);
        hotKeyHandler.addHotkey(openListHomesHotKey);
    }

    private void loadConfigs() {
        chestConfig = Config.loadConfig(configsPath + "chests.json", true);
        if(!chestConfig.getConfig().has("chests")) {
            chestConfig.getConfig().add("chests", new JsonArray());
            chestConfig.saveConfig();
        }
        chestHomeConfig = Config.loadConfig(configsPath + "chestHomes.json", true);
        if(!chestHomeConfig.getConfig().has("homes")) {
            chestHomeConfig.getConfig().add("homes", new JsonArray());
            chestHomeConfig.saveConfig();
        }
    }

    private void setupBaritone() {
        if(Baritone.checkForBaritone()) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            String s = BaritoneCommands.CONFIG.get();
            player.sendChatMessage(s + BaritoneCommands.DISABLEBREAK.get());
            player.sendChatMessage(s + BaritoneCommands.DISABLEPLACE.get());
            player.sendChatMessage(s + BaritoneCommands.ENABLESPRINT.get());
            player.sendChatMessage(s + BaritoneCommands.DISABLEFREELOOK.get());
            player.sendChatMessage(s + BaritoneCommands.DISABLESPRINTASCENDS.get());
            player.sendChatMessage(s + BaritoneCommands.ENABLEPARKOUR.get());
            LabyMod.getInstance().displayMessageInChat(prefix + MsgConfig.BARITONE_INITIALIZED.get());
        }
    }
    
    
}
