package de.thedodo24.adventuria;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import de.thedodo24.adventuria.common.CommonModule;
import de.thedodo24.adventuria.common.module.ModuleManager;
import de.thedodo24.adventuria.common.utils.ConfigFile;
import de.thedodo24.adventuria.economy.EconomyModule;
import de.thedodo24.adventuria.job.JobModule;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public class Adventuria extends JavaPlugin {

    @Getter
    public static Adventuria instance;

    public ArangoDatabase arangoDatabase;
    public ArangoDB arangoDB;
    public String prefix = "[Adventuria] ";

    private ModuleManager moduleManager;


    @Override
    public void onEnable() {
        instance = this;

        ConfigFile config = new ConfigFile(getDataFolder(), "config.yml");
        config.create("database.host", "localhost");
        config.create("database.user", "user");
        config.create("database.password", "password");
        config.create("database.database", "database");
        config.create("database.port", 8529);
        config.save();

        arangoDB = new ArangoDB.Builder().host(config.getString("database.host"), config.getInt("database.port"))
                .user(config.getString("database.user")).password(config.getString("database.password")).build();
        arangoDatabase = arangoDB.db(config.getString("database.database"));

        moduleManager = new ModuleManager(getArangoDatabase(), this);
        moduleManager.loadModules(CommonModule.class, EconomyModule.class, JobModule.class);
        getLogger().log(Level.INFO, "Loaded Adventuria plugin");
    }
}
