import java.util.Random;

public class House {


    static Random random = new Random();

    public synchronized int getCandy(){
        int candy = random.nextInt(1,11);
        return candy;

    }
}
