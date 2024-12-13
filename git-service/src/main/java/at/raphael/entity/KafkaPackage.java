package at.raphael.entity;

public class KafkaPackage {

    public Long deploymentId;
    public DeploymentInfo deploymentInfo;
    public int task;


    public KafkaPackage() {
    }

    public KafkaPackage(DeploymentInfo deploymentInfo, int task) {
        this.deploymentInfo = deploymentInfo;
        this.task = task;
    }

}
