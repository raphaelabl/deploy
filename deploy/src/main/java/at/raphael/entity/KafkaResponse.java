package at.raphael.entity;

import java.util.List;

public class KafkaResponse {

    public int task;
    public List<ModulePath> modulePaths;
    public Long deploymentId;

    public KafkaResponse() {
    }
}
