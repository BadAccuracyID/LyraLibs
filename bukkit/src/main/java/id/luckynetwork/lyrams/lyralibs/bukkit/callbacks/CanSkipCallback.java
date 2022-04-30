package id.luckynetwork.lyrams.lyralibs.bukkit.callbacks;

import id.luckynetwork.lyrams.lyralibs.bukkit.LyraLibsBukkit;
import lombok.Data;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
public class CanSkipCallback {
    private final CommandSender sender;
    private final boolean canSkip;

    @Nullable
    private final List<String> reason;

    public static CanSkipCallback canSkip(String action, TargetsCallback targetsCallback, CommandSender sender) {
        if (targetsCallback.size() == 1) {
            Player target = targetsCallback.getTargets().stream().findFirst().orElse(null);
            if (target != null && target.equals(sender)) {
                return new CanSkipCallback(sender, true, null);
            }
        }

        if (targetsCallback.size() >= 75) {
            return new CanSkipCallback(sender, false, Collections.singletonList(
                    LyraLibsBukkit.getMessagePrefix() + "§eAre you sure you want to execute §d" + action + " §eon §b" + targetsCallback.size() + " §eplayers?"
            ));
        }

        boolean playerSender = sender instanceof Player;

        World world = null;
        for (Player target : targetsCallback.getTargets()) {
            if (world != null) {
                if (world != target.getWorld()) {
                    return new CanSkipCallback(sender, false, Arrays.asList(
                            LyraLibsBukkit.getMessagePrefix() + "§eAre you sure you want to execute §d" + action + " §eon §b" + targetsCallback.size() + " §eplayers?",
                            LyraLibsBukkit.getMessagePrefix() + "§eSome players are scattered across different worlds."
                    ));
                }

                if (playerSender) {
                    if (((Player) sender).getWorld() == target.getWorld() && ((Player) sender).getLocation().distanceSquared(target.getLocation()) >= 50) {
                        return new CanSkipCallback(sender, false, Arrays.asList(
                                LyraLibsBukkit.getMessagePrefix() + "§eAre you sure you want to execute §d" + action + " §eon §b" + targetsCallback.size() + " §eplayers?",
                                LyraLibsBukkit.getMessagePrefix() + "§eSome players are located really far away from you."
                        ));
                    }
                }

                continue;
            }

            world = target.getWorld();
        }

        return new CanSkipCallback(sender, true, null);
    }

}
