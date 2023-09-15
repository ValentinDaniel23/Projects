package Lab4.people;

import Lab4.database.Database;

import java.security.interfaces.DSAKey;
import java.util.List;
import java.util.ArrayList;

public class Teacher {
    private String firstName;
    private String lastName;
    private List<String> subjects;

    public Teacher(String firstName, String lastName, List<String> subjects) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.subjects = subjects;
    }

    public Teacher(Teacher teacher) {
        firstName = teacher.firstName;
        lastName = teacher.lastName;
        subjects = new ArrayList<>(teacher.subjects);
    }
    // TODO: copy constructor

    @Override
    public String toString() {
        return "Teacher: " + firstName + " " + lastName + "\n"
                + "Subjects: " + subjects + "\n";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<String> subjects) {
        this.subjects = subjects;
    }

    public List<Teacher> getAllTeachers() {
        return Database.getInstance().findAllTeachers();
    }

    public List<Teacher> getTeachersBySubject(String subject) {
        return Database.getInstance().findTeachersBySubject(subject);
    }

    public List<Student> getAllStudents() {
        return Database.getInstance().findAllStudents();
    }

    public List<Student> getStudentsBySubject(String subject) {
        return Database.getInstance().getStudentsBySubject(subject);
    }

    public List<Student> getStudentsByAverageGrade() {
        return Database.getInstance().getStudentsByAverageGrade();
    }

    public List<Student> getStudentsByGradeForSubject(String subject) {
        return Database.getInstance().getStudentsByGradeForSubject(subject);
    }
}
