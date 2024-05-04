package net.cycasc.mc.cycasceventhandler;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World.Environment;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class CycascEventHandler extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        // Save a copy of embedded config.yml (will not overwrite existing file)
        this.saveDefaultConfig();

        Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Plugin enabled");
        Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Version " + this.getDescription().getVersion());
        Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Plugin by " + this.getDescription().getAuthors().get(0));
    }

    @Override
    public void onDisable() {
        this.saveConfig();

        Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Configuration saved");
        Bukkit.getLogger().info("[" + this.getDescription().getName() + "] Plugin disabled");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("eventhandler")) {
            if (sender.hasPermission(this.getDescription().getName() + "." + cmd.getName())) {
                if (args.length == 1) {
                    switch (args[0].toLowerCase()) {
                        case "cancelteleportfromliquidblock":
                            sender.sendMessage(ChatColor.GRAY + "Current setting of cancelTeleportFromLiquidBlock: " + ChatColor.ITALIC + String.valueOf(this.getConfig().getBoolean("cycascEventHandler.cancelTeleportFromLiquidBlock")));
                            break;
                        case "cancelteleportiftargetsolid":
                            sender.sendMessage(ChatColor.GRAY + "Current setting of cancelTeleportIfTargetSolid: " + ChatColor.ITALIC + String.valueOf(this.getConfig().getBoolean("cycascEventHandler.cancelTeleportIfTargetSolid")));
                            break;
                        case "cancelteleportifdifferentworld":
                            sender.sendMessage(ChatColor.GRAY + "Current setting of cancelTeleportIfDifferentWorld: " + ChatColor.ITALIC + String.valueOf(this.getConfig().getBoolean("cycascEventHandler.cancelTeleportIfDifferentWorld")));
                            break;
                        case "cancelfirebylightningstrike":
                            sender.sendMessage(ChatColor.GRAY + "Current setting of cancelFireByLightningStrike: " + ChatColor.ITALIC + String.valueOf(this.getConfig().getBoolean("cycascEventHandler.cancelFireByLightningStrike")));
                            break;
                        default:
                            sender.sendMessage(ChatColor.DARK_RED + "Unknown option \"" + args[0] + "\".");
                            sender.sendMessage(ChatColor.GRAY + "Possible options...");
                            sender.sendMessage(ChatColor.GRAY + " - cancelTeleportFromLiquidBlock");
                            sender.sendMessage(ChatColor.GRAY + " - cancelTeleportIfTargetSolid");
                            sender.sendMessage(ChatColor.GRAY + " - cancelTeleportIfDifferentWorld");
                            sender.sendMessage(ChatColor.GRAY + " - cancelFireByLightningStrike");
                            return false;
                    }
                }
                else if (args.length == 2) {
                    if (args[1].equalsIgnoreCase(Boolean.toString(true)) || args[1].equalsIgnoreCase(Boolean.toString(false))) {
                        boolean newValue = Boolean.parseBoolean(args[1]);

                        switch (args[0].toLowerCase()) {
                            case "cancelteleportfromliquidblock":
                                this.getConfig().set("cycascEventHandler.cancelTeleportFromLiquidBlock", newValue);
                                this.saveConfig();
                                sender.sendMessage(ChatColor.GRAY + "Setting cancelTeleportFromLiquidBlock changed to: " + ChatColor.ITALIC + String.valueOf(newValue));
                                break;
                            case "cancelteleportiftargetsolid":
                                this.getConfig().set("cycascEventHandler.cancelTeleportIfTargetSolid", newValue);
                                this.saveConfig();
                                sender.sendMessage(ChatColor.GRAY + "Setting cancelTeleportIfTargetSolid changed to: " + ChatColor.ITALIC + String.valueOf(newValue));
                                break;
                            case "cancelteleportifdifferentworld":
                                this.getConfig().set("cycascEventHandler.cancelTeleportIfDifferentWorld", newValue);
                                this.saveConfig();
                                sender.sendMessage(ChatColor.GRAY + "Setting cancelTeleportIfDifferentWorld changed to: " + ChatColor.ITALIC + String.valueOf(newValue));
                                break;
                            case "cancelfirebylightningstrike":
                                this.getConfig().set("cycascEventHandler.cancelFireByLightningStrike", newValue);
                                this.saveConfig();
                                sender.sendMessage(ChatColor.GRAY + "Setting cancelFireByLightningStrike changed to: " + ChatColor.ITALIC + String.valueOf(newValue));
                                break;
                            default:
                                sender.sendMessage(ChatColor.DARK_RED + "Unknown option \"" + args[0] + "\".");
                                sender.sendMessage(ChatColor.GRAY + "Possible options...");
                                sender.sendMessage(ChatColor.GRAY + " - cancelTeleportFromLiquidBlock");
                                sender.sendMessage(ChatColor.GRAY + " - cancelTeleportIfTargetSolid");
                                sender.sendMessage(ChatColor.GRAY + " - cancelTeleportIfDifferentWorld");
                                sender.sendMessage(ChatColor.GRAY + " - cancelFireByLightningStrike");
                                return false;
                        }
                    }
                    else {
                        sender.sendMessage(ChatColor.DARK_RED + "Unknown value \"" + args[1] + "\" (has to be " + Boolean.toString(true) + " or " + Boolean.toString(false) + ").");
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            else {
                sender.sendMessage(ChatColor.DARK_RED + "You do not have the permissions to execute this command.");
            }
        }

        return true;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerChat(AsyncPlayerChatEvent e) {
        e.setFormat(ChatColor.DARK_GREEN + "%s: " + ChatColor.WHITE + "%s");
    }

    @EventHandler (priority = EventPriority.LOWEST)
    private void onPlayerDeath(PlayerDeathEvent e) {
        e.setDeathMessage(ChatColor.DARK_RED + e.getDeathMessage());
    }

    @EventHandler (priority = EventPriority.LOWEST)
    private void onPlayerBedEnter(PlayerBedEnterEvent e) {
        Player p = e.getPlayer();
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[PlayerEvent] " + ChatColor.GRAY + p.getName() + " went to bed.");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onWorldSave(WorldSaveEvent e) {
        if (e.getWorld().getEnvironment() == Environment.NORMAL) {
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[ServerEvent] " + ChatColor.GRAY + "Saving all changes to disk...");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPortalCreate(PortalCreateEvent e) {
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[WorldEvent] " + ChatColor.GRAY + "A nether portal was created in the world " + e.getWorld().getName() + ".");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerPortal(PlayerPortalEvent e) {
        Player p = e.getPlayer();
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[PlayerEvent] " + ChatColor.GRAY + p.getName() + " went into " + e.getTo().getWorld().getEnvironment().toString() + ".");
    }

    @EventHandler (priority = EventPriority.LOWEST)
    private void onPlayerQuit(PlayerQuitEvent e) {
        e.setQuitMessage(ChatColor.DARK_GREEN + e.getPlayer().getName() + ChatColor.GRAY + " left the server.");
    }

    @EventHandler (priority = EventPriority.LOWEST)
    private void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        if (p.hasPlayedBefore()) {
            e.setJoinMessage(ChatColor.DARK_GREEN + p.getName() + ChatColor.GRAY + " has joined the server.");
            p.sendMessage(ChatColor.DARK_GREEN + "Welcome back!");
        }
        else {
            e.setJoinMessage(ChatColor.DARK_GREEN + p.getName() + ChatColor.GRAY + " entered the server for the first time!");
            Bukkit.broadcastMessage(ChatColor.GRAY + "Welcome, " + ChatColor.DARK_GREEN + p.getName() + ChatColor.GRAY + "!");
        }

        if (Bukkit.isHardcore()) {
            p.sendMessage(ChatColor.DARK_RED + "ATTENTION! " + ChatColor.GRAY + "The server is running in hardcore mode!");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onPlayerTeleport(PlayerTeleportEvent e) {
        if ((e.getCause() == TeleportCause.PLUGIN) || (e.getCause() == TeleportCause.COMMAND)) {
            Player p = e.getPlayer();

            if ((p.getGameMode() != GameMode.CREATIVE) && (p.getGameMode() != GameMode.SPECTATOR)) {
                boolean cancelTeleportFromLiquidBlock = this.getConfig().getBoolean("cycascEventHandler.cancelTeleportFromLiquidBlock");
                boolean cancelTeleportIfTargetSolid = this.getConfig().getBoolean("cycascEventHandler.cancelTeleportIfTargetSolid");
                boolean cancelTeleportIfDifferentWorld = this.getConfig().getBoolean("cycascEventHandler.cancelTeleportIfDifferentWorld");

                Location targetHeadLocation = new Location(e.getTo().getWorld(), e.getTo().getBlockX(), e.getTo().getBlockY() + 1, e.getTo().getBlockZ());

                if (cancelTeleportFromLiquidBlock && e.getFrom().getBlock().isLiquid()) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.DARK_RED + "Teleportation canceled! " + ChatColor.GRAY + "The policy does not allow teleportation from liquid blocks.");
                }

                if (cancelTeleportIfDifferentWorld && (e.getFrom().getWorld() != e.getTo().getWorld())) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.DARK_RED + "Teleportation canceled! " + ChatColor.GRAY + "The policy does not allow teleportations across different worlds.");
                }
                else if (e.getFrom().getWorld().getEnvironment() != e.getTo().getWorld().getEnvironment()) {
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[PlayerEvent] " + ChatColor.GRAY + p.getName() + " went into " + e.getTo().getWorld().getEnvironment().toString() + ".");
                }

                if (cancelTeleportIfTargetSolid && targetHeadLocation.getBlock().getType().isSolid()) {
                    e.setCancelled(true);
                    p.sendMessage(ChatColor.DARK_RED + "Teleportation canceled! " + ChatColor.GRAY + "There is a solid block on the target head location (block type: " + targetHeadLocation.getBlock().getType().name() + ").");
                }
            }
            else if (e.getFrom().getWorld().getEnvironment() != e.getTo().getWorld().getEnvironment()) {
                Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[PlayerEvent] " + ChatColor.GRAY + p.getName() + " went into " + e.getTo().getWorld().getEnvironment().toString() + ".");
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    private void onBlockIgnite(BlockIgniteEvent e) {
        if ((e.getCause() == IgniteCause.LIGHTNING) && (this.getConfig().getBoolean("cycascEventHandler.cancelFireByLightningStrike"))) {
            e.setCancelled(true);
            //Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "[BlockEvent] " + ChatColor.GRAY + "Block ignition by lightning strike canceled.");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onPlayerInteractEvent(PlayerInteractEvent e) {
        if ((e.getPlayer().getGameMode() != GameMode.SPECTATOR) && ((e.getAction() == Action.LEFT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "[PlayerInteraction] " + ChatColor.GRAY + "Player name: " + e.getPlayer().getName() + ", Player loc.: " + e.getPlayer().getLocation().getBlockX() + " " + e.getPlayer().getLocation().getBlockY() + " " + e.getPlayer().getLocation().getBlockZ() + ", Action: " + e.getAction().name() + ", Clicked block type: " + e.getClickedBlock().getType().name() + ", Clicked block loc.: " + e.getClickedBlock().getX() + " " + e.getClickedBlock().getY() + " " + e.getClickedBlock().getZ());
        }
    }
}
