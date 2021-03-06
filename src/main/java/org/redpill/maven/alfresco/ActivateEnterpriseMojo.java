package org.redpill.maven.alfresco;

import java.util.HashSet;
import java.util.Set;

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
    try {
      executeInternal();
    } catch (Exception ex) {
      ex.printStackTrace();
      
      throw new MojoFailureException(ex.getMessage(), ex); 
    }
  }

  private void executeInternal() {    
    char incrementalVersion = _alfrescoVersion.charAt(_alfrescoVersion.length()-1);
    
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
      
      Set<Artifact> dependencyArtifacts = project.getDependencyArtifacts();
      Set<Artifact> deps = new HashSet<Artifact>();
      deps.addAll(dependencyArtifacts);
      deps.add(artifact1);
      deps.add(artifact2);
      project.setDependencyArtifacts(deps);
    } else {
      if (getLog().isInfoEnabled()) {
        getLog().info("Build is a community build, doing nothing...");
      }
    }
  }
  
  public void setAlfrescoGroupId(String alfrescoGroupId) {
    this._alfrescoGroupId = alfrescoGroupId;
  }
  
  public String getAlfrescoGroupId() {
    return _alfrescoGroupId;
  }
  
  public void setAlfrescoVersion(String alfrescoVersion) {
    this._alfrescoVersion = alfrescoVersion;
  }
  
  public void setProject(MavenProject project) {
    this.project = project;
  }
  
  public void setArtifactHandlerManager(ArtifactHandlerManager artifactHandlerManager) {
    this.artifactHandlerManager = artifactHandlerManager;
  }

}
