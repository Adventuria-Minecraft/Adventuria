package de.thedodo24.xenrodsystem;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import de.thedodo24.xenrodsystem.common.CommonModule;
import de.thedodo24.xenrodsystem.common.module.ModuleManager;
import de.thedodo24.xenrodsystem.common.utils.ConfigFile;
import de.thedodo24.xenrodsystem.economy.EconomyModule;
import de.thedodo24.xenrodsystem.job.JobModule;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

@Getter
public class XenrodSystem extends JavaPlugin {

    @Getter
    public static XenrodSystem instance;

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
        getLogger().log(Level.INFO, "\n" +
                "__  __                     _ ___         _             \n" +
                "\\ \\/ /___ _ _  _ _ ___  __| / __|_  _ __| |_ ___ _ __  \n" +
                " >  </ -_) ' \\| '_/ _ \\/ _` \\__ \\ || (_-<  _/ -_) '  \\ \n" +
                "/_/\\_\\___|_||_|_| \\___/\\__,_|___/\\_, /__/\\__\\___|_|_|_|\n" +
                "                                 |__/                  ");
        getLogger().log(Level.INFO, "\n\nLoaded plugin.");
    }
}
