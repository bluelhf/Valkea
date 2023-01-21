package blue.lhf.valkea.model;

import net.kyori.adventure.text.*;
import org.bukkit.*;
import org.bukkit.configuration.*;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText;

public record Spawn(String id, String displayName, UnresolvedLocation location) {
    public static Spawn fromSection(final ConfigurationSection section) {
        return new Spawn(
            section.getName(),
            section.getString("display_name", section.getName()),
            new UnresolvedLocation(
                section.getString("world", Bukkit.getWorlds().get(0).getName()),
                section.getDouble("x", 0.0),
                section.getDouble("y", 0.0),
                section.getDouble("z", 0.0),
                section.getDouble("yaw", 0.0),
                section.getDouble("pitch", 0.0)
            )
        );
    }

    public Component getComponentName() {
        return miniMessage().deserialize(displayName);
    }

    public String getPlainTextName() {
        return plainText().serialize(getComponentName());
    }
}
