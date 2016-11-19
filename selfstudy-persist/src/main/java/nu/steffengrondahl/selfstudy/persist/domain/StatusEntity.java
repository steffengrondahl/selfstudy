package nu.steffengrondahl.selfstudy.persist.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen on 28-10-2016.
 */
@Entity
@Table(name="status")
public class StatusEntity implements Comparable<StatusEntity> {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", length = 16)
    private String name;

    // mappedBy="status" is mandatory as we are implementing a bidirectional
    // one to many relation. It's value should match the name for field for this
    // entity (i.e. for StatusEntity) in the owner Entity (i.e. in
    // ProjectEntity).
    // We are not cascading because this class should not be changed run-time.
    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @OrderBy("priority DESC")
    private List<ProjectEntity> projects = new ArrayList<ProjectEntity>();

    public StatusEntity() {

    }

    public StatusEntity(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    public List<ProjectEntity> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectEntity> projects) {
        this.projects = projects;
    }

    public int compareTo(StatusEntity o) {
        if(o.getId() == null) {
            if(this.getId() == null)
                return 0;
            else
                return 1;
        }
        if(this.getId() == null)
            return -1;
        return this.getId() - o.getId();
    }

    @Override
    public int hashCode() {
        if(this.getId() == null)
            return 0;
        return this.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof StatusEntity) {
            StatusEntity status = (StatusEntity) obj;
            if (status.getId() == null) {
                return this.getId() == null;
            }
            return (this.getId().intValue() == status.getId().intValue());
        }
        return false;
    }

}
