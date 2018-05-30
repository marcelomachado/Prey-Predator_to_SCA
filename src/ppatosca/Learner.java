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

    public Learner(String registrationCode, float lowerTime, float upperTime, int atvref, int senint, int visver, int seqglo, ArrayList<Concept> learningGoals) {
        this.registrationCode = registrationCode;        
        // Time is in hours than we must convert to seconds        
        this.lowerTime = (int)(lowerTime*3600);
        this.upperTime = (int)(upperTime*3600);
        
        this.learningGoals = learningGoals;
        switch (atvref) {
            case -11:
            case -9:
                this.atvref = -3;
                break;
            case -7:
            case -5:
                this.atvref = -2;
                break;
            case -3:
            case -1:
                this.atvref = -1;
                break;
            case 11:
            case 9:
                this.atvref = 3;
                break;
            case 7:
            case 5:
                this.atvref = 2;
                break;
            case 3:
            case 1:
                this.atvref = 1;
                break;
            default:
                break;
        }
        
        switch (senint) {
            case -11:
            case -9:
                this.senint = -3;
                break;
            case -7:
            case -5:
                this.senint = -2;
                break;
            case -3:
            case -1:
                this.senint = -1;
                break;
            case 11:
            case 9:
                this.senint = 3;
                break;
            case 7:
            case 5:
                this.senint = 2;
                break;
            case 3:
            case 1:
                this.senint = 1;
                break;
            default:
                break;
        }
        
        switch (visver) {
            case -11:
            case -9:
                this.visver = -3;
                break;
            case -7:
            case -5:
                this.visver = -2;
                break;
            case -3:
            case -1:
                this.visver = -1;
                break;
            case 11:
            case 9:
                this.visver = 3;
                break;
            case 7:
            case 5:
                this.visver = 2;
                break;
            case 3:
            case 1:
                this.visver = 1;
                break;
            default:
                break;
        }
        
        switch (seqglo) {
            case -11:
            case -9:
                this.seqglo = -3;
                break;
            case -7:
            case -5:
                this.seqglo = -2;
                break;
            case -3:
            case -1:
                this.seqglo = -1;
                break;
            case 11:
            case 9:
                this.seqglo = 3;
                break;
            case 7:
            case 5:
                this.seqglo = 2;
                break;
            case 3:
            case 1:
                this.seqglo = 1;
                break;
            default:
                break;
        }
        
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
