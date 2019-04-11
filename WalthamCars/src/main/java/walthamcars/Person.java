package walthamcars;

public abstract class Person {

    private String firstName, lastName, email, department;
    private String id;

    public Person(String id, String firstName, String lastName, String email, String department) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.id = id;
    }

    @Override
    public String toString() {
        return "Person   {" + "firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", department=" + department + ", id=" + id + '}';
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getId() {
        return id;
    }

}
//1,Mavra,Tabram,mtabram0@skype.com,Engineering
