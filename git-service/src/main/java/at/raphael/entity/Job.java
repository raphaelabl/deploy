package at.raphael.entity;

import java.util.List;

public class Job {

    public String name;

    public String pathToRoot;

    public String dockerImageName;

    public JobTemplate refTemplate;

    public List<JobAttribute> workflowAttributes;

    public List<JobAttribute> dockerfileAttributes;

    public List<JobAttribute> dockerComposeAttributes;


    //region Constructor
    public Job() {
    }

    public Job(String name, JobTemplate refTemplate,  List<JobAttribute> workflowAttributes, List<JobAttribute> dockerfileAttributes, List<JobAttribute> dockerComposeAttributes, String dockerImageName) {
        this.name = name;
        this.refTemplate = refTemplate;
        this.workflowAttributes = workflowAttributes;
        this.dockerfileAttributes = dockerfileAttributes;
        this.dockerComposeAttributes = dockerComposeAttributes;
        this.dockerImageName = dockerImageName;
    }
    //endregion

}
