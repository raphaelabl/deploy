package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class RepositoryInfo extends PanacheEntity {

    @Column(name = "repoName")
    public String name;
    public String fullName;
    public String description;
    public String htmlUrl;

//    public void update(RepositoryInfo newEntity) {
//        this.name = newEntity.name;
//        this.fullName = newEntity.fullName;
//        this.description = newEntity.description;
//        this.htmlUrl = newEntity.htmlUrl;
//    }
//
//    public RepositoryInfo saveOrUpdate(){
//            if(this.id == null|| this.id == 0){
//                this.persist();
//                return this;
//            }
//
//            RepositoryInfo persited = RepositoryInfo.findById(this.id);
//            persited.update(this);
//            return persited;
//    }


    public RepositoryInfo() {
    }

    public RepositoryInfo(String name, String fullName, String description, String htmlUrl) {
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.htmlUrl = htmlUrl;
    }
}
