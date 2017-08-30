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
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        Random random = new Random();

        ArrayList<Concept> concepts = new ArrayList<>();
        ArrayList<Concept> auxConceps;

        String[] courseWords = {"computação", "ciência", "dado", "mineração", "bit", "organização", "mídia", "sistema", "segurança", "informação", "programação", "computador",
            "internet", "servidor", "software", "engenharia", "informatica", "rede", "protocolo", "comunicação", "planejamento", "gestão", "grafo", "teoria", "recomendação", "introdução",
            "modelagem", "algoritmo", "estrutura", "educação", "digital", "genético", "móvel", "multimídia", "hipermídia", "compilador", "número", "cálculo", "virtualização", "modelo",
            "evolução", "conhecimento", "análise", "projeto", "automático", "sintético"};

        String[] learningMaterials = {"mídias", "explicação", "base", "mudança", "fixação", "recorte", "começando", "início", "computação", "nuvem", "semáforo",
            "projeto", "metaheurística", "heurística", "guloso", "algoritmo", "leitura", "imagem", "video", "aprendendo", "lógica", "dado", "teste", "mesa", "sintaxe", "semântica", "web", "gráfico", "jogos", "prática"};

        String[] learningMaterialType = {"exercise", "simulation", "questionnaire", "diagram", "figure", "graph", "index", "slide", "table", "narrative text", "exam", "experiment", "problem statement", "self assessment", "lecture"};

        String[] connect = {"de"};

        String courseName;
        String learningMaterialName;
        int typicalLearningType;

        /**
         * Generate Concepts file
         */
        for (int i = 0; i < Integer.parseInt(args[0]); i++) {

            courseName = courseWords[random.nextInt(courseWords.length)];
            if (random.nextInt(2) == 1) {
                courseName += " " + connect[random.nextInt(connect.length)];
            }
            courseName += " " + courseWords[random.nextInt(courseWords.length)];
            System.out.println(courseName);

            concepts.add(new Concept(i, courseName));

        }

        int random_prerequisite;
        auxConceps = (ArrayList<Concept>) concepts.clone();

        /**
         * Generate prerequisites among concepts
         */
        int[] busy = new int[Integer.parseInt(args[0])];

        for (int i = 0; i < busy.length; i++) {
            busy[i] = 0;
        }
        for (Concept con : concepts) {
            busy[con.id] = 1;
            ArrayList<Integer> prerequisites = new ArrayList<>();
            int max_prerequisites = (int) Math.floor(auxConceps.size() / 5);
            while (max_prerequisites > 0) {
                random_prerequisite = random.nextInt(auxConceps.size());
                if (random.nextInt(2) == 1) { // 50% of chance 
                    if (con.id != auxConceps.get(random_prerequisite).getId() && busy[auxConceps.get(random_prerequisite).getId()] == 0) {
                        prerequisites.add(auxConceps.get(random_prerequisite).getId());
                        busy[auxConceps.get(random_prerequisite).getId()] = 1;
                        auxConceps.remove(random_prerequisite);
                    }
                }
                max_prerequisites--;
            }

            con.setPrerequisites(prerequisites);
        }
        int concepts_size = concepts.size();
        for (int i = 0; i < concepts_size; i++) {
            for (Concept con : concepts) {
                if (con.prerequisites.isEmpty()) {
                    con.level = 0;
                } else {
                    for (Integer pre : con.prerequisites) {
                        Concept child = concepts.get(pre);
                        if (child.level != -1 && (con.level == -1) || child.level + 1 > con.level) {
                            concepts.get(con.id).level = child.level + 1;
                        }
                    }
                }

            }
        }

        try (BufferedWriter txtFile = new BufferedWriter(new FileWriter(args[2]))) {
            for (Concept concept : concepts) {
                txtFile.append(concept.id + ";" + concept.name + ";" + concept.level + "\n");
            }
            txtFile.close();
        }

        printContepts(concepts, args[3]);

        /**
         * Generate Learning Materials file
         */
        try (BufferedWriter txtFile = new BufferedWriter(new FileWriter(args[4]))) {
            for (int i = 0; i < Integer.parseInt(args[1]); i++) {

                learningMaterialName = learningMaterials[random.nextInt(learningMaterials.length)];
                if (random.nextInt(2) == 1) {
                    learningMaterialName += " " + connect[random.nextInt(connect.length)];
                }
                learningMaterialName += " " + learningMaterials[random.nextInt(learningMaterials.length)];
                Double dificulty = ((double) (random.nextInt(11))) / 10;
                int concept;
                if (i < Integer.parseInt(args[0])) {
                    concept = i;
                } else {
                    concept = random.nextInt(Integer.parseInt(args[0]));
                }
                typicalLearningType = random.nextInt(11);

                txtFile.append(i + ";" + learningMaterialName + ";" + learningMaterialType[random.nextInt(learningMaterialType.length)] + ";" + typicalLearningType + ";" + dificulty + ";" + concept + "\n");

            }
            txtFile.close();
        }

        /**
         * Generate Learners
         */
//         private int id;
//    private HashMap<Concept, Double> score;
//    private Double abilityLevel;
//    private int lower_time;
//    private int upper_time;
//    private ArrayList<Concept> learningGoals;
        try (BufferedWriter txtFile = new BufferedWriter(new FileWriter(args[6]))) {
            for (int i = 0; i < Integer.parseInt(args[5]); i++) {
                Double abilityLevel = ((double) (random.nextInt(11))) / 10;
                int lower_time = random.nextInt(11);
                int upper_time = random.nextInt(11) * 2;
                String learningGoals = "";
                int size = random.nextInt(Integer.parseInt(args[0]));
                ArrayList<Integer> learningGoalsUsed = new ArrayList<>();
                for (int j = 0; j < size; j++) {
                    int randomLearningGoal = random.nextInt(Integer.parseInt(args[0]));
                    if (!learningGoalsUsed.contains(randomLearningGoal)) {
                        learningGoalsUsed.add(randomLearningGoal);
                        learningGoals += ";" +randomLearningGoal;
                    }

                }
                txtFile.append(i + ";" + abilityLevel + ";" + lower_time + ";" + upper_time + learningGoals + "\n");
            }
            txtFile.close();

        }
    }

    public static void printContepts(ArrayList<Concept> concepts, String file) throws IOException {
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
