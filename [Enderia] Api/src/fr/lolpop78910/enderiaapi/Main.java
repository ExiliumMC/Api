package fr.lolpop78910.enderiaapi;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import fr.lolpop78910.enderiaapi.commands.Commands;
import fr.lolpop78910.enderiaapi.mysql.MySQL;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class Main extends JavaPlugin implements Listener{
   
    public static MySQL sql;
    public Map<Player, ScoreboardSign> boards = new HashMap<>();
   
    public void onEnable(){
        sql = new MySQL("jdbc:mysql://","","Gestion_Joueur","root","enderia");
        getCommand("money").setExecutor(new Commands(sql));
        Bukkit.getPluginManager().registerEvents(this, this);
        sql.connection();
    }
   
    public void onDisable(){
        sql.disconnect();
    }
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
		sql.createAccount(player);
        
        PermissionUser user = PermissionsEx.getUser(player);
        
        for(Map.Entry<Player, ScoreboardSign> sign : boards.entrySet()){
            sign.getValue().setLine(5, "§7Joueurs (hub) §a" + Bukkit.getOnlinePlayers().size());
        }

        player.getInventory().clear();
        ScoreboardSign scoreboard = new ScoreboardSign(player, "§4§lEnderia§7§lMC");
        scoreboard.create();
        scoreboard.setLine(0, "§b");
        scoreboard.setLine(1, "§7Compte » " + player.getName());
        scoreboard.setLine(2, "§7Grade » " + user.getPrefix().replaceAll("&", "§"));
        scoreboard.setLine(3, "§7Coins » " + sql.getBalance(player));
        scoreboard.setLine(4, "§r");
        scoreboard.setLine(5, "§7Joueurs (hub) » " + Bukkit.getOnlinePlayers().size());
        scoreboard.setLine(6, "§0");
        scoreboard.setLine(7, "§7Hub » §aHub01");
        scoreboard.setLine(8, "§c");
        scoreboard.setLine(9, "§eplay.enderiamc.net");
        boards.put(player, scoreboard);

        getLogger().log(Level.INFO, "Le scoreboard de " + player.getName() + " a ete cree");
        
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        for(Map.Entry<Player, ScoreboardSign> sign : boards.entrySet()){
            sign.getValue().setLine(5, "§7Joueurs (hub) §a" + (Bukkit.getOnlinePlayers().size() - 1 ));
        }

        if(boards.containsKey(player)){
            boards.get(player).destroy();
        }
        getLogger().log(Level.INFO, "Le scoreboard de " + player.getName() + " a ete supprime");
        event.setQuitMessage(null);

    }
   
}