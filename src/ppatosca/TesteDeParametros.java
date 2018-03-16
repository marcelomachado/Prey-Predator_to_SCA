/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ppatosca;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author gtbavi
 */
public class TesteDeParametros {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, FileNotFoundException, CloneNotSupportedException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        PreyPredatorAlgorithmConfig ppac = new PreyPredatorAlgorithmConfig();
        ppac.setDistanceFactor(1d);
        ppac.setSurvivalValueFactor(1d);
        ppac.setMinimumStepLength(4);
        ppac.setMaximumStepLength(50);
        ppac.setFollowedPreysQuantity(1);
        ppac.setFollowUp(0.8);
        ppac.setQuantityBestRandomPreys(1);
        ppac.setMaxPredatorQuantity(1);
        ppac.setMaxBestPreyQuantity(1);
        
        for (int k = 10; k <= 40; k += 10) {
            ppac.setPopulationSize(k);
            for (int j = 100; j <= 8000; j += 500) {
                ppac.setMovementQuantity(j);       
                
                try (BufferedWriter buffWrite = new BufferedWriter(new FileWriter(args[3]+Integer.toString(k)+"-"+Integer.toString(j)))) {
                    for (int i = 1; i <= 10; i++) { // Execução com 100 testes
                        double fitness;
                        long time;
                        Object resposta = PPAtoSCA.main(args, i, ppac);
                        fitness = (double) resposta.getClass().getDeclaredField("fitness").get(resposta);
                        time = (long) resposta.getClass().getDeclaredField("time").get(resposta);
                        //System.out.println(fitness+";"+time);
                        buffWrite.append(fitness + ";" + time + "\n");
                    }
                }
                if(j == 100){
                    j = 0;
                }
            }
        }
    }

}
