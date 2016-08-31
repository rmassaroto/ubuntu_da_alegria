package br.com.horizonnew.ubuntudaalegria.model;

/**
 * Created by renan on 30/08/16.
 */
public class Post {

    public static final int TYPE_TEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_VIDEO = 2;

    private int type;
    private long id;
    private boolean capaign;
    private String title, url, description, text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isCapaign() {
        return capaign;
    }

    public void setCapaign(boolean capaign) {
        this.capaign = capaign;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
