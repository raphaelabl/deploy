package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.transaction.Transactional;

import java.util.List;

@Entity
public class Workflow extends PanacheEntity {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Job> modules;

    public String name;


    public void update(Workflow newEntity){
        this.name = newEntity.name;

        this.modules.clear();
        for(Job job : newEntity.modules){
            job = job.persistOrUpdate();
            if(!job.isPersistent()){
                job = Job.findById(job.id);
            }
            modules.add(job);
        }
    }

    @Transactional
    public Workflow persistOrUpdate(){
        if(this.id == null || this.id == 0){

            for(Job j : this.modules){
                JobTemplate refTemplate = JobTemplate.findById(j.refTemplate.id);
                j.refTemplate = refTemplate;
            }

            this.persist();
            return this;
        }

        Workflow persisted = Workflow.findById(this.id);
        for(Job j : persisted.modules){
            JobTemplate refTemplate = JobTemplate.findById(j.refTemplate.id);
            j.refTemplate = refTemplate;
        }
        persisted.update(this);
        return persisted;
    }


    //region Constructor
    public Workflow() {
    }
    //endregion


}
