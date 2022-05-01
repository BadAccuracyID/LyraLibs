package id.luckynetwork.lyrams.lyralibs.core.command.data;

import id.luckynetwork.lyrams.lyralibs.core.command.data.CommandExampleInfo;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Data
@RequiredArgsConstructor
@Builder(builderMethodName = "newBuilder")
public class CommandHelpInfo {

    private final @NotNull String command, description;
    private final @Nullable CommandExampleInfo info;

}
