package blue.lhf.valkea.commands;

import blue.lhf.valkea.model.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.command.defaults.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.PlayerTeleportEvent.*;
import org.jetbrains.annotations.*;

import static java.util.Collections.emptyList;
import static net.kyori.adventure.text.Component.*;
import static net.kyori.adventure.text.format.NamedTextColor.*;

public class SpawnCommand extends BukkitCommand {
    private final Spawn spawn;

    public SpawnCommand(@NotNull String name, final Spawn spawn) {
        super(name, "Teleports the player to " + spawn.getPlainTextName() + ".", "/" + name, emptyList());
        this.spawn = spawn;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (!(sender instanceof Entity entity)) {
            sender.sendMessage(text("Only entities can be teleported to a spawn.").color(RED));
            return false;
        }

        try {
            final Location location = spawn.location().resolve();
            entity.teleportAsync(location, TeleportCause.PLUGIN).handle((success, exc) -> {
                if (success == null || !success || exc != null) {
                    reportException(sender, exc != null ? exc : new Exception("An unknown error occurred while teleporting."));
                    if (exc != null) exc.printStackTrace();
                    return false;
                }

                entity.sendActionBar(empty().color(GREEN)
                    .append(text("You were teleported to "))
                    .append(spawn.getComponentName())
                    .append(text("!"))
                );

                return true;
            });
        } catch (IllegalStateException failure) {
            reportException(sender, failure);
        }

        return true;
    }

    protected void reportException(final CommandSender sender, final Throwable failure) {
        sender.sendMessage(empty().color(RED).append(text("Failed to teleport you to ")).append(spawn.getComponentName()).append(text(": " + failure.getLocalizedMessage())));
    }
}
