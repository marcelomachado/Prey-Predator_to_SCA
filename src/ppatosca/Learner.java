package ppatosca;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author gtbavi
 */
public class Learner {
    
    private int id;
    private String registrationCode;
    private HashMap<Concept, Double> score;
    private int lowerTime;
    private int upperTime;    
    private ArrayList<Concept> learningGoals;  
    // Learning Style
    private int atvref;
    private int senint;
    private int visver;
    private int seqglo;
    

    public Learner() {
    }

    public Learner(int id, String registrationCode, int lowerTime, int upperTime, int atvref, int senint, int visver, int seqglo, ArrayList<Concept> learningGoals) {
        this.id = id;
        this.registrationCode = registrationCode;
        this.lowerTime = lowerTime;
        this.upperTime = upperTime;
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

    public int getLowerTime() {
        return lowerTime;
    }

    public void setLowerTime(int lowerTime) {
        this.lowerTime = lowerTime;
    }

    public int getUpperTime() {
        return upperTime;
    }

    public void setUpperTime(int upperTime) {
        this.upperTime = upperTime;
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

    public String getRegistrationCode() {
        return registrationCode;
    }

    public void setRegistrationCode(String registrationCode) {
        this.registrationCode = registrationCode;
    }            
    
}
