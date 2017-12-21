package fr.lolpop78910.enderiaapi.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.lolpop78910.enderiaapi.mysql.MySQL;
 
public class Commands implements CommandExecutor {
 
    private MySQL sql;
   
    public Commands(MySQL sql) {
        this.sql = sql;
    }
 
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
       
        if(sender instanceof Player){
           
            Player p = (Player)sender;
           
            if(msg.equalsIgnoreCase("money")) {
	            if(args.length == 0){
	                int balance = sql.getBalance(p);
	                p.sendMessage("§7Vous avez : §e" + balance+" §7EnderiaCoins");
	            }
	           
	            //money add 50 gravenilvec
	           
	            if(args.length >= 1){
	               
	                //money add
	                if(args[0].equalsIgnoreCase("add")){
	                   
	                    if(args.length == 1 || args.length == 2){
	                        p.sendMessage("Veuillez tapez /money add <joueur> <montant> ");
	                    }
	                   
	                    if(args.length == 3){
	                       
	                        Player cible = Bukkit.getPlayer(args[1]);
	                        if(cible != null){ 
	                            int montant = Integer.valueOf(args[2]);
	                            sql.addMoney(cible, montant);
	                            cible.sendMessage("Vous recevez : " + montant+" EnderiaCoins de la part de " + p.getName());
	                            p.sendMessage("Vous venez d'envoyer : " + montant+" EnderiaCoins à " + cible.getName());
	                        }
	                       
	                       
	                    }
	                   
	                }
	               
	                //money remove
	                if(args[0].equalsIgnoreCase("remove")){
	                   
	                    if(args.length == 1 || args.length == 2){
	                        p.sendMessage("Veuillez tapez /money remove <joueur> <montant>");
	                    }
	                   
	                    if(args.length == 3){
	                       
	                        Player cible = Bukkit.getPlayer(args[1]);
	                        if(sql.getBalance(cible) <= 0)return false;
	                        if(cible != null){ 
	                            int montant = Integer.valueOf(args[2]);
	                            sql.removeMoney(cible, montant);
	                            cible.sendMessage("Vous recevez : " + montant+" EnderiaCoins de la part de " + p.getName());
	                            cible.sendMessage("Vous venez de retirer : " + montant+" EnderiaCoins à " + cible.getName());
	                        }
	                       
	                       
	                    }
	                   
	                   
	                } if(args[0].equalsIgnoreCase("pay")) {
	                	if(args.length == 1 || args.length == 2){
	                        p.sendMessage("Veuillez tapez /money remove <joueur> <montant>");
	                    }
	                   
	                    if(args.length == 3){
	                       
	                        Player cible = Bukkit.getPlayer(args[1]);
	                        if(sql.getBalance(cible) <= 0)return false;
	                        if(cible != null){ 
	                            int montant = Integer.valueOf(args[2]);
	                            sql.removeMoney(p, montant);
	                            sql.addMoney(cible, montant);
	                        }
	                       
	                       
	                    }
	                }
	                
	                
	               
	            }
            }
            //money remove gravenilvec 50
           
        }
       
        return false;
    }
 
}