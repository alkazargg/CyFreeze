package cy.freeze;

import org.bukkit.plugin.java.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;
import org.bukkit.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

import static org.bukkit.Bukkit.dispatchCommand;
import static org.bukkit.Bukkit.getConsoleSender;

public class Freeze extends JavaPlugin implements Listener
{
    public ArrayList<Player> frozen;

    public Freeze() {
        this.frozen = new ArrayList<>();
    }

    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
    }

    public void onDisable() {
    }

    public boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
        if (cmd.getName().equalsIgnoreCase("freeze")) {
            final Player player = (Player)sender;
            if (sender.hasPermission("freeze.use")) {
                if (args.length == 0) {
                        player.sendMessage(ChatColor.WHITE + "Cy" + ChatColor.AQUA + "Eyes" + ChatColor.DARK_GRAY + " »" + ChatColor.GRAY + " 使用方法/freeze [player]");
                    return true;
                }
                final Player t = Bukkit.getServer().getPlayer(args[0]);
                if (t == null) {
                    player.sendMessage(ChatColor.WHITE + "Cy" + ChatColor.AQUA + "Eyes" + ChatColor.DARK_GRAY + " »" + ChatColor.GRAY + " 这名玩家不在线!");
                    return true;
                }
                if (!this.frozen.contains(t)) {
                    this.frozen.add(t);
                    player.sendMessage(ChatColor.WHITE + "Cy" + ChatColor.AQUA + "Eyes" + ChatColor.DARK_GRAY + " »" + ChatColor.GRAY + "你冻结了" + t.getName() + ".");
                    t.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588\u2588&c\u2588&f\u2588\u2588\u2588\u2588"));
                    t.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588&c\u2588&6\u2588&c\u2588&f\u2588\u2588\u2588       &F&LCy&B&LEyes &8&l- &f冻结系统"));
                    t.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588"));
                    t.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588"));
                    t.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&c\u2588&6\u2588\u2588&0\u2588&6\u2588\u2588&c\u2588&f\u2588  &f你已被&c&l临时冻结!&f,请配合staff进行查端"));
                    t.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&c\u2588&6\u2588\u2588\u2588\u2588\u2588&c\u2588&f\u2588          &f&c退出您将会受到封禁处罚!"));
                    t.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c\u2588&6\u2588\u2588\u2588&0\u2588&6\u2588\u2588\u2588&c\u2588"));
                    t.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
                    return true;
                }
                if (this.frozen.contains(t)) {
                    player.sendMessage(ChatColor.WHITE + "Cy" + ChatColor.AQUA + "Eyes" + ChatColor.DARK_GRAY + " »" + ChatColor.GRAY + " 这名玩家已经被冻结了!");
                    return true;
                }
            }
            if (!sender.hasPermission("freeze.use")) {
                player.sendMessage("Unknown command. Type /help for help.");
                return true;
            }
        }
        if (cmd.getName().equalsIgnoreCase("unfreeze")) {
            final Player player = (Player)sender;
            if (sender.hasPermission("freeze.use")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.WHITE + "Cy" + ChatColor.AQUA + "Eyes" + ChatColor.DARK_GRAY + " »" + ChatColor.GRAY + " 使用方法/unfreeze [player]");
                    return true;
                }
                final Player t = Bukkit.getServer().getPlayer(args[0]);
                if (t == null) {
                    player.sendMessage(ChatColor.WHITE + "Cy" + ChatColor.AQUA + "Eyes" + ChatColor.DARK_GRAY + " »" + ChatColor.GRAY + " 这名玩家不在线!");
                    return true;
                }
                if (this.frozen.contains(t)) {
                    this.frozen.remove(t);
                    player.sendMessage(ChatColor.WHITE + "Cy" + ChatColor.AQUA + "Eyes" + ChatColor.DARK_GRAY + " »" + ChatColor.GRAY + " 这名玩家被你解除冻结.");
                    t.sendMessage(ChatColor.GREEN +  sender.getName() + "为你解除冻结.");
                    return true;
                }
                if (!this.frozen.contains(t)) {
                    player.sendMessage(ChatColor.WHITE + "Cy" + ChatColor.AQUA + "Eyes" + ChatColor.DARK_GRAY + " »" + ChatColor.GRAY + " 这名玩家没有被冻结");
                    return true;
                }
            }
            if (!sender.hasPermission("freeze.use")) {
                player.sendMessage("Unknown command. Type /help for help.");
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerMove(final PlayerMoveEvent event) {
        final Player p = event.getPlayer();
        if (this.frozen.contains(p)) {
            event.setTo(event.getFrom());
        }
    }

    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            final Player p = (Player)event.getEntity();
            if (this.frozen.contains(p)) {
                event.setCancelled(true);
                (event.getDamager()).sendMessage(ChatColor.RED + "你不能攻击一名被冻结的玩家!");

            }
        }
        if (event.getDamager() instanceof Player) {
            final Player a = (Player)event.getDamager();
            if (this.frozen.contains(a)) {
                event.setCancelled(true);
                a.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588\u2588&c\u2588&f\u2588\u2588\u2588\u2588"));
                a.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588&c\u2588&6\u2588&c\u2588&f\u2588\u2588\u2588       &F&LCy&B&LEyes &8&l- &f冻结系统"));
                a.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588"));
                a.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588"));
                a.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&c\u2588&6\u2588\u2588&0\u2588&6\u2588\u2588&c\u2588&f\u2588  &f你已被&c&l临时冻结!&f,请配合staff进行查端"));
                a.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&c\u2588&6\u2588\u2588\u2588\u2588\u2588&c\u2588&f\u2588          &f&c退出您将会受到封禁处罚!"));
                a.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c\u2588&6\u2588\u2588\u2588&0\u2588&6\u2588\u2588\u2588&c\u2588"));
                a.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
            }
        }
    }

    @EventHandler
    public void onBlockBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        if (this.frozen.contains(player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588\u2588&c\u2588&f\u2588\u2588\u2588\u2588"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588&c\u2588&6\u2588&c\u2588&f\u2588\u2588\u2588       &F&LCy&B&LEyes &8&l- &f冻结系统"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&c\u2588&6\u2588\u2588&0\u2588&6\u2588\u2588&c\u2588&f\u2588  &f你已被&c&l临时冻结!&f,请配合staff进行查端"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&c\u2588&6\u2588\u2588\u2588\u2588\u2588&c\u2588&f\u2588          &f&c退出您将会受到封禁处罚!"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c\u2588&6\u2588\u2588\u2588&0\u2588&6\u2588\u2588\u2588&c\u2588"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
        }
    }

    @EventHandler
    public void onPlace(final BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        if (this.frozen.contains(player)) {
            event.setCancelled(true);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588\u2588&c\u2588&f\u2588\u2588\u2588\u2588"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588\u2588&c\u2588&6\u2588&c\u2588&f\u2588\u2588\u2588       &F&LCy&B&LEyes &8&l- &f冻结系统"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588\u2588&c\u2588&6\u2588&0\u2588&6\u2588&c\u2588&f\u2588\u2588"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&c\u2588&6\u2588\u2588&0\u2588&6\u2588\u2588&c\u2588&f\u2588  &f你已被&c&l临时冻结!&f,请配合staff进行查端"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&f\u2588&c\u2588&6\u2588\u2588\u2588\u2588\u2588&c\u2588&f\u2588          &f&c退出您将会受到封禁处罚!"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c\u2588&6\u2588\u2588\u2588&0\u2588&6\u2588\u2588\u2588&c\u2588"));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588\u2588"));
        }
    }


    @EventHandler
    public void onLogOut(final PlayerQuitEvent event) {
        final Player p = event.getPlayer();
        if (this.frozen.contains(p)) {
            dispatchCommand(getConsoleSender(), "ban " + p.getPlayer().getName() + " 5d &F&LCy&B&LEyes &8&l- &f拒绝查端");
        }
    }
}
