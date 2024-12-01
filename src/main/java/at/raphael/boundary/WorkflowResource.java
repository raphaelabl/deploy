package at.raphael.boundary;

import at.raphael.entity.Workflow;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("workflow")
public class WorkflowResource{

    @POST
    @Path("addWorkflow")
    public Response addWorkflow(Workflow workflow){
        Workflow responseWorkflow = workflow;

        if(workflow.id == null || workflow.id == 0) {
            workflow.persist();
        }else{
            Workflow persited = Workflow.findById(workflow.id);
            persited.update(workflow);
            responseWorkflow = persited;
        }

        return Response.ok(responseWorkflow).build();
    }


}
