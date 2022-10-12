package de.thedodo24.xenrodsystem.common.commands;

import com.google.common.collect.Lists;
import de.thedodo24.xenrodsystem.common.CommonModule;
import de.thedodo24.xenrodsystem.common.player.User;
import de.thedodo24.xenrodsystem.common.utils.Language;
import de.thedodo24.xenrodsystem.common.utils.Ontime;
import de.thedodo24.xenrodsystem.common.utils.SkullItems;
import de.thedodo24.xenrodsystem.common.utils.TimeFormat;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OntimeCommand implements CommandExecutor, TabCompleter {

    public OntimeCommand() {
        PluginCommand cmd = CommonModule.getInstance().getPlugin().getCommand("ontime");
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }

    private String prefix = Language.getLanguage().get("ontime-prefix");

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == 0) {
            if(s instanceof Player p) {
                User u = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
                Ontime.checkTime();
                long currentOntime = System.currentTimeMillis() - CommonModule.getInstance().getPlayerOnline().get(p.getUniqueId());
                long afkTime = 0;
                if(CommonModule.getInstance().getAfkPlayer().containsKey(p.getUniqueId())) {
                    afkTime = CommonModule.getInstance().getAfkPlayer().get(p.getUniqueId());
                    currentOntime -= afkTime;
                }
                long total = u.getTotalOntime() + currentOntime;
                long week = u.getWeekOntime() + currentOntime;
                long day = u.getDayOntime() + currentOntime;
                long totalAfk = u.getAfkTime() + afkTime;
                p.sendMessage(prefix + Language.getLanguage().get("ontime-self", TimeFormat.getString(total), TimeFormat.getString(week), TimeFormat.getString(day), TimeFormat.getString(totalAfk)));
            } else {
                s.sendMessage("Du musst ein Spieler sein.");
            }
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("history")) {
                if(s instanceof Player) {
                    Player p = (Player) s;
                    int inventorySize = (Bukkit.getOnlinePlayers().size() / 9) + 1;
                    Inventory inv = Bukkit.createInventory(null, inventorySize * 9, Component.text("§aWähle einen Spieler aus:"));
                    Bukkit.getOnlinePlayers().stream().map(SkullItems::getPlayerHead).forEach(inv::addItem);
                    p.openInventory(inv);
                } else {
                    s.sendMessage("Du musst ein Spieler sein");
                }
            } else if(args[0].equalsIgnoreCase("top")) {
                LinkedHashMap<User, Long> map = CommonModule.getInstance().getManager().getUserManager().getHighestOntime();
                s.sendMessage("§7|-----| §aTop 10 Ontime §7|-----|");
                AtomicInteger a = new AtomicInteger(1);
                map.forEach((key, val) -> s.sendMessage("§7"+a.getAndIncrement()+". §a" + key.getName() + "§7: " + TimeFormat.getInDays(val)));
            } else {
                User u = CommonModule.getInstance().getManager().getUserManager().getByName(args[0]);
                if(u != null) {
                    Ontime.checkTime();
                    long total = u.getTotalOntime();
                    long week = u.getWeekOntime();
                    long day = u.getDayOntime();
                    long totalAfk = u.getAfkTime();
                    if(Bukkit.getPlayer(u.getKey()) != null) {
                        if(CommonModule.getInstance().getPlayerOnline().containsKey(u.getKey())) {
                            long currentOntime = System.currentTimeMillis() - CommonModule.getInstance().getPlayerOnline().get(u.getKey());
                            long afkTime = 0;
                            if(CommonModule.getInstance().getAfkPlayer().containsKey(u.getKey())) {
                                afkTime = System.currentTimeMillis() - CommonModule.getInstance().getAfkPlayer().get(u.getKey());
                                currentOntime -= afkTime;
                            }
                            total += currentOntime;
                            week += currentOntime;
                            day += currentOntime;
                            totalAfk += afkTime;
                        }
                    }
                    s.sendMessage(prefix + Language.getLanguage().get("ontime-others", u.getName(), TimeFormat.getString(total), TimeFormat.getString(week), TimeFormat.getString(day), TimeFormat.getString(totalAfk)));
                } else {
                    s.sendMessage(prefix + Language.getLanguage().get("player-not-exists", args[0]));
                }
            }
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("history")) {
                if(s instanceof Player) {
                    Player p = (Player) s;
                    User u = CommonModule.getInstance().getManager().getUserManager().getByName(args[1]);
                    if(u != null) {
                        Inventory inventory = Bukkit.createInventory(null, 9, Component.text("§a» " + u.getName()));
                        Map<String, Long> ontimeHistoryMap = u.getOntimeHistoryMap();
                        Map<String, Long> afkHistoryMap = u.getAfkTimeHistoryMap();
                        for(int i = 0; i < 10; i++) {
                            int week = 9 - i;
                            if(ontimeHistoryMap.containsKey(String.valueOf(week)) && afkHistoryMap.containsKey(String.valueOf(week))) {
                                String ontime = TimeFormat.getInDays(ontimeHistoryMap.get(String.valueOf(week)));
                                String afkTime;
                                if(afkHistoryMap.containsKey(String.valueOf(week + 1))) {
                                    afkTime = TimeFormat.getInDays(afkHistoryMap.get(String.valueOf(week)) - afkHistoryMap.get(String.valueOf(week + 1)));
                                } else {
                                    afkTime = TimeFormat.getInDays(afkHistoryMap.get(String.valueOf(week)));
                                }
                                inventory.setItem(i,
                                        SkullItems.getNumberSkull(week,
                                                (week == 1 ? "§aLetzte Woche" : (week == 2 ? "§aVorletzte Woche" : "§aVor " + week + " Wochen")),
                                                Lists.newArrayList("§7» Ontime: §a" + ontime,
                                                        "§7» AFK-Zeit: §a" + afkTime)));
                            }
                        }
                        p.openInventory(inventory);
                        p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    } else {
                        p.sendMessage(prefix + Language.getLanguage().get("player-not-exists", args[1]));
                    }
                } else {
                    s.sendMessage(prefix + Language.getLanguage().get("haveto-player"));
                }
            } else {
                s.sendMessage(prefix + Language.getLanguage().get("ontime-help"));
            }
        } else {
            s.sendMessage(prefix + Language.getLanguage().get("ontime-help"));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 1) {
            List<String> allPlayers = Bukkit.getOnlinePlayers().stream().map(all -> all.getName()).collect(Collectors.toList());
            allPlayers.add("history");
            return CommonModule.getInstance().removeAutoComplete(allPlayers, args[0]);
        }
        return Collections.emptyList();
    }
}
