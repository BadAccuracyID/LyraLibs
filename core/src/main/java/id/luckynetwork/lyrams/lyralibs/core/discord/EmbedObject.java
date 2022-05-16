package id.luckynetwork.lyrams.lyralibs.core.discord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * It's a class that represents an embed object
 */
@Getter
@Setter
public class EmbedObject {

    private String title;
    private String description;
    private String url;
    private Color color;

    private Footer footer;
    private Thumbnail thumbnail;
    private Image image;
    private Author author;
    private final List<Field> fields = new ArrayList<>();

    public EmbedObject setFooter(String text, String icon) {
        this.footer = new Footer(text, icon);
        return this;
    }

    public EmbedObject setAuthor(String name, String url, String icon) {
        this.author = new Author(name, url, icon);
        return this;
    }

    @AllArgsConstructor
    @Getter
    protected static class Footer {
        private String text;
        private String iconUrl;
    }

    @AllArgsConstructor
    @Getter
    protected static class Thumbnail {
        private String url;
    }

    @AllArgsConstructor
    @Getter
    protected static class Image {
        private String url;
    }

    @AllArgsConstructor
    @Getter
    protected static class Author {
        private String name;
        private String url;
        private String iconUrl;
    }

    @AllArgsConstructor
    @Getter
    protected static class Field {
        private String name;
        private String value;
        private boolean inline;
    }
}
