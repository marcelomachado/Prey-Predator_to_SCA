package ppatosca;

import java.util.ArrayList;

/**
 *
 * @author gtbavi
 */
public class FitnessFunction {

    public static final Double COURSE_COMPLETED = Double.MAX_VALUE;
    public static ArrayList<LearningMaterial> learningMaterials;
    public static Learner learner;
    public static ArrayList<Concept> concepts;

    public ArrayList<LearningMaterial> getLearningMaterials() {
        return learningMaterials;
    }

    public void setLearningMaterials(ArrayList<LearningMaterial> LearningMaterials) {
        this.learningMaterials = LearningMaterials;
    }

    public Learner getLearner() {
        return learner;
    }

    public void setLearner(Learner learner) {
        this.learner = learner;
    }

    public ArrayList<Concept> getConcepts() {
        return concepts;
    }

    public void setConcepts(ArrayList<Concept> concepts) {
        this.concepts = concepts;
    }

    public static Double executeFitnessFunction(Double... objetiveFunctions) {
        Double fitnessValue = 0d;
        for (Double objtiveFunction : objetiveFunctions) {
            fitnessValue += objtiveFunction;
        }
        return fitnessValue;
    }

    // O1
    public static Double conceptsObjetiveFunction(int[] individual) {
        int qntt = 0;
        Double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;
                for (Concept concept : concepts) {
                    sum += Math.abs((concept.getLMs().contains(learningMaterials.get(i)) ? 1 : 0) - ((learner.getLearningGoals().contains(concept)) ? 1 : 0));
                }
            }
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;
    }

    // O2
    public static Double difficultyObjetiveFunction(int[] individual) {
        Double sum = 0d;
        Double learnerConceptAbility;
        ArrayList<Concept> coveredConcepts;
        int qntt = 0;
        int numberOfConcepts;
        
        for (int i = 0; i < individual.length; i++) {
            learnerConceptAbility = 0d;
            numberOfConcepts = 0;
            if (individual[i] == 1) {
                coveredConcepts = learningMaterials.get(i).getCoveredConcepts();
                for (Concept concept : coveredConcepts) {
                    if (learner.getLearningGoals().contains(concept)) {
                        numberOfConcepts++;
                        learnerConceptAbility += learner.getScore().get(concept);
                    }
                }
                if(numberOfConcepts != 0){
                    learnerConceptAbility /= numberOfConcepts;
                }
                sum += Math.abs(learningMaterials.get(i).getDifficulty() - learnerConceptAbility);
                qntt++;
            }
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;
    }

    // O3
    public static Double timeObjetiveFunction(int[] individual) {
        int totalTime = 0;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                totalTime += learningMaterials.get(i).getTypicalLearningTime();
            }
        }
        return Math.max(0d, (learner.getLowerTime() - totalTime)) + Math.max(0d, (totalTime - learner.getUpperTime()));
    }

    // O4
    public static Double balanceObjetiveFunction(int[] individual) {
        Double sum = 0d;
        int learningGoal;
        int i;

        // Dividend: the amount of concepts covered by learn material
        int conceptsCoveredByLM = 0;
        for (i = 0; i < individual.length; i++) {
            for (Concept concept_k : concepts) {
                if (individual[i] == 1) {
                    conceptsCoveredByLM += (concept_k.getLMs().contains(learningMaterials.get(i)) ? 1 : 0);
                }
            }
        }
        // Divisor: the amount of concepts the learner should learn
        int conceptsLearnShouldLearn = learner.getLearningGoals().size();

        Double div = (double) conceptsCoveredByLM / conceptsLearnShouldLearn;

        for (Concept concept : concepts) {
            int relevantConcepts = 0;
            // Elj
            learningGoal = ((learner.getLearningGoals().contains(concept)) ? 1 : 0);
            if (learningGoal == 0) {
                continue;
            }

            for (i = 0; i < individual.length; i++) {
                if (individual[i] == 1) {
                    relevantConcepts += (concept.getLMs().contains(learningMaterials.get(i)) ? 1 : 0);
                }
            }

            sum += Math.abs(relevantConcepts - div);

        }

        return sum;
    }
    
    // O5
    //    sum(-| [(MaterialMaxAtv - Material_i_Atv) -AlunoAtvRef]- [(MaterialMaxRef - Material_i_Ref) + AlunoAtvRef)] + 
    //    [(MaterialMaxSen - Material_i_Sen) -AlunoSenInt]- [(MaterialMaxInt - Material_i_Int) + AlunoSenInt)] +
    //    [(MaterialMaxVis - Material_i_Vis) -AlunoVisVer]- [(MaterialMaxVer - Material_i_Ver) + AlunoVisVer)] +
    //    [(MaterialMaxAtvSeq - Material_i_Seq) -AlunoSeqGlo]- [(MaterialMaxGlo - Material_i_Glo) + AlunoSeqGlo)] |)_i_0_N
    //    _____________________________________________________________________________________________________________________
    //                                               sum(individual[i])_i_0_N
    public static Double learningStyleObjetiveFunction(int[] individual) {
        int qntt = 0;
        int atvRef;
        int senInt;
        int visVer;
        int seqGlo;
        
        Double sum = 0d;

        for (int i = 0; i < individual.length; i++) {
            if (individual[i] == 1) {
                qntt++;
                //TODO delete hardcoded numbers
                //        atvRef = -Math.abs(((learningMaterialMaxAtv - learningMaterialAtv) - learnerAtvRef) - ((learningMaterialMaxRef - learningMaterialRef) + learnerAtvRef));
                atvRef = -Math.abs(((6 - learningMaterials.get(i).getLearningStyleActiveValue()) - learner.getAtvRef()) - ((6 - learningMaterials.get(i).getLearningStyleReflexiveValue()) + learner.getAtvRef()));
                //        senInt = -Math.abs(((learningMaterialMaxSen - learningMaterialSen) - learnerSenInt) - ((learningMaterialMaxInt - learningMaterialInt) + learnerSenInt));
                senInt = -Math.abs(((1 - learningMaterials.get(i).getLearningStyleSensorialValue()) - learner.getSenInt()) - ((1 - learningMaterials.get(i).getLearningStyleIntuitiveValue()) + learner.getSenInt()));
                //        visVer = -Math.abs(((learningMaterialMaxVis - learningMaterialVis) - learnerVisVer) - ((learningMaterialMaxVer - learningMaterialVer) + learnerVisVer));
                visVer = -Math.abs(((1 - learningMaterials.get(i).getLearningStyleVisualValue()) - learner.getVisVer()) - ((1 - learningMaterials.get(i).getLearningStyleVerbalValue()) + learner.getVisVer()));
                //        seqGlo = -Math.abs(((learningMaterialMaxSeq - learningMaterialSeq) - learnerSeqGlo) - ((learningMaterialMaxGlo - learningMaterialGlo) + learnerSeqGlo));
                seqGlo = -Math.abs(((1 - learningMaterials.get(i).getLearningStyleSequencialValue()) - learner.getSeqGlo()) - ((1 - learningMaterials.get(i).getLearningStyleGlobalValue()) + learner.getSeqGlo()));
                //sum += (-Math.abs(learningMaterials.get(i).getLearningStyleActiveValue() + learningMaterials.get(i).getLearningStyleReflexiveValue() -2*learner.getAtvref())+12)/12;
                sum+=(atvRef+senInt+visVer+seqGlo);
            }
        }

        return (qntt != 0) ? (sum / qntt) : COURSE_COMPLETED;
    }

}
