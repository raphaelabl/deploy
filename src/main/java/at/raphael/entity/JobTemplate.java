package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
public class JobTemplate extends PanacheEntity {

    public String name;
    public String runsOn;

    @ElementCollection
    public List<String> variables;

    @Column(columnDefinition = "TEXT")
    public String filePart;

    public void update(JobTemplate updated) {
        this.name = updated.name;
        this.runsOn = updated.runsOn;
        this.filePart = updated.filePart;
        this.variables = updated.variables;
    }

    //region Constructor
    public JobTemplate() {
    }
    //endregion

}
