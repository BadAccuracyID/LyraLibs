package id.luckynetwork.lyrams.lyralibs.bukkit.helpers;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import id.luckynetwork.lyrams.lyralibs.bukkit.callbacks.CanSkipCallback;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ConfirmationManager {

    private final Map<Player, Callable> confirmationMap = new HashMap<>();

    /**
     * Requests a confirmation before executing something
     *
     * @param callable the code to execute if confirmed
     * @param sender   the executor
     * @param skip     the required condition to skip the confirmation
     * @param warnings warnings to be sent to the player
     */
    public void requestConfirmation(Callable callable, CommandSender sender, boolean skip, @Nullable List<String> warnings, String confirmCommand) {
        if (skip || sender instanceof ConsoleCommandSender) {
            callable.call();
            return;
        }

        Player player = (Player) sender;
        if (warnings != null && !warnings.isEmpty()) {
            warnings.forEach(player::sendMessage);
        }

        player.sendMessage(LyraLibsBukkit.getMessagePrefix() + "§ePlease type §d/" + confirmCommand + " §eto confirm your action.");
        confirmationMap.put(player, callable);
    }

    /**
     * @see ConfirmationManager#requestConfirmation(Callable, CommandSender, boolean, List, String)
     */
    public void requestConfirmation(Callable callable, CommandSender sender, boolean skip, String confirmCommand) {
        requestConfirmation(callable, sender, skip, null, confirmCommand);
    }

    /**
     * @see ConfirmationManager#requestConfirmation(Callable, CommandSender, boolean, List, String)
     */
    public void requestConfirmation(Callable callable, CommandSender sender, @Nullable List<String> warnings, String confirmCommand) {
        requestConfirmation(callable, sender, false, warnings, confirmCommand);
    }

    /**
     * @see ConfirmationManager#requestConfirmation(Callable, CommandSender, boolean, List, String)
     */
    public void requestConfirmation(Callable callable, CommandSender sender, String confirmCommand) {
        requestConfirmation(callable, sender, false, null, confirmCommand);
    }

    /**
     * @see ConfirmationManager#requestConfirmation(Callable, CommandSender, boolean, List, String)
     */
    public void requestConfirmation(Callable callable, CanSkipCallback skipCallback, String confirmCommand) {
        requestConfirmation(callable, skipCallback.getSender(), skipCallback.isCanSkip(), skipCallback.getReason(), confirmCommand);
    }

    /**
     * Confirms the execution of a pending code
     *
     * @param player the player
     */
    public void confirm(Player player) {
        if (!confirmationMap.containsKey(player)) {
            player.sendMessage(LyraLibsBukkit.getMessagePrefix() + "§cYou don't have any pending action!");
            return;
        }

        player.sendMessage(LyraLibsBukkit.getMessagePrefix() + "§aAction confirmed.");
        confirmationMap.get(player).call();
        confirmationMap.remove(player);
    }


    /**
     * Deletes the queued pending code from {@link ConfirmationManager#confirmationMap}
     *
     * @param player the player who has the code pending
     */
    public void deleteConfirmation(Player player) {
        if (!confirmationMap.containsKey(player)) {
            if (player != null && player.isOnline()) {
                player.sendMessage(LyraLibsBukkit.getMessagePrefix() + "§cYou don't have any pending action!");
            }
            return;
        }

        confirmationMap.remove(player);
    }


    public interface Callable {
        void call();
    }
}
