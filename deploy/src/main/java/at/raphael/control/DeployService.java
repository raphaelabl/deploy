package at.raphael.control;


import at.raphael.entity.DeploymentInfo;
import at.raphael.entity.KafkaPackage;
import at.raphael.entity.UserConnector;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class DeployService {

    @Inject
    Logger LOG;

    @Inject
    GithubService githubService;

    @Inject
    KafkaService kafkaService;

    //region Datahandling
    @Transactional
    public DeploymentInfo persistDeployment(DeploymentInfo deploymentInfo) {
        if(deploymentInfo.id != null&& deploymentInfo.id != 0) {
            return updateDeployment(deploymentInfo);
        }

        UserConnector persisted = new UserConnector();

        if(deploymentInfo.ghUser.id != null && deploymentInfo.ghUser.id != 0){
            deploymentInfo.ghUser = UserConnector.findById(deploymentInfo.ghUser.id);
        }

        if(deploymentInfo.repository.id != null && deploymentInfo.repository.id != 0){
            deploymentInfo.repository = UserConnector.findById(deploymentInfo.repository.id);
        }

        deploymentInfo.state = "Persisted";

        deploymentInfo.persist();

        return deploymentInfo;
    }

    @Transactional
    public DeploymentInfo updateDeployment(DeploymentInfo deploymentInfo) {

        if(deploymentInfo.id == null || deploymentInfo.id == 0){
            persistDeployment(deploymentInfo);
            return deploymentInfo;
        }

        DeploymentInfo persisted = DeploymentInfo.findById(deploymentInfo.id);

        if(persisted != null){
            persisted.update(deploymentInfo);
        }

        return persisted;
    }


    public List<DeploymentInfo> fromUser(String username){

        List<DeploymentInfo> resultList = DeploymentInfo.find("ghUser.username", username).list();

        return resultList;

    }

    //endregion

    //region Workflow Handling
    public void autoImportModules(DeploymentInfo deploymentInfo) {
        // Not Auto import if Deployment info is null
        if(deploymentInfo == null || deploymentInfo.id == 0) return;

        // Not Auto importing if Workflow already is existing!
        if(deploymentInfo.workflow != null && deploymentInfo.workflow.id != 0) return;

        // Is set in Constructor
        //deploymentInfo.ghUser.token = deploymentInfo.ghUser.getToken();

        Log.info("In AutoImportModules"+deploymentInfo.deploymentName);

        KafkaPackage kafkaPackage = new KafkaPackage(deploymentInfo, 1, deploymentInfo.ghUser.id);

        this.kafkaService.processGitRequest(kafkaPackage);
    }

    //endregion

    /**
     * Setting up Deployment Kafka Package and Process
     * @param deploymentInfo
     * @return
     */
    public String deploy(DeploymentInfo deploymentInfo) {
        if(deploymentInfo != null && deploymentInfo.id != 0) {
            KafkaPackage kafkaPackage = new KafkaPackage(deploymentInfo, 2, deploymentInfo.id);
            this.kafkaService.processGitRequest(kafkaPackage);
        }
        return "";
    }



}
