package de.thedodo24.xenrodsystem.common.commands;

import com.google.common.collect.Lists;
import de.thedodo24.xenrodsystem.common.CommonModule;
import de.thedodo24.xenrodsystem.common.player.CustomScoreboardType;
import de.thedodo24.xenrodsystem.common.player.User;
import de.thedodo24.xenrodsystem.common.utils.Language;
import de.thedodo24.xenrodsystem.common.utils.ManagerScoreboard;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class ScoreboardCommand implements CommandExecutor, TabCompleter {

    public ScoreboardCommand() {
        PluginCommand cmd = CommonModule.getInstance().getPlugin().getCommand("sboard");
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }

    private String prefix = Language.getLanguage().get("prefix");

    // /sboard set [line] [module]
    // /sboard off [line]
    // /sboard toggle
    // /sboard modules

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
        if(s instanceof Player) {
            Player p = (Player) s;
            if(args.length == 1) {
                if(args[0].equalsIgnoreCase("toggle")) {
                    if(ManagerScoreboard.getScoreboardMap().containsKey(p.getUniqueId())) {
                        ManagerScoreboard.getScoreboardMap().get(p.getUniqueId()).removeScoreboard(p);
                        ManagerScoreboard.getScoreboardMap().remove(p.getUniqueId());
                        p.sendMessage(prefix + "§7Du hast dein §aScoreboard §7ausgeblendet.");
                    } else {
                        new ManagerScoreboard(p);
                        p.sendMessage(prefix + "§7Du hast dein §aScoreboard §7eingeblendet.");
                    }
                } else if(args[0].equalsIgnoreCase("modules")) {
                    p.sendMessage(prefix + Arrays.stream(CustomScoreboardType.values()).map(Enum::toString).map(String::toLowerCase).collect(Collectors.joining("§7, §a")));
                } else {
                    p.sendMessage(prefix + "§a/sboard set [Zeile] [Modulname] <[Ontime]>\n" +
                            prefix + "§a/sboard off [Zeile]\n" +
                            prefix + "§a/sboard toggle\n" +
                            prefix + "§a/sboard modules");
                }
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("off")) {
                    int line;
                    try {
                        line = Integer.parseInt(args[1]);
                    } catch(NumberFormatException e) {
                        p.sendMessage(prefix + "§aArgument 2 §7muss eine positive ganzzahlige Zahl sein.");
                        return false;
                    }
                    if(line >= 0) {
                        User user = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
                        if(user.checkCustomScoreboard(line)) {
                            List<Integer> sortedList = user.getCustomScoreboard().keySet().stream().map(Integer::parseInt).sorted().collect(Collectors.toList());
                            Collections.reverse(sortedList);
                            int highestLine = sortedList.get(0);
                            if(highestLine == 0 && line == 0) {
                                p.sendMessage(prefix + "§7Du kannst diese Linie nicht löschen.");
                                return false;
                            }
                            Set<String> strings = user.getCustomScoreboard().keySet();
                            Map<String, Map<String, String>> newMap = new HashMap<>();
                            strings.stream().filter(key -> Integer.parseInt(key) > line).forEach(key -> newMap.put(String.valueOf(Integer.parseInt(key) - 1),
                                    user.getCustomScoreboard(Integer.parseInt(key))));
                            strings.stream().filter(key -> Integer.parseInt(key) < line).forEach(key -> newMap.put(key, user.getCustomScoreboard(Integer.parseInt(key))));
                            user.setCustomScoreboard(newMap);
                            p.sendMessage(prefix + "§aZeile #" + line + " §7wurde entfernt.");
                            ManagerScoreboard.getScoreboardMap().get(user.getKey()).sendScoreboard(p);
                        } else {
                            p.sendMessage(prefix + "§7Du hast den §aPlatz #" + line + " §7nicht besetzt.");
                        }
                    } else {
                        p.sendMessage(prefix + "§aArgument 2 §7muss eine positive ganzzahlige Zahl sein.");
                    }
                } else {
                    p.sendMessage(prefix + "§a/sboard set [Zeile] [Modulname] <[Ontime]>\n" +
                            prefix + "§a/sboard off [Zeile]\n" +
                            prefix + "§a/sboard toggle\n" +
                            prefix + "§a/sboard modules");
                }
            } else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("set")) {
                    int line;
                    try {
                        line = Integer.parseInt(args[1]);
                    } catch(NumberFormatException e) {
                        p.sendMessage(prefix + "§aArgument 2 §7muss eine positive ganzzahlige Zahl sein.");
                        return false;
                    }
                    if(line >= 0) {
                        String module = args[2];
                        CustomScoreboardType type;
                        try {
                            type = CustomScoreboardType.valueOf(module.toUpperCase());
                        } catch(Exception e) {
                            p.sendMessage(prefix + "§7Gebe bitte einen gültigen Modulnamen an.");
                            return false;
                        }
                        if(type.equals(CustomScoreboardType.MONEY) || type.equals(CustomScoreboardType.ONLINE) || type.equals(CustomScoreboardType.TPS)
                                || type.equals(CustomScoreboardType.PING) || type.equals(CustomScoreboardType.JOB)) {
                            User user = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
                            List<Integer> sortedList = user.getCustomScoreboard().keySet().stream().map(Integer::parseInt).sorted().collect(Collectors.toList());
                            Collections.reverse(sortedList);
                            int highestLine = sortedList.get(0);
                            if((highestLine + 1) >= line) {
                                if (line <= 3) {
                                    if(!user.checkCustomScoreboard(type)) {
                                        user.setCustomScoreboard(line, new HashMap<>() {{
                                            put("type", type.toString());
                                            put("value", "");
                                        }});
                                        p.sendMessage(prefix + "§7Du hast das Modul §a" + type + " §7in §aLinie #" + line + " §7aktiviert.");
                                        ManagerScoreboard.getScoreboardMap().get(user.getKey()).sendScoreboard(p);
                                    } else {
                                        p.sendMessage(prefix + "§7Dieses Modul ist schon §aaktiviert§7.");
                                    }
                                } else {
                                    p.sendMessage(prefix + "§7Du kannst maximal §a4 Zeilen §7haben.");
                                }
                            } else {
                                p.sendMessage(prefix + "§7Deine ausgewählte Linie ist zu groß. Dein Maximum liegt bei §a" + (highestLine + 1) + "§7.");
                            }
                        } else {
                            p.sendMessage(prefix + "§7Du musst noch den §cOntime-Abstand §7angeben.");
                        }
                    } else {
                        p.sendMessage(prefix + "§aArgument 2 §7muss eine positive ganzzahlige Zahl sein.");
                    }
                } else {
                    p.sendMessage(prefix + "§a/sboard set [Zeile] [Modulname] <[Ontime]>\n" +
                            prefix + "§a/sboard off [Zeile]\n" +
                            prefix + "§a/sboard toggle\n" +
                            prefix + "§a/sboard modules");
                }
            } else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("set")) {
                    int line;
                    try {
                        line = Integer.parseInt(args[1]);
                    } catch(NumberFormatException e) {
                        p.sendMessage(prefix + "§aArgument 2 §7muss eine positive ganzzahlige Zahl sein.");
                        return false;
                    }
                    if(line >= 0) {
                        String module = args[2];
                        CustomScoreboardType type;
                        try {
                            type = CustomScoreboardType.valueOf(module.toUpperCase());
                        } catch(Exception e) {
                            p.sendMessage(prefix + "§7Gebe bitte einen gültigen Modulnamen an.");
                            return false;
                        }
                        if(type.equals(CustomScoreboardType.ONTIME)) {
                            String value = args[3];
                                if(!(value.equalsIgnoreCase("day") || value.equalsIgnoreCase("week") || value.equalsIgnoreCase("total"))) {
                                    p.sendMessage(prefix + "§7Wähle bitte zwischen §aday§7, §aweek§7 oder §atotal§7.");
                                    return false;
                                }
                            User user = CommonModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
                            List<Integer> sortedList = user.getCustomScoreboard().keySet().stream().map(Integer::parseInt).sorted().collect(Collectors.toList());
                            Collections.reverse(sortedList);
                            int highestLine = sortedList.get(0);
                            if((highestLine + 1) >= line) {
                                if(line <= 3) {
                                    if(!user.checkCustomScoreboard(type)) {
                                        user.setCustomScoreboard(line, new HashMap<>() {{
                                            put("type", type.toString());
                                            put("value", value);
                                        }});
                                        ManagerScoreboard.getScoreboardMap().get(user.getKey()).sendScoreboard(p);
                                        p.sendMessage(prefix + "§7Du hast das Modul §a" + type + " §7in §aLinie #" + line + " §7aktiviert.");
                                    } else {
                                        p.sendMessage(prefix + "§7Dieses Modul ist schon §aaktiviert§7.");
                                    }
                                } else {
                                    p.sendMessage(prefix + "§7Du kannst maximal §a4 Zeilen §7haben.");
                                }
                            } else {
                                p.sendMessage(prefix + "§7Deine ausgewählte Linie ist zu groß. Dein Maximum liegt bei §a" + (highestLine + 1) + "§7.");
                            }
                        } else {
                            p.sendMessage(prefix + "§7Du musst noch den §cOntime-Abstand §7angeben.");
                        }
                    } else {
                        p.sendMessage(prefix + "§aArgument 2 §7muss eine positive ganzzahlige Zahl sein.");
                    }
                } else {
                    p.sendMessage(prefix + "§a/sboard set [Zeile] [Modulname] <[Ontime]>\n" +
                            prefix + "§a/sboard off [Zeile]\n" +
                            prefix + "§a/sboard toggle\n" +
                            prefix + "§a/sboard modules");
                }
            } else {
                p.sendMessage(prefix + "§a/sboard set [Zeile] [Modulname] <[Ontime]>\n" +
                        prefix + "§a/sboard off [Zeile]\n" +
                        prefix + "§a/sboard toggle\n" +
                        prefix + "§a/sboard modules");
            }
        } else {
            s.sendMessage("Du musst ein Spieler sein.");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String label, String[] args) {
        if(s instanceof Player) {
            Player p = (Player) s;
            if(args.length == 1) {
                List<String> returnList = Lists.newArrayList("set", "off", "toggle", "modules");
                return CommonModule.getInstance().removeAutoComplete(returnList, args[0]);
            } else if(args.length == 2) {
                if(args[0].equalsIgnoreCase("set") || args[0].equalsIgnoreCase("off"))
                    return CommonModule.getInstance().removeAutoComplete(Lists.newArrayList("0", "1", "2", "3"), args[1]);
            } else if(args.length == 3) {
                if(args[0].equalsIgnoreCase("set")) {
                    return CommonModule.getInstance().removeAutoComplete(Arrays.stream(CustomScoreboardType.values()).map(Enum::toString).map(String::toLowerCase).collect(Collectors.toList()), args[2]);
                }
            } else if(args.length == 4) {
                if(args[0].equalsIgnoreCase("set")) {
                    if (args[2].equalsIgnoreCase("ontime")) {
                        List<String> returnList = Lists.newArrayList("day", "week", "total");
                        return CommonModule.getInstance().removeAutoComplete(returnList, args[3]);
                    }
                }
            }
        }
        return Lists.newArrayList();
    }


}
