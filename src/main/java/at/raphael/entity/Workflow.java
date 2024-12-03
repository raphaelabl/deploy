package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Workflow extends PanacheEntity {

    @ManyToOne(cascade = CascadeType.ALL)
    public UserConnector ghUser;

    @OneToOne(cascade = CascadeType.ALL)
    public RepositoryInfo ghRepository;

    @OneToMany(cascade = CascadeType.ALL)
    public List<Job> modules;

    public String name;

//    public void update(Workflow newEntity){
//        this.name = newEntity.name;
//        this.ghUser = newEntity.ghUser;
//
//        this.ghRepository = newEntity.ghRepository.saveOrUpdate();
//        this.modules = newEntity.modules;
//    }
//
//    public Workflow saveOrUpdate(){
//        if(this.id == null|| this.id == 0){
//
//            for(Job j : modules){
//                j = j.saveOrUpdate();
//            }
//
//            ghRepository = ghRepository.saveOrUpdate();
//
//
//            this.persist();
//            return this;
//        }
//
//        Workflow persited = Workflow.findById(this.id);
//        persited.update(this);
//        return persited;
//    }

    //region Constructor
    public Workflow() {
    }
    //endregion


}
