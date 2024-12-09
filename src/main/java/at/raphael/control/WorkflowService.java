package at.raphael.control;

import at.raphael.entity.DeploymentInfo;
import at.raphael.entity.Workflow;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class WorkflowService {

    @Transactional
    public Workflow persistWorkflowToDeploy(Workflow workflow, int deploymentId, int userId) {

        DeploymentInfo deploymentInfo = DeploymentInfo.findById(deploymentId);

        if (deploymentInfo != null && deploymentInfo.ghUser != null) {
            if (deploymentInfo.isPersistent() && deploymentInfo.ghUser.id == userId
            ) {
                workflow = workflow.persistOrUpdate();

                if (workflow.isPersistent()) {
                    deploymentInfo.workflow = workflow;
                }
            }

        }

        return workflow;

    }

}
