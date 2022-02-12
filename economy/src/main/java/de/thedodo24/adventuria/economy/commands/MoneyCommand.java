package de.thedodo24.adventuria.economy.commands;

import com.google.common.collect.Lists;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.player.User;
import de.thedodo24.adventuria.common.utils.Language;
import de.thedodo24.adventuria.economy.EconomyModule;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MoneyCommand implements CommandExecutor, TabCompleter {


    public MoneyCommand() {
        PluginCommand cmd = EconomyModule.getInstance().getPlugin().getCommand("money");
        assert cmd != null;
        cmd.setExecutor(this);
        cmd.setTabCompleter(this);
    }

    private String formatValue(long v) {
        if(v == 1)
            return v + " Coin";
        return v + " Coins";
    }

    private String prefix = Language.get("money-prefix");

    private void sendHelpMessage(CommandSender p) {
        TextComponent payHelp = new TextComponent(prefix);
        TextComponent payHelpCommand = new TextComponent(Language.get("money-help-pay"));
        TextComponent payHelpInfo = new TextComponent(Language.get("money-help-pay-desc"));
        payHelpCommand.setColor(ChatColor.GREEN);
        payHelpCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/money pay "));
        payHelpCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/money pay ").create()));
        payHelpInfo.setColor(ChatColor.GRAY);
        payHelp.addExtra(payHelpCommand);
        payHelp.addExtra(payHelpInfo);
        TextComponent balanceHelp = new TextComponent(prefix);
        TextComponent balanceHelpCommand = new TextComponent(Language.get("money-help-balance"));
        TextComponent balanceHelpInfo = new TextComponent(Language.get("money-help-balance-desc"));
        balanceHelpCommand.setColor(ChatColor.GREEN);
        balanceHelpCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/money balance "));
        balanceHelpCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/money balance ").create()));
        balanceHelpInfo.setColor(ChatColor.GRAY);
        balanceHelp.addExtra(balanceHelpCommand);
        balanceHelp.addExtra(balanceHelpInfo);
        TextComponent topHelp = new TextComponent(prefix);
        TextComponent topHelpCommand = new TextComponent(Language.get("money-help-top"));
        TextComponent topHelpInfo = new TextComponent(Language.get("money-help-top-desc"));
        topHelpCommand.setColor(ChatColor.GREEN);
        topHelpCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/money top"));
        topHelpCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/money top").create()));
        topHelpInfo.setColor(ChatColor.GRAY);
        topHelp.addExtra(topHelpCommand);
        topHelp.addExtra(topHelpInfo);


        TextComponent takeHelp = new TextComponent(prefix);
        TextComponent takeHelpCommmand = new TextComponent(Language.get("money-help-take"));
        TextComponent takeHelpInfo = new TextComponent(Language.get("money-help-take-desc"));
        takeHelpCommmand.setColor(ChatColor.GREEN);
        takeHelpCommmand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/money take "));
        takeHelpCommmand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/money take ").create()));
        takeHelpInfo.setColor(ChatColor.GRAY);
        takeHelp.addExtra(takeHelpCommmand);
        takeHelp.addExtra(takeHelpInfo);

        TextComponent giveHelp = new TextComponent(prefix);
        TextComponent giveHelpCommand = new TextComponent(Language.get("money-help-give"));
        TextComponent giveHelpInfo = new TextComponent(Language.get("money-help-give-desc"));
        giveHelpCommand.setColor(ChatColor.GREEN);
        giveHelpCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/money give "));
        giveHelpCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/money give ").create()));
        giveHelpInfo.setColor(ChatColor.GRAY);
        giveHelp.addExtra(giveHelpCommand);
        giveHelp.addExtra(giveHelpInfo);

        TextComponent setHelp = new TextComponent(prefix);
        TextComponent setHelpCommand = new TextComponent(Language.get("money-help-set"));
        TextComponent setHelpInfo = new TextComponent(Language.get("money-help-set-desc"));
        setHelpCommand.setColor(ChatColor.GREEN);
        setHelpCommand.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/money set "));
        setHelpCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("/money set ").create()));
        setHelpInfo.setColor(ChatColor.GRAY);
        setHelp.addExtra(setHelpCommand);
        setHelp.addExtra(setHelpInfo);
        if(p instanceof Player) {
            Player a = (Player) p;
            a.spigot().sendMessage(payHelp);
            a.spigot().sendMessage(balanceHelp);
            a.spigot().sendMessage(topHelp);
            if(a.hasPermission("money.admin")) {
                a.sendMessage("\n");
                a.spigot().sendMessage(giveHelp);
                a.spigot().sendMessage(takeHelp);
                a.spigot().sendMessage(setHelp);
            }
        } else {
            p.sendMessage(Language.get("money-help-console"));
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if(args.length == 1) {
            if(s instanceof Player) {
                Player p = (Player) s;
                if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal")) {
                    String money = formatValue(EconomyModule.getInstance().getManager().getUserManager().get(p.getUniqueId()).getBalance());
                    p.sendMessage(prefix + Language.get("money-balance", money));
                } else if(args[0].equalsIgnoreCase("top")) {
                    HashMap<User, Long> top = EconomyModule.getInstance().getManager().getUserManager().getHighestMoney();
                    p.sendMessage("§7|------| §aReichste Spieler §7|------|");
                    AtomicInteger a = new AtomicInteger(1);
                    top.forEach((user, val) -> p.sendMessage("§7"+a.getAndIncrement()+". §a" + user.getName() + "§7: " + formatValue(val)));
                } else {
                    sendHelpMessage(p);
                }
            } else {
                s.sendMessage(Language.get("haveto-player"));
            }
        } else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("balance") || args[0].equalsIgnoreCase("bal")) {
                User m = EconomyModule.getInstance().getManager().getUserManager().getByName(args[1]);
                if(m != null) {
                    s.sendMessage(prefix + Language.get("money-balance-player", m.getName(), formatValue(m.getBalance())));
                } else {
                    s.sendMessage(prefix + Language.get("player-not-exists", args[1]));
                }
            } else {
                sendHelpMessage(s);
            }
        } else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("pay")) {
                if(s instanceof Player) {
                    Player p = (Player) s;
                    User pMoney = EconomyModule.getInstance().getManager().getUserManager().get(p.getUniqueId());
                    String arg = args[2];
                    if(arg.contains(","))
                        arg = arg.replace(",", ".");
                    long value;
                    try {
                        value = Long.parseLong(arg);
                    } catch(NumberFormatException e) {
                        s.sendMessage(prefix + Language.get("argument-has-to-be", "2", "eine positive Zahl", args[2]));
                        return false;
                    }
                    if(value > 0) {
                        if((pMoney.getBalance() - value) >= 0) {
                            String name = "";
                            User oMoney = EconomyModule.getInstance().getManager().getUserManager().getByName(args[1]);
                            if(oMoney != null) {
                                if(!oMoney.getKey().equals(p.getUniqueId())) {
                                    oMoney.depositMoney(value);
                                    name = oMoney.getName();
                                    Player other;
                                    if((other = Bukkit.getPlayer(oMoney.getKey())) != null) {
                                        other.sendMessage(prefix + Language.get("money-pay-other", p.getName(), formatValue(value)));
                                    }
                                } else {
                                    p.sendMessage(prefix + Language.get("money-pay-yourself"));
                                    return false;
                                }
                            } else {
                                p.sendMessage(prefix + Language.get("player-not-exists", args[1]));
                                return false;
                            }
                            pMoney.withdrawMoney(value);
                            p.sendMessage(prefix + Language.get("money-pay"), name, formatValue(value));
                        } else {
                            p.sendMessage(prefix + Language.get("money-not-enough"));
                        }
                    } else {
                        p.sendMessage(prefix + Language.get("argument-has-to-be", "2", "eine positive Zahl", args[2]));
                    }
                } else {
                    s.sendMessage(Language.get("haveto-player"));
                }
            } else if(args[0].equalsIgnoreCase("give")) {
                if(s.hasPermission("money.give")) {
                    String arg = args[2];
                    if(arg.contains(","))
                        arg = arg.replace(",", ".");
                    long value;
                    try {
                        value = Long.parseLong(arg);
                    } catch(NumberFormatException e) {
                        s.sendMessage(prefix + Language.get("argument-has-to-be", "2", "eine positive Zahl", args[2]));
                        return false;
                    }
                    if(value > 0) {
                        String name = "";
                            User oMoney = EconomyModule.getInstance().getManager().getUserManager().getByName(args[1]);
                            if(oMoney != null) {
                                oMoney.depositMoney(value);
                                name = oMoney.getName();
                                Player other;
                                if((other = Bukkit.getPlayer(oMoney.getKey())) != null) {
                                    other.sendMessage(prefix + Language.get("money-give-other", s.getName(), formatValue(value)));
                                }
                            } else {
                                s.sendMessage(prefix + Language.get("player-not-exists"), args[1]);
                                return false;
                            }
                        s.sendMessage(prefix + Language.get("money-give", name, formatValue(value)));
                        String finalName = name;
                        Bukkit.getOnlinePlayers().forEach(all -> {
                            if(all.hasPermission("money.notify"))
                                all.sendMessage(prefix + Language.get("money-give-notify", s.getName(), finalName, formatValue(value)));
                        });
                    } else {
                        s.sendMessage(prefix + Language.get("argument-has-to-be", "2", "eine positive Zahl", args[2]));
                    }
                } else {
                    s.sendMessage(Language.get("no-permissions", "money.give"));
                }
            } else if(args[0].equalsIgnoreCase("set")) {
                if(s.hasPermission("money.set")) {
                    String arg = args[2];
                    if(arg.contains(","))
                        arg = arg.replace(",", ".");
                    long value;
                    try {
                        value = Long.parseLong(arg);
                    } catch(NumberFormatException e) {
                        s.sendMessage(prefix + Language.get("argument-has-to-be", "2", "eine positive Zahl", args[2]));
                        return false;
                    }
                    if(value >= 0) {
                        String name = "";
                            User oMoney = EconomyModule.getInstance().getManager().getUserManager().getByName(args[1]);
                            if(oMoney != null) {
                                oMoney.setBalance(value);
                                name = oMoney.getName();
                                Player other;
                                if((other = Bukkit.getPlayer(oMoney.getKey())) != null) {
                                    other.sendMessage(prefix + Language.get("money-set-other", s.getName(), formatValue(value)));
                                }
                            } else {
                                s.sendMessage(prefix + Language.get("player-not-exists"), args[1]);
                            }
                        s.sendMessage(prefix + Language.get("money-set", name, formatValue(value)));
                        String finalName = name;
                        Bukkit.getOnlinePlayers().forEach(all -> {
                            if(all.hasPermission("money.notify"))
                                all.sendMessage(prefix + Language.get("money-set-notify", s.getName(), finalName, formatValue(value)));
                        });
                    } else {
                        s.sendMessage(prefix + Language.get("argument-has-to-be", "2", "eine positive Zahl", args[2]));
                    }
                } else {
                    s.sendMessage(Language.get("no-permissions", "money.set"));
                }
            } else if(args[0].equalsIgnoreCase("take")) {
                if(s.hasPermission("money.take")) {
                    String arg = args[2];
                    if(arg.contains(","))
                        arg = arg.replace(",", ".");
                    long value;
                    try {
                        value = Long.parseLong(arg);
                    } catch(NumberFormatException e) {
                        s.sendMessage(prefix + Language.get("argument-has-to-be", "2", "eine positive Zahl", args[2]));
                        return false;
                    }
                    if(value >= 0) {
                        String name = "";
                            User oMoney = EconomyModule.getInstance().getManager().getUserManager().getByName(args[1]);
                            if(oMoney != null) {
                                name = oMoney.getName();
                                oMoney.withdrawMoney(value);
                                Player other;
                                if((other = Bukkit.getPlayer(oMoney.getKey())) != null) {
                                    other.sendMessage(prefix + Language.get("money-take-other", s.getName(), formatValue(value)));
                                }
                            } else {
                                s.sendMessage(prefix + Language.get("player-not-exists"), args[1]);
                            }
                        s.sendMessage(prefix + Language.get("money-take", name, formatValue(value)));
                        String finalName = name;
                        Bukkit.getOnlinePlayers().forEach(all -> {
                            if(all.hasPermission("money.notify"))
                                all.sendMessage(prefix + Language.get("money-take-notify", s.getName(), finalName, formatValue(value)));
                        });
                    } else {
                        s.sendMessage(prefix + Language.get("argument-has-to-be", "2", "eine positive Zahl", args[2]));
                    }
                } else {
                    s.sendMessage(Language.get("no-permissions", "money.take"));
                }
            } else {
                sendHelpMessage(s);
            }
        } else {
            sendHelpMessage(s);
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender s, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if(s instanceof Player p) {
            if(args.length == 1) {
                List<String> r = Lists.newArrayList("pay", "balance", "top");
                if(p.hasPermission("money.admin")) {
                    r.add("give");
                    r.add("take");
                    r.add("set");
                }
                return CommonModule.getInstance().removeAutoComplete(r, args[0]);
            } else if(args.length == 2) {
                switch(args[0].toLowerCase()) {
                    case "pay":
                    case "balance":
                    case "bal":
                        return CommonModule.getInstance().removeAutoComplete(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), args[1]);
                    case "give":
                    case "take":
                    case "set":
                        if(p.hasPermission("money.admin")) {
                            return CommonModule.getInstance().removeAutoComplete(Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), args[1]);
                        }
                }
            }
        }
        return Collections.emptyList();
    }
}
