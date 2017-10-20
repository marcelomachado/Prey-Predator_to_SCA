package ppatosca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author gtbavi
 */
public class PPAtoSCA {

    public static void main(String[] args) throws FileNotFoundException, IOException, CloneNotSupportedException {
        
        // Configuration file
        Properties config = new Properties();
        config.load(new FileInputStream(new File(args[0])));
        
        // path
        String path = config.getProperty("ppatosca.path");
        
        // files
        String conceptsFile = path+config.getProperty("ppatosca.file.concepts");
        String prerequisitesFile = path+config.getProperty("ppatosca.file.prerequisites");
        String learningMaterialsFile = path+config.getProperty("ppatosca.file.learningMaterials");
        String learnersFile = path+config.getProperty("ppatosca.file.learners");
        String learnersScoreFile = path+config.getProperty("ppatosca.file.learnersScore");
        
        // args
        int movementQuantity  = Integer.parseInt(config.getProperty("ppatosca.arg.movementQuantity","100"));
        int populationSize  = Integer.parseInt(config.getProperty("ppatosca.arg.populationSize","10"));
        Double distanceFactor = Double.parseDouble(config.getProperty("ppatosca.arg.distanceFactor"));
        Double survivalValueFactor = Double.parseDouble(config.getProperty("ppatosca.arg.survivalValueFactor"));
        int minimumStepLength = Integer.parseInt(config.getProperty("ppatosca.arg.minimumStepLength"));
        int maximumStepLength = Integer.parseInt(config.getProperty("ppatosca.arg.maximumStepLength"));
        
        // Fitness
        boolean conceptFitness = Boolean.parseBoolean(config.getProperty("ppatosca.fitness.concept"));
        boolean dificultyFitness = Boolean.parseBoolean(config.getProperty("ppatosca.fitness.dificulty"));
        boolean balanceFitness = Boolean.parseBoolean(config.getProperty("ppatosca.fitness.balance"));
        boolean timeFitness = Boolean.parseBoolean(config.getProperty("ppatosca.fitness.time"));
        
        
        FileInputStream stream = new FileInputStream(new File(conceptsFile));
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
        stream = new FileInputStream(new File(prerequisitesFile));
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
        stream = new FileInputStream(new File(learningMaterialsFile));
        reader = new InputStreamReader(stream);
        br = new BufferedReader(reader);
        line = br.readLine();
        ArrayList<LearningMaterial> learningMaterials = new ArrayList<>();
        while (line != null) {
            ccp_info = line.split(";");
            LearningMaterial learningMaterial = new LearningMaterial(Integer.parseInt(ccp_info[0]), ccp_info[1], ccp_info[2], Integer.parseInt(ccp_info[3]), Double.parseDouble(ccp_info[4]));
            learningMaterials.add(learningMaterial);
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
        stream = new FileInputStream(new File(learnersFile));
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
        stream = new FileInputStream(new File(learnersScoreFile));
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
        // ArrayList<Individual> bestIndividuals = new ArrayList<>();

        ppa.generatePopulation(populationSize, learningMaterials.size());
        ppa.updatePopulation();
        System.out.println("População Original");
        System.out.println(ppa.getPopulation().toString());

        long tempoInicial = System.currentTimeMillis();

        for (int j = 1; j <= movementQuantity; j++) {
            System.out.println("MOVIMENTO " + j);
            Population populationClone = Population.clone(ppa.getPopulation());
            for (Individual individual : populationClone.getIndividuals()) {
                ppa.moveIndividual(individual, distanceFactor, survivalValueFactor, minimumStepLength, maximumStepLength, 1);
            }
            ppa.setPopulation(populationClone);

            ppa.updatePopulation();
            //System.out.println("o metodo executou em " + (System.currentTimeMillis() - tempoInicial));
            System.out.println(ppa.getPopulation().toString());
            //System.out.println("");

            //Individual bestIndividual = ppa.getPopulation().getIndividuals().get(ppa.getPopulation().getBestPreyId()).clone();
            //  bestIndividuals.add(bestIndividual);
        }
        //System.out.println();
        //for (Individual ind : bestIndividuals) {
        // for (int i = 0; i < ind.getPrey().length; i++) {
        //System.out.print(ind.getPrey()[i] + " ");
        //   }
        //System.out.println("Survival value: " + ind.getSurvivalValue());
        // }

    }

}
