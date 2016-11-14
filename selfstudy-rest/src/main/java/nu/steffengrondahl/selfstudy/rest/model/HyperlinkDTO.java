package nu.steffengrondahl.selfstudy.rest.model;

/**
 * Created by Steffen on 14-11-2016.
 */
public class HyperlinkDTO {

    private Integer id;

    private String url;

    public HyperlinkDTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
