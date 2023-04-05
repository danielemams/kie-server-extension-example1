package org.redhat.rhpam.tests.extension;

import org.jbpm.runtime.manager.impl.identity.UserDataServiceProvider;
import org.kie.api.task.UserGroupCallback;
import org.kie.internal.identity.IdentityProvider;
import org.kie.server.api.KieServerConstants;
import org.kie.server.api.model.definition.ProcessDefinitionList;
import org.kie.server.services.api.KieServerRegistry;
import org.kie.server.services.jbpm.RuntimeDataServiceBase;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.jbpm.kie.services.impl.CommonUtils.getCallbackUserRoles;

@Path("/server/containers/myRestApi")
public class CustomResource {

	private RuntimeDataServiceBase runtimeDataService;
	private KieServerRegistry context;
	private IdentityProvider identityProvider;
	private boolean bypassAuthUser;

	public CustomResource(RuntimeDataServiceBase runtimeDataService, KieServerRegistry context) {
		this.runtimeDataService = runtimeDataService;
		this.context = context;
		this.identityProvider = this.context.getIdentityProvider();
		this.bypassAuthUser = Boolean.parseBoolean(this.context.getConfig().getConfigItemValue(
				KieServerConstants.CFG_BYPASS_AUTH_USER, "false"));
	}

	@GET
	@Path("/ping")
	public Response ping() {
		return Response.ok("pong").build();
	}

	@GET
	@Path("/restapi/{processId}")
	public Response restapiProcessId(@PathParam("processId") String processId) {
		final ProcessDefinitionList processesById = runtimeDataService.getProcessesById(processId);
		return Response.ok(processesById).build();
	}

	@GET
	@Path("/restapi2/{userId}")
	public Response restapiUserId(@PathParam("userId") String userId) {
		final UserGroupCallback userGroupCallback = UserDataServiceProvider.getUserGroupCallback();
		System.out.println(userGroupCallback);
		userId = getUser(userId);
		System.out.println(userId);
		final List<String> callbackUserRoles = getCallbackUserRoles(userGroupCallback, userId);
		System.out.println(callbackUserRoles);
		return Response.ok("allFine").build();
	}

	protected String getUser(String queryParamUser) {
		if (bypassAuthUser) {
			return queryParamUser;
		}
		return identityProvider.getName();
	}
}
