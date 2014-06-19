package org.redpill.maven.alfresco;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "activate", defaultPhase = LifecyclePhase.VALIDATE, threadSafe = true)
public class ActivateEnterpriseMojo extends AbstractMojo {

  @Component
  private MavenProject project;

  @Parameter(defaultValue = "${alfresco.version}")
  private String _alfrescoVersion;

  @Parameter(defaultValue = "${alfresco.groupId}")
  private String _alfrescoGroupId;

  @Component
  private ArtifactHandlerManager artifactHandlerManager;

  public void execute() throws MojoExecutionException, MojoFailureException {
    char incrementalVersion = _alfrescoVersion.charAt(4);
    
    if (getLog().isInfoEnabled()) {
      getLog().info("Incremental version is: " + incrementalVersion);
    }

    boolean enterprise = Character.isDigit(incrementalVersion);

    if (enterprise) {
      ArtifactHandler handler = artifactHandlerManager.getArtifactHandler("jar");

      Artifact artifact1 = new DefaultArtifact(_alfrescoGroupId, "alfresco-enterprise-repository", _alfrescoVersion, "provided", "jar", null, handler);
      Artifact artifact2 = new DefaultArtifact(_alfrescoGroupId, "alfresco-enterprise-repository", _alfrescoVersion, "provided", "jar", "config", handler);

      if (getLog().isInfoEnabled()) {
        getLog().info("Build is an enterprise build, adding two artifacts to the dependencies...");
        getLog().info(artifact1.toString());
        getLog().info(artifact2.toString());
      }

      project.getDependencyArtifacts().add(artifact1);
      project.getDependencyArtifacts().add(artifact2);
    } else {
      if (getLog().isInfoEnabled()) {
        getLog().info("Build is a community build, doing nothing...");
      }
    }
  }

}
