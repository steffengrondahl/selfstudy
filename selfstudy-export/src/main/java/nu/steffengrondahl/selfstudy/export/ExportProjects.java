package nu.steffengrondahl.selfstudy.export;

import nu.steffengrondahl.selfstudy.persist.PersistUtil;
import nu.steffengrondahl.selfstudy.persist.ProjectEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.domain.HyperlinkEntity;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Export all projects to projects.json in user selected directory
 *
 */
public class ExportProjects
{
    private Path directory;

    public ExportProjects() throws IOException {
        File rootDir = new File(".");
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
            directory = rootDir.toPath();
            validate(directory.toString());
        }
        else {
            throw new IOException("No directory selected");
        }
    }

    public ExportProjects(String dir) throws IOException {
        directory = Paths.get(dir);
        validate(dir);
    }

    private void validate(String dir) throws IOException {
        boolean isRegularWritableDir = Files.isDirectory(directory) & Files.isReadable(directory) & Files.isWritable(directory);
        if(!isRegularWritableDir) {
            throw new IOException("Unable to write to directory, " + dir);
        }
    }

    public void createJsonFiles() throws IOException {
        try {
            Map<String, Object> properties = new HashMap<>(1);
            properties.put(JsonGenerator.PRETTY_PRINTING, true);
            JsonGeneratorFactory factory = Json.createGeneratorFactory(properties);

            // Build json array with all projects and save to selected file
            OutputStream fos = new FileOutputStream(directory.toAbsolutePath() + File.separator + "projects.json");
            JsonGenerator jsonGenerator = factory.createGenerator(fos, Charset.forName("UTF-8"));

            jsonGenerator.writeStartArray();

            ProjectEntityDAO dao = new ProjectEntityDAO();

            List<ProjectEntity> list = dao.query(QuerySpecificationFactory.queryAll());
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

                fos = new FileOutputStream(directory.toAbsolutePath() + File.separator + "project" + projectEntity.getId() + ".json");
                jsonGenerator = factory.createGenerator(fos, Charset.forName("UTF-8"));
                jsonGenerator.writeStartObject();

                jsonGenerator.write("id", projectEntity.getId());
                jsonGenerator.write("description", projectEntity.getDescription());
                String goals = projectEntity.getGoals();
                if(goals != null) {
                    jsonGenerator.write("goals", projectEntity.getGoals());
                }
                else {
                    jsonGenerator.write("goals", "");
                }
                String actions = projectEntity.getActions();
                if(actions != null) {
                    jsonGenerator.write("actions", projectEntity.getActions());
                }
                else {
                    jsonGenerator.write("actions", "");
                }
                LocalDate start = projectEntity.getStart();
                if (start != null) {
                    jsonGenerator.write("start", DateTimeFormatter.ISO_LOCAL_DATE.format(start));
                }
                else {
                    jsonGenerator.write("start", "");
                }
                LocalDate deadline = projectEntity.getDeadline();
                if (deadline != null) {
                    jsonGenerator.write("deadline", DateTimeFormatter.ISO_LOCAL_DATE.format(deadline));
                }
                else {
                    jsonGenerator.write("deadline", "");
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
            throw e;
        }
        finally {
            PersistUtil.shutdown();
            System.out.println("Entity manager factory has been closed");
        }
    }

    public static void main( String[] args ) {
        try {
            ExportProjects exportProjects;
            if(args.length > 0) {
                exportProjects = new ExportProjects(args[0]);
            }
            else {
                exportProjects = new ExportProjects();
            }
            exportProjects.createJsonFiles();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println( "Done export" );
    }
}
