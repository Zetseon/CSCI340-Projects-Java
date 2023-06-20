import java.util.ArrayList;

public class Group {
    volatile ArrayList<Student> students;
    int id;

    Group(int id){ students = new ArrayList<>(); this.id = id;}

    void addStudent(Student s){
        students.add(s);
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public double getAverageCandies(){
        int sum = 0;
        for(Student s: students)
            sum += s.getNumCandiesFromHouse();

        return (double)sum/students.size();
    }

}
