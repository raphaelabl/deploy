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
    public List<String> variables;

    @Column(columnDefinition = "TEXT")
    public String filePart;
//
//    public void update(JobTemplate newEntity) {
//        this.name = newEntity.name;
//        this.runsOn = newEntity.runsOn;
//        this.variables = newEntity.variables;
//        this.filePart = newEntity.filePart;
//    }
//
//    public JobTemplate saveOrUpdate(){
//        if(this.id == null|| this.id == 0){
//            this.persist();
//            return this;
//        }
//
//        JobTemplate persited = JobTemplate.findById(this.id);
//        persited.update(this);
//        return persited;
//    }

    //region Constructor
    public JobTemplate() {
    }
    //endregion

}
