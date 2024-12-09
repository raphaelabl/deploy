package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;

import java.util.List;

@Entity
public class Job extends PanacheEntity {

    public String name;

    @ManyToOne
    public JobTemplate refTemplate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<JobAttribute> workflowAttributeList;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<JobAttribute> dockerfileAttributeList;


    public void update(Job newEntity){
        this.name = newEntity.name;
        this.refTemplate = JobTemplate.findById(newEntity.refTemplate.id);

        this.workflowAttributeList.clear();
        for (JobAttribute attribute : newEntity.workflowAttributeList) {
            this.workflowAttributeList.add(attribute.persistOrUpdate());
        }

        this.dockerfileAttributeList.clear();
        for (JobAttribute attribute : newEntity.dockerfileAttributeList) {
            this.dockerfileAttributeList.add(attribute.persistOrUpdate());
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

    public Job(String name, JobTemplate refTemplate, List<JobAttribute> attributeList) {
        this.name = name;
        this.refTemplate = refTemplate;
        this.workflowAttributeList = attributeList;
        this.dockerfileAttributeList = attributeList;
    }
    //endregion

}
