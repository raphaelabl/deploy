package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Job extends PanacheEntity {

    public String name;

    @ManyToOne
    public JobTemplate refTemplate;

    @OneToMany
    public List<JobAttribute> attributeList;

    //region Constructor
    public Job() {
    }

    public Job(String name, JobTemplate refTemplate, List<JobAttribute> attributeList) {
        this.name = name;
        this.refTemplate = refTemplate;
        this.attributeList = attributeList;
    }

    //endregion

}
