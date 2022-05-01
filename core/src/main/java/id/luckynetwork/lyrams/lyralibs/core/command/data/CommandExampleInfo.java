package id.luckynetwork.lyrams.lyralibs.core.command.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class CommandExampleInfo {

    private final String example;
    private final Map<String, String> descriptionMap;

    public static CommandExampleInfoBuilder newBuilder() {
        return new CommandExampleInfoBuilder();
    }

    public static class CommandExampleInfoBuilder {
        private String example;
        private final Map<String, String> descriptionMap = new HashMap<>();

        public CommandExampleInfoBuilder example(String example) {
            this.example = example;
            return this;
        }

        public CommandExampleInfoBuilder addDescription(String usage, String example) {
            this.descriptionMap.put(usage, example);
            return this;
        }

        public CommandExampleInfo build() {
            return new CommandExampleInfo(this.example, this.descriptionMap);
        }
    }
}
