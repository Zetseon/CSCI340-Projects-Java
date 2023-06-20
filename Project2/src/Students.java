import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Semaphore;


public class Students extends Thread{
    volatile int groupId, candy, groupCounter = 0;
    public static int counter = 0, mainFirstCounter = 0,groupOneCounter=0,groupTwoCounter,groupThreeCounter;
    public  static Semaphore gOneLock = new Semaphore(0);
    public  static Semaphore group2Lock = new Semaphore(0);
    public  static Semaphore group3Lock = new Semaphore(0);

    public static Semaphore studentLock = new Semaphore(0);
    public static Semaphore mainFirstLock = new Semaphore(0);
    public static long time = System.currentTimeMillis();
    static Random random = new Random();
    static int id;
    public static Semaphore tempLock= new Semaphore(0);
    public volatile static HashMap<Integer, Semaphore> lockMap = new HashMap<>(){
        {
            put(1,gOneLock);
            put(2,group2Lock);
            put(3,group3Lock);
        }};
    static HashMap<Integer, Integer> counterMap = new HashMap<>(){
        {
            put(1,groupOneCounter);
            put(2,groupTwoCounter);
            put(3,groupThreeCounter);
        }};



    Teacher teacher;




    Students(Teacher teach,int StudID){
        teacher =teach;
        id = StudID;
        setName("Student_" + StudID);

    }

    public int getStudentName(){
        return id;
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- time)+"] "+getName()+": "+m);
    }

    //simulates travel of students
    public synchronized void commute() throws InterruptedException {
        Thread.sleep((long) (Math.random()*2000));
        msg(" Student is Excited");
    }

    //Generates groups for students
    public synchronized int  generateNum() {
        groupId = random.nextInt(1,Main.numGroups+1);
        return groupId;
    }



    public synchronized int trickOrTreat(){
        candy = random.nextInt(1, 10);
        return candy;
    }






    @Override
    public synchronized void run(){


        try {
            //Going to school
            commute();

            Main.mutex.acquire();  //P(mutex)
            counter ++;            //waiting for students to commute to school
            Main.mutex.release();  //V(mutex)
            if(counter == Main.numStudent){ //checks if all students are in class
                counter=0;
                Teacher.teacherLock.release(); //releases teachers lock so it can proceed with the lecture
                studentLock.acquire();  //locks the LAST student while lecture
            }else {
                studentLock.acquire();  //Locks student while lecture
            }
            msg("got a bag of candy.");




//            Main.mutex.acquire();  //P(mutex)
//            groupFormedCounter++;            //waiting for students, forming group
//            Main.mutex.release();  //V(mutex)
//            if(groupFormedCounter == Main.numStudent){//checking if group is formed.
//
//                studentLock.release();
//            }else {
//                studentLock.acquire();  //P(teacherLock)
//            }
//            studentLock.release();
//
//
//
//            Main.mutex.acquire();  //P(mutex)
//            CC ++;            //waiting for students, forming group
//            Main.mutex.release();  //V(mutex)
//            if(CC == Main.numStudent){//checking if group is formed.
//                studentLock.release();
//                Main.rainbow.release();
//            }else {
//                studentLock.acquire();  //P(teacherLock)
//            }
//            studentLock.release();

            Main.mutex.acquire();
            generateNum();
            msg("is in group:" + groupId);
            Main.groups.get(groupId-1).addStudent(this);
            Main.mutex.release();


            Main.mutex.acquire();
            mainFirstCounter++; //Waits
            Main.mutex.release();
            if(mainFirstCounter == Main.numStudent){
                studentLock.drainPermits();
                Main.mainLockOne.release(); //Releases Main

                mainFirstLock.acquire(); //Locks the LAST student so MAIN can run first
            }   else {
                mainFirstLock.acquire();   //Locks the students until counter hits (numStudent-1)
            }




//            trickOrTreat();
//            msg("candy received: " + candy);
//            Main.groups.get(groupId-1).totalCandies += candy ;





            if(groupId == 1){
                Main.mutex.acquire();
                groupOneCounter++;
                Main.mutex.release();
                if(groupOneCounter == Main.groups.get(0).groupList.size()){
                    groupOneCounter = 0;
                    Main.houseLock.acquire();
                    System.out.println("Group " + groupId+ " entered the house.");
                    while(gOneLock.getQueueLength() != 0){
                        gOneLock.release();
                    }
                }else {
                    gOneLock.acquire();
                }

            } else if (groupId == 2) {
                Main.mutex.acquire();
                groupTwoCounter++;
                Main.mutex.release();
                if(groupTwoCounter == Main.groups.get(1).groupList.size()){
                    groupTwoCounter = 0;
                    Main.houseLock.acquire();
                    System.out.println("Group " + groupId+ " entered the house.");
                    while(group2Lock.getQueueLength() != 0){
                        group2Lock.release();
                    }
                }else {
                    group2Lock.acquire();
                }

            } else if (groupId == 3){
                Main.mutex.acquire();
                groupThreeCounter++;
                Main.mutex.release();
                if(groupThreeCounter == Main.groups.get(2).groupList.size()){
                    groupThreeCounter = 0;
                    Main.houseLock.acquire();
                    System.out.println("Group " + groupId+ " entered the house.");
                    while(group3Lock.getQueueLength() != 0){
                        group3Lock.release();
                    }
                }else {
                    group3Lock.acquire();
                }
            }

 //_____________________________________________________________________________________________________________________

            trickOrTreat();
            msg("candy received: " + candy);
            Main.groups.get(groupId-1).totalCandies += candy;
//            Main.terminate.get(candy).addToTerminateArrayList(this);





            if(groupId == 1){
                Main.mutex.acquire();
                groupOneCounter++;
                Main.mutex.release();
                if(groupOneCounter == Main.groups.get(0).groupList.size()){
                    System.out.println("Group " + groupId+ " left the house.");
                    Main.houseLock.release();
                }else {
                    gOneLock.acquire();
                }

            } else if (groupId == 2) {
                Main.mutex.acquire();
                groupTwoCounter++;
                Main.mutex.release();
                if(groupTwoCounter == Main.groups.get(1).groupList.size()){
                    System.out.println("Group " + groupId+ " left the house.");
                    Main.houseLock.release();
                }else {
                    group2Lock.acquire();
                }

            } else if (groupId == 3){
                Main.mutex.acquire();
                groupThreeCounter++;
                Main.mutex.release();
                if(groupThreeCounter == Main.groups.get(2).groupList.size()){
                    System.out.println("Group " + groupId+ " left the house.");
                    Main.houseLock.release();
                }else {
                    group3Lock.acquire();
                }
            }

            Main.mutex.acquire();  //P(mutex)
            counter ++;            //waiting for students to commute to school
            Main.mutex.release();  //V(mutex)
            if(counter == Main.numStudent){ //checks if all students are in class
                counter=0;

                Main.mainLockTwo.release();
                                          //locks the LAST student while lecture
            }else {
                lockMap.get(groupId).acquire();  //Locks student while lecture
            }


            studentLock.acquire();


            msg("Terminating with "+ candy +" candies.");


//            msg("test");
//            for(int i = 10; i >= 1; i--) {
//                if(candy==i){
//                    tempLock.acquire();
//                }
//            }

            //System.out.println("Total candies of group "+(groupId)+ ":" + Main.groups.get(groupId-1).totalCandies);







//            House.houseLock.release();
            //Teacher.teacherLock.release(); // V(teacherLock)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);

        }

    }
}