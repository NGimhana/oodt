package org.apache.oodt.cas.wmservices.services;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.oodt.cas.wmservices.enums.ErrorType;
import org.apache.oodt.cas.wmservices.exceptions.NotFoundException;
import org.apache.oodt.cas.wmservices.resources.WMRequestStatusResource;
import org.apache.oodt.cas.wmservices.resources.WorkflowInstancePageResource;
import org.apache.oodt.cas.wmservices.resources.WorkflowInstanceResource;
import org.apache.oodt.cas.workflow.exceptions.WorkflowException;
import org.apache.oodt.cas.workflow.structs.WorkflowInstance;
import org.apache.oodt.cas.workflow.structs.WorkflowInstancePage;
import org.apache.oodt.cas.workflow.system.WorkflowManagerClient;
import org.slf4j.LoggerFactory;

/**
 * Service class for Proposing Apache OODT-2.0 WorkflowManager REST-APIs This handles HTTP requests
 * and returns workflow manager entities JAX-RS resources converted to different formats.
 *
 * @author ngimhana (Nadeeshan Gimhana)
 */
public class WMJaxrsServiceV2 {

  private static org.slf4j.Logger logger = LoggerFactory.getLogger(WMJaxrsServiceV2.class);

  // The servlet context, which is used to retrieve context parameters.
  @Context private ServletContext context;

  /**
   * Gets the workflow manager client instance from the servlet context.
   *
   * @return the workflow manager client instance from the servlet context attribute
   * @throws Exception if an object cannot be retrieved from the context attribute
   */
  public WorkflowManagerClient getContextClient() throws WorkflowException {
    /** Get the workflow manager client from the servlet context.* */
    Object clientObject = context.getAttribute("client");
    if (clientObject != null && clientObject instanceof WorkflowManagerClient) {
      return (WorkflowManagerClient) clientObject;
    }

    String message = ErrorType.CAS_PRODUCT_EXCEPTION_WORKFLOWMGR_CLIENT_UNAVILABLE.getErrorType();
    logger.debug("Warning Message: ", message);
    throw new WorkflowException(message);
  }

  /**
   * Gets an HTTP response that represents a {@link WorkflowInstance} from the workflow manager.
   *
   * @param workflowInstId the ID of the workflow Instance
   * @return an HTTP response that represents a {@link WorkflowInstance} from the workflow manager
   */
  @GET
  @Path("workflowInst")
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public WorkflowInstanceResource getWorkflowInstanceById(
      @QueryParam("workflowInstId") String workflowInstId) throws WebApplicationException {
    try {
      WorkflowManagerClient wmclient = getContextClient();
      WorkflowInstance workflowInstanceById = wmclient.getWorkflowInstanceById(workflowInstId);
      WorkflowInstanceResource workflowResource =
          new WorkflowInstanceResource(workflowInstanceById);
      logger.debug("WorkFlowInstance ID : " + workflowInstId);
      return workflowResource;
    } catch (Exception e) {
      throw new NotFoundException(e.getMessage());
    }
  }

  /**
   * Gets an HTTP response that represents a {@link WorkflowInstancePage} from the workflow manager.
   * Gives the First Page of WorkFlow Instances
   *
   * @return an HTTP response that represents a {@link WorkflowInstancePage} from the workflow
   *     manager
   */
  @GET
  @Path("workflows/firstpage")
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public WorkflowInstancePageResource getWorkflowInstancesAtFirstPage()
      throws WebApplicationException {
    try {
      WorkflowManagerClient wmclient = getContextClient();
      WorkflowInstancePage firstPage = wmclient.getFirstPage();
      WorkflowInstancePageResource firstPageResource = new WorkflowInstancePageResource(firstPage);
      return firstPageResource;
    } catch (Exception e) {
      throw new NotFoundException(e.getMessage());
    }
  }

  /**
   * This REST API stops a running {@link WorkflowInstance}.
   *
   * @param workflowInstanceId the ID of the workflow Instance
   * @return {@link Response}
   * @throws Exception if there occurred an error while executing the operation
   */
  @POST
  @Path("stop/workflow")
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response stopWorkflowInstance(@QueryParam("workflowInstanceId") String workflowInstanceId)
      throws WebApplicationException {
    try {
      WorkflowManagerClient wmclient = getContextClient();
      boolean workflowStatus = wmclient.stopWorkflowInstance(workflowInstanceId);
      WMRequestStatusResource status =
          new WMRequestStatusResource(
              wmclient.getWorkflowManagerUrl().toString(),
              "Sucessfully Stopped : "
                  + workflowInstanceId
                  + " "
                  + getWorkflowInstanceById(workflowInstanceId).getWorkflowState().getName());
      return Response.status(Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();
    } catch (Exception e) {
      throw new NotFoundException(e.getMessage());
    }
  }

  /**
   * This REST API pauses a running {@link WorkflowInstance}.
   *
   * @param workflowInstanceId the ID of the workflow Instance
   * @return {@link Response}
   * @throws Exception if there occurred an error while executing the operation
   */
  @POST
  @Path("pause/workflow")
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response pauseWorkflowInstance(@QueryParam("workflowInstanceId") String workflowInstanceId)
      throws WebApplicationException {
    try {
      WorkflowManagerClient wmclient = getContextClient();
      boolean workflowStatus = wmclient.stopWorkflowInstance(workflowInstanceId);
      WMRequestStatusResource status =
          new WMRequestStatusResource(
              wmclient.getWorkflowManagerUrl().toString(),
              "Sucessfully Paused : "
                  + workflowInstanceId
                  + " "
                  + getWorkflowInstanceById(workflowInstanceId).getWorkflowState().getName());
      return Response.status(Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();
    } catch (Exception e) {
      throw new NotFoundException(e.getMessage());
    }
  }

  /**
   * This REST API resumes a paused {@link WorkflowInstance}.
   *
   * @param workflowInstanceId the ID of the workflow Instance
   * @return {@link Response}
   * @throws Exception if there occurred an error while executing the operation
   */
  @POST
  @Path("resume/workflow")
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response resumeWorkflowInstance(
      @QueryParam("workflowInstanceId") String workflowInstanceId) throws WebApplicationException {
    try {
      WorkflowManagerClient wmclient = getContextClient();
      boolean workflowStatus = wmclient.stopWorkflowInstance(workflowInstanceId);

      WMRequestStatusResource status =
          new WMRequestStatusResource(
              wmclient.getWorkflowManagerUrl().toString(),
              "Sucessfully resumed : "
                  + workflowInstanceId
                  + " "
                  + getWorkflowInstanceById(workflowInstanceId).getWorkflowState().getName());
      return Response.status(Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();

    } catch (Exception e) {
      throw new NotFoundException(e.getMessage());
    }
  }

  /**
   * This REST API updates the state of {@link WorkflowInstance}.
   *
   * @param workflowInstanceId the ID of the workflow Instance
   * @param wmInstanceStatus state of the workflowState(etc.FINISHED,Running...)
   * @return {@link Response}
   * @throws Exception if there occurred an error while executing the operation
   */
  @POST
  @Path("updatestatus/workflow")
  @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
  public Response resumeWorkflowInstance(
      @QueryParam("workflowInstanceId") String workflowInstanceId,
      @QueryParam("status") String wmInstanceStatus)
      throws WebApplicationException {
    try {
      WorkflowManagerClient wmclient = getContextClient();
      String previousStatus =
          wmclient.getWorkflowInstanceById(workflowInstanceId).getState().getName();
      boolean workflowStatus =
          wmclient.updateWorkflowInstanceStatus(workflowInstanceId, wmInstanceStatus);

      WMRequestStatusResource status =
          new WMRequestStatusResource(
              wmclient.getWorkflowManagerUrl().toString(),
              "Sucessfully Updated Status of workflow : "
                  + workflowInstanceId
                  + " from "
                  + previousStatus
                  + " to "
                  + getWorkflowInstanceById(workflowInstanceId).getWorkflowState().getName());
      return Response.status(Status.OK).entity(status).type(MediaType.APPLICATION_JSON).build();

    } catch (Exception e) {
      throw new NotFoundException(e.getMessage());
    }
  }
}