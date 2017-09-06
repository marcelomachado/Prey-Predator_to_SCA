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
public class PPAtoSCA {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, CloneNotSupportedException {
        FileInputStream stream = new FileInputStream(new File(args[0]));
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader br = new BufferedReader(reader);
        String line = br.readLine();

        String[] ccp_info;
        /**
         * Concepts
         */
        ArrayList<Concept> concepts = new ArrayList<>();
        while (line != null) {
            ccp_info = line.split(";");
            concepts.add(new Concept(Integer.parseInt(ccp_info[0]), ccp_info[1], Integer.parseInt(ccp_info[2])));
            line = br.readLine();
        }
        stream = new FileInputStream(new File(args[1]));
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
        stream = new FileInputStream(new File(args[2]));
        reader = new InputStreamReader(stream);
        br = new BufferedReader(reader);
        line = br.readLine();
        ArrayList<LearningMaterial> learningMaterials = new ArrayList<>();
        while (line != null) {
            ccp_info = line.split(";");
            LearningMaterial learningMaterial = new LearningMaterial(Integer.parseInt(ccp_info[0]), ccp_info[1], ccp_info[2], Integer.parseInt(ccp_info[3]), Double.parseDouble(ccp_info[4]));
            learningMaterials.add(learningMaterial);
            Concept conceptMaterial = concepts.get(Integer.parseInt(ccp_info[5]));
            if (conceptMaterial.getLMs() == null) {
                ArrayList<LearningMaterial> lMs = new ArrayList<>();
                learningMaterials.add(learningMaterial);
                conceptMaterial.setLMs(lMs);
            } else {
                conceptMaterial.getLMs().add(learningMaterial);
            }

            line = br.readLine();
        }

        /**
         * Learner
         */
        stream = new FileInputStream(new File(args[3]));
        reader = new InputStreamReader(stream);
        br = new BufferedReader(reader);
        line = br.readLine();
        ArrayList<Learner> learners = new ArrayList<>();
        while (line != null) {
            ccp_info = line.split(";");
            Learner learner = new Learner(Integer.parseInt(ccp_info[0]), Double.parseDouble(ccp_info[1]), Integer.parseInt(ccp_info[2]), Integer.parseInt(ccp_info[3]));
            if (ccp_info.length > 4) {
                for (int i = 4; i < ccp_info.length; i++) {
                    if (learner.getLearningGoals() == null) {
                        ArrayList<Concept> learningGoals = new ArrayList<>();
                        learningGoals.add(concepts.get(i));
                        learner.setLearningGoals(learningGoals);
                    } else {
                        learner.getLearningGoals().add(concepts.get(i));
                    }

                }
            }
            learners.add(learner);
            line = br.readLine();
        }

        stream = new FileInputStream(new File(args[4]));
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

        /**
         * Population
         */
        PPA ppa = new PPA(learningMaterials, learners.get(0), concepts);
        ArrayList<Individual> bestIndividuals = new ArrayList<>();

        ppa.generatePopulation(10, learningMaterials.size());
        System.out.println(ppa.getPopulation().toString());

        for (int j = 0; j < 200; j++) {
            for (Individual individual : ppa.getPopulation().getIndividuals()) {
                System.out.println("Moving: " + individual.getId() + " prey");
                ppa.moveIndividual(individual.getId(), 1d, 1d, 1);

            }
            ppa.updatePopulation();
            System.out.println(ppa.getPopulation().toString());

            Individual bestIndividual = ppa.getPopulation().getIndividuals().get(ppa.getPopulation().getBestPreyId()).clone();

            bestIndividuals.add(bestIndividual);
        }

        System.out.println();
        for (Individual ind : bestIndividuals) {
            for (int i = 0; i < ind.getPrey().length; i++) {
                System.out.print(ind.getPrey()[i] + " ");
            }
            System.out.println("Survival value: " + ind.getSurvival_value());
        }

    }

}
