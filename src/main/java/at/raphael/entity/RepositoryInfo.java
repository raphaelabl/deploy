package at.raphael.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

@Entity
public class RepositoryInfo extends PanacheEntity {
    public String name;
    public String fullName;
    public String description;
    public String htmlUrl;


    public RepositoryInfo() {
    }

    public RepositoryInfo(String name, String fullName, String description, String htmlUrl) {
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.htmlUrl = htmlUrl;
    }
}
