package org.redpill.maven.alfresco;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.manager.ArtifactHandlerManager;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

public class ActivateEnterpriseMojoTest {
  ActivateEnterpriseMojo aem;

  @Test
  public void testCommunityVersion() throws MojoExecutionException, MojoFailureException {
    MavenProject mavenProject = new MavenProject();
    Set<Artifact> deps = new HashSet<Artifact>();
    mavenProject.setDependencyArtifacts(deps);
    aem = new ActivateEnterpriseMojo();
    aem.setProject(mavenProject);
    aem.setAlfrescoGroupId("org.alfresco");
    aem.setAlfrescoVersion("4.2.c");
    aem.execute();

    Set<Artifact> dependencies = mavenProject.getDependencyArtifacts();
    assertEquals(0, dependencies.size());
  }

  @Test
  public void testEnterpriseVersion() throws MojoExecutionException, MojoFailureException {
    final ArtifactHandlerManager artifactHandlerManager;
    final ArtifactHandler handler;
    Mockery m = new Mockery();
    artifactHandlerManager = m.mock(ArtifactHandlerManager.class);
    handler = m.mock(ArtifactHandler.class);

    m.checking(new Expectations() {
      {
        allowing(artifactHandlerManager).getArtifactHandler("jar");
        will(returnValue(handler));
        allowing(handler).getClassifier();
        will(returnValue(""));
      }
    });
    
    Set<Artifact> deps = new HashSet<Artifact>();
    MavenProject mavenProject = new MavenProject();
    mavenProject.setDependencyArtifacts(deps);
    aem = new ActivateEnterpriseMojo();
    aem.setProject(mavenProject);
    aem.setAlfrescoGroupId("org.alfresco");
    aem.setAlfrescoVersion("4.2.2");

    aem.setArtifactHandlerManager(artifactHandlerManager);
    aem.execute();

    Set<Artifact> dependencies = mavenProject.getDependencyArtifacts();
    assertEquals(2, dependencies.size());
  }
  
  @Test
  public void testEnterpriseVersion50() throws MojoExecutionException, MojoFailureException {
    final ArtifactHandlerManager artifactHandlerManager;
    final ArtifactHandler handler;
    Mockery m = new Mockery();
    artifactHandlerManager = m.mock(ArtifactHandlerManager.class);
    handler = m.mock(ArtifactHandler.class);

    m.checking(new Expectations() {
      {
        allowing(artifactHandlerManager).getArtifactHandler("jar");
        will(returnValue(handler));
        allowing(handler).getClassifier();
        will(returnValue(""));
      }
    });
    
    Set<Artifact> deps = new HashSet<Artifact>();
    MavenProject mavenProject = new MavenProject();
    mavenProject.setDependencyArtifacts(deps);
    aem = new ActivateEnterpriseMojo();
    aem.setProject(mavenProject);
    aem.setAlfrescoGroupId("org.alfresco");
    aem.setAlfrescoVersion("5.0");

    aem.setArtifactHandlerManager(artifactHandlerManager);
    aem.execute();

    Set<Artifact> dependencies = mavenProject.getDependencyArtifacts();
    assertEquals(2, dependencies.size());
  }
}
