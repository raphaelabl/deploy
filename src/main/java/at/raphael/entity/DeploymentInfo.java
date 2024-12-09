package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class DeploymentInfo extends PanacheEntity {

    public String deploymentName;

    /**
     * Save all Steps which should be done in the deployment
     * User can decide which steps the program should do
     *
     * "file-path-localization",
     * "dockerfile-creation",
     * "workflow-creation",
     * "docker-compose-creation",
     * "github-push",
     * "server-installation"
     */
    @ElementCollection
    public List<String> selectedDeploymentSteps;

    @ManyToOne(cascade = CascadeType.ALL)
    public UserConnector ghUser;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public RepositoryInfo repository;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    public Workflow workflow;

    public String state;

    @ElementCollection
    public List<String> errors;


    //region Constructor
    public DeploymentInfo() {
    }
    //endregion

    public void update(DeploymentInfo newEntity) {
        this.deploymentName = newEntity.deploymentName;
        this.selectedDeploymentSteps = newEntity.selectedDeploymentSteps;
        this.state = newEntity.state;
        this.errors = newEntity.errors;

        if(newEntity.repository != null) {
            if(newEntity.repository.id != null && newEntity.repository.id != 0) {
                this.repository = RepositoryInfo.findById(newEntity.repository.id);
            }else{
                newEntity.repository.persist();
                this.repository = newEntity.repository;
            }
        }

        if(newEntity.workflow != null) {
            if(newEntity.workflow.id != null && newEntity.workflow.id != 0) {
                this.workflow = RepositoryInfo.findById(newEntity.workflow.id);
            }else{
                newEntity.workflow.persist();
                this.workflow = newEntity.workflow;
            }
        }
    }
}
