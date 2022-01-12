package de.thedodo24.adventuria.common;

import de.thedodo24.adventuria.common.module.Module;
import de.thedodo24.adventuria.common.module.ModuleManager;
import de.thedodo24.adventuria.common.module.ModuleSettings;
import de.thedodo24.adventuria.common.player.User;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

@ModuleSettings
@Getter
public class CommonModule extends Module {

    @Getter
    private static CommonModule instance;

    private final Map<UUID, Long> playerOnline = new HashMap<>();
    private final Map<UUID, Long> afkPlayer = new HashMap<>();

    private long nextDay;
    private long nextWeek;
    private SimpleDateFormat dateFormat;


    public CommonModule(ModuleSettings settings, ModuleManager manager, JavaPlugin plugin) {
        super(settings, manager, plugin);
        instance = this;
    }

    @Override
    public void onEnable() {
        dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss.SS");
        setTimes();
        if(Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(all -> getPlayerOnline().put(all.getUniqueId(), System.currentTimeMillis()));
        }
    }

    private void setTimes() {
        getPlugin().getLogger().log(Level.INFO, "Setted next day to " + dateFormat.format(new Date(setNextDay())));
        getPlugin().getLogger().log(Level.INFO, "Setted next week to " + dateFormat.format(new Date(setNextWeek())));
    }

    public long setNextDay() {
        Calendar calendar = Calendar.getInstance(Locale.GERMANY);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        nextDay = calendar.getTimeInMillis();
        return nextDay;
    }

    private long setNextWeek() {
        Calendar calendar = Calendar.getInstance(Locale.GERMANY);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        nextWeek = calendar.getTimeInMillis();
        return nextWeek;
    }

    @Override
    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(all -> {
            User u = CommonModule.getInstance().getManager().getUserManager().get(all.getUniqueId());
            long ontime = System.currentTimeMillis() - CommonModule.getInstance().getPlayerOnline().get(all.getUniqueId());
            long afkTime = 0;
            if(CommonModule.getInstance().getAfkPlayer().containsKey(all.getUniqueId())) {
                afkTime = System.currentTimeMillis() - CommonModule.getInstance().getAfkPlayer().get(all.getUniqueId());
                ontime -= afkTime;
                CommonModule.getInstance().getAfkPlayer().remove(all.getUniqueId());
            }
            u.updateOntime(ontime);
            u.updateAfkTime(afkTime);
            CommonModule.getInstance().getPlayerOnline().remove(all.getUniqueId());
        });
    }

    public List<String> removeAutoComplete(List<String> list, String s) {
        if (!s.equalsIgnoreCase("")) {
            List<String> newList = new ArrayList<>(list);
            newList.stream().filter(a -> !a.startsWith(s)).forEach(list::remove);
        }
        return list;
    }
}
