package de.thedodo24.xenrodsystem.common;

import de.thedodo24.xenrodsystem.common.commands.OntimeCommand;
import de.thedodo24.xenrodsystem.common.commands.ScoreboardCommand;
import de.thedodo24.xenrodsystem.common.listener.PlayerListener;
import de.thedodo24.xenrodsystem.common.module.Module;
import de.thedodo24.xenrodsystem.common.module.ModuleManager;
import de.thedodo24.xenrodsystem.common.module.ModuleSettings;
import de.thedodo24.xenrodsystem.common.player.User;
import de.thedodo24.xenrodsystem.common.utils.Language;
import de.thedodo24.xenrodsystem.common.utils.Ontime;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

@ModuleSettings
@Getter
public class CommonModule extends Module {

    @Getter
    private static CommonModule instance;

    public final Map<UUID, Long> playerOnline = new HashMap<>();
    private final Map<UUID, Long> afkPlayer = new HashMap<>();

    private long nextDay;
    private long nextWeek;
    private SimpleDateFormat dateFormat;

    private final NamespacedKey inventoryType = new NamespacedKey(getPlugin(), "inventory-type");
    private final NamespacedKey inventoryKey = new NamespacedKey(getPlugin(), "inventory-key");
    private final NamespacedKey clickableItemKey = new NamespacedKey(getPlugin(), "clickable-key");

    public Language language;
    public Language materials;


    public CommonModule(ModuleSettings settings, ModuleManager manager, JavaPlugin plugin) {
        super(settings, manager, plugin);
        instance = this;
    }

    @Override
    public void onEnable() {
        language = new Language("language.yml");
        materials = new Language("materials.yml");
        dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss.SS");
        setTimes();
        getManager().getJobManager().createJobs();
        if(Bukkit.getOnlinePlayers().size() > 0) {
            Bukkit.getOnlinePlayers().forEach(all -> getPlayerOnline().put(all.getUniqueId(), System.currentTimeMillis()));
        }
        registerListener(new PlayerListener());
        registerCommands();
        startScheduler();
    }

    private void startScheduler() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(getPlugin(), this::checkTimes, 20, 20);
    }

    private void checkTimes() {
        getManager().getQuestManager().checkTime();
        Ontime.checkTime();
    }



    private void registerCommands() {
        new OntimeCommand();
        new ScoreboardCommand();
    }

    public void setTimes() {
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

    public long setNextWeek() {
        Calendar calendar = Calendar.getInstance(Locale.GERMANY);
        calendar.add(Calendar.WEEK_OF_YEAR, 1);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        nextWeek = calendar.getTimeInMillis();
        //nextWeek = System.currentTimeMillis() + 20000;
        return nextWeek;
    }

    @Override
    public void onDisable() {
        checkTimes();
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
