package nu.steffengrondahl.selfstudy.rest.model;

/**
 * Created by Steffen on 30-10-2016.
 */
public class StatusDTO {

    private Integer id;

    private String name;

    public StatusDTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
