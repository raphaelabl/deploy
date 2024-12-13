package at.raphael.boundary;

import at.raphael.entity.JobTemplate;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("job")
public class JobResource {

    @GET
    @Path("getAllTemplates")
    public Response getAllJobs() {
        List<JobTemplate> allJobs = JobTemplate.listAll();
        return Response.ok(allJobs).build();
    }

    @POST
    @Path("addTemplate")
    @Transactional
    public Response addJobTemplate(JobTemplate jobTemplate) {

        if(jobTemplate.id == null || jobTemplate.id == 0) {
            jobTemplate.persist();
        }else{
            JobTemplate persited = JobTemplate.findById(jobTemplate.id);
            persited.update(jobTemplate);


            return Response.ok(persited).build();
        }

        return Response.ok(jobTemplate).build();
    }


}
