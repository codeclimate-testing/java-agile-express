package com.express.domain;

import com.express.testutils.SetterGetterInvoker;
import static junit.framework.Assert.assertEquals;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.unitils.UnitilsJUnit4;

public class ProjectTest extends UnitilsJUnit4 {
   private static final Log LOG = LogFactory.getLog(ProjectTest.class);
   
   Project project;

   @Before
   public void setUp() {
      project = new Project();
      LOG.info(project);
   }
   
   @Test
   public void shouldSetAndGetProperties() {
       SetterGetterInvoker<Project> setterGetterInvoker = new SetterGetterInvoker<Project>(new Project());
       setterGetterInvoker.invokeSettersAndGetters();
   }

   @Test
   public void testDevelopers() {
      assertNotNull(project.getProjectWorkers());
      assertEquals(0, project.getProjectWorkers().size());
      ProjectWorker projectWorker = new ProjectWorker();
      project.addProjectWorker(projectWorker);
      assertEquals(1, project.getProjectWorkers().size());
      project.removeProjectWorker(projectWorker);
      assertEquals(0, project.getProjectWorkers().size());
   }

   @Test
   public void testAccessRequests() {
      assertNotNull(project.getAccessRequests());
      assertEquals(0, project.getAccessRequests().size());
      AccessRequest request = new AccessRequest();
      project.addAccessRequest(request);
      assertEquals(1, project.getAccessRequests().size());
      assertEquals(project, request.getProject());
      project.removeAccessRequest(request);
      assertEquals(0, project.getAccessRequests().size());
      assertNull(request.getProject());
   }
   
   @Test
   public void testThemeSetList() {
      assertNotNull(project.getThemes());
      assertEquals(0, project.getThemes().size());
      Theme theme = new Theme();
      project.addTheme(theme);
      assertEquals(1, project.getThemes().size());
      assertEquals(project, theme.getProject());
      project.removeTheme(theme);
      assertEquals(0, project.getThemes().size());
      assertNull(theme.getProject());
   }

   @Test
   public void testProjectManagersReturned() {
      assertNotNull(project.getProjectWorkers());
      assertEquals(0, project.getProjectWorkers().size());
      ProjectWorker projectWorker = new ProjectWorker();
      projectWorker.getPermissions().setProjectAdmin(true);
      project.addProjectWorker(projectWorker);
      //Add non-manager
      projectWorker = new ProjectWorker();
      project.addProjectWorker(projectWorker);
      assertEquals(1, project.getProjectManagers().size());
   }

}
