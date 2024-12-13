package at.raphael.entity;

public class RepositoryInfo{

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
