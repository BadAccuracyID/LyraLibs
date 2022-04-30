package id.luckynetwork.lyrams.lyralibs.bukkit.command;

import id.luckynetwork.lyrams.lyralibs.bukkit.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class LyraParentCommand extends Command {

    private final String permission;
    private final List<LyraSubCommand> subCommands = new ArrayList<>();

    public LyraParentCommand(String command, String permission, List<String> aliases) {
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

    public LyraParentCommand(String command, List<String> aliases) {
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

    public LyraParentCommand(String command, String permission) {
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

    public LyraParentCommand(String command) {
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
        if (!PlayerUtils.checkPermission(sender, permission, false, false)) {
            return true;
        }

        if (args.length == 0) {
            this.sendDefaultMessage(sender);
            return true;
        }

        for (LyraSubCommand subCommand : this.subCommands) {
            if (subCommand.getName().equals(args[0])) {
                subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
            if (subCommand.getAliases().contains(args[0].toLowerCase())) {
                subCommand.execute(sender, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }

        this.sendDefaultMessage(sender);
        return true;
    }

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
        if (!PlayerUtils.checkPermission(sender, permission, false, true)) {
            return null;
        }

        if (args.length == 1) {
            return subCommands.stream()
                    .filter(subCommand -> subCommand.hasPermission(sender))
                    .map(LyraSubCommand::getName)
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            if (this.hasSubCommand(args[0])) {
                LyraSubCommand subCommand = this.getSubCommand(args[0]);
                if (subCommand.hasPermission(sender)) {
                    return subCommand.tabComplete(sender, alias, args);
                }
            }
        }

        return null;
    }

    private boolean hasSubCommand(String name) {
        return this.subCommands.stream().anyMatch(it -> it.getName().equalsIgnoreCase(name));
    }

    public abstract void sendDefaultMessage(CommandSender sender);

    public void addSubCommand(LyraSubCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    public void removeSubCommand(String name) {
        this.subCommands.removeIf(it -> it.getName().equalsIgnoreCase(name));
    }

    public LyraSubCommand getSubCommand(String name) {
        return this.subCommands.stream()
                .filter(it -> it.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

}
