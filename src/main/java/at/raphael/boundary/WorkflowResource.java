package at.raphael.boundary;

import at.raphael.entity.Job;
import at.raphael.entity.JobTemplate;
import at.raphael.entity.Workflow;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("workflow")
public class WorkflowResource{

    @POST
    @Path("/addWorkflow")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addWorkflow(Workflow workflow){
        for(Job j : workflow.modules){
            JobTemplate refTemplate = JobTemplate.findById(j.refTemplate.id);
            j.refTemplate = refTemplate;
        }
        workflow.persist();
        return Response.ok(workflow).build();
    }


}
