package at.raphael.control;

import at.raphael.entity.JobTemplate;
import at.raphael.entity.KafkaPackage;
import at.raphael.entity.KafkaResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class KafkaService {

    @Inject
    Logger log;

    @Inject
    GithubService githubService;
    @Inject
    Logger logger;

    @Incoming("git-service-input")
    public void processGitRequest(KafkaPackage kafkaPackage) {

        if(kafkaPackage.deploymentInfo == null || kafkaPackage.deploymentId == null || kafkaPackage.deploymentId == 0) {
            log.info("HERE IS NULL");
            log.info(kafkaPackage.deploymentInfo);
            return;
        }
        /***
         * TASKS
         * 1- Get all Modules with Routes from Project
         * 2- Push to Github-Repository
         */

        Runnable runnable = null;

        switch(kafkaPackage.task){
            case 1:
                if(kafkaPackage.deploymentInfo.ghUser == null || kafkaPackage.deploymentInfo.repository == null) return;

                runnable = () -> {
                    KafkaResponse response = new KafkaResponse();
                    response.modulePaths = githubService.defineModules(kafkaPackage.deploymentInfo.ghUser, kafkaPackage.deploymentInfo.repository);
                    response.modulePaths.forEach(element -> log.info(element.modulePath));
                    response.task = 1;
                    response.deploymentId = kafkaPackage.deploymentId;
                    sendResponse(response);
                };
                break;
            case 2:
                if(kafkaPackage.deploymentInfo.ghUser == null || kafkaPackage.deploymentInfo.repository == null) {
                    log.info("DeploymentInfo.ghuser or repository is null");
                    return;
                }
                runnable = () -> {
                    List<String> errors = githubService.pushToGithub(kafkaPackage.deploymentInfo);
                    if(errors != null && !errors.isEmpty()) {
                        sendResponse(new KafkaResponse(errors));
                    }
                };
                break;
        }

        if(runnable != null){
            new Thread(runnable).start();
        }
    }

    @Inject
    @Channel("git-service-result")
    Emitter<KafkaResponse> emitter;

    public void sendResponse(KafkaResponse j) {
        emitter.send(j);
    }

}
