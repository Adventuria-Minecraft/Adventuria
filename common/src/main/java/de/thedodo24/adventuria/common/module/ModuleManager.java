package de.thedodo24.adventuria.common.module;

import com.arangodb.ArangoDatabase;
import de.thedodo24.adventuria.common.job.JobManager;
import de.thedodo24.adventuria.common.player.UserManager;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@Getter
public class ModuleManager {

    private JavaPlugin plugin;
    private ArangoDatabase database;
    private HashMap<String, Module> modules;


    private UserManager userManager;
    private JobManager jobManager;

    public ModuleManager(ArangoDatabase database, JavaPlugin plugin) {
        this.plugin = plugin;
        this.database = database;
        this.modules = new HashMap<>();

        this.userManager = new UserManager(database);
        this.jobManager = new JobManager(database);

    }

    public final void loadModules(Class<? extends Module>... modules) {
        try {
            for(Class<? extends Module> clazz : modules)
                loadModule(clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadModule(Class<? extends Module> clazz) throws IllegalArgumentException, IllegalStateException {
        try {
            ModuleSettings settings;
            if((settings = clazz.getAnnotation(ModuleSettings.class)) == null)
                throw(new IllegalArgumentException("module-settings not set"));

            Module module =
                    clazz.getDeclaredConstructor(ModuleSettings.class, ModuleManager.class, JavaPlugin.class)
                            .newInstance(settings, this, plugin);

            modules.put(module.getName(), module);
            module.onLoad();
            if(module.isEnabled())
                module.onEnable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unloadModule(String moduleName) throws IllegalArgumentException {
        Module module;
        if((module = modules.get(moduleName)) == null)
            throw(new IllegalArgumentException("module not registered"));

        try {
            if(module.isEnabled())
                module.onDisable();

            this.modules.remove(moduleName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
