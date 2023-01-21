package blue.lhf.valkea;

import blue.lhf.valkea.commands.*;
import blue.lhf.valkea.model.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.configuration.*;
import org.bukkit.plugin.java.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;

public final class Valkea extends JavaPlugin {
    private CommandManager manager;

    @Override
    public void onEnable() {
        manager = new CommandManager(this);
        saveDefaultConfig();
        watcher.start();
    }

    @Override
    public void onDisable() {
        manager.unregisterCommands();
        watcher.close();
    }

    private final FileWatcher watcher = new FileWatcher(getConfigFile().toPath()) {
        @Override
        public void onChange() {
            getLogger().info("Configuration file updated. Reloading changes...");
            reloadConfig();

            final Configuration yamlConfig = getConfig();
            final ValkeaConfig valkeaConfig = ValkeaConfig.parse(yamlConfig);
            final Set<Command> commands = valkeaConfig.buildCommands();

            registerLater(commands).handle((unused, error) -> {
                if (error != null) {
                    getLogger().log(Level.SEVERE, "Failed to reload changes!", error);
                } else getLogger().info("Changes reloaded! :)");

                return null;
            });
        }

        private CompletableFuture<Void> registerLater(final Set<Command> commands) {
            final CompletableFuture<Void> future = new CompletableFuture<>();
            Bukkit.getScheduler().runTask(Valkea.this, () -> {
                manager.unregisterCommands();
                if (!manager.registerCommands(commands)) {
                    getLogger().warning("One or more commands had the same names as existing commands.");
                    getLogger().warning("To use them, add '" + Valkea.this.getName().toLowerCase() + ":' to the start of the command.");
                }
                future.complete(null);
            });

            return future;
        }
    };


    private File getConfigFile() {
        // This is hard-coded into JavaPlugin and inaccessible. Hope this is right...
        return new File(getDataFolder(), "config.yml");
    }
}
