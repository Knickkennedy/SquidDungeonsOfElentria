package roguelike.utilities;

import java.util.Random;

public class Roll {

    private static Random random = new Random(System.currentTimeMillis());

    public static int rand(int min, int max){
        return min + (int)(random.nextDouble() * ((max - min) + 1));
    }
}
