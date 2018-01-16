/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gtbavi
 */
public class LearningStyleTest {

    public LearningStyleTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of calculateFitnessFunction method, of class LearningStyle.
     */
//    @Test
//    public void testCalculateFitnessFunction() {
//        System.out.println("calculateFitnessFunction");
//        int learnerAtvRef = 0;
//        int learningMaterialAtv = 0;
//        int learningMaterialRef = 0;
//        LearningStyle instance = new LearningStyle();
//        int expResult = 0;
//        int result = instance.calculateFitnessFunction(learnerAtvRef, learningMaterialAtv, learningMaterialRef);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of calculateFitnessFunctionTotal method, of class LearningStyle.
     */
    @Test
    public void testCalculateFitnessFunctionTotal() {
        System.out.println("calculateFitnessFunctionTotal");
        int learnerAtvRef = 3;
        int learningMaterialAtv = 6;
        int learningMaterialRef = 6;

        int learnerSenInt = 3;
        int learningMaterialSen = 1;
        int learningMaterialInt = 1;

        int learnerVisVer = -3;
        int learningMaterialVis = 1;
        int learningMaterialVer = 1;

        int learnerSeqGlo = -3;
        int learningMaterialSeq = 1;
        int learningMaterialGlo = 0;

        LearningStyle instance = new LearningStyle();
        int result = instance.calculateFitnessFunctionTotal(learnerAtvRef, learningMaterialAtv, learningMaterialRef, learnerSenInt, learningMaterialSen, learningMaterialInt, learnerVisVer, learningMaterialVis, learningMaterialVer, learnerSeqGlo, learningMaterialSeq, learningMaterialGlo);
        System.out.println("Resultado: " + result);
        int expResult = -33;
        assertTrue(result > -24);

        int maior = -20, menor = 0;
        System.out.println("Verifica Range");
        for (learningMaterialAtv = 0; learningMaterialAtv <= 6; learningMaterialAtv++) {
            for (learningMaterialRef = 0; learningMaterialRef <= 6; learningMaterialRef++) {
                for (learningMaterialSen = 0; learningMaterialSen <= 1; learningMaterialSen++) {
                    for (learningMaterialInt = 0; learningMaterialInt <= 1; learningMaterialInt++) {
                        for (learningMaterialVis = 0; learningMaterialVis <= 1; learningMaterialVis++) {
                            for (learningMaterialVer = 0; learningMaterialVer <= 1; learningMaterialVer++) {
                                for (learningMaterialSeq = 0; learningMaterialSeq <= 1; learningMaterialSeq++) {
                                    for (learningMaterialGlo = 0; learningMaterialGlo <= 1; learningMaterialGlo++) {
                                        for (learnerAtvRef = -3; learnerAtvRef <= 3; learnerAtvRef++) {
                                            for (learnerSenInt = -3; learnerSenInt <= 3; learnerSenInt++) {
                                                for (learnerVisVer = -3; learnerVisVer <= 3; learnerVisVer++) {
                                                    for (learnerSeqGlo = -3; learnerSeqGlo <= 3; learnerSeqGlo++) {
                                                        result = instance.calculateFitnessFunctionTotal(learnerAtvRef, learningMaterialAtv, learningMaterialRef, learnerSenInt, learningMaterialSen, learningMaterialInt, learnerVisVer, learningMaterialVis, learningMaterialVer, learnerSeqGlo, learningMaterialSeq, learningMaterialGlo);
                                                        if (result < menor) {
                                                            menor = result;
                                                        }
                                                        if (result > maior) {
                                                            maior = result;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Maior = "+maior);
        System.out.println("Menor = "+menor);
        assertEquals(0, maior);
        assertEquals(-33, menor);
    }

}
