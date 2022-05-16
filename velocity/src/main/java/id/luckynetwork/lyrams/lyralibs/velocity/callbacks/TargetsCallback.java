package id.luckynetwork.lyrams.lyralibs.velocity.callbacks;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import id.luckynetwork.lyrams.lyralibs.velocity.LyraLibsVelocity;
import id.luckynetwork.lyrams.lyralibs.velocity.utils.ChatUtils;
import lombok.Data;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Data
public class TargetsCallback {

    private boolean notified = false;
    private Set<Player> targets = new HashSet<>();

    /**
     * It adds a player to the list of targets
     *
     * @param player The player to add to the list of targets.
     */
    public void add(Player player) {
        this.targets.add(player);
    }

    /**
     * It adds all the players in the collection to the targets list
     *
     * @param player The player to add to the list of targets.
     */
    public void addAll(Collection<? extends Player> player) {
        this.targets.addAll(player);
    }

    /**
     * Returns the number of targets in the list.
     *
     * @return The size of the targets arraylist.
     */
    public int size() {
        return this.targets.size();
    }

    /**
     * Returns true if the list of targets is empty.
     *
     * @return The method isEmpty() returns a boolean value.
     */
    public boolean isEmpty() {
        return this.targets.isEmpty();
    }

    /**
     * If the queue is empty and we haven't already notified, then return true.
     *
     * @return The return value is a boolean.
     */
    public boolean notifyIfEmpty() {
        return this.isEmpty() && !this.isNotified();
    }

    /**
     * If the player is not in the list of targets, return true.
     *
     * @param player The player to check if the targets list contains.
     * @return A boolean value.
     */
    public boolean doesNotContain(Player player) {
        return !this.targets.contains(player);
    }

    /**
     * This function returns a stream of all the targets of this event.
     *
     * @return A stream of the targets
     */
    public Stream<Player> stream() {
        return StreamSupport.stream(Spliterators.spliterator(targets, 0), false);
    }

    /**
     * For each player in the targets list, execute the action.
     *
     * @param action The action to be performed for each element
     */
    public void forEach(Consumer<? super Player> action) {
        for (Player target : targets) {
            action.accept(target);
        }
    }

    /**
     * Gets a set of target player(s) from the input arg.
     * <p>
     * <p>
     * Setting arg to null or 'self' can be used to only add the sender to the target set.
     * <p>
     * Setting arg to '*' or '@a' can be used to add all online players to the target set.
     * <p>
     * Setting arg to '*[r=number]' or '@a[r=number]' can be used to add all online players in the range of number to the target set.
     * <p>
     * Setting arg to '*[r=number1,n=number2]' or '@a[r=number1,n=number2]' can be used to add number2 amount of online players in the range of number1 to the target set.
     * <p>
     * Setting arg to '@r' can be used to add one random online player to the target set.
     * <p>
     * Setting arg to '@r[r=number]' can be used to add one random online player in the range of number to the target set.
     * <p>
     * Setting arg to '@r[n=number]' can be used to add number amount of random online player to the target set.
     * <p>
     * Setting arg to '@r[r=number1,n=number2]' can be used to add number2 amount of random online players in the range of number1 to the target set.
     * <p>
     * Setting arg to 'player1,player2,player3,...' can be used to add player1,player2,player3,... to the target set.
     * <p>
     * <p>
     * Is this overengineered? maybe lol
     *
     * @param sender the sender
     * @param arg    the arg
     * @return a set of target(s)
     */
    public static TargetsCallback getTargets(ProxyServer server, CommandSource sender, @Nullable String arg) {
        TargetsCallback callback = new TargetsCallback();
        if (sender instanceof Player) {
            if (arg == null) {
                // self
                callback.add((Player) sender);
                return callback;
            }

            switch (arg.toLowerCase()) {
                case "self": {
                    // self
                    callback.add((Player) sender);
                    return callback;
                }
                case "*":
                case "@a": {
                    // all players
                    callback.addAll(server.getAllPlayers());
                    return callback;
                }
                case "@r": {
                    // random player
                    Player[] players = server.getAllPlayers().toArray(new Player[0]);

                    Random random = new Random();
                    Player randomPlayer = players[random.nextInt(players.length)];

                    callback.add(randomPlayer);
                    return callback;
                }
            }

            if (arg.startsWith("@r[n=") && arg.endsWith("]")) {
                // random players with a set amount
                IsIntegerCallback isIntegerCallback = IsIntegerCallback.asInteger(arg.split("=")[1].split("]")[0]);
                if (isIntegerCallback.isInteger()) {
                    int amount = Integer.parseInt(arg.split("=")[1].split("]")[0]);
                    List<Player> onlinePlayers = new ArrayList<>(server.getAllPlayers());

                    if (amount >= onlinePlayers.size()) {
                        callback.addAll(onlinePlayers);
                    } else {
                        Random random = new Random();
                        while (amount > callback.size()) {
                            Player randomPlayer = onlinePlayers.get(random.nextInt(onlinePlayers.size()));

                            callback.add(randomPlayer);
                            onlinePlayers.remove(randomPlayer);
                        }
                    }
                } else {
                    ChatUtils.sendMessage(sender, LyraLibsVelocity.getMessagePrefix() + "§cInvalid amount value!");
                    callback.setNotified(true);
                }
                return callback;
            }

            if (arg.contains(",")) {
                // selected players
                for (String potTarget : arg.split(",")) {
                    Optional<Player> potTargetPlayer = server.getPlayer(potTarget);
                    if (!potTargetPlayer.isPresent()) {
                        ChatUtils.sendMessage(sender, LyraLibsVelocity.getMessagePrefix() + "§cPlayer §l" + potTarget + " §cnot found!");
                        continue;
                    }

                    callback.add(potTargetPlayer.get());
                }
                return callback;
            }

            // selected player
            Optional<Player> targetPlayer = server.getPlayer(arg);
            if (!targetPlayer.isPresent()) {
                ChatUtils.sendMessage(sender, LyraLibsVelocity.getMessagePrefix() + "§cPlayer not found!");
                callback.setNotified(true);
                return callback;
            }

            callback.add(targetPlayer.get());
            return callback;
        }

        if (arg == null) {
            ChatUtils.sendMessage(sender, LyraLibsVelocity.getMessagePrefix() + "§cPlease specify a target player!");
            callback.setNotified(true);
            return callback;
        }

        switch (arg.toLowerCase()) {
            case "self": {
                ChatUtils.sendMessage(sender, LyraLibsVelocity.getMessagePrefix() + "§cPlease specify a target player!");
                callback.setNotified(true);
                return callback;
            }
            case "*":
            case "@a": {
                // all players
                callback.addAll(server.getAllPlayers());
                return callback;
            }
            case "@r": {
                // random player
                Player[] players = server.getAllPlayers().toArray(new Player[0]);

                Random random = new Random();
                Player randomPlayer = players[random.nextInt(players.length)];

                callback.add(randomPlayer);
                return callback;
            }
        }

        if (arg.startsWith("@r[n=") && arg.endsWith("]")) {
            // random players with a set amount
            IsIntegerCallback isIntegerCallback = IsIntegerCallback.asInteger(arg.split("=")[1].split("]")[0]);
            if (isIntegerCallback.isInteger()) {
                int amount = Integer.parseInt(arg.split("=")[1].split("]")[0]);
                List<Player> onlinePlayers = new ArrayList<>(server.getAllPlayers());

                if (amount >= onlinePlayers.size()) {
                    callback.addAll(onlinePlayers);
                } else {
                    Random random = new Random();
                    while (amount > callback.size()) {
                        Player randomPlayer = onlinePlayers.get(random.nextInt(onlinePlayers.size()));

                        callback.add(randomPlayer);
                        onlinePlayers.remove(randomPlayer);
                    }
                }
            } else {
                ChatUtils.sendMessage(sender, LyraLibsVelocity.getMessagePrefix() + "§cInvalid amount value!");
                callback.setNotified(true);
            }
            return callback;
        }

        if (arg.contains(",")) {
            // selected players
            for (String potTarget : arg.split(",")) {
                Optional<Player> potTargetPlayer = server.getPlayer(potTarget);
                if (!potTargetPlayer.isPresent()) {
                    ChatUtils.sendMessage(sender, LyraLibsVelocity.getMessagePrefix() + "§cPlayer §l" + potTarget + " §cnot found!");
                    continue;
                }

                callback.add(potTargetPlayer.get());
            }
            return callback;
        }

        Optional<Player> targetPlayer = server.getPlayer(arg);
        if (!targetPlayer.isPresent()) {
            ChatUtils.sendMessage(sender, LyraLibsVelocity.getMessagePrefix() + "§cPlayer not found!");
            callback.setNotified(true);
            return callback;
        }

        callback.add(targetPlayer.get());
        return callback;
    }

}
