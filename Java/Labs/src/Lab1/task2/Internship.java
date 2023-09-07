package Lab1.task2;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
public class Internship {
    private String name;
    private double minGrade;
    private List<Student> students = new ArrayList<>();

    public void Internship(String name, double minGrade) {
        this.name = name;
        this.minGrade = minGrade;
    }

    public void addPerson(Student student) {
        students.add(student);
    }
    public Student chooseCandidateRandomly() {
        if (students.isEmpty()) {
            return null; // Returnăm null dacă nu avem studenți în listă
        }

        Random random = new Random();
        int randomIndex = random.nextInt(students.size());
        return students.get(randomIndex);
    }

    public void chooseCandidatesForInterview() {
            System.out.println("Interviuri la " + name + ":");

            for (Student student : students) {
                if (student.getGrade() >= minGrade) {
                    System.out.println("Candidate " + student.getName() + " got a phone interview at " + name);
                }
            }
            System.out.println();
    }
}
