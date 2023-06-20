import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;

public class Teacher extends Thread {
    ArrayList<Students> numOfStudents;
    public static int num = 0;
    private boolean exit;

    public static long time = System.currentTimeMillis();

    public static Semaphore teacherLock = new Semaphore(0);


    Teacher() {
        //student array list for when students commute
        numOfStudents = new ArrayList<>();
        setName("Teacher");

    }

    synchronized public void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] " + getName() + ": " + m);
    }

    synchronized public void isWaitingForStudents() throws InterruptedException {
        msg("Teacher is waiting for the students to arrive.");
    }

    public synchronized void lectureOn() throws InterruptedException {
        msg("Lecture is done.");
        msg("Students are lining up for candies.");

    }

    @Override
    public synchronized void run() {
        try {
            isWaitingForStudents();
            teacherLock.acquire();   //P(teacherLock)
            msg("All students have arrived. Lecture is on.");
            lectureOn();

            while(Students.studentLock.getQueueLength()  != 0){   //students get in line and get candies
              // Thread.sleep((long) (Math.random()*200)); // candies
                Students.studentLock.release();
            }


            //teacherLock.release();   //V(teacherLock)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}



