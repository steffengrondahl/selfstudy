package nu.steffengrondahl.selfstudy.persist;

import nu.steffengrondahl.selfstudy.persist.domain.EstimateEntity;
import nu.steffengrondahl.selfstudy.persist.domain.HyperlinkEntity;
import nu.steffengrondahl.selfstudy.persist.domain.PriorityEntity;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;
import nu.steffengrondahl.selfstudy.persist.domain.StatusEntity;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

/**
 * Created by Steffen on 29-10-2016.
 */
public class ProjectTest {

    private static ProjectEntityDAO dao = new ProjectEntityDAO();

    @Ignore
    @Test
    public void testCreateProjects() {

        System.out.println("Creating projects");

        EstimateEntity estimateHours = new EstimateEntity();
        estimateHours.setId(1);

        EstimateEntity estimateDays = new EstimateEntity();
        estimateDays.setId(2);

        PriorityEntity priorityVeryHigh = new PriorityEntity();
        priorityVeryHigh.setId(5);

        PriorityEntity priorityHigh = new PriorityEntity();
        priorityHigh.setId(4);

        StatusEntity statusOpen = new StatusEntity();
        statusOpen.setId(1);

        ProjectEntity presupposed = new ProjectEntity("PRESUPPOSED PROJECT");
        presupposed.setEstimate(estimateHours);
        presupposed.setPriority(priorityVeryHigh);
        presupposed.setStatus(statusOpen);

        ProjectEntity subsequent = new ProjectEntity("SUBSEQUENT PROJECT");
        subsequent.setEstimate(estimateDays);
        subsequent.setPriority(priorityHigh);
        subsequent.setStatus(statusOpen);

        dao.add(presupposed);
        dao.add(subsequent);

        // links projects and save one of them
        //presupposed.getSubsequent().add(subsequent);
        subsequent.getPresupposed().add(presupposed);
        // the project is now detached, so it has to be updated (i.e. merge to
        // persistence)
        // the subsequent project will be updated as the many-to-many relation
        // has cascaded set for update
        //dao.update(presupposed);

        // With unidirectional many-to-many the subsequent project is the one to persist (and merge)
        dao.update(subsequent);

        // link all projects and their dependencies (i.e. presupposed projects)
        System.out.println("List all projects and their pressuposed projects");
        List<ProjectEntity> projectList = dao.query(QuerySpecificationFactory.queryAll());
        for (ProjectEntity pe : projectList) {
            System.out.printf("Project: %s. %s (%s, %s, %s)%n", pe.getId(), pe.getDescription(),
                    pe.getPriority().getName(), pe.getStatus().getName(), pe.getEstimate().getName());
            // We have explicit to fetch the dependent projects as these are
            // fetched lazy
            ProjectEntity project = dao.find(pe.getId(), true);
            for (ProjectEntity presup : project.getPresupposed()) {
                System.out.printf("* Presupposed: %s. %s%n", presup.getId(), presup.getDescription());
            }
        }

        // Show link able projects for presupposed project
        ProjectEntity project = dao.find(presupposed.getId(), true);
        Assert.assertNotNull(project);
        System.out.printf("Possible links for presupposed project, %s. %s%n", project.getId(), project.getDescription());
        for (ProjectEntity linkable : project.getLinkable()) {
            System.out.printf("- Linkable: %s. %s%n", linkable.getId(), linkable.getDescription());
        }

        // Finally remove projects
        dao.delete(presupposed);
        dao.delete(subsequent);


    }

    @Ignore
    @Test
    public void testProject() {
        List<ProjectEntity> projectList;
        projectList = dao.query(QuerySpecificationFactory.queryAll());
        System.out.println("All projects ");
        for (ProjectEntity pe : projectList) {
            System.out.printf("Project: %s. %s%n", pe.getId(), pe.getDescription());
        }

        projectList = dao.query(QuerySpecificationFactory.queryByPriorityRange(3, 5));
        System.out.println("Projects restricted by priority");
        for (ProjectEntity pe : projectList) {
            System.out.printf("Project: %s. %s (%s)%n", pe.getId(), pe.getDescription(), pe.getPriority().getName());
        }

        projectList = dao.query(QuerySpecificationFactory.queryByPriorityAndStatusRange(3, 5, 1, 2));
        System.out.println("Projects restricted by priority and status");
        for (ProjectEntity pe : projectList) {
            System.out.printf("Project: %s. %s (%s, %s)%n", pe.getId(), pe.getDescription(),
                    pe.getPriority().getName(), pe.getStatus().getName());
        }

    }

    @Ignore
    @Test
    public void testPresupposedSorting() {
        System.out.println("Creating lots of projects");

        EstimateEntityDAO estimateDao = new EstimateEntityDAO();
        EstimateEntity estimate = estimateDao.find(3, false);
        PriorityEntityDAO priorityDao = new PriorityEntityDAO();
        PriorityEntity priority = priorityDao.find(3, false);
        StatusEntityDAO statusDao = new StatusEntityDAO();
        //StatusEntity status = statusDao.find(1, false);

        ProjectEntity subsequent = new ProjectEntity("SUBSEQUENT PROJECT");
        subsequent.setEstimate(estimate);
        subsequent.setPriority(priority);
        subsequent.setStatus(statusDao.find(1, false));
        dao.add(subsequent);

        ProjectEntity[] presupposed = new ProjectEntity[5];
        int[] statusList = { 2, 3, 2, 1, 3};
        for(int i=0; i<5; i++) {
            int number = new Integer(i+1);
            presupposed[i] = new ProjectEntity("PRESUPPOSED PROJECT " + number);
            presupposed[i].setEstimate(estimate);
            presupposed[i].setPriority(priority);
            presupposed[i].setStatus(statusDao.find(statusList[i], false));
            dao.add(presupposed[i]);
        }

        for(int i=0; i<5; i++) {
            // links projects and save one of them
            //presupposed[i].getSubsequent().add(subsequent);
            subsequent.getPresupposed().add(presupposed[i]);
        }
        // the project is now detached, so it has to be updated (i.e. merge to
        // persistence)
        // the subsequent project will be updated as the many-to-many relation
        // has cascaded set for update
        dao.update(subsequent);

        ProjectEntity project = dao.find(subsequent.getId(), true);
        for (ProjectEntity presup : project.getPresupposed()) {
            System.out.printf("Presupposed: %s. %s (%s)%n", presup.getId(), presup.getDescription(), presup.getStatus().getId());
        }

        // Finally clean up
        dao.delete(subsequent);
        for(int i=0; i<5; i++) {
            dao.delete(presupposed[i]);
        }

    }

    @Test
    public void testHyperlink() {
        System.out.println("Creating project and hyperlinks");

        EstimateEntity estimateHours = new EstimateEntity();
        estimateHours.setId(1);

        EstimateEntity estimateDays = new EstimateEntity();
        estimateDays.setId(2);

        PriorityEntity priorityVeryHigh = new PriorityEntity();
        priorityVeryHigh.setId(5);

        PriorityEntity priorityHigh = new PriorityEntity();
        priorityHigh.setId(4);

        StatusEntity statusOpen = new StatusEntity();
        statusOpen.setId(1);

        ProjectEntity project = new ProjectEntity("PROJECT WITH LINKS");
        project.setEstimate(estimateHours);
        project.setPriority(priorityVeryHigh);
        project.setStatus(statusOpen);

        Integer projectId = dao.add(project);

        HyperlinkEntity hyperlink = new HyperlinkEntity();
        hyperlink.setUrl("http://steffengrondahl.nu/index.html");
        hyperlink.setProject(project);

        HyperlinkEntityDAO hyperlinkEntityDAO = new HyperlinkEntityDAO();
        // Using update and not persist as the project is detached!
        hyperlinkEntityDAO.update(hyperlink);

        ProjectEntity receivedProject = dao.find(projectId, true);
        for(HyperlinkEntity h : receivedProject.getHyperlinks()) {
            System.out.printf("Hyperlink for project %s: %s%n ", receivedProject.getDescription(), h.getUrl());
        }

        List<HyperlinkEntity> hyperlinkList =  hyperlinkEntityDAO.query(QuerySpecificationFactory.queryByProjectId(projectId));
        for(HyperlinkEntity h : hyperlinkList) {
            System.out.printf("Hyperlink fetched from query: %s (project %s)%n ", h.getUrl(), receivedProject.getDescription());
        }

        // Finally remove projects
        dao.delete(project);

    }

}
