package ppatosca;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gtbavi
 */
public class Learner {
    
    private int id;
    private HashMap<Concept, Double> score;
    private int lower_time;
    private int upper_time;    
    private ArrayList<Concept> learningGoals;  
    // Learning Style
    private int atvref;
    private int senint;
    private int visver;
    private int seqglo;
    

    public Learner() {
    }

    public Learner(HashMap<Concept, Double> score, int lower_time, int upper_time, ArrayList<Concept> learningGoals, int atvref, int senint, int visver, int seqglo) {
        this.score = score;
        this.lower_time = lower_time;
        this.upper_time = upper_time;
        this.learningGoals = learningGoals;
        this.atvref = atvref;
        this.senint = senint;
        this.visver = visver;
        this.seqglo = seqglo;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Concept, Double> getScore() {
        return score;
    }

    public void setScore(HashMap<Concept, Double> score) {
        this.score = score;
    }        

    public int getLower_time() {
        return lower_time;
    }

    public void setLower_time(int lower_time) {
        this.lower_time = lower_time;
    }

    public int getUpper_time() {
        return upper_time;
    }

    public void setUpper_time(int upper_time) {
        this.upper_time = upper_time;
    }

    public int getAtvref() {
        return atvref;
    }

    public void setAtvref(int atvref) {
        this.atvref = atvref;
    }

    public int getSenint() {
        return senint;
    }

    public void setSenint(int senint) {
        this.senint = senint;
    }

    public int getVisver() {
        return visver;
    }

    public void setVisver(int visver) {
        this.visver = visver;
    }

    public int getSeqglo() {
        return seqglo;
    }

    public void setSeqglo(int seqglo) {
        this.seqglo = seqglo;
    }

    
    
    public ArrayList<Concept> getLearningGoals() {
        return learningGoals;
    }

    public void setLearningGoals(ArrayList<Concept> learningGoals) {
        this.learningGoals = learningGoals;
    }
    
    
    
    
}
