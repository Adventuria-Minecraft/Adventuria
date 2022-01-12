package de.thedodo24.adventuria.economy;

import de.thedodo24.adventuria.common.module.Module;
import de.thedodo24.adventuria.common.module.ModuleManager;
import de.thedodo24.adventuria.common.module.ModuleSettings;
import de.thedodo24.adventuria.economy.vault.EconomyHandler;
import lombok.Getter;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

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
            System.err.println("[Adventuria] Vault is depended to load the economy module");
            onDisable();
        }
    }
}
