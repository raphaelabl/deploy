package at.raphael.entity;

import java.util.List;
import java.util.Map;

public class KafkaResponse {

    public int task;
    public List<ModulePath> modulePaths;
    public Long deploymentId;
    public List<String> errors;


    // Error Response
    public KafkaResponse(List<String> errors) {
        this.errors = errors;
    }

    public KafkaResponse() {
    }

}
