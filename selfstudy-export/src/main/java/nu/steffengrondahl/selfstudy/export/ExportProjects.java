package nu.steffengrondahl.selfstudy.export;

import nu.steffengrondahl.selfstudy.persist.PersistUtil;
import nu.steffengrondahl.selfstudy.persist.ProjectEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.domain.HyperlinkEntity;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Export all projects to projects.json in user selected directory
 *
 */
public class ExportProjects
{
    public static void main( String[] args ) {

        try {
            ProjectEntityDAO dao = new ProjectEntityDAO();
            List<ProjectEntity> list = dao.query(QuerySpecificationFactory.queryAll());

            // Build json array with all projects and save to selected file

            File rootDir = new java.io.File(".");
            JFileChooser chooser = new JFileChooser();
            chooser.setCurrentDirectory(rootDir);
            chooser.setDialogTitle("Select export directory");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            //
            // disable the "All files" option.
            //
            chooser.setAcceptAllFileFilterUsed(false);
            //
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                rootDir = chooser.getSelectedFile();
                System.out.println("getSelectedFile() : "
                        +  chooser.getSelectedFile());
            }
            else {
                System.out.println("No Selection ");
                System.exit(0);
            }

            // write json file for all projects
            OutputStream fos = new FileOutputStream(rootDir.getAbsolutePath() + File.separator + "projects.json");
            JsonGenerator jsonGenerator = Json.createGenerator(fos);
            jsonGenerator.writeStartArray();
            for(ProjectEntity pe : list) {
                jsonGenerator.writeStartObject();
                jsonGenerator.write("id", pe.getId());
                jsonGenerator.write("description", pe.getDescription());

                jsonGenerator.writeStartObject("estimate");
                jsonGenerator.write("id", pe.getEstimate().getId());
                jsonGenerator.write("name", pe.getEstimate().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeStartObject("priority");
                jsonGenerator.write("id", pe.getPriority().getId());
                jsonGenerator.write("name", pe.getPriority().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeStartObject("status");
                jsonGenerator.write("id", pe.getStatus().getId());
                jsonGenerator.write("name", pe.getStatus().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeEnd(); // end project
            }
            jsonGenerator.writeEnd(); // end array
            jsonGenerator.close();

            // Write json file for each project
            for(ProjectEntity pe : list) {

                ProjectEntity projectEntity = dao.find(pe.getId(), true);

                fos = new FileOutputStream(rootDir.getAbsolutePath() + File.separator + "projects" + projectEntity.getId() + ".json");
                jsonGenerator = Json.createGenerator(fos);
                jsonGenerator.writeStartObject();

                jsonGenerator.write("id", projectEntity.getId());
                jsonGenerator.write("description", projectEntity.getDescription());
                String goals = projectEntity.getGoals();
                if(goals != null) {
                    jsonGenerator.write("goals", projectEntity.getGoals());
                }
                String actions = projectEntity.getActions();
                if(actions != null) {
                    jsonGenerator.write("actions", projectEntity.getActions());
                }
                LocalDate start = projectEntity.getStart();
                if (start != null) {
                    jsonGenerator.write("start", DateTimeFormatter.ISO_LOCAL_DATE.format(start));
                }
                LocalDate deadline = projectEntity.getDeadline();
                if (deadline != null) {
                    jsonGenerator.write("start", DateTimeFormatter.ISO_LOCAL_DATE.format(deadline));
                }

                jsonGenerator.writeStartObject("estimate");
                jsonGenerator.write("id", projectEntity.getEstimate().getId());
                jsonGenerator.write("name", projectEntity.getEstimate().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeStartObject("priority");
                jsonGenerator.write("id", projectEntity.getPriority().getId());
                jsonGenerator.write("name", projectEntity.getPriority().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeStartObject("status");
                jsonGenerator.write("id", projectEntity.getStatus().getId());
                jsonGenerator.write("name", projectEntity.getStatus().getName());
                jsonGenerator.writeEnd();

                // add hyperlinks
                jsonGenerator.writeStartArray("hyperlinks");
                for(HyperlinkEntity hyperlinkEntity : projectEntity.getHyperlinks()) {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.write("id", hyperlinkEntity.getId());
                    jsonGenerator.write("url", hyperlinkEntity.getUrl());
                    jsonGenerator.writeEnd();
                }
                jsonGenerator.writeEnd(); // end array

                // Write presupposed
                jsonGenerator.writeStartArray("presupposed");
                for(ProjectEntity presupposed : projectEntity.getPresupposed()) {
                    jsonGenerator.writeStartObject();
                    jsonGenerator.write("id", presupposed.getId());
                    jsonGenerator.write("description", presupposed.getDescription());

                    jsonGenerator.writeStartObject("estimate");
                    jsonGenerator.write("id", presupposed.getEstimate().getId());
                    jsonGenerator.write("name", presupposed.getEstimate().getName());
                    jsonGenerator.writeEnd();

                    jsonGenerator.writeStartObject("priority");
                    jsonGenerator.write("id", presupposed.getPriority().getId());
                    jsonGenerator.write("name", presupposed.getPriority().getName());
                    jsonGenerator.writeEnd();

                    jsonGenerator.writeStartObject("status");
                    jsonGenerator.write("id", presupposed.getStatus().getId());
                    jsonGenerator.write("name", presupposed.getStatus().getName());
                    jsonGenerator.writeEnd();

                    jsonGenerator.writeEnd();
                }
                jsonGenerator.writeEnd(); // end array

                jsonGenerator.writeEnd(); // end project
                jsonGenerator.close();
            }

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            PersistUtil.shutdown();
        }

        System.out.println( "Done export" );
    }
}
