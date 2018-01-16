package util;

/**
 *
 * @author marcelo
 */
public class LearningStyle {

    public LearningStyle() {
    }
    
    
    public int calculateFitnessFunction(int learnerAtvRef, int learningMaterialAtv, int learningMaterialRef){
        int learningMaterialMaxAtv = 6;
        int learningMaterialMaxRef = 6;
        return - Math.abs(((learningMaterialMaxAtv - learningMaterialAtv) - learnerAtvRef)-((learningMaterialMaxRef - learningMaterialRef)+ learnerAtvRef)) + 12;
    }
    
}
