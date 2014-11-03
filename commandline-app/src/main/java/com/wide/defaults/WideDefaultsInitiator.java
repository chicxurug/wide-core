package com.wide.defaults;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wide.common.FeatureFactory;
import com.wide.domainmodel.Category;
import com.wide.domainmodel.Exercise;
import com.wide.domainmodel.Exercise.DifficultyLevel;
import com.wide.domainmodel.Exercise.SolutionType;
import com.wide.domainmodel.ExercisePoint;
import com.wide.domainmodel.Feature;
import com.wide.domainmodel.Test;
import com.wide.service.WideService;

public class WideDefaultsInitiator {

    private static final Logger logger = LoggerFactory.getLogger(WideDefaultsInitiator.class);

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private WideService service;

    private WideDefaultsInitiator(WideService wideService) {
        this.service = wideService;
    }

    private void initDefaults() {
        Category root = new Category("", null);
        Category pharmacology = new Category("Pharmacology", root);
        Category engineering = new Category("Engineering", root);
        Category naturalSciences = new Category("Natural Sciences", root);
        Category medicine = new Category("Medicine", root);
        Category business = new Category("Business", root);
        Category language = new Category("Language", root);
        Category literature = new Category("Literature", root);
        Category agriculture = new Category("Agriculture", root);
        Category law = new Category("Law", root);
        Category music = new Category("Music", root);
        Category physics = new Category("Physics", root);
        Category chemistry = new Category("Chemistry", root);
        Category maths = new Category("Maths", root);
        Category lifeSciences = new Category("Life Sciences", root);
        Category astronomy = new Category("Astronomy", root);
        Category informatics = new Category("Informatics", root);
        Category geology = new Category("Geology", root);
        Category environment = new Category("Environment", root);

        Category quantumMechanics = new Category("Quantum mechanics", physics);
        Category statisticalPhysics = new Category("Statistical physics", physics);
        Category particlePhysics = new Category("Particle physics", physics);
        Category thermodynamics = new Category("Thermodynamics", physics);
        Category optics = new Category("Optics", physics);
        Category acoustics = new Category("Acoustics", physics);
        Category waves = new Category("Waves", physics);
        Category electromagnetism = new Category("Electromagnetism", physics);

        Category exampleSubCategory1 = new Category("Subcategory 1", law);
        Category exampleSubCategory2 = new Category("Subcategory 2", law);
        Category exampleSubCategory3 = new Category("Subcategory 3", law);
        Category exampleSubCategory4 = new Category("Subcategory 4", law);

        Category stringTheory = new Category("String theory", particlePhysics);
        Category radiation = new Category("Radiation", particlePhysics);

        ImmutableList<Category> categoryList = ImmutableList.<Category> of(
                // level 0
                // root,
                // level 1
                pharmacology, engineering, naturalSciences, medicine, business, language, literature, agriculture, law, music,
                physics, chemistry, maths, lifeSciences, astronomy, informatics, geology, environment,
                // level 2
                quantumMechanics, statisticalPhysics, particlePhysics, thermodynamics, optics, acoustics, waves, electromagnetism,
                exampleSubCategory1, exampleSubCategory2, exampleSubCategory3, exampleSubCategory4,
                // level 3
                stringTheory, radiation);

        Multimap<String, String> categories = LinkedHashMultimap.create();
        for (Category category : categoryList) {
            StringBuilder sb = new StringBuilder();
            Category parent = category.getParent();
            while (parent != null && parent != root) {
                sb.insert(0, '/' + parent.getName());
                parent = parent.getParent();
            }
            categories.put(sb.toString(), category.getName());
        }
        String maps = GSON.toJson(categories.asMap());
        System.out.println(maps);

        // save to db
        this.service.createCategory(root);
        for (Category category : categoryList) {
            this.service.createCategory(category);
        }

    }

    private void initDefaultsFromMM(File mmFile, boolean generateRandom) throws ParserConfigurationException, SAXException, IOException {
        Map<Node, Category> nodeCategories = new HashMap<Node, Category>();
        ArrayList<Category> categoryList = new ArrayList<Category>();
        Category root = null;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(mmFile);
        NodeList nList = doc.getElementsByTagName("node");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                if ("WIDE".equals(nNode.getAttributes().getNamedItem("TEXT").getNodeValue())) {
                    root = new Category("", null);
                    nodeCategories.put(nNode, root);
                } else {
                    Category cat = new Category(nNode.getAttributes().getNamedItem("TEXT").getNodeValue(), nodeCategories.get(nNode.getParentNode()));
                    categoryList.add(cat);
                    nodeCategories.put(nNode, cat);
                }
            }
        }

        // save to db
        this.service.createCategory(root);
        for (Category category : categoryList) {
            this.service.createCategory(category);
            if (generateRandom) {
                generateRandomExercises(category);
            }
        }
    }

    private void initExercisesFromLatex(File exLatex, File solLatex, String separator) throws IOException {
        BufferedReader exbr = new BufferedReader(new FileReader(exLatex));
        BufferedReader solbr = new BufferedReader(new FileReader(solLatex));
        Category category = this.service.getOrCreateCategory("Linear Algebra", null);
        int counter = 1;
        String line = "";
        String sline = "";

        do {
            StringBuffer exText = new StringBuffer();
            Exercise ex = new Exercise("Serbian", "Polinomi i racionalne funkcije 1." + counter, "hpeter", DifficultyLevel.D2_AVERAGE, 1,
                    "M1_prvi", Exercise.SchoolLevel.HIGHSCHOOL, "Polygon Group Ltd.", "M1_prvi", category, null, SolutionType.MULTI_CHOICE);
            List<Feature> exFeatures = new ArrayList<Feature>();
            while ((line = exbr.readLine()) != null) {
                if (line.contains(separator)) {
                    break;
                }
                exText.append(line.trim());
            }
            exFeatures.add(FeatureFactory.createExerciseText(exText.toString()));
            boolean first = true;
            StringBuffer solText = new StringBuffer();
            while ((sline = solbr.readLine()) != null) {
                if (sline.contains(separator)) {
                    break;
                }
                if (first) {
                    exFeatures.add(FeatureFactory.createShortAnswer("Odgovor:=" + sline.replace("<br/>", "").replace(",", ";").trim() + "#:=#:=#:="));
                    first = false;
                } else {
                    solText.append(sline.trim());
                }
            }
            exFeatures.add(FeatureFactory.createSolutionText(solText.toString()));
            ex.setFeatures(exFeatures);
            counter++;
            this.service.saveOrUpdateExercise(ex);
        } while (line != null);
        exbr.close();
        solbr.close();
    }

    private void generateRandomExercises(Category category) {
        List<ExercisePoint> exs = new ArrayList<ExercisePoint>();
        Test t = new Test();
        for (int i = 1; i < 4; i++) {
            Exercise ex = new Exercise("English", "Exercise" + category.getName() + i, "generator", DifficultyLevel.randomDifficulty(), i,
                    "Author" + category.getName() + i, Exercise.SchoolLevel.COLLEGE, "Publisher", "Title" + category.getName() + i, category, null,
                    SolutionType.SIMPLE);
            exs.add(new ExercisePoint(ex, new Long(i)));
        }
        t.setDescription("Test" + category.getName());
        t.setExercises(exs);
        this.service.saveOrUpdateTest(t);
    }

    public static void main(String[] args) throws Exception {
        logger.info("Starting WIDE command line application.");
        // Use local persistence.xml configuration
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.wide.commandline");
        WideService wideService = new WideService(emf);
        WideDefaultsInitiator main = new WideDefaultsInitiator(wideService);
        if (args.length == 0) {
            main.initDefaults();
        } else if (args.length == 2) {
            main.initDefaultsFromMM(new File(args[0]), Boolean.parseBoolean(args[1]));
        } else {
            main.initExercisesFromLatex(new File(args[0]), new File(args[1]), args[2]);
        }
        // Close at application end
        emf.close();
        logger.info("Done.");
    }

}
