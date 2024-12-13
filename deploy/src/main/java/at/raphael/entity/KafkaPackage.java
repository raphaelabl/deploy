package at.raphael.entity;

public class KafkaPackage {

    // External id because from entity is not getting Provided
    public Long deploymentId;
    public DeploymentInfo deploymentInfo;
    public int task;


    public KafkaPackage() {
    }

    public KafkaPackage(DeploymentInfo deploymentInfo, int task, Long deploymentId) {
        this.deploymentId = deploymentId;
        this.deploymentInfo = deploymentInfo;
        this.deploymentInfo.ghUser.token = deploymentInfo.ghUser.token;
        this.task = task;
    }

}
