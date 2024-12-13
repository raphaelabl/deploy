package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.List;

@Entity
public class Job extends PanacheEntity {

    public String name;

    public String pathToRoot;

    public String dockerImageName;

    @ManyToOne
    public JobTemplate refTemplate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "job_workflow_attributes")
    public List<JobAttribute> workflowAttributes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "job_dockerfile_attributes")
    public List<JobAttribute> dockerfileAttributes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(name = "job_dockercompose_attributes")
    public List<JobAttribute> dockerComposeAttributes;



    public void update(Job newEntity){
        this.name = newEntity.name;
        this.refTemplate = JobTemplate.findById(newEntity.refTemplate.id);

        this.workflowAttributes.clear();
        for (JobAttribute attribute : newEntity.workflowAttributes) {
            this.workflowAttributes.add(attribute.persistOrUpdate());
        }

        this.dockerfileAttributes.clear();
        for (JobAttribute attribute : newEntity.dockerfileAttributes) {
            this.dockerfileAttributes.add(attribute.persistOrUpdate());
        }

        this.dockerComposeAttributes.clear();
        for (JobAttribute attribute : newEntity.dockerComposeAttributes) {
            this.dockerComposeAttributes.add(attribute.persistOrUpdate());
        }
    }

    @Transactional
    public Job persistOrUpdate(){

        if(this.id == null || this.id == 0){
            if(!this.refTemplate.isPersistent()){
                this.refTemplate = JobTemplate.findById(this.refTemplate.id);
            }
            this.persist();

            return this;
        }

        Job j = Job.findById(this.id);
        j.name = this.name;
        j.update(this);

        return j;

    }

    //region Constructor
    public Job() {
    }

    public Job(String name, JobTemplate refTemplate, List<JobAttribute> workflowAttributes, List<JobAttribute> dockerfileAttributes, List<JobAttribute> dockerComposeAttributes, String dockerImageName) {
        this.name = name;
        this.refTemplate = refTemplate;
        this.workflowAttributes = workflowAttributes;
        this.dockerfileAttributes = dockerfileAttributes;
        this.dockerComposeAttributes = dockerComposeAttributes;
        this.dockerImageName = dockerImageName;
    }
    //endregion

}
