import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.*;
public class Group {
    static Random random = new Random();

    volatile static int houseNum, groupOneCandies, groupTwoCandies, groupThreeCandies;
    volatile int totalCandies;
    volatile double groupAvg;
    volatile ArrayList<Students> groupList;
    static int groupID;


    Group(int id){
        groupList = new ArrayList<>();
        this.groupID= id;
//        int houseNum;
//        int totalCandies;
//        double groupAvg;
    }
    //adds students to group
    void addStudent(Students student){
        groupList.add(student);
    }

    //Generates which houses the groups can select
    public static int houseGen(){
        houseNum = random.nextInt(1, Main.numHouses+1);
        return houseNum;
    }


//    public void setName(String, int s){
//        return
//    }

    //Groups select which house they want to go to
    public static void selectHouse(ArrayList<Group> group){

        for(int i = 1; i <= group.size(); i++){
            group.get(i-1).houseNum = houseGen();
            Main.msg("Group " + (i) + " selected House: " + group.get(i-1).houseNum);
        }

    }



    public static void groupAverage(ArrayList<Group> list){
        double average;
        for(int i = 0; i <= Main.numGroups-1; i++) {
            if (list.get(i).groupList.size() == 0) {
                average = 0;
                list.get(i).groupAvg = average;
            } else {
                average = list.get(i).totalCandies / (double) list.get(i).groupList.size();
                list.get(i).groupAvg = average;
            }
            Main.msg("Group " + (i+1) + "'s total average is: "+ list.get(i).groupAvg);
        }
    }
//    public static ArrayList<Group> highestAverage(ArrayList<Group> list) {
//
//        double maxAvg = 0;
//        ArrayList<Group> newList = new ArrayList<>();
//        for(int i=0; i< list.size(); i++){
//            double avg = list.get(i).groupAvg;
//            if(avg >= maxAvg) {
//                newList.add(0,list.get(i));
//                maxAvg = avg;
//            }else{
//                newList.add(list.get(i));
//            }
//        }
//        return newList;
//    }
    public static void swap(List<Group> sort, int i, int j) {
    Group tmp = sort.get(i);
    sort.set(i, sort.get(j));
    sort.set(j, tmp);
    }
    public static ArrayList<Group> highestAverage(ArrayList<Group> sort) {
        for (int i = 0; i < sort.size(); i++) {

            for (int j = sort.size() - 1; j > i; j--) {
                if (sort.get(i).groupAvg < sort.get(j).groupAvg) {

                    Group tmp = sort.get(i);
                    sort.set(i,sort.get(j));
                    sort.set(j,tmp);

                }


            }

        }
        return sort;
    }

    public static synchronized void releaseLock(ArrayList<Group> list){

        for(int i = 0; i < list.size();i++) {
            int terminateTurn = list.get(i).groupID;
           // System.out.println("Queue length for Group"+ (i+1) + ":"+ Students.lockMap.get(terminateTurn).getQueueLength());
            while(Students.lockMap.get(terminateTurn).getQueueLength() != 0){
                Students.lockMap.get(terminateTurn).release();
            }
           // System.out.println("Queue length for Group"+ (i+1) + ":"+ Students.lockMap.get(terminateTurn).hasQueuedThreads());
        }
//
//    }
//        if(list.get(0).groupID == 1){
//            while(Students.gOneLock.getQueueLength() != 0 ){
//                Students.gOneLock.release();
//            }
//        } else if (list.get(1).groupID == 2) {
//            while(Students.group2Lock.getQueueLength() != 0 ){
//                Students.group2Lock.release();
//            }
//        } else if (list.get(2).groupID == 3) {
//            while(Students.group3Lock.getQueueLength() != 0 ){
//                Students.group3Lock.release();
//            }
//        } else{
//            System.out.println("Group release not in bound.");
        }
    }

