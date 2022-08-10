package id.luckynetwork.lyrams.lyralibs.core.command.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class CommandExample {

    private final String example;
    private final Map<String, String> descriptionMap;

    public static CommandExampleInfoBuilder newBuilder() {
        return new CommandExampleInfoBuilder();
    }

    public static class CommandExampleInfoBuilder {
        private String example;
        private final Map<String, String> descriptionMap = new HashMap<>();

        /**
         * It adds an example to the command
         * Example: /message LyraMS hi
         *
         * @param example The example of the command.
         * @return The CommandExampleInfoBuilder object.
         */
        public CommandExampleInfoBuilder example(String example) {
            this.example = example;
            return this;
        }

        /**
         * It adds a description to the command
         * <p>
         * Example:
         * LyraMS = "The player Name"
         * hi = "The message"
         *
         * @param usage   The usage of the command.
         * @param example The example command.
         * @return The CommandExampleInfoBuilder object.
         */
        public CommandExampleInfoBuilder addDescription(String usage, String example) {
            this.descriptionMap.put(usage, example);
            return this;
        }

        /**
         * Builds the CommandExampleInfo object
         *
         * @return A new CommandExampleInfo object.
         */
        public CommandExample build() {
            return new CommandExample(this.example, this.descriptionMap);
        }
    }
}
