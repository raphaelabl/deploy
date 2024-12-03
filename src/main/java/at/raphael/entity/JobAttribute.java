package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class JobAttribute extends PanacheEntity {

    public String name;
    public String value;

//    public void update(JobAttribute newEntity) {
//        this.name = newEntity.name;
//        this.value = newEntity.value;
//    }
//
//    public JobAttribute saveOrUpdate(){
//        if(this.id == null|| this.id == 0){
//            this.persist();
//            return this;
//        }
//
//        JobAttribute persited = JobAttribute.findById(this.id);
//        persited.update(this);
//        return persited;
//    }

    //region Constructor
    public JobAttribute() {
    }

    public JobAttribute(String name, String value) {
        this.name = name;
        this.value = value;
    }
    //endregion

}
