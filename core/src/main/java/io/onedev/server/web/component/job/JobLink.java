package io.onedev.server.web.component.job;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.eclipse.jgit.lib.FileMode;

import io.onedev.server.ci.CISpec;
import io.onedev.server.ci.job.Job;
import io.onedev.server.git.BlobIdent;
import io.onedev.server.model.Project;
import io.onedev.server.security.SecurityUtils;
import io.onedev.server.web.page.project.blob.ProjectBlobPage;
import io.onedev.server.web.page.project.blob.render.renderers.cispec.CISpecRendererProvider;

@SuppressWarnings("serial")
public class JobLink extends BookmarkablePageLink<Void> {

	private final IModel<Project> projectModel;
	
	private final String revision;
	
	private final String jobName;
	
	public JobLink(String id, IModel<Project> projectModel, String revision, String jobName) {
		super(id, ProjectBlobPage.class);
		this.projectModel = projectModel;
		this.revision = revision;
		this.jobName = jobName;
	}

	@Override
	protected void onConfigure() {
		super.onConfigure();
		setEnabled(SecurityUtils.canReadCode(projectModel.getObject().getFacade()));
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		super.onComponentTag(tag);
		if (!isEnabled())
			tag.setName("span");
	}

	@Override
	protected void onDetach() {
		projectModel.detach();
		super.onDetach();
	}

	@Override
	public PageParameters getPageParameters() {
		ProjectBlobPage.State state = new ProjectBlobPage.State();
		state.blobIdent = new BlobIdent(revision, CISpec.BLOB_PATH, FileMode.REGULAR_FILE.getBits()); 
		state.position = CISpecRendererProvider.getPosition(Job.SELECTION_PREFIX + jobName);
		return ProjectBlobPage.paramsOf(projectModel.getObject(), state);
	}

}
