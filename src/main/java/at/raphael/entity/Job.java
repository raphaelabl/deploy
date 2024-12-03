package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Job extends PanacheEntity {

    public String name;

    @ManyToOne(cascade = CascadeType.ALL)
    public JobTemplate refTemplate;

    @OneToMany(cascade = CascadeType.ALL)
    public List<JobAttribute> attributeList;


//    public void update(Job newEntity){
//        this.name = newEntity.name;
//        this.refTemplate = refTemplate.saveOrUpdate();
//
//        this.attributeList = new ArrayList<>();
//        for(JobAttribute attr : newEntity.attributeList){
//            this.attributeList.add(attr.saveOrUpdate());
//        }
//
//        this.attributeList = newEntity.attributeList;
//    }
//
//    public Job saveOrUpdate(){
//        if(this.id == null|| this.id == 0){
//            this.id = null;
//
//            for(JobAttribute attr : this.attributeList){
//                attr = attr.saveOrUpdate();
//            }
//
//            this.refTemplate = refTemplate.saveOrUpdate();
//
//            this.persist();
//            return this;
//        }
//
//        Job persited = Job.findById(this.id);
//        persited.update(this);
//        return persited;
//    }

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
