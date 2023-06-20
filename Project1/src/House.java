import java.util.ArrayList;
import java.util.Random;

public class House extends Thread{
    static volatile ArrayList<Integer> chosenGroups;
    static volatile ArrayList<Group>  groups;

    House(ArrayList<Group> g, int id , ArrayList<Integer> chosen){
        groups = g;
        chosenGroups = chosen;
        setName("House-"+id);
    }

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()- Globals.time)+"] "+getName()+": "+m);
    }

    public static ArrayList<Integer> getChosenGroups() {
        return chosenGroups;
    }

    @Override
    public void run() {
        super.run();

        // one round
        // pick group
        Random rand = new Random();
        int chosen = rand.nextInt(groups.size());

        // already picked by this house before or by another house
        while(chosenGroups.contains(chosen)  || groups.get(chosen) == null){
            chosen = rand.nextInt( groups.size()); // re pick
        }

        chosenGroups.add(chosen);

        // save it so we can set it back to
   \
        // threads don't use it
        Group tmpGroup = groups.get(chosen);
        ArrayList<Student> students = groups.get(chosen).students;

        // remove the chosen group so that
        // other house threads don't select them
        groups.set(chosen, null);


        for(int i = 0; i<students.size(); i++){
            students.get(i).interrupt(); // give candies
        }

        msg("handed out candies to group #" + (chosen+1));

        // put group back on the array
        // so that other houses can pick them
        // in the next round
        groups.set(chosen, tmpGroup);
    }
}
