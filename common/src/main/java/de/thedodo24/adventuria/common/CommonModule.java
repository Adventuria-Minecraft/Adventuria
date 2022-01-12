package de.thedodo24.adventuria.common;

import de.thedodo24.adventuria.common.module.Module;
import de.thedodo24.adventuria.common.module.ModuleManager;
import de.thedodo24.adventuria.common.module.ModuleSettings;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@ModuleSettings
@Getter
public class CommonModule extends Module {

    @Getter
    private static CommonModule instance;


    public CommonModule(ModuleSettings settings, ModuleManager manager, JavaPlugin plugin) {
        super(settings, manager, plugin);
        instance = this;
    }
}
