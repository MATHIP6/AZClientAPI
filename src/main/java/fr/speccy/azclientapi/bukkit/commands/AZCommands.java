package fr.speccy.azclientapi.bukkit.commands;

import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import fr.speccy.azclientapi.bukkit.AZManager;
import fr.speccy.azclientapi.bukkit.AZPlayer;
import fr.speccy.azclientapi.bukkit.packets.PacketEntityMeta;
import fr.speccy.azclientapi.bukkit.packets.PacketPlayerModel;
import fr.speccy.azclientapi.bukkit.packets.PacketVignette;
import fr.speccy.azclientapi.bukkit.packets.PacketWorldEnv;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AZCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (s.equalsIgnoreCase("az")){
            Player p = (Player) commandSender;
            if (args.length == 0){
                commandSender.sendMessage("§ccheck, list, size, model, opacity, worldenv, vignette, tag, item");
            } else if (args[0].equalsIgnoreCase("list")) {
                List<String> pactifyList = new ArrayList<>();
                List<String> vanillaList = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (AZPlayer.hasAZLauncher(player)) {
                        pactifyList.add(player.getName());
                        continue;
                    }
                    vanillaList.add(player.getName());
                }
                pactifyList.sort(String::compareToIgnoreCase);
                vanillaList.sort(String::compareToIgnoreCase);
                commandSender.sendMessage(ChatColor.YELLOW + "Players using the Pactify Launcher: " + (
                        pactifyList.isEmpty() ? (ChatColor.GRAY + "(none)") : (ChatColor.GREEN + String.join(", ", (Iterable)pactifyList))));
                commandSender.sendMessage(ChatColor.YELLOW + "Players not using the Pactify Launcher: " + (
                        vanillaList.isEmpty() ? (ChatColor.GRAY + "(none)") : (ChatColor.RED + String.join(", ", (Iterable)vanillaList))));

            } else if (args[0].equalsIgnoreCase("check")) {
                if (args.length == 0) {
                    commandSender.sendMessage(ChatColor.RED + "Usage: /az <player>");
                    return true;
                }
                Player player = Bukkit.getPlayer(args[1]);
                if (player == null) {
                    commandSender.sendMessage(ChatColor.RED + "Player " + args[0] + " is not online");
                    return true;
                }
                commandSender.sendMessage(ChatColor.YELLOW + "Player " + player.getName() + " is " + (
                        AZPlayer.hasAZLauncher(player) ? (ChatColor.GREEN + "using") : (ChatColor.RED + "not using")) + ChatColor.YELLOW + " the Pactify Launcher");

            } else if (commandSender instanceof Player) {
                if (args[0].equalsIgnoreCase("size")){
                    if (args.length >= 3){
                        if (Bukkit.getPlayer(args[2]) != null){
                            try {
                                Float size = Float.parseFloat(args[1]);
                                System.out.println(size);
                                PacketEntityMeta.setPlayerScale(Bukkit.getPlayer(args[2]), size, size, size, size, size, true);
                                commandSender.sendMessage("§achangement de taille effectué !");
                            } catch (NumberFormatException e) {
                                commandSender.sendMessage("§cErreur : La taille n'est pas un nombre valide.");
                            }
                        } else {
                            commandSender.sendMessage("§cCe joueur est hors-ligne !");
                        }
                    } else if (args.length == 2){
                        try {
                            Float size = Float.parseFloat(args[1]);
                            System.out.println(size);
                            PacketEntityMeta.setPlayerScale(p, size, size, size, size, size, true);
                            commandSender.sendMessage("§achangement de taille effectué !");
                        } catch (NumberFormatException e) {
                            commandSender.sendMessage("§cErreur : La taille n'est pas un nombre valide.");
                        }
                    } else {
                        commandSender.sendMessage("/az size <size> [player]");
                    }

                } else if (args[0].equalsIgnoreCase("model")) {
                    if (args.length >= 3){
                        if (Bukkit.getPlayer(args[2]) != null){
                            int id = Integer.parseInt(args[1]);
                            try {
                                PacketPlayerModel.setPlayerModel(Bukkit.getPlayer(args[2]), id);
                                p.sendMessage("§achangement de skin effectué !");
                            } catch (NumberFormatException e){
                                p.sendMessage("§cErreur : La valeur est invalide !.");
                            }
                        } else {
                            p.sendMessage("§cCe joueur est hors-ligne !");
                        }
                    } else if (args.length == 2){
                        int id = Integer.parseInt(args[1]);
                        try {
                            PacketPlayerModel.setPlayerModel(p, id);
                            p.sendMessage("§achangement de skin effectué !");
                        } catch (NumberFormatException e){
                            p.sendMessage("§cErreur : La valeur est invalide !.");
                        }
                    } else {
                        p.sendMessage("/az model <id> [player]");
                    }

                } else if (args[0].equalsIgnoreCase("opacity")) {
                    if (args.length >= 3){
                        if (Bukkit.getPlayer(args[2]) != null){
                            try {
                                Float opacity = Float.parseFloat(args[1]);
                                PacketEntityMeta.setPlayerOpacity(Bukkit.getPlayer(args[2]), opacity);
                                PacketEntityMeta.setNameTagOpacity(Bukkit.getPlayer(args[2]), opacity);
                                PacketEntityMeta.setSneakNameTagOpacity(Bukkit.getPlayer(args[2]), opacity);
                                p.sendMessage("§achangement de d'opacité effectué !");
                            } catch (NumberFormatException e){
                                p.sendMessage("§cErreur : La valeur est invalide !.");
                            }
                        } else {
                            p.sendMessage("§cCe joueur est hors-ligne !");
                        }
                    } else if (args.length == 2){
                        try {
                            Float opacity = Float.parseFloat(args[1]);
                            PacketEntityMeta.setPlayerOpacity(p, opacity);
                            PacketEntityMeta.setNameTagOpacity(p, opacity);
                            PacketEntityMeta.setSneakNameTagOpacity(p, opacity);
                            p.sendMessage("§achangement de d'opacité effectué !");
                        } catch (NumberFormatException e){
                            p.sendMessage("§cErreur : La valeur est invalide !.");
                        }
                    } else {
                        p.sendMessage("/az opacity <velue> [player]");
                    }

                } else if (args[0].equalsIgnoreCase("worldenv")) {
                    if (args.length >= 3) {
                        if (Bukkit.getPlayer(args[2]) != null) {
                            if (args[1].equals("NORMAL") || args[1].equals("NETHER") || args[1].equals("THE_END")){
                                PacketWorldEnv.setWorldEnv(Bukkit.getPlayer(args[2]), args[1]);
                                p.sendMessage("§achangement de d'environnement effectué !");
                            } else {
                                p.sendMessage("§cErreur : La valeur est invalide !.");
                            }
                        } else {
                            p.sendMessage("§cCe joueur est hors-ligne !");
                        }
                        PacketWorldEnv.setWorldEnv(p, args[1]);
                        p.sendMessage("§achangement de d'environnement effectué !");
                    } else if (args.length == 2){
                        if (args[1].equals("NORMAL") || args[1].equals("NETHER") || args[1].equals("THE_END")){
                            PacketWorldEnv.setWorldEnv(p, args[1]);
                            p.sendMessage("§achangement de d'environnement effectué !");
                        } else {
                            p.sendMessage("§cErreur : La valeur est invalide !.");
                        }
                    } else {
                        p.sendMessage("/az worldenv <NORMAL, NETHER, THE_END> [player]");
                    }
                } else if (args[0].equalsIgnoreCase("vignette")) {
                    if (args.length >= 5) {
                        if (Bukkit.getPlayer(args[4]) != null){
                            try {
                                Float red = Float.parseFloat(args[1]);
                                Float green = Float.parseFloat(args[2]);
                                Float blue = Float.parseFloat(args[3]);
                                PacketVignette.setVignette(Bukkit.getPlayer(args[4]), red, green, blue);
                                p.sendMessage("§achangement de d'environnement effectué !");
                            } catch (NumberFormatException e){
                                p.sendMessage("§cErreur : Les valeur est invalide !.");
                            }
                        } else {
                            p.sendMessage("§cCe joueur est hors-ligne !");
                        }
                    } else if (args.length == 4){
                        try {
                            Float red = Float.parseFloat(args[1]);
                            Float green = Float.parseFloat(args[2]);
                            Float blue = Float.parseFloat(args[3]);
                            PacketVignette.setVignette(p, red, green, blue);
                            p.sendMessage("§achangement de d'environnement effectué !");
                        } catch (NumberFormatException e){
                            p.sendMessage("§cErreur : Les valeur est invalide !.");
                        }
                    } else {
                        p.sendMessage("/az vignette <red> <green> <blue> [player]");
                    }
                } else if (args[0].equalsIgnoreCase("tag")) {
                    if (args.length >= 3){
                        if (Bukkit.getPlayer(args[2]) != null){
                            PacketEntityMeta.setNameTag(Bukkit.getPlayer(args[2]), args[1]);
                            p.sendMessage("§achangement de d'environnement effectué !");
                        } else {
                            p.sendMessage("§cCe joueur est hors-ligne !");
                        }
                    } else if (args.length == 2){
                        PacketEntityMeta.setNameTag(p, args[1]);
                        p.sendMessage("§achangement de d'environnement effectué !");
                    } else {
                        p.sendMessage("/az tag <name> [player]");
                    }
                } else if (args[0].equalsIgnoreCase("item")) {
                    if (args[1].equalsIgnoreCase("render")){
                        if (args.length >= 4){
                            try {
                                NBTItem nbti = new NBTItem(p.getItemInHand());
                                nbti.mergeCompound(new NBTContainer("{PacRender: {Scale: "+Float.parseFloat(args[2])+", Color: "+ AZManager.getColor(args[3])+"}, PacDisplay: {Color: "+AZManager.getColor(args[3])+"}}"));
                                p.getItemInHand().setItemMeta(nbti.getItem().getItemMeta());
                            } catch (NumberFormatException e){
                                p.sendMessage("§cErreur : Les valeur est invalide !.");
                            }
                        } else if (args.length == 3) {
                            try {
                                NBTItem nbti = new NBTItem(p.getItemInHand());
                                nbti.mergeCompound(new NBTContainer("{PacRender: {Scale: "+Float.parseFloat(args[2])+"}}"));
                                p.getItemInHand().setItemMeta(nbti.getItem().getItemMeta());
                            } catch (NumberFormatException e){
                                p.sendMessage("§cErreur : Les valeur est invalide !.");
                            }
                        } else {
                            p.sendMessage("§c/az item render <scale> [color(Hex)]");
                        }
                    }
                }

            } else {
                commandSender.sendMessage("§cVous devez etre joueur pour executer cette commande !");
            }
            return true;
        }
        return false;
    }
}

