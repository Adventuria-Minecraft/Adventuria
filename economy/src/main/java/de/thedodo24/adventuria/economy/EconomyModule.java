package de.thedodo24.adventuria.economy;

import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.module.Module;
import de.thedodo24.adventuria.common.module.ModuleManager;
import de.thedodo24.adventuria.common.module.ModuleSettings;
import de.thedodo24.adventuria.economy.commands.MoneyCommand;
import de.thedodo24.adventuria.economy.vault.EconomyHandler;
import lombok.Getter;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@ModuleSettings
public class EconomyModule extends Module {

    @Getter
    private static EconomyModule instance;

    public EconomyModule(ModuleSettings settings, ModuleManager manager, JavaPlugin plugin) {
        super(settings, manager, plugin);

        instance = this;
    }

    @Override
    public void onLoad() {
        try {
            Class.forName("net.milkbowl.vault.permission.Permission");

            getPlugin().getServer().getServicesManager().register(net.milkbowl.vault.economy.Economy.class, new EconomyHandler(), getPlugin(), ServicePriority.Highest);
        } catch (Exception e) {
            CommonModule.getInstance().getPlugin().getLogger().log(Level.SEVERE, "[Adventuria] Vault is depended to load the economy module");
            onDisable();
        }

        new MoneyCommand();
    }
}
