package id.luckynetwork.lyrams.lyralibs.bukkit.command;

import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public abstract class LyraSubCommand {

    private final String name, permission;
    private final List<String> aliases;

    public LyraSubCommand(String name, String permission, List<String> aliases) {
        this.name = name.toLowerCase();
        this.permission = permission;
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public LyraSubCommand(String name, String permission) {
        this.name = name.toLowerCase();
        this.permission = permission;
        this.aliases = new ArrayList<>();
    }

    public LyraSubCommand(String name, List<String> aliases) {
        this.name = name.toLowerCase();
        this.permission = "";
        this.aliases = aliases.stream().map(String::toLowerCase).collect(Collectors.toList());
    }

    public LyraSubCommand(String name) {
        this.name = name.toLowerCase();
        this.permission = "";
        this.aliases = new ArrayList<>();
    }

    /**
     * "This function is called when the command is executed."
     *
     * The first parameter is the CommandSender. This is the person who executed the command. It can be a player, the
     * console, or a command block
     *
     * @param sender The CommandSender who executed the command.
     * @param args The arguments passed to the command.
     */
    public abstract void execute(CommandSender sender, String[] args);

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

    /**
     * If the sender has the permission, or the permission is empty, return true
     *
     * @param sender The CommandSender that is executing the command.
     * @return A boolean value.
     */
    public boolean hasPermission(CommandSender sender) {
        return this.permission.isEmpty() || sender.hasPermission(this.permission);
    }

}
