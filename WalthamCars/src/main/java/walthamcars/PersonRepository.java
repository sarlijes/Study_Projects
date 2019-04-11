package walthamcars;

import java.util.ArrayList;
import java.util.Collections;

public class PersonRepository {

    private ArrayList<Person> people = new ArrayList();
    private ArrayList<Worker> workers;
    private ArrayList<Teamleader> teamleaders;

    public PersonRepository() {
        people = new ArrayList();
        workers = new ArrayList();
        teamleaders = new ArrayList();
    }

    public void addWorker(Worker w) {
        workers.add(w);
        people.add(w);
        System.out.println(w.toString());
    }

    public void addTeamleader(Teamleader t) {
        teamleaders.add(t);
        people.add(t);
    }

    public void printAllPeople() {
        for (Person p : people) {
            System.out.println(p.toString());
        }
    }

    public void printTeamleaders() {
        for (Teamleader t : teamleaders) {
            System.out.println(t.toString());
        }
    }

    public void printAllWorkers() {
        for (Worker w : workers) {
            System.out.println(w.toString());
        }
    }

}
