import java.util.ArrayList;

public class SortArrayList {

    public ArrayList<Students> studentTerminate;
    volatile int studentCandyCount = 0;

    SortArrayList(int id){
        studentTerminate = new ArrayList<>();
        this.studentCandyCount= id;
    }

    void addToTerminateArrayList(Students s){
        studentTerminate.add(s);
    }
}

//public static void groupAverage(ArrayList<Group> arrayList){
//        double tempAvg;
//        for (int i = 0; i <= Main.numGroups; i++) {
//            if (Main.groups.get(i).groupList.size() == 0) {
//                tempAvg = 0;
//                Main.groups.get(i).groupAvg = tempAvg;
//            } else {
//
//                tempAvg = Main.groups.get(i).totalCandies / Main.groups.get(i).groupList.size();
//                Main.groups.get(i).groupAvg = tempAvg;
//            }
//            System.out.println("Group " + (i) + " average is: " + Main.groups.get(i).groupAvg);
//        }
//    }