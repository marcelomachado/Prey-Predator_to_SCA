package ppatosca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author gtbavi
 */
public class Course {

    private ArrayList<Concept> concepts;
    private ArrayList<LearningMaterial> learningMaterials;
    private ArrayList<Learner> learners;
    FileInputStream stream;
    InputStreamReader reader;
    BufferedReader br;

    public Course(String configFile) {
        // Configuration: course args
        CourseConfig courseConfig;
        try {
            courseConfig = new CourseConfig(configFile);
            stream = new FileInputStream(new File(courseConfig.getConceptsFile()));
            reader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(reader);
            String line = br.readLine();

            String[] ccp_info;
            /**
             * Concepts
             */
            concepts = new ArrayList<>();
            while (line != null) {
                ccp_info = line.split(";");
                concepts.add(new Concept(Integer.parseInt(ccp_info[0]), ccp_info[1], ccp_info[2], Integer.parseInt(ccp_info[3])));
                line = br.readLine();
            }
            stream = new FileInputStream(new File(courseConfig.getPrerequisitesFile()));
            reader = new InputStreamReader(stream);
            br = new BufferedReader(reader);

            /**
             * Prerequisites
             */
            line = br.readLine();
            while (line != null) {
                ccp_info = line.split(";");
                if (ccp_info.length > 1) {
                    ArrayList<Concept> prerequisites = new ArrayList<>();
                    for (int i = 1; i < ccp_info.length; i++) {
                        prerequisites.add(concepts.get(Integer.parseInt(ccp_info[i])));
                    }
                    concepts.get(Integer.parseInt(ccp_info[0])).setPrerequisites(prerequisites);
                }

                line = br.readLine();
            }

            /**
             * Learning Materials
             */
            learningMaterials = new ArrayList<>();

            File lomFIles[];
            File path = new File(courseConfig.getLearningMaterialsLOM());
            lomFIles = path.listFiles();
            Element xmlElement;
            for (int i = 0; i < lomFIles.length; i++) {
                xmlElement = Util.readXMLFile(lomFIles[i]);
                try {
                    
                    int materialId = Integer.parseInt(xmlElement.getElementsByTagName("entry").item(0).getTextContent());
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
                    LearningMaterial learningMaterial = new LearningMaterial(materialId, materialName, type, typicalLearningType, difficulty, interactivityLevel, interactivityType);

                    learningMaterials.add(learningMaterial);
                } catch (NullPointerException e) {
                    System.out.println(e);
                }

            }

            stream = new FileInputStream(new File(courseConfig.getLearningMaterialsFile()));
            reader = new InputStreamReader(stream);
            br = new BufferedReader(reader);
            line = br.readLine();
            learningMaterials = new ArrayList<>();
            while (line != null) {
                ccp_info = line.split(";");
                LearningMaterial learningMaterial = learningMaterials.get(Integer.parseInt(ccp_info[0]));
                for (int i = 3; i < ccp_info.length; i++) {
                    Concept conceptMaterial = concepts.get(Integer.parseInt(ccp_info[i]));
                    // Insert concept in material list
                    if (learningMaterial.getCoveredConcepts() == null) {
                        ArrayList<Concept> concepts = new ArrayList<>();
                        concepts.add(conceptMaterial);
                        learningMaterial.setCoveredConcepts(concepts);
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
            learners = new ArrayList<>();
            while (line != null) {
                ccp_info = line.split(";");
                if (ccp_info.length > 8) {
                    ArrayList<Concept> learningGoals = new ArrayList<>();
                    for (int i = 8; i < ccp_info.length; i++) {
                        learningGoals.add(concepts.get(Integer.parseInt(ccp_info[i])));
                    }
                    int learnerId = Integer.parseInt(ccp_info[0]);
                    String registrationCode = ccp_info[1];
                    int learnerLowerTime = Integer.parseInt(ccp_info[2]);
                    int learnerUpperTime = Integer.parseInt(ccp_info[3]);
                    int atvref = Integer.parseInt(ccp_info[4]);
                    int senint = Integer.parseInt(ccp_info[5]);
                    int visver = Integer.parseInt(ccp_info[6]);
                    int seqglo = Integer.parseInt(ccp_info[7]);

                    Learner learner = new Learner(learnerId, registrationCode, learnerLowerTime, learnerUpperTime, atvref, senint, visver, seqglo, learningGoals);

                    learners.add(learner);
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
                int learnerId = Integer.parseInt(ccp_info[0]);
                int conceptId = Integer.parseInt(ccp_info[2]);
                Double conceptScore = Double.parseDouble(ccp_info[3]);
                for (Learner learner : learners) {
                    if (learner.getId() == learnerId) {
                        concept = concepts.get(conceptId);
                        if (learner.getScore() == null) {
                            score = new HashMap<>();
                            score.put(concept, conceptScore);
                            learner.setScore(score);
                        } else {
                            learner.getScore().put(concept, conceptScore);
                        }
                    }
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

    public ArrayList<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(ArrayList<Concept> concepts) {
        this.concepts = concepts;
    }

    public ArrayList<LearningMaterial> getLearningMaterials() {
        return learningMaterials;
    }

    public void setLearningMaterials(ArrayList<LearningMaterial> learningMaterials) {
        this.learningMaterials = learningMaterials;
    }

    public ArrayList<Learner> getLearners() {
        return learners;
    }

    public void setLearners(ArrayList<Learner> learners) {
        this.learners = learners;
    }

}
