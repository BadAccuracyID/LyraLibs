package id.luckynetwork.lyrams.lyralibs.core.command;

import com.google.common.base.Joiner;
import id.luckynetwork.lyrams.lyralibs.core.command.data.CommandExample;
import id.luckynetwork.lyrams.lyralibs.core.command.data.CommandInfo;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommandHelpGenerator {

    /**
     * It generates a help command for a plugin
     *
     * @param pluginName      The name of your plugin.
     * @param version         The version of the plugin
     * @param authors         A list of authors for the plugin.
     * @param commandInfoList This is a list of CommandHelpInfo objects.
     * @return A list of strings that will be sent to the player.
     */
    public List<String> generateHelpCommand(String pluginName, String version, List<String> authors, List<CommandInfo> commandInfoList) {
        List<String> helpCommand = new ArrayList<>();

        helpCommand.add(" ");
        helpCommand.add("§8§l|- §e" + pluginName + " §d" + version + " §7- §cCommands");
        helpCommand.add("§8§l|- §7§oPlugin by " + Joiner.on(", ").join(authors));
        helpCommand.add(" ");

        buildCommandHelp(commandInfoList, helpCommand);
        return helpCommand;
    }

    /**
     * It generates a help command for a plugin
     *
     * @param pluginName      The name of your plugin.
     * @param version         The version of the plugin
     * @param type            The type of command.
     * @param authors         A list of authors of the plugin.
     * @param commandInfoList This is a list of CommandHelpInfo objects.
     * @return A list of strings.
     */
    public List<String> generateHelpCommand(String pluginName, String version, String type, List<String> authors, List<CommandInfo> commandInfoList) {
        List<String> helpCommand = new ArrayList<>();

        helpCommand.add(" ");
        helpCommand.add("§8§l|- §e" + pluginName + " §d" + version + " §7- §c" + type);
        helpCommand.add("§8§l|- §7§oPlugin by " + Joiner.on(", ").join(authors));
        helpCommand.add(" ");

        buildCommandHelp(commandInfoList, helpCommand);
        return helpCommand;
    }

    private void buildCommandHelp(List<CommandInfo> commandInfoInfo, List<String> helpCommand) {
        commandInfoInfo.forEach(it -> {
            helpCommand.add("§8├─ §7Command: §f" + it.getCommand());
            helpCommand.add("§8├─ §7Description: §e" + it.getDescription());
            CommandExample exampleInfo = it.getExample();
            if (exampleInfo != null) {
                if (exampleInfo.getExample() != null) {
                    helpCommand.add("§8├─ §7Example: §a" + exampleInfo.getExample());
                    exampleInfo.getDescriptionMap().forEach((key, val) -> helpCommand.add("          §8├─ §7" + key + ": §a" + val));
                }
                helpCommand.add(" ");
            }
        });
    }

}
