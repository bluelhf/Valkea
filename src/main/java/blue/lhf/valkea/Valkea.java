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
            final CompletableFuture<Void> future = new CompletableFuture<>();
            final Set<Command> commands = buildCommands(parseConfig());
            Bukkit.getScheduler().runTask(Valkea.this, () -> {
                manager.unregisterCommands();
                manager.registerCommands(commands);
                future.complete(null);
            });

            future.handle((unused, error) -> {
                if (error != null) {
                    getLogger().log(Level.SEVERE, "Failed to reload changes!", error);
                } else getLogger().info("Changes reloaded! :)");

                return null;
            });
        }

        private Set<Spawn> parseConfig() {
            final Set<Spawn> set = new HashSet<>(getConfig().getKeys(false).size());
            for (final String label : getConfig().getKeys(false)) {
                final ConfigurationSection section = getConfig().getConfigurationSection(label);
                if (section == null) {
                    getLogger().warning(() -> "Tried to define spawn '%s', but it's not a section in the configuration.".formatted(label));
                    if (List.of("world", "x", "y", "z", "yaw", "pitch").contains(label)) {
                        getLogger().warning("HINT: Did you forget to put spaces before it?");
                    }
                    getLogger().warning("");
                    getLogger().warning("Ignoring it until the configuration is fixed.");

                    continue;
                }

                final boolean disabled = section.getBoolean("disabled", false);
                if (!disabled) set.add(Spawn.fromSection(section));
            }

            return set;
        }

        private Set<Command> buildCommands(final Set<Spawn> spawns) {
            final Set<Command> commands = new HashSet<>();
            for (final Spawn spawn : spawns) {
                final Command command = new SpawnCommand(spawn.id(), spawn);
                commands.add(command);
            }

            return commands;
        }
    };


    private File getConfigFile() {
        // This is hard-coded into JavaPlugin and inaccessible. Hope this is right...
        return new File(getDataFolder(), "config.yml");
    }
}
