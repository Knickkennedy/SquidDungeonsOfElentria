package roguelike.utilities;

import squidpony.squidmath.LinnormRNG;

public class Roll {

    // You can pass a seed to the LinnormRNG constructor to make rand() return the
    // same sequence of random numbers when the calls to it are the same.
    // This can be useful for testing or reproducing a bug. Classes are loaded in
    // an indeterminate order in Java, so if you get random numbers in static or
    // class-level initialization for variables, a seeded sequence may change.
    // For most usage it's fine to use the zero-arg constructor, which gets some
    // random starting values from Math.random() and is different every time.
    private static LinnormRNG random = new LinnormRNG(); 

    public static int rand(int min, int max){
        return random.nextInt(min, max+1);
    }
}
