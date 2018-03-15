package scripts.randomcourse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author gtbavi
 */
public class CapturaValoresDaSolucao {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        File resultados[];
        File path = new File(args[0]);
        resultados = path.listFiles();
        double maior_valor_de_fitness;
        double menor_valor_de_fitness;
        double media_valor_de_fitness;
        
        int maior_valor_de_tempo;
        int menor_valor_de_tempo;
        int media_valor_de_tempo;
        
        int linhas;       
        Double valor_atual_fitness;
        int valor_atual_tempo;
        
        String[] solucao_movimento;
        
        try (BufferedWriter w = new BufferedWriter(new FileWriter(args[1]))) {
            for (File resultado : resultados) {
                solucao_movimento = resultado.getName().split("-");
                try (FileInputStream stream = new FileInputStream(resultado); InputStreamReader reader = new InputStreamReader(stream); BufferedReader br = new BufferedReader(reader)) {
                    String line = br.readLine();
                    String[] ccp_info;
                    menor_valor_de_fitness = Double.POSITIVE_INFINITY;
                    media_valor_de_fitness =0;
                    maior_valor_de_fitness = 0;
                    
                    menor_valor_de_tempo = Integer.MAX_VALUE;
                    media_valor_de_tempo = 0;
                    maior_valor_de_tempo = 0;
                    
                    linhas = 0;
                    while (line != null && !line.trim().isEmpty()) {
                        linhas++;
                        
                        ccp_info = line.split(";");
                        // valor de fitness
                        valor_atual_fitness = Double.parseDouble(ccp_info[0]);
                        if(valor_atual_fitness > maior_valor_de_fitness){
                            maior_valor_de_fitness = valor_atual_fitness;
                        }else if(valor_atual_fitness < menor_valor_de_fitness){
                            menor_valor_de_fitness = valor_atual_fitness;
                        }
                        media_valor_de_fitness +=valor_atual_fitness;
                        
                        valor_atual_tempo = Integer.parseInt(ccp_info[1]);
                        if(valor_atual_tempo > maior_valor_de_tempo){
                            maior_valor_de_tempo = valor_atual_tempo;
                        }else if(valor_atual_tempo < menor_valor_de_tempo){
                            menor_valor_de_tempo = valor_atual_tempo;
                        }
                        media_valor_de_tempo +=valor_atual_tempo;
                        
                        line = br.readLine();
                    }
                    try{
                    media_valor_de_fitness = media_valor_de_fitness/linhas;
                    media_valor_de_tempo = media_valor_de_tempo/linhas;
                    }
                    catch(ArithmeticException a){
                        a.notify();
                    }
                    
                    w.append(solucao_movimento[0]+";"+solucao_movimento[1]+";"+menor_valor_de_fitness+";"+maior_valor_de_fitness+";"+media_valor_de_fitness+";"+menor_valor_de_tempo+";"+maior_valor_de_tempo+";"+media_valor_de_tempo+"\n");
                }
            }
        }

    }
    
}
