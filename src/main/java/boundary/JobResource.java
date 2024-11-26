package boundary;

import at.raphael.entity.JobTemplate;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("job")
public class JobResource {

    @Path("getTemplates")
    @GET
    public Response getAllJobs() {
        List<JobTemplate> allJobs = JobTemplate.listAll();
        return Response.ok(allJobs).build();
    }

    @Path("addTemplate")
    @POST
    @Transactional
    public Response addJobTemplate(JobTemplate jobTemplate) {
        if(!jobTemplate.isPersistent()){
            jobTemplate.persist();
        }

        return Response.ok(jobTemplate).build();
    }


}
