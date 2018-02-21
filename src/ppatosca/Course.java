package ppatosca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author gtbavi
 */
public class Course {

    private HashMap<String, Concept> concepts;
    private HashMap<Integer, LearningMaterial> learningMaterials;
    private HashMap<String, Learner> learners;
    private FileInputStream stream;
    private InputStreamReader reader;
    private BufferedReader br;

    public Course(String configFile) {
        // Configuration: course args
        CourseConfig courseConfig;
        try {
            courseConfig = new CourseConfig(configFile);
            stream = new FileInputStream(new File(courseConfig.getConceptsFile()));
            reader = new InputStreamReader(stream);
            br = new BufferedReader(reader);
            String line = br.readLine();

            String[] ccp_info;
            /**
             * Concepts
             */

            concepts = new HashMap<>();
            while (line != null) {
                ccp_info = line.split(";");
                String abbreviation = ccp_info[0];
                String conceptName = ccp_info[1];
                concepts.put(abbreviation, new Concept(conceptName, abbreviation));
                line = br.readLine();
            }
//            stream = new FileInputStream(new File(courseConfig.getPrerequisitesFile()));
//            reader = new InputStreamReader(stream);
//            br = new BufferedReader(reader);

            /**
             * Prerequisites
             */
//            line = br.readLine();
//            while (line != null) {
//                ccp_info = line.split(";");
//                if (ccp_info.length > 1) {
//                    ArrayList<Concept> prerequisites = new ArrayList<>();
//                    for (int i = 1; i < ccp_info.length; i++) {
//                        prerequisites.add(concepts.get(Integer.parseInt(ccp_info[i])));
//                    }
//                    concepts.get(Integer.parseInt(ccp_info[0])).setPrerequisites(prerequisites);
//                }
//
//                line = br.readLine();
//            }
            /**
             * Learning Materials
             */
            File lomFIles[];
            File path = new File(courseConfig.getLearningMaterialsLOM());
            lomFIles = path.listFiles();
            Arrays.sort(lomFIles);
            learningMaterials = new HashMap<>();
            Element xmlElement;
            for (File lomFIle : lomFIles) {
                xmlElement = Util.readXMLFile(lomFIle);
                //System.out.println(lomFIle.getName());
                try {
                    int materialId = Integer.parseInt(xmlElement.getElementsByTagName("entry").item(0).getTextContent());
                    //TODO alterar parara capturar lista de tecnical types;
                    String materialName = xmlElement.getElementsByTagName("title").item(0).getChildNodes().item(0).getTextContent();

                    String type = xmlElement.getElementsByTagName("technical").item(0).getChildNodes().item(0).getTextContent();

                    Node typicalLearningTypeNode = xmlElement.getElementsByTagName("typicalLearningTime").item(0);
                    Element typicalLearningTypeElement = (Element) typicalLearningTypeNode;
                    String typicalLearningType = typicalLearningTypeElement.getElementsByTagName("duration").item(0).getTextContent();

                    Node difficultyNode = xmlElement.getElementsByTagName("difficulty").item(0);
                    Element difficultyElement = (Element) difficultyNode;
                    String difficulty = difficultyElement.getElementsByTagName("value").item(0).getTextContent();

                    Node interactivityLevelNode = xmlElement.getElementsByTagName("interactivityLevel").item(0);
                    Element interactivityLevelElement = (Element) interactivityLevelNode;
                    String interactivityLevel = interactivityLevelElement.getElementsByTagName("value").item(0).getTextContent();

                    Node interactivityTypeNode = xmlElement.getElementsByTagName("interactivityType").item(0);
                    Element interactivityTypeElement = (Element) interactivityTypeNode;
                    String interactivityType = interactivityTypeElement.getElementsByTagName("value").item(0).getTextContent();

                    NodeList learningResourceTypeNodeList = xmlElement.getElementsByTagName("learningResourceType");
                    int learningResourceTypeQuantity = learningResourceTypeNodeList.getLength();
                    String[] learningResourceType = new String[learningResourceTypeQuantity];
                    for (int i = 0; i < learningResourceTypeQuantity; i++) {
                        Node learningResourceTypeNode = learningResourceTypeNodeList.item(i);
                        Element learningResourceTypeElement = (Element) learningResourceTypeNode;
                        learningResourceType[i] = learningResourceTypeElement.getElementsByTagName("value").item(0).getTextContent();
                    }

                    LearningMaterial learningMaterial = new LearningMaterial(materialId, materialName, type, typicalLearningType, difficulty, learningResourceType, interactivityLevel, interactivityType);

                    learningMaterials.put(materialId, learningMaterial);
                } catch (NullPointerException e) {
                    System.out.println("Problema " + e.toString() + " no arquivo: " + lomFIle.getName());
                }
            }
            /**
             * Concepts <-> Learning Materials
             */
            stream = new FileInputStream(new File(courseConfig.getLearningMaterialsFile()));
            reader = new InputStreamReader(stream);
            br = new BufferedReader(reader);
            line = br.readLine();
            while (line != null) {
                ccp_info = line.split(";");
                int learningMaterialId = Integer.parseInt(ccp_info[0]);
                LearningMaterial learningMaterial = learningMaterials.get(learningMaterialId);
                for (int i = 2; i < ccp_info.length; i++) {
                    String conceptAbbreviation = ccp_info[i];
                    Concept conceptMaterial = concepts.get(conceptAbbreviation);
                    try {
                        // Insert concept in material list
                        if (learningMaterial.getCoveredConcepts() == null) {
                            ArrayList<Concept> coveredConcepts = new ArrayList<>();
                            coveredConcepts.add(conceptMaterial);
                            learningMaterial.setCoveredConcepts(coveredConcepts);
                        } else {
                            learningMaterial.getCoveredConcepts().add(conceptMaterial);
                        }

                        // Insert material in concept list
                        if (conceptMaterial.getLMs() == null) {
                            ArrayList<LearningMaterial> lMs = new ArrayList<>();
                            lMs.add(learningMaterial);
                            conceptMaterial.setLMs(lMs);
                        } else {
                            conceptMaterial.getLMs().add(learningMaterial);
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

                line = br.readLine();
            }

            /**
             * Learner
             */
            stream = new FileInputStream(new File(courseConfig.getLearnersFile()));
            reader = new InputStreamReader(stream);
            br = new BufferedReader(reader);
            line = br.readLine();
            learners = new HashMap<>();
            while (line != null) {
                ccp_info = line.split(";");
                if (ccp_info.length > 7) {
                    ArrayList<Concept> learningGoals = new ArrayList<>();
                    for (int i = 7; i < ccp_info.length; i++) {
                        String learnerLearningGoal = ccp_info[i];
                        learningGoals.add(concepts.get(learnerLearningGoal));
                    }
                    String registrationCode = ccp_info[0];
                    float learnerLowerTime = Float.parseFloat(ccp_info[1]);
                    float learnerUpperTime = Float.parseFloat(ccp_info[2]);
                    int atvref = Integer.parseInt(ccp_info[3]);
                    int senint = Integer.parseInt(ccp_info[4]);
                    int visver = Integer.parseInt(ccp_info[5]);
                    int seqglo = Integer.parseInt(ccp_info[6]);

                    Learner learner = new Learner(registrationCode, learnerLowerTime, learnerUpperTime, atvref, senint, visver, seqglo, learningGoals);

                    learners.put(registrationCode, learner);
                }
                line = br.readLine();
            }

            /**
             * Learner Score
             */
            stream = new FileInputStream(new File(courseConfig.getLearnersScoreFile()));
            reader = new InputStreamReader(stream);
            br = new BufferedReader(reader);
            line = br.readLine();
            HashMap<Concept, Double> score;
            Concept concept;
            while (line != null) {
                ccp_info = line.split(";");
                String learnerRegistrationCode = ccp_info[0];
                String conceptAbbreviation = ccp_info[1];
                Double conceptScore = Double.parseDouble(ccp_info[2]);
                Learner learner = learners.get(learnerRegistrationCode);

                concept = concepts.get(conceptAbbreviation);
                try {
                    // Normalize score
                    //conceptScore = (conceptScore - 1) / 4;
                    if (learner.getScore() == null) {
                        score = new HashMap<>();
                        score.put(concept, conceptScore);
                        learner.setScore(score);
                    } else {
                        learner.getScore().put(concept, conceptScore);
                    }
                } catch (Exception e) {

                }

                line = br.readLine();
            }
            br.close();
            reader.close();
            stream.close();
        } catch (IOException | ParserConfigurationException | SAXException ex) {
            Logger.getLogger(Course.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public HashMap<String, Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(HashMap<String, Concept> concepts) {
        this.concepts = concepts;
    }

    public HashMap<Integer, LearningMaterial> getLearningMaterials() {
        return learningMaterials;
    }

    public void setLearningMaterials(HashMap<Integer, LearningMaterial> learningMaterials) {
        this.learningMaterials = learningMaterials;
    }

    public HashMap<String, Learner> getLearners() {
        return learners;
    }

    public void setLearners(HashMap<String, Learner> learners) {
        this.learners = learners;
    }

}
