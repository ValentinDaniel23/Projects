package Lab4.database;

import Lab4.people.Student;
import Lab4.people.Teacher;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Database {
    private final List<Student> students = new ArrayList<>();
    private final List<Teacher> teachers = new ArrayList<>();
    private static Database instance = null;
    private static int numberInstances = 0;

    private Database() {
        numberInstances++;
    }

    public static Database getInstance() {
        if(instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public static int getNumberOfInstances() {
        return numberInstances;
    }

    public void addTeachers(List<Teacher> teachers) {
        this.teachers.addAll(teachers);
    }

    public void addStudents(List<Student> students) {
        this.students.addAll(students);
    }

    public List<Teacher> findTeachersBySubject(String subject) {
        List<Teacher> bySubject = new ArrayList<>();
        for(Teacher teacher : teachers) {
            if( (teacher.getSubjects()).contains(subject) ) {
              bySubject.add(teacher);
            }
        }
        return bySubject;
    }

    public List<Student> findAllStudents() {
        return students;
    }

    public List<Teacher> findAllTeachers() {
        return teachers;
    }

    public List<Student> getStudentsBySubject(String subject) {
        List<Student> bySubject = new ArrayList<>();
        for(Student student : students) {
            if( (student.getSubjects()).containsKey(subject) ) {
              bySubject.add(student);
            }
        }
        return bySubject;
    }

    public List<Student> getStudentsByAverageGrade() {
        List<Student> studentsCpy = new ArrayList<>();
        for(Student student : students) {
            studentsCpy.add(new Student(student));
        }

        studentsCpy.sort(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if ((double) o1.averageGrade() <= (double) o2.averageGrade()) return 1;
                return -1;
            }
        });
        return studentsCpy;
    }

    public List<Student> getStudentsByGradeForSubject(String subject) {
        List<Student> bySubject = this.getStudentsBySubject(subject);

        bySubject.sort(new Comparator<Student>() {
            @Override
            public int compare(Student o1, Student o2) {
                if((o1.getSubjects()).get(subject) <= (o2.getSubjects().get(subject))) return 1;
                return -1;
            }
        });
        return bySubject;
    }
}
