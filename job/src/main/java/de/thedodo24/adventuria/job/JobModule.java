package de.thedodo24.adventuria.job;

import de.thedodo24.adventuria.common.module.Module;
import de.thedodo24.adventuria.common.module.ModuleManager;
import de.thedodo24.adventuria.common.module.ModuleSettings;
import de.thedodo24.adventuria.job.commands.JobCommand;
import de.thedodo24.adventuria.job.commands.QuestCommand;
import de.thedodo24.adventuria.job.listener.InventoryListener;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

@ModuleSettings
@Getter
public class JobModule extends Module {

    @Getter
    private static JobModule instance;


    public JobModule(ModuleSettings settings, ModuleManager manager, JavaPlugin plugin) {
        super(settings, manager, plugin);
        instance = this;
    }



    @Override
    public void onEnable() {
        new JobCommand();
        new QuestCommand();
        registerListener(new InventoryListener());
    }
}
