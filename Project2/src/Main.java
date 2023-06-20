import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Main {
    public static long time = System.currentTimeMillis();
    public static final int numStudent = 5, numGroups = 3,numHouses = 2;

    public static Semaphore houseLock = new Semaphore(numHouses);
    public static Semaphore mutex = new Semaphore(1);
    public static Semaphore mainLockOne = new Semaphore(0), mainLockTwo = new Semaphore(0);
    public static Students[] students = new Students[numStudent];
    public static ArrayList<Group> groups = new ArrayList<Group>(), winnerList = new ArrayList<>();


    public static void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- time)+"] Main: "+m);
    }

    public static void main(String[] args) throws InterruptedException {


        Teacher teacher = new Teacher();
        teacher.start();




        //create a list of students
        for(int i = 0; i< students.length; i++) {

            Students student = new Students(teacher, i);
            students[i] = student;
        }
        for (int i = 0; i < students.length; i++){
            students[i].start();

        }

        for(int i=1; i<=numGroups; i++) {
            groups.add(new Group(i));
        }


        mainLockOne.acquire();

        Group.selectHouse(groups);


        while(Students.mainFirstLock.getQueueLength() != 0){
            Students.mainFirstLock.release();
        }


        mainLockTwo.acquire();



        Group.groupAverage(groups);

        winnerList = Group.highestAverage(groups);
//        for(int i = 0; i < winnerList.size(); i++) {
//            msg("Winning Groups: " + winnerList.get(i).groupID);
//        }


        msg(winnerList.get(0).groupAvg + " is the highest average.");


        Group.releaseLock(winnerList);
        Group.releaseLock(winnerList);
        while(Students.studentLock.getQueueLength() != 0){
            Students.studentLock.release();
        }


//
//        for(int i = 10; i >= 1; i--){
//            for(int j=0; j <= terminate.get(i-1).studentTerminate.size()-1 && (terminate.get(i-1).studentTerminate.size() != 0); j++){
//                terminate.get(i-1).studentTerminate.get(j);
//                System.out.println(Students.tempLock.hasQueuedThreads());
//            }
//        }
//        System.out.println(Students.tempLock.getQueueLength());
//        while(Students.tempLock.getQueueLength() != 0){
//            Students.tempLock.release();
//        }


//        System.out.println("This is the ave "+Group.groupAverage(Group.groupOneCandies,Group.group1.size()));
//        System.out.println("This is the ave " + Group.groupAverage(Group.groupTwoCandies,Group.group2.size()));
//        System.out.println("This is the ave "+ Group.groupAverage(Group.groupThreeCandies,Group.group3.size()));






        //house.start();
//        Random rand = new Random();
//        for(int i =0; i<numStudent; i++){
//            Students student = students[rand.nextInt(1, 3)];
//            System.out.println(student);
//        }








    }
}