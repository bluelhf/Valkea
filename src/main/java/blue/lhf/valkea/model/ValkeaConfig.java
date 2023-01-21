package blue.lhf.valkea.model;

import blue.lhf.valkea.commands.*;
import org.bukkit.command.*;
import org.bukkit.configuration.*;

import java.util.*;

public record ValkeaConfig(Set<SpawnPoint> spawnPoints) {
    public ValkeaConfig(final Set<SpawnPoint> spawnPoints) {
        this.spawnPoints = Collections.unmodifiableSet(spawnPoints);
    }

    public Set<Command> buildCommands() {
        final Set<Command> commands = new HashSet<>();
        for (final SpawnPoint spawnPoint : spawnPoints) {
            final Command command = new SpawnPointCommand(spawnPoint.id(), spawnPoint);
            commands.add(command);
        }

        return commands;
    }

    public static ValkeaConfig parse(final Configuration config) {
        final ConfigurationSection spawnPointSection =
            config.getConfigurationSection("spawn_points");

        return new ValkeaConfig(
            parseSpawnPoints(spawnPointSection));
    }

    private static Set<SpawnPoint> parseSpawnPoints(final ConfigurationSection section) {
        final Set<SpawnPoint> set = new HashSet<>();
        if (section == null) return set;

        for (final String label : section.getKeys(false)) {
            final ConfigurationSection spawnPoint = section.getConfigurationSection(label);
            if (spawnPoint == null) {
                continue;
            }

            final boolean disabled = spawnPoint.getBoolean("disabled", false);
            if (!disabled) set.add(SpawnPoint.parseSpawnPoint(spawnPoint));
        }

        return set;
    }
}
