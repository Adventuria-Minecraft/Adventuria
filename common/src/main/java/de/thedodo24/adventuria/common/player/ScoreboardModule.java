package de.thedodo24.adventuria.common.player;

import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.utils.Lag;
import de.thedodo24.adventuria.common.utils.TimeFormat;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Getter
public class ScoreboardModule {

    private String prefixPrefix = "§8§l▰§7▰ ";
    private String prefixSuffix = "§8§l» ";

    private String formatValue(double v) { return NumberFormat.getCurrencyInstance(Locale.GERMANY).format(v).split(" ")[0] + " A"; }

    public String getPattern(CustomScoreboardType type) {
        switch(type) {
            case MONEY:
                return "§bGeld";
            case ONLINE:
                return "§aOnline";
            case ONTIME:
                return "§5Ontime";
            case TPS:
                return "§4TPS";
            case PING:
                return "§cPing";
            case WORLDTIME:
                return "§eWeltzeit";
            default:
                return "";
        }

    }

    public String getValue(CustomScoreboardType type, User user, String value) {
        switch (type) {
            case ONTIME:
                long currentOntime = 0;
                if(CommonModule.getInstance().getPlayerOnline().containsKey(user.getKey()))
                    currentOntime = System.currentTimeMillis() - CommonModule.getInstance().getPlayerOnline().get(user.getKey());
                switch(value.toLowerCase()) {
                    case "day":
                        return TimeFormat.getInDays(user.getDayOntime() + currentOntime);
                    case "week":
                        return TimeFormat.getInDays(user.getWeekOntime() + currentOntime);
                    case "total":
                        return TimeFormat.getInDays(user.getTotalOntime() + currentOntime);
                    default:
                        return TimeFormat.getInDays(user.getTotalOntime() + currentOntime);
                }
            case ONLINE:
                return Bukkit.getOnlinePlayers().size() + " §8/§7 " + Bukkit.getMaxPlayers();
            case MONEY:
                return formatValue(((Long) user.getBalance()).doubleValue() / 100);
            case TPS:
                return String.valueOf(((Long) (long) (Lag.getTPS() * 100)).doubleValue() / 100);
            case PING:
                return Bukkit.getPlayer(user.getKey()).spigot().getPing() + " ms";
            case WORLDTIME:
                Date date = TimeFormat.ticksToDate(Bukkit.getPlayer(user.getKey()).getWorld().getTime());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.GERMAN);
                return simpleDateFormat.format(date);
            case JOB:
                if(user.hasIndividualJob())
                    return user.getIndividualJob().getName();
                return "Kein Job";
            default:
                return "";
        }
    }

    public String getPlaceholder(int index) {
        if(index >= 1 && index <= 9) {
            return "§" + index;
        } else if(index > 9) {
            switch(index) {
                case 10:
                    return "§a";
                case 11:
                    return "§b";
                case 12:
                    return "§c";
                case 13:
                    return "§d";
                case 14:
                    return "§e";
                case 15:
                    return "§f";
                case 16:
                    return "§k";
                case 17:
                    return "§l";
                case 18:
                    return "§m";
                case 19:
                    return "§n";
                case 20:
                    return "§o";
            }
        }
        return "";
    }

}
