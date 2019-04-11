package walthamcars;

import java.util.ArrayList;

public class Teamleader extends Person {

    private ArrayList<Worker> teamMembers;

    public Teamleader(String id, String firstName, String lastName, String email, String department) {
        super(id, firstName, lastName, email, department);
        this.teamMembers = new ArrayList();
    }

}
