import java.util.Random;

public class Student extends Thread implements Comparable<Student> {
    int id, groupId;
    volatile boolean gotCandies;
    Teacher teacher;

    volatile int numCandiesFromHouse;

    Student(Teacher t, int i){
        teacher = t; id = i;
        setName("StudentThread-" + i);
    }


    public int getStudentId() {
        return id;
    }

    public int getNumCandiesFromHouse() {
        return numCandiesFromHouse;
    }

    @Override
    public int compareTo(Student o) {
        //  For Descending order
        return  o.getNumCandiesFromHouse() - this.numCandiesFromHouse;
    }


    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Globals.time)+"] "+getName()+": "+m);
    }


    void commute() throws InterruptedException {
        Random rand = new Random();
        sleep(rand.nextInt(900)+100); // from 100 to 1000
    }

    void hesitate() throws InterruptedException {
        Random rand = new Random();
        sleep(rand.nextInt(900)+100); // from 100 to 1000
        Thread.yield();
        Thread.yield();
    }

    void listenToLecture(){
        while(teacher.isLecturing()){};
    }

    void lineUp(){teacher.addStudentToLine();}

    void giveCandies(){ gotCandies = true; }

    @Override
    public void run() {
        super.run();

        Random rand = new Random();
        try {
            commute();
        } catch (InterruptedException e){
            msg("interrupted");
        }

        try {
            hesitate();
        } catch (InterruptedException e){
            msg("interrupted");
        }

        teacher.
                addStudent(this);


        while(!teacher.isLecturing()){}
        listenToLecture();

        setPriority(getPriority()+1);
        try { sleep(rand.nextInt(1000)); }
        catch ( InterruptedException e){ }

        setPriority(NORM_PRIORITY);

        lineUp();
        while(!gotCandies){} // wait for candies

        groupId = rand.nextInt(Globals.NUM_GROUPS);

        // waiting for invitation


        int rounds = 0;
        while(rounds < Globals.NUM_HOUSES){
            try {
                sleep(3000);
            } catch (InterruptedException e){
                numCandiesFromHouse =  rand.nextInt(9)+1;
                msg("got " + numCandiesFromHouse + " candies");
            }
            rounds++;
        }


    }


}
