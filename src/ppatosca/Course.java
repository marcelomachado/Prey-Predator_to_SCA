package ppatosca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gtbavi
 */
public class Course {
    private ArrayList<Concept> concepts;
    private ArrayList<LearningMaterial> learningMaterials;
    private ArrayList<Learner> learners;
            
    public Course(String configFile) throws FileNotFoundException, IOException {
        // Configuration: course args
        CourseConfig courseConfig = new CourseConfig(configFile);
        
        FileInputStream stream = new FileInputStream(new File(courseConfig.getConceptsFile()));
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();

        String[] ccp_info;
        /**
         * Concepts
         */
        concepts = new ArrayList<>();
        while (line != null) {
            ccp_info = line.split(";");
            concepts.add(new Concept(Integer.parseInt(ccp_info[0]), ccp_info[1], Integer.parseInt(ccp_info[2])));
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
        ///TODO: fazer parser do LOM para capturar as informações
        stream = new FileInputStream(new File(courseConfig.getLearningMaterialsFile()));
        reader = new InputStreamReader(stream);
        br = new BufferedReader(reader);
        line = br.readLine();
        learningMaterials = new ArrayList<>();
        while (line != null) {
            ccp_info = line.split(";");
            LearningMaterial learningMaterial = new LearningMaterial(Integer.parseInt(ccp_info[0]), ccp_info[1], ccp_info[2], Integer.parseInt(ccp_info[3]), Double.parseDouble(ccp_info[4]));
            learningMaterials.add(learningMaterial);
            ///TODO: Ler outro arquivo para capturar essas informações
            for (int i = 5; i < ccp_info.length; i++) {
                Concept conceptMaterial = concepts.get(Integer.parseInt(ccp_info[i]));
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
            Learner learner = new Learner(Integer.parseInt(ccp_info[0]), Double.parseDouble(ccp_info[1]), Integer.parseInt(ccp_info[2]), Integer.parseInt(ccp_info[3]));
            if (ccp_info.length > 4) {
                for (int i = 4; i < ccp_info.length; i++) {
                    if (learner.getLearningGoals() == null) {
                        ArrayList<Concept> learningGoals = new ArrayList<>();
                        learningGoals.add(concepts.get(Integer.parseInt(ccp_info[i])));
                        learner.setLearningGoals(learningGoals);
                    } else {
                        learner.getLearningGoals().add(concepts.get(Integer.parseInt(ccp_info[i])));
                    }

                }
            }
            learners.add(learner);
            line = br.readLine();
        }
        
        /**
         * Learner Score
         */
        stream = new FileInputStream(new File(courseConfig.getLearnersScoreFile()));
        reader = new InputStreamReader(stream);
        br = new BufferedReader(reader);
        line = br.readLine();
        Learner learner;
        HashMap<Concept, Double> score = null;
        while (line != null) {
            ccp_info = line.split(";");
            if (ccp_info.length == 1) {// get id
                learner = learners.get(Integer.parseInt(ccp_info[0]));
                score = new HashMap<>();
                learner.setScore(score);
            } else {
                score.put(concepts.get(Integer.parseInt(ccp_info[0])), Double.parseDouble(ccp_info[1]));
            }
            line = br.readLine();
        }

        br.close();
        reader.close();
        stream.close();

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