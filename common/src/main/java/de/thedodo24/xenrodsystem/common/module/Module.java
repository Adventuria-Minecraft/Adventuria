package de.thedodo24.xenrodsystem.common.module;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import de.thedodo24.xenrodsystem.common.utils.ConfigFile;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Module {

    protected String name;
    protected String commandGroup;

    protected ConfigFile config;
    protected ModuleManager manager;
    protected JavaPlugin plugin;
    protected List<Listener> activeListeners;

    @Setter
    protected boolean enabled;

    public Module(ModuleSettings settings, ModuleManager manager, JavaPlugin plugin) {
        this.name = settings.name().replace("module", getClass().getSimpleName().replace("Module", "")).toLowerCase();
        this.commandGroup = settings.commandGroup().replace("[module]", this.name);

        this.manager = manager;
        this.plugin = plugin;

        this.enabled = true;
        this.activeListeners = new ArrayList<>();

        this.config = new ConfigFile(plugin.getDataFolder(), this.name);
    }

    protected final void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    protected final void registerListener(Listener... listeners) {
        for(Listener listener : listeners)
            registerListener(listener);
    }

    public void onLoad() { plugin.getLogger().info("Loaded " + name + " module"); }

    public void onEnable() { plugin.getLogger().info("Enabling " + name + " module"); }

    public void onDisable() { plugin.getLogger().info("Disabling " + name + " module"); }

}
