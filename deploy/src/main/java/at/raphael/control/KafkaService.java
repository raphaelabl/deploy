package at.raphael.control;

import at.raphael.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import java.util.ArrayList;

@ApplicationScoped
public class KafkaService {

    @Inject
    Logger logger;

    @Inject
    DeployService deployService;

    @Inject
    WorkflowService workflowService;


    @Inject
    @Channel("git-service-input")
    Emitter<KafkaPackage> emitter;

    public void processGitRequest(KafkaPackage kafkaPackage) {
        if(kafkaPackage.deploymentInfo == null || kafkaPackage.deploymentId == 0) {
            logger.info("Is Null in Emitter");
            return;
        }
        emitter.send(kafkaPackage);
    }

    @Incoming("git-service-result")
    @Transactional
    public void processGitResponse(KafkaResponse j) {
        logger.info("Received KafkaResponse: deploymentId={}, modulePaths={}, errors={}"+
                j.deploymentId+
                j.modulePaths);

        if (j.modulePaths == null || j.modulePaths.isEmpty()) {
            logger.warn("modulePaths is null or empty!");
        }

        if(j.deploymentId == null || j.deploymentId == 0) {
            logger.info("Is Null in Emitter");
            return;
        }
        DeploymentInfo d = DeploymentInfo.findById(j.deploymentId);

        if (d != null && d.isPersistent() && j.modulePaths != null && !j.modulePaths.isEmpty()) {
            switch (j.task) {
                case 1:
                    Workflow w = new Workflow();
                    w.name = "Auto-Gen";
                    j.modulePaths.forEach(element -> {
                        if (w.modules == null) {
                            w.modules = new ArrayList<>();
                        }
                        Job job = new Job();
                        JobTemplate jobTemplate = JobTemplate.findById(element.jobTemplateId);
                        if(jobTemplate == null || jobTemplate.id == 0)return;
                        job.name = jobTemplate.name;
                        job.projectDirName = element.modulePath.split("/")[element.modulePath.split("/").length - 1];
                        job.refTemplate = jobTemplate;
                        job.pathToRoot = element.modulePath;
                        job.dockerImageName = d.repository.fullName + "-" + job.name;

                        if (jobTemplate.workflowVariables != null && !jobTemplate.workflowVariables.isEmpty()) {
                            job.workflowAttributes = jobTemplate.workflowVariables
                                    .stream()
                                    .map(wv -> new JobAttribute(wv, ""))
                                    .toList();

                            job.workflowAttributes
                                    .stream()
                                    .filter(attribute -> "src".equals(attribute.name)) // Prüfen, ob der Name "src" ist
                                    .findAny()
                                    .ifPresent(attribute -> attribute.value = job.pathToRoot); // Attribut ändern

                            job.workflowAttributes
                                    .stream()
                                    .filter(attribute -> "docker-image".equals(attribute.name)) // Prüfen, ob der Name "src" ist
                                    .findAny()
                                    .ifPresent(attribute -> attribute.value = job.dockerImageName.toLowerCase()); // Attribut ändern

                        }

                        if (jobTemplate.dockerFileVariables != null && !jobTemplate.dockerFileVariables.isEmpty()) {
                            job.dockerfileAttributes = jobTemplate.dockerFileVariables
                                    .stream()
                                    .map(wv -> new JobAttribute(wv, ""))
                                    .toList();

                            job.dockerfileAttributes
                                    .stream()
                                    .filter(attribute -> "src".equals(attribute.name)) // Prüfen, ob der Name "src" ist
                                    .findAny()
                                    .ifPresent(attribute -> attribute.value = job.pathToRoot); // Attribut ändern

                            job.dockerfileAttributes
                                    .stream()
                                    .filter(attribute -> "workdir-name".equals(attribute.name)) // Prüfen, ob der Name "src" ist
                                    .findAny()
                                    .ifPresent(attribute -> attribute.value = job.projectDirName); // Attribut ändern

                        }


                        if (jobTemplate.dockerComposeContent != null && !jobTemplate.dockerComposeContent.isEmpty()) {
                            job.dockerComposeAttributes = jobTemplate.dockerComposeVariables
                                    .stream()
                                    .map(wv -> new JobAttribute(wv, ""))
                                    .toList();

                            job.dockerComposeAttributes
                                    .stream()
                                    .filter(attribute -> "docker-image".equals(attribute.name)) // Prüfen, ob der Name "src" ist
                                    .findAny()
                                    .ifPresent(attribute -> attribute.value = job.dockerImageName.toLowerCase()); // Attribut ändern
                        }

                        w.modules.add(job);

                    });
                    workflowService.persistWorkflowToDeploy(w, d.id.intValue(), d.ghUser.id.intValue());
                    break;
                case 2:
                    break;
            }
        }
    }

}
