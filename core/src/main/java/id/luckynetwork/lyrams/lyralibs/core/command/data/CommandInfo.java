package id.luckynetwork.lyrams.lyralibs.core.command.data;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@RequiredArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class CommandInfo {

    /**
     * The command
     * example: /help
     */
    private final @NotNull String command;
    /**
     * The description of the command
     * example: Shows the help command
     */
    private final @NotNull String description;
    /**
     * The example usage of the command
     * see {@link CommandExample}
     */
    private final @Nullable CommandExample example;

}
