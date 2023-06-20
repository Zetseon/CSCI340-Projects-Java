import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import static java.util.Collections.sort;

public class Main extends  Thread{


    public static void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-Globals.time)+"] MAIN "+m);
    }

    public static void main(String[] args) throws InterruptedException {
        Globals.time = System.currentTimeMillis();
        Semaphore mutex = new Semaphore(1, false);
        Teacher teacher = new Teacher();
        ArrayList<Student> students = new ArrayList<>();

        for(int i = 0; i< Globals.NUM_STUDENTS; i++) students.add(new Student(teacher, i));

        // starting teacher and student threads
        teacher.start();
        for(int i = 0; i< Globals.NUM_STUDENTS; i++) students.get(i).start();

        // create groups and houses
        ArrayList<Group> groups = new ArrayList<>();
        for(int i=0; i<Globals.NUM_GROUPS; i++) groups.add(new Group(i));

        // wait for teacher to finish
        try {
            teacher.join();
        } catch (InterruptedException e) {
            System.out.println("Teacher got interrupted");
        }

        // add students to their respective groups
        for(int i=0; i < Globals.NUM_STUDENTS; i++) {
            int gid = students.get(i).groupId;
            msg("Student-" + i + " is in group #" + gid);
            groups.get(gid).addStudent(students.get(i));
        }

        // run `NUM_HOUSES` rounds

        // key: house id, val: group
        //HashMap<Integer, ArrayList<Integer>> chosenGroupsByHouses = new HashMap<Integer, ArrayList<Integer>>();

        for(int i=0; i<Globals.NUM_HOUSES; i++) {
            // creating a list of house threads to execute for this round
            ArrayList<House> houses = new ArrayList<>();
            for(int j=0; j<Globals.NUM_HOUSES; j++) {
                // getting groups that have been chosen
                // in the previous round so that the thread
                // doesn't pick the same one again


                /*else*/ houses.add(new House(groups, j, new ArrayList<>()));
            }

            // starting the threads
            for(int k=0; k<Globals.NUM_HOUSES; k++)
                houses.get(k).start();

            // waiting for threads to complete
            for(int l=0; l<Globals.NUM_HOUSES; l++) {
                try {
                    //chosenGroupsByHouses.put(l, houses.get(l).getChosenGroups());
                    houses.get(l).join();
                } catch (InterruptedException e) {
                    System.out.println("House thread got interrupted");
                }
            }


            msg("houses finished round #" + (i+1));
        }

        double maxAvg = 0;
        int maxAvgGroup = 0;
        for(int i=0; i< groups.size(); i++){
            double avg = groups.get(i).getAverageCandies();
            System.out.println("Group #" + (i+1) + " average: " + avg);

            if(avg > maxAvg) {
                maxAvg = avg;
                maxAvgGroup = i+1;
            }
        }

        msg("Finished all the rounds");
        msg("Group #"+maxAvgGroup + " is in 1st place!");

        // sort so that we start terminating students
        // with most candies first
        sort(students);
        for(Student s: students){
            if(s.isAlive()) {
                try {
                    s.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            msg("Terminated Student-" + s.getStudentId() + " with " + s.getNumCandiesFromHouse() + " candies. ");
        }
        System.out.println(groups.get(0).students);
        System.out.println(groups.get(1).students);
        System.out.println(groups.get(2).students);
    }


}
