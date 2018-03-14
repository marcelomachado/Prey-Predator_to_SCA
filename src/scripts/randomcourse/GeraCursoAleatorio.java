package geracursoaleatorio;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author marcelo
 */
public class GeraCursoAleatorio {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        ArrayList<String> conceitos = new ArrayList<String>() {
            {
                add("ICCT01");
            }

            {
                add("ICCT02");
            }

            {
                add("ICCT03");
            }

            {
                add("ICCT04");
            }

            {
                add("ICCT05");
            }

            {
                add("ICCT06");
            }

            {
                add("ICCT07");
            }

            {
                add("ICCT08");
            }

            {
                add("ICCT09");
            }

            {
                add("ICCT10");
            }

            {
                add("ICCT11");
            }

            {
                add("ICCT12");
            }

            {
                add("ICCT13");
            }

            {
                add("ICCT14");
            }

            {
                add("ICCT15");
            }
        };

        ArrayList<String> tipos = new ArrayList<String>() {
            {
                add("simulation");
                add("diagram");
                add("figure");
                add("graphic");
                add("slide");
                add("table");
                add("narrative text");
                add("test");
                add("problems declaration");
                
            }
        };
        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(args[1] + args[0]));
        for (int i = 0; i < Integer.parseInt(args[0]); i++) {
            int x = new Random().nextInt(15) + 1;
            Collections.shuffle(conceitos);
            buffWrite.append(Integer.toString(i) + ";a");
            for (int j = 0; j < x; j++) {
                buffWrite.append(";" + conceitos.get(j));
            }
            buffWrite.append("\n");
            BufferedWriter w = new BufferedWriter(new FileWriter(args[1] + "LOM/" + Integer.toString(i) + ".xml"));
            w.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                    + "<lom xmlns=\"http://ltsc.ieee.org/xsd/LOM\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://ltsc.ieee.org/xsd/LOM http://ltsc.ieee.org/xsd/lomv1.0/lom.xsd\">\n");
            w.append("<entry>" + Integer.toString(i) + "</entry>\n");
            w.append("<title><string language=\"pt-BR\">a</string>\n"
                    + "</title>\n");
            w.append("<technical><format>video/mp4</format></technical>\n");
            int h = new Random().nextInt(24);
            String hora = (h != 0) ? Integer.toString(h) + "H" : "";

            int m = new Random().nextInt(120) + 1;
            String minuto = Integer.toString(m) + "M";
            int s = new Random().nextInt(59) + 1;
            String segundo = Integer.toString(s) + "S";
            w.append("<typicalLearningTime><duration>PT"+ minuto + segundo + "</duration>\n"
                    + "</typicalLearningTime>\n");
            int il = new Random().nextInt(5) + 1;
            String interactivityLevel;
            switch (il) {
                case 1:
                    interactivityLevel = "very low";
                    break;
                case 2:
                    interactivityLevel = "low";
                    break;
                case 4:
                    interactivityLevel = "high";
                    break;
                case 5:
                    interactivityLevel = "very high";
                    break;
                default:
                    interactivityLevel = "medium";
            }

            w.append("<interactivityLevel><source>LOMv1.0</source>\n"
                    + "<value>" + interactivityLevel + "</value></interactivityLevel>\n");

            int d = new Random().nextInt(5) + 1;
            String dificuldade;
            switch (d) {
                case 1:
                    dificuldade = "very easy";
                    break;
                case 2:
                    dificuldade = "easy";
                    break;
                case 4:
                    dificuldade = "difficult";
                    break;
                case 5:
                    dificuldade = "very difficult";
                    break;
                default:
                    dificuldade = "medium";
            }

            w.append("<difficulty><source>LOMv1.0</source>\n"
                    + "<value>" + dificuldade + "</value></difficulty>\n");

            String interactivityType;
            int t = new Random().nextInt(3) + 1;
            switch (t) {
                case 1:
                    interactivityType = "active";
                    break;
                case 2:
                    interactivityType = "expositive";
                    break;
                default:
                    interactivityType = "mixed";
            }
            w.append("<educational><interactivityType><source>LOMv1.0</source>\n"
                    + "<value>" + interactivityType + "</value></interactivityType>\n");
            int type = new Random().nextInt(5) + 1;
            Collections.shuffle(tipos);           
            for (int j = 0; j < type; j++) {
                w.append("<learningResourceType><source>LOMv1.0</source>\n" +
"<value>"+tipos.get(j)+"</value></learningResourceType>\n");
            }
            w.append("</educational>\n");
            w.append("</lom>");
            w.close();
        }
        buffWrite.close();
    }

}
