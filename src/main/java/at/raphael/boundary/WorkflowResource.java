package at.raphael.boundary;

import at.raphael.control.WorkflowService;
import at.raphael.entity.Workflow;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("workflow")
public class WorkflowResource{


    @Inject
    WorkflowService workflowService;

    @POST
    @Path("/addWorkflow")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addWorkflowToDeployment(Workflow workflow, @QueryParam("deploymentId") int deploymentId, @QueryParam("userId") int userId){
        Workflow response = workflowService.persistWorkflowToDeploy(workflow, deploymentId, userId);

        return Response.ok(response).build();
    }


}
