package blue.lhf.valkea.commands;

import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.plugin.*;

import java.lang.reflect.*;
import java.util.*;
import java.util.logging.*;

public class CommandManager {
    private final Plugin host;
    private final Set<Command> commands = new HashSet<>();

    public CommandManager(final Plugin host) {
        this.host = host;
    }

    public boolean registerCommands(final Set<Command> toAdd) {
        boolean success = true;
        for (final Command command : toAdd) {
            success &= registerCommand(command);
        }

        synchroniseCommands();
        return success;
    }

    private boolean registerCommand(final Command toAdd) {
        final CommandMap commandMap = Bukkit.getCommandMap();
        if (commandMap.register(host.getName().toLowerCase(), toAdd)) {
            return this.commands.add(toAdd);
        } else return false;
    }

    public void unregisterCommands() {
        final CommandMap commandMap = Bukkit.getCommandMap();
        commandMap.getKnownCommands().values().removeAll(this.commands);
        this.commands.forEach((command) -> command.unregister(commandMap));
        this.commands.clear();

        this.synchroniseCommands();
    }

    private void synchroniseCommands() {
        try {
            Bukkit.getServer().getClass().getDeclaredMethod("syncCommands").invoke(Bukkit.getServer());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            host.getLogger().log(Level.SEVERE, "Failed to synchronise Brigadier with Bukkit. Commands will not update.", e);
        }
    }
}
