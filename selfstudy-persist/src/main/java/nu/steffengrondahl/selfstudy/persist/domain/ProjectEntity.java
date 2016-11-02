package nu.steffengrondahl.selfstudy.persist.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen on 28-10-2016.
 */
@Entity
@Table(name="project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "description", length = 32, nullable = false)
    private String description;

    @Lob
    @Column(name = "goals")
    private String goals;

    @Lob
    @Column(name = "actions")
    private String actions;

    @Column(name = "start_date")
    private LocalDate start;

    private LocalDate deadline;

    // name="estimate_fk" is the name for the column in project table
    // holding the foreign key to (column id) in estimate project
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "estimate_fk", nullable = false)
    private EstimateEntity estimate;

    // name="priority_fk" is the name for the column in project table
    // holding the foreign key to (column id) in table priority
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "priority_fk", nullable = false)
    private PriorityEntity priority;

    // name="status_fk" is the name for the column in project table
    // holding the foreign key to (column id) in table status
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "status_fk", nullable = false)
    private StatusEntity status;

    @JoinTable(name = "dependency", joinColumns = {
            @JoinColumn(name = "subsequent", referencedColumnName = "id", nullable = false) }, inverseJoinColumns = {
            @JoinColumn(name = "presupposed", referencedColumnName = "id", nullable = false) })
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @OrderBy("status ASC")
    private List<ProjectEntity> presupposed = new ArrayList<ProjectEntity>();

    @ManyToMany(mappedBy = "presupposed", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<ProjectEntity> subsequent = new ArrayList<ProjectEntity>();

    @Transient
    private List<ProjectEntity> linkable = new ArrayList<ProjectEntity>();

    public ProjectEntity() {

    }

    public ProjectEntity(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public EstimateEntity getEstimate() {
        return estimate;
    }

    public void setEstimate(EstimateEntity estimate) {
        this.estimate = estimate;
    }

    public PriorityEntity getPriority() {
        return priority;
    }

    public void setPriority(PriorityEntity priority) {
        this.priority = priority;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public List<ProjectEntity> getPresupposed() {
        return presupposed;
    }

    public void setPresupposed(List<ProjectEntity> presupposed) {
        this.presupposed = presupposed;
    }

    public List<ProjectEntity> getSubsequent() {
        return subsequent;
    }

    public void setSubsequent(List<ProjectEntity> subsequent) {
        this.subsequent = subsequent;
    }

    public List<ProjectEntity> getLinkable() {
        return linkable;
    }

    public void setLinkable(List<ProjectEntity> linkable) {
        this.linkable = linkable;
    }


}
