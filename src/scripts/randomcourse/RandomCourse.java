package scripts.randomcourse;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author gtbavi
 */
public class RandomCourse {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Random random = new Random();

        ArrayList<Concept> concepts = new ArrayList<>();
        ArrayList<Concept> aux_conceps;

        String[] course_words = {"computação", "ciência", "dado", "mineração", "bit", "organização", "mídia", "sistema", "segurança", "informação", "programação", "computador",
            "internet", "servidor", "software", "engenharia", "informatica", "rede", "protocolo", "comunicação", "planejamento", "gestão", "grafo", "teoria", "recomendação", "introdução",
            "modelagem", "algoritmo", "estrutura", "educação", "digital", "genético", "móvel", "multimídia", "hipermídia", "compilador", "número", "cálculo", "virtualização", "modelo",
            "evolução", "conhecimento", "análise", "projeto", "automático", "sintético"};

        String[] learning_materials = {"mídias", "explicação", "base", "mudança", "fixação", "recorte", "começando", "início", "computação", "nuvem", "semáforo",
            "projeto", "metaheurística", "heurística", "guloso", "algoritmo", "leitura", "imagem", "video", "aprendendo", "lógica", "dado", "teste", "mesa", "sintaxe", "semântica", "web", "gráfico", "jogos", "prática"};

        String[] leraning_material_type = {"exercise", "simulation", "questionnaire", "diagram", "figure", "graph", "index", "slide", "table", "narrative text", "exam", "experiment", "problem statement", "self assessment", "lecture"};

        String[] connect = {"de"};
        String tuple = "";

        String course_name;
        String learning_material_name;
        try (BufferedWriter txtFile = new BufferedWriter(new FileWriter(args[2]))) {
            for (int i = 1; i <= Integer.parseInt(args[0]); i++) {

                course_name = course_words[random.nextInt(course_words.length)];
                if (random.nextInt(2) == 1) {
                    course_name += " " + connect[random.nextInt(connect.length)];
                }
                course_name += " " + course_words[random.nextInt(course_words.length)];
                System.out.println(course_name);

                concepts.add(new Concept(i, course_name));
                txtFile.append(i + ";" + course_name + "\n");

            }
        }

        int random_prerequisite;
        aux_conceps = (ArrayList<Concept>) concepts.clone();

        for (Concept con : concepts) {
            ArrayList<Integer> prerequisites = new ArrayList<>();
            int max_prerequisites = (int) Math.floor(aux_conceps.size() / 4);
            while (max_prerequisites > 0) {
                random_prerequisite = random.nextInt(aux_conceps.size());
                if (random.nextInt(2) == 1) { // 50% of chance 
                    if (con.id != aux_conceps.get(random_prerequisite).getId()) {
                        prerequisites.add(aux_conceps.get(random_prerequisite).getId());
                        aux_conceps.remove(random_prerequisite);
                    }
                }
                max_prerequisites--;
            }

            con.setPrerequisites(prerequisites);
        }

        print_contepts(concepts, args[3]);
        
        try (BufferedWriter txtFile = new BufferedWriter(new FileWriter(args[4]))) {
            for (int i = 1; i <= Integer.parseInt(args[1]); i++) {

                learning_material_name = learning_materials[random.nextInt(learning_materials.length)];
                if (random.nextInt(2) == 1) {
                    learning_material_name += " " + connect[random.nextInt(connect.length)];
                }
                learning_material_name += " " + learning_materials[random.nextInt(learning_materials.length)];
                System.out.println(learning_material_name);
                Double dificulty = ((double)(random.nextInt(11)))/10;
                int concept;
                if(i<=Integer.parseInt(args[0])){
                    concept = i;
                }
                else{
                    concept = random.nextInt(Integer.parseInt(args[0])+1);
                }
                txtFile.append(i + ";" + learning_material_name +";"+leraning_material_type[random.nextInt(leraning_material_type.length)]+";"+dificulty+";"+concept+"\n");

            }
        txtFile.close();
        }
        

    }

    public static void print_course() {

    }

    public static void print_contepts(ArrayList<Concept> concepts, String file) throws IOException {
        try (BufferedWriter txtFile = new BufferedWriter(new FileWriter(file))) {
            for (Concept con : concepts) {
                System.out.print(con.id + " " + con.name + " ");
                txtFile.append(con.id + "");
                for (int i = 0; i < con.prerequisites.size(); i++) {
                    System.out.print(con.prerequisites.get(i).toString() + " ");
                    txtFile.append(";" + con.prerequisites.get(i));
                }
                System.out.println();
                txtFile.append("\n");
            }
            txtFile.close();
        }

    }

}
