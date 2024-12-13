package at.raphael.entity;

public class ModulePath {

    public Long jobTemplateId;
    public String modulePath;

    public ModulePath(Long jobTemplateId, String modulePath) {
        this.jobTemplateId = jobTemplateId;
        this.modulePath = modulePath;
    }

    public ModulePath() {
    }
}
