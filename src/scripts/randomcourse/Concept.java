package scripts.randomcourse;

import java.util.ArrayList;

/**
 *
 * @author gtbavi
 */
public class Concept {

    public int id;
    public String name;
    ArrayList<Integer> prerequisites;

    public Concept(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public ArrayList<Integer> getPrerequisites() {
        return prerequisites;
    }

    public void setPrerequisites(ArrayList<Integer> prerequisites) {
        this.prerequisites = prerequisites;
    }

    

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
