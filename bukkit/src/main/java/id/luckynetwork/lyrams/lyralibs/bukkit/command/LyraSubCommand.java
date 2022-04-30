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

    public abstract void execute(CommandSender sender, String[] args);

    public abstract List<String> tabComplete(CommandSender sender, String alias, String[] args);

    protected boolean hasPermission(CommandSender sender) {
        return sender.hasPermission(this.permission) || this.permission.isEmpty();
    }

}
