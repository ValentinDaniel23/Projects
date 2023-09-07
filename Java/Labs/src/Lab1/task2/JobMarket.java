package Lab1.task2;

public class JobMarket {
    public static void main(String[] args) {
        Student gigel = new Student("Gigel", 8.52);
        Student dorel = new Student("Dorel", 9.03);
        Student marcel = new Student("Marcel", 7.18);
        Student ionel = new Student("Ionel", 9.65);


        Internship google = new Internship();
        Internship amazon = new Internship();
        Internship facebook = new Internship();
        Internship microsoft = new Internship();

        google.Internship("Google", 8.23);
        google.addPerson(gigel);
        google.addPerson(dorel);
        google.addPerson(marcel);
        google.addPerson(ionel);
        amazon.Internship("Amazon", 7.53);
        amazon.addPerson(gigel);
        amazon.addPerson(dorel);
        amazon.addPerson(marcel);
        amazon.addPerson(ionel);
        facebook.Internship("Facebook", 9.05);
        facebook.addPerson(gigel);
        facebook.addPerson(dorel);
        facebook.addPerson(marcel);
        facebook.addPerson(ionel);
        microsoft.Internship("Microsoft", 8.88);
        microsoft.addPerson(gigel);
        microsoft.addPerson(dorel);
        microsoft.addPerson(marcel);
        microsoft.addPerson(ionel);

        google.chooseCandidatesForInterview();
        amazon.chooseCandidatesForInterview();
        facebook.chooseCandidatesForInterview();
        microsoft.chooseCandidatesForInterview();
    }
}
