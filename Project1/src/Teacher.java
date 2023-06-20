import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Teacher extends Thread{
    AtomicBoolean lecturing;
    AtomicInteger linedUpStudents;
    ArrayList<Student> students;

    Teacher(){
        students = new ArrayList<>();
        lecturing = new AtomicBoolean(false);
        linedUpStudents = new AtomicInteger();
        setName("Teacher");
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-Globals.time)+"] "+getName()+": "+m);
    }
    void addStudent(Student s){ 
        students.add(s); 
    }
    boolean isWaitingForStudents(){ return students.size() != Globals.NUM_STUDENTS; }

    void addStudentToLine(){ linedUpStudents.set(linedUpStudents.get()+1); }
    void doLecture() throws InterruptedException {
        lecturing.set(true);
        msg("Lecture started.");
        sleep(4000);
        lecturing.set(false);
        msg("Lecture finished.");
    }

    boolean isLecturing(){ return lecturing.get(); }

    @Override
    public void run() {
        super.run();

        // busy wait for students
        //System.out.println("Teacher is waiting for students");
        msg("waiting for students");
        while(isWaitingForStudents()){
            System.out.print("");
        }
        msg("All students have arrived!");

        try {
            doLecture();
        } catch (InterruptedException e){
            System.out.println("Lecture was interrupted");
        }

        while(linedUpStudents.get() != Globals.NUM_STUDENTS){} // busy wait for students to line up

        // give candies
        for(int i = 0; i< Globals.NUM_STUDENTS; i++){
            students.get(i).giveCandies();
        }

        msg("Gave out candies to all students");
    }
}
