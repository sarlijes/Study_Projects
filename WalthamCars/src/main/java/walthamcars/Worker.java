package walthamcars;

public class Worker extends Person {
    
    private Teamleader teamleader;

    public Worker(Integer id, String firstName, String lastName, String email, String department) {
        super(id, firstName, lastName, email, department);
    }

}
