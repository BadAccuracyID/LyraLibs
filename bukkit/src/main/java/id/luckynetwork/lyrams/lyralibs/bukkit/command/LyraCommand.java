package id.luckynetwork.lyrams.lyralibs.bukkit.command;

import id.luckynetwork.lyrams.lyralibs.bukkit.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public abstract class LyraCommand extends Command {

    private final String permission;

    public LyraCommand(String command, String permission, List<String> aliases) {
        super(command, "", "/" + command, aliases);
        this.permission = permission;

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(command, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public LyraCommand(String command, List<String> aliases) {
        super(command, "", "/" + command, aliases);
        this.permission = "";

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(command, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public LyraCommand(String command, String permission) {
        super(command, "", "/" + command, new ArrayList<>());
        this.permission = permission;

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(command, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public LyraCommand(String command) {
        super(command, "", "/" + command, new ArrayList<>());
        this.permission = "";

        try {
            Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);

            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());
            commandMap.register(command, this);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes the command, returning its success
     *
     * @param sender       Source object which is executing this command
     * @param commandLabel The alias of the command used
     * @param args         All arguments passed to the command, split via ' '
     * @return true if the command was successful, otherwise false
     */
    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!permission.isEmpty()) {
            if (!PlayerUtils.checkPermission(sender, permission, false, false)) {
                return true;
            }
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                this.sendDefaultMessage(sender);
                return true;
            }
        }

        this.execute(sender, args);
        return true;
    }

    public abstract void execute(CommandSender sender, String[] args);

    /**
     * Executed on tab completion for this command, returning a list of
     * options the player can tab through.
     *
     * @param sender Source object which is executing this command
     * @param alias  the alias being used
     * @param args   All arguments passed to the command, split via ' '
     * @return a list of tab-completions for the specified arguments. This
     * will never be null. List may be immutable.
     * @throws IllegalArgumentException if sender, alias, or args is null
     */
    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (!permission.isEmpty()) {
            if (!PlayerUtils.checkPermission(sender, permission, false, true)) {
                return null;
            }
        }

        return this.getTabSuggestions(sender, alias, args);
    }

    /**
     * Send the default message to the given CommandSender.
     *
     * @param sender The CommandSender that sent the command.
     */
    public abstract void sendDefaultMessage(CommandSender sender);

    /**
     * "Returns a list of tab suggestions for the specified command arguments."
     *
     * The first parameter is the CommandSender that is requesting the tab suggestions. This is the player that is typing
     * the command
     *
     * @param sender The CommandSender who is executing the command.
     * @param alias The alias that was used to call the command.
     * @param args The arguments that were passed to the command.
     * @return A list of strings.
     */
    public abstract List<String> getTabSuggestions(CommandSender sender, String alias, String[] args);
}
