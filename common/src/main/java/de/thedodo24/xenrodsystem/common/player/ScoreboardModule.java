package de.thedodo24.xenrodsystem.common.player;

import de.thedodo24.xenrodsystem.common.CommonModule;
import de.thedodo24.xenrodsystem.common.utils.Lag;
import de.thedodo24.xenrodsystem.common.utils.Language;
import de.thedodo24.xenrodsystem.common.utils.TimeFormat;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Getter
public class ScoreboardModule {

    private String prefixPrefix = "§8§l▰§7▰ ";
    private String prefixSuffix = "§8§l» ";

    private String formatValue(long v) {
        if(v == 1L)
            return v + " " + Language.getLanguage().get("money-value-one");
        return v  + " " + Language.getLanguage().get("money-value");
    }

    public String getPattern(CustomScoreboardType type) {
        return switch (type) {
            case MONEY -> "§bGeld";
            case ONLINE -> "§aOnline";
            case ONTIME -> "§5Ontime";
            case TPS -> "§4TPS";
            case PING -> "§cPing";
            case JOB -> "§9Job";
        };

    }

    public String getValue(CustomScoreboardType type, User user, String value) {
        switch (type) {
            case ONTIME:
                long currentOntime = 0;
                if(CommonModule.getInstance().getPlayerOnline().containsKey(user.getKey()))
                    currentOntime = System.currentTimeMillis() - CommonModule.getInstance().getPlayerOnline().get(user.getKey());
                return switch (value.toLowerCase()) {
                    case "day" -> TimeFormat.getInDays(user.getDayOntime() + currentOntime);
                    case "week" -> TimeFormat.getInDays(user.getWeekOntime() + currentOntime);
                    case "total" -> TimeFormat.getInDays(user.getTotalOntime() + currentOntime);
                    default -> TimeFormat.getInDays(user.getTotalOntime() + currentOntime);
                };
            case ONLINE:
                return Bukkit.getOnlinePlayers().size() + " §8/§7 " + Bukkit.getMaxPlayers();
            case MONEY:
                return formatValue(user.getBalance());
            case TPS:
                return String.valueOf(((Long) (long) (Lag.getTPS() * 100)).doubleValue() / 100);
            case PING:
                return Bukkit.getPlayer(user.getKey()).spigot().getPing() + " ms";
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
