package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;

import java.util.List;

@Entity
public class JobTemplate extends PanacheEntity {

    public String name;
    public String runsOn;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> identificationFiles;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> workflowVariables;
    @Column(columnDefinition = "TEXT")
    public String workflowFileContent;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> dockerFileVariables;
    @Column(columnDefinition = "TEXT")
    public String dockerFileContent;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> dockerComposeVariables;
    @Column(columnDefinition = "TEXT")
    public String dockerComposeContent;


    public void update(JobTemplate newEntity) {
        this.name = newEntity.name;
        this.runsOn = newEntity.runsOn;

        this.identificationFiles = newEntity.identificationFiles;

        this.workflowVariables = newEntity.workflowVariables;
        this.workflowFileContent = newEntity.workflowFileContent;

       this.dockerFileVariables = newEntity.dockerFileVariables;
       this.dockerFileContent = newEntity.dockerFileContent;

       this.dockerComposeVariables = newEntity.dockerComposeVariables;
       this.dockerComposeContent = newEntity.dockerComposeContent;
    }


    //region Constructor
    public JobTemplate() {
    }
    //endregion

}
