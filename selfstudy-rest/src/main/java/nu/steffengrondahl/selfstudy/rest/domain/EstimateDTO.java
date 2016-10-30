package nu.steffengrondahl.selfstudy.rest.domain;

/**
 * Created by Steffen on 30-10-2016.
 */
public class EstimateDTO {

    private Integer id;

    private String name;

    public EstimateDTO() {

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
