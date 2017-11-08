package analyzes;

import java.io.IOException;
import java.util.ArrayList;
import ppatosca.Course;
import ppatosca.FitnessFunction;

/**
 *
 * @author gtbavi
 */
public class ExecuteAnalyzes extends FitnessFunction {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Course course = new Course(args[0]);
        learningMaterials = course.getLearningMaterials();
        concepts = course.getConcepts();
        learner = course.getLearners().get(0);
        
        
        BinaryPermutation bp = new BinaryPermutation(course.getLearningMaterials().size());
        ArrayList<int[]> binaryElements = bp.binaryVectorPermutation(course.getLearningMaterials().size());
        for (int[] binaryElement : binaryElements) {
            System.out.println(GenerateResult(binaryElement));
        }

        System.out.println();

    }

    public static Double GenerateResult(int[] binaryElement) {
        return executeFitnessFunction(conceptsObjetiveFunction(binaryElement), timeObjetiveFunction(binaryElement), balanceObjetiveFunction(binaryElement), difficultyObjetiveFunction(binaryElement));
    }
}
