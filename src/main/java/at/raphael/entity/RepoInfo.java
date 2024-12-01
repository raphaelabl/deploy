package at.raphael.entity;

public class RepoInfo {
    public String name;
    public String fullName;
    public String description;
    public String htmlUrl;


    public RepoInfo(String name, String fullName, String description, String htmlUrl) {
        this.name = name;
        this.fullName = fullName;
        this.description = description;
        this.htmlUrl = htmlUrl;
    }
}
