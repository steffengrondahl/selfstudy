package nu.steffengrondahl.selfstudy.export;

import nu.steffengrondahl.selfstudy.persist.EstimateEntityDAO;
import nu.steffengrondahl.selfstudy.persist.HyperlinkEntityDAO;
import nu.steffengrondahl.selfstudy.persist.PersistUtil;
import nu.steffengrondahl.selfstudy.persist.PriorityEntityDAO;
import nu.steffengrondahl.selfstudy.persist.ProjectEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.StatusEntityDAO;
import nu.steffengrondahl.selfstudy.persist.domain.EstimateEntity;
import nu.steffengrondahl.selfstudy.persist.domain.HyperlinkEntity;
import nu.steffengrondahl.selfstudy.persist.domain.PriorityEntity;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;
import nu.steffengrondahl.selfstudy.persist.domain.StatusEntity;

import javax.json.Json;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Export resources to json files in user selected directory
 *
 * Created by Steffen Grøndahl
 */
public class JsonExporter {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    private Path directory;

    private JsonGeneratorFactory factory;

    public JsonExporter() throws IOException {
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
            initialize();
        } else {
            throw new IOException("No directory selected");
        }
    }

    public JsonExporter(Path directory) throws IOException {
        this.directory = directory;
        initialize();
    }

    private void initialize() throws IOException {
        boolean isRegularWritableDir = Files.isDirectory(directory) & Files.isReadable(directory) & Files.isWritable(directory);
        if (!isRegularWritableDir) {
            throw new IOException("Unable to write to directory, " + directory.toString());
        }
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        factory = Json.createGeneratorFactory(properties);

        System.out.printf("Using directory %s%n", directory.toString());
    }

    public List<Integer> exportProjects() throws Exception {
        List<Integer> idList = new ArrayList<>();

        Path filePath = directory.resolve("projects.json");
        try (OutputStream fos = Files.newOutputStream(filePath)) {
            JsonGenerator jsonGenerator = factory.createGenerator(fos, UTF8);

            jsonGenerator.writeStartArray();

            ProjectEntityDAO dao = new ProjectEntityDAO();

            List<ProjectEntity> list = dao.query(QuerySpecificationFactory.queryAll());
            for (ProjectEntity pe : list) {
                idList.add(pe.getId());

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

            return idList;
        }
    }

    public void exportProject(Integer id) throws Exception {

        Path filePath = directory.resolve("project"+id+".json");
        try (OutputStream fos = Files.newOutputStream(filePath)) {
            // Write json file for each project

            ProjectEntityDAO dao = new ProjectEntityDAO();
            ProjectEntity projectEntity = dao.find(id, true);

            JsonGenerator jsonGenerator = factory.createGenerator(fos, UTF8);
            jsonGenerator.writeStartObject();

            jsonGenerator.write("id", projectEntity.getId());
            jsonGenerator.write("description", projectEntity.getDescription());
            String goals = projectEntity.getGoals();
            if (goals != null) {
                jsonGenerator.write("goals", projectEntity.getGoals());
            } else {
                jsonGenerator.write("goals", "");
            }
            String actions = projectEntity.getActions();
            if (actions != null) {
                jsonGenerator.write("actions", projectEntity.getActions());
            } else {
                jsonGenerator.write("actions", "");
            }
            LocalDate start = projectEntity.getStart();
            if (start != null) {
                jsonGenerator.write("start", DateTimeFormatter.ISO_LOCAL_DATE.format(start));
            } else {
                jsonGenerator.write("start", "");
            }
            LocalDate deadline = projectEntity.getDeadline();
            if (deadline != null) {
                jsonGenerator.write("deadline", DateTimeFormatter.ISO_LOCAL_DATE.format(deadline));
            } else {
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
            for (HyperlinkEntity hyperlinkEntity : projectEntity.getHyperlinks()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.write("id", hyperlinkEntity.getId());
                jsonGenerator.write("url", hyperlinkEntity.getUrl());
                jsonGenerator.writeEnd();
            }
            jsonGenerator.writeEnd(); // end of array with hyperlinks

            // Write presupposed
            jsonGenerator.writeStartArray("presupposed");
            for (ProjectEntity presupposed : projectEntity.getPresupposed()) {
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
            jsonGenerator.writeEnd(); // end of array with presupposed projects

            // Write linkable
            jsonGenerator.writeStartArray("linkable");
            for (ProjectEntity linkable : projectEntity.getLinkable()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.write("id", linkable.getId());
                jsonGenerator.write("description", linkable.getDescription());

                jsonGenerator.writeStartObject("estimate");
                jsonGenerator.write("id", linkable.getEstimate().getId());
                jsonGenerator.write("name", linkable.getEstimate().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeStartObject("priority");
                jsonGenerator.write("id", linkable.getPriority().getId());
                jsonGenerator.write("name", linkable.getPriority().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeStartObject("status");
                jsonGenerator.write("id", linkable.getStatus().getId());
                jsonGenerator.write("name", linkable.getStatus().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeEnd();
            }
            jsonGenerator.writeEnd(); // end of array with linkable projects

            jsonGenerator.writeEnd(); // end of object

            jsonGenerator.close();
        }
    }

    public List<Integer> exportEstimates() throws Exception {
        List<Integer> idList = new ArrayList<>();

        Path filePath = directory.resolve("estimates.json");
        try (OutputStream fos = Files.newOutputStream(filePath)) {
            JsonGenerator jsonGenerator = factory.createGenerator(fos, UTF8);

            jsonGenerator.writeStartArray();

            EstimateEntityDAO dao = new EstimateEntityDAO();

            List<EstimateEntity> list = dao.query(QuerySpecificationFactory.queryAll());
            for (EstimateEntity ee : list) {
                idList.add(ee.getId());

                jsonGenerator.writeStartObject();
                jsonGenerator.write("id", ee.getId());
                jsonGenerator.write("name", ee.getName());
                jsonGenerator.writeEnd(); // end project
            }
            jsonGenerator.writeEnd(); // end array
            jsonGenerator.close();

            return idList;
        }
    }

    public void exportEstimate(Integer id) throws Exception {

        Path filePath = directory.resolve("estimate"+id+".json");
        try (OutputStream fos = Files.newOutputStream(filePath)) {

            EstimateEntityDAO dao = new EstimateEntityDAO();
            EstimateEntity estimateEntity = dao.find(id, true);

            JsonGenerator jsonGenerator = factory.createGenerator(fos, UTF8);
            jsonGenerator.writeStartObject();

            jsonGenerator.write("id", estimateEntity.getId());
            jsonGenerator.write("name", estimateEntity.getName());

            // add projects
            jsonGenerator.writeStartArray("projects");
            for (ProjectEntity projectEntity : estimateEntity.getProjects()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.write("id", projectEntity.getId());
                jsonGenerator.write("description", projectEntity.getDescription());

                jsonGenerator.writeStartObject("priority");
                jsonGenerator.write("id", projectEntity.getPriority().getId());
                jsonGenerator.write("name", projectEntity.getPriority().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeStartObject("status");
                jsonGenerator.write("id", projectEntity.getStatus().getId());
                jsonGenerator.write("name", projectEntity.getStatus().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeEnd();
            }
            jsonGenerator.writeEnd(); // end of array with projects

            jsonGenerator.writeEnd(); // end of object

            jsonGenerator.close();
        }
    }

    public List<Integer> exportPriorities() throws Exception {
        List<Integer> idList = new ArrayList<>();

        Path filePath = directory.resolve("priorities.json");
        try (OutputStream fos = Files.newOutputStream(filePath)) {
            JsonGenerator jsonGenerator = factory.createGenerator(fos, UTF8);

            jsonGenerator.writeStartArray();

            PriorityEntityDAO dao = new PriorityEntityDAO();

            List<PriorityEntity> list = dao.query(QuerySpecificationFactory.queryAll());
            for (PriorityEntity pe : list) {
                idList.add(pe.getId());

                jsonGenerator.writeStartObject();
                jsonGenerator.write("id", pe.getId());
                jsonGenerator.write("name", pe.getName());
                jsonGenerator.writeEnd(); // end project
            }
            jsonGenerator.writeEnd(); // end array
            jsonGenerator.close();

            return idList;
        }
    }

    public void exportPriority(Integer id) throws Exception {

        Path filePath = directory.resolve("priority"+id+".json");
        try (OutputStream fos = Files.newOutputStream(filePath)) {

            PriorityEntityDAO dao = new PriorityEntityDAO();
            PriorityEntity priorityEntity = dao.find(id, true);

            JsonGenerator jsonGenerator = factory.createGenerator(fos, UTF8);
            jsonGenerator.writeStartObject();

            jsonGenerator.write("id", priorityEntity.getId());
            jsonGenerator.write("name", priorityEntity.getName());

            // add projects
            jsonGenerator.writeStartArray("projects");
            for (ProjectEntity projectEntity : priorityEntity.getProjects()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.write("id", projectEntity.getId());
                jsonGenerator.write("description", projectEntity.getDescription());

                jsonGenerator.writeStartObject("status");
                jsonGenerator.write("id", projectEntity.getStatus().getId());
                jsonGenerator.write("name", projectEntity.getStatus().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeEnd();
            }
            jsonGenerator.writeEnd(); // end of array with projects

            jsonGenerator.writeEnd(); // end of object

            jsonGenerator.close();
        }
    }

    public List<Integer> exportStatuses() throws Exception {
        List<Integer> idList = new ArrayList<>();


        Path filePath = directory.resolve("statuses.json");
        try (OutputStream fos = Files.newOutputStream(filePath)) {
            JsonGenerator jsonGenerator = factory.createGenerator(fos, UTF8);

            jsonGenerator.writeStartArray();

            StatusEntityDAO dao = new StatusEntityDAO();

            List<StatusEntity> list = dao.query(QuerySpecificationFactory.queryAll());
            for (StatusEntity se : list) {
                idList.add(se.getId());

                jsonGenerator.writeStartObject();
                jsonGenerator.write("id", se.getId());
                jsonGenerator.write("name", se.getName());
                jsonGenerator.writeEnd(); // end project
            }
            jsonGenerator.writeEnd(); // end array
            jsonGenerator.close();

            return idList;
        }
    }

    public void exportStatus(Integer id) throws Exception {

        Path filePath = directory.resolve("status"+id+".json");
        try (OutputStream fos = Files.newOutputStream(filePath)) {

            StatusEntityDAO dao = new StatusEntityDAO();
            StatusEntity statusEntity = dao.find(id, true);

            JsonGenerator jsonGenerator = factory.createGenerator(fos, UTF8);
            jsonGenerator.writeStartObject();

            jsonGenerator.write("id", statusEntity.getId());
            jsonGenerator.write("name", statusEntity.getName());

            // add projects
            jsonGenerator.writeStartArray("projects");
            for (ProjectEntity projectEntity : statusEntity.getProjects()) {
                jsonGenerator.writeStartObject();
                jsonGenerator.write("id", projectEntity.getId());
                jsonGenerator.write("description", projectEntity.getDescription());

                jsonGenerator.writeStartObject("priority");
                jsonGenerator.write("id", projectEntity.getPriority().getId());
                jsonGenerator.write("name", projectEntity.getPriority().getName());
                jsonGenerator.writeEnd();

                jsonGenerator.writeEnd();
            }
            jsonGenerator.writeEnd(); // end of array with projects

            jsonGenerator.writeEnd(); // end of object

            jsonGenerator.close();
        }
    }

    public void exportHyperlinks() throws Exception {

        Path filePath = directory.resolve("hyperlinks.json");
        try (OutputStream fos = Files.newOutputStream(filePath)) {
            JsonGenerator jsonGenerator = factory.createGenerator(fos, UTF8);

            jsonGenerator.writeStartArray();

            HyperlinkEntityDAO dao = new HyperlinkEntityDAO();

            List<HyperlinkEntity> list = dao.query(QuerySpecificationFactory.queryAll());
            for (HyperlinkEntity he : list) {
                jsonGenerator.writeStartObject();
                jsonGenerator.write("id", he.getId());
                jsonGenerator.write("url", he.getUrl());

                jsonGenerator.writeStartObject("project");
                jsonGenerator.write("id", he.getProject().getId());
                jsonGenerator.write("description", he.getProject().getDescription());
                jsonGenerator.writeEnd();

                jsonGenerator.writeEnd(); // end project
            }
            jsonGenerator.writeEnd(); // end array
            jsonGenerator.close();
        }
    }

    public void exportAll() throws Exception {
        List<Integer> idList = exportProjects();
        for(Integer id: idList) {
            exportProject(id);
        }
        idList = exportEstimates();
        for(Integer id: idList) {
            exportEstimate(id);
        }
        idList = exportPriorities();
        for(Integer id: idList) {
            exportPriority(id);
        }
        idList = exportStatuses();
        for(Integer id: idList) {
            exportStatus(id);
        }
        exportHyperlinks();
    }

    public static void main(String[] args) {
        try {
            JsonExporter jsonExporter;
            if (args.length > 0) {
                jsonExporter = new JsonExporter(Paths.get(args[0]));
            } else {
                jsonExporter = new JsonExporter();
            }
            jsonExporter.exportAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            PersistUtil.shutdown();
        }
        System.out.println("Done export");
    }
}
