package id.luckynetwork.lyrams.lyralibs.core.discord;

import lombok.experimental.UtilityClass;

import java.awt.*;
import java.util.List;

@UtilityClass
public class DiscordHandler {

    public void sendWebhook(String url, String title, List<String> description, String color) {
        DiscordWebhook hook = new DiscordWebhook(url);
        hook.setTts(false);

        EmbedObject embedBuilder = new EmbedObject();
        embedBuilder.setTitle(title);
        embedBuilder.setDescription(DiscordHandler.buildDescription(description));
        embedBuilder.setColor(DiscordHandler.getColorFromString(color));

        hook.addEmbed(embedBuilder);

        try {
            hook.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Color getColorFromString(String color) {
        return ColorFactory.valueOf(color);
    }

    private String buildDescription(List<String> from) {
        String back = from.toString();
        return back.substring(1, back.length() - 1).replace(", ", "<line>");
    }
}
