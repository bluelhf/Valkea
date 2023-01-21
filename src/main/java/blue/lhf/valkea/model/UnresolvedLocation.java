package blue.lhf.valkea.model;

import org.bukkit.*;

import java.util.*;

public record UnresolvedLocation(String worldKey, double x, double y, double z, double yaw, double pitch) {
    public Location resolve() throws IllegalStateException {
        final World world = parseWorld(worldKey);
        if (world == null) throw new IllegalStateException("World '" + worldKey + "' couldn't be found. Is it loaded?");
        return new Location(world, x, y, z, (float) yaw, (float) pitch);
    }


    private static World parseWorld(final String key) {
        if (key == null) return null;
        World world;
        if ((world = Bukkit.getWorld(key)) != null) return world;

        final UUID uuid = parseUUID(key);
        if (uuid != null && ((world = Bukkit.getWorld(uuid))) != null) return world;

        final NamespacedKey location = NamespacedKey.fromString(key);
        if (location != null && (world = Bukkit.getWorld(location)) != null) return world;
        return null;
    }

    private static UUID parseUUID(final String text) {
        try {
            return UUID.fromString(text);
        } catch (IllegalArgumentException invalid) {
            return null;
        }
    }
}
