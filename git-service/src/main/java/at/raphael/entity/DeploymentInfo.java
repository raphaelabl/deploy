package at.raphael.entity;

import java.util.List;

public class DeploymentInfo{

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
    public List<String> selectedDeploymentSteps;

    public UserConnector ghUser;

    public RepositoryInfo repository;

    public Workflow workflow;

    public String state;

    public List<String> errors;


    //region Constructor
    public DeploymentInfo() {
    }
    //endregion

}
