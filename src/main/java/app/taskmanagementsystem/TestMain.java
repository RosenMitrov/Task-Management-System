package app.taskmanagementsystem;

import java.util.Random;

public class TestMain {
    public static void main(String[] args) {


        Random random = new Random();
        random.setSeed(10L);

        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextLong(4) +1);

        }
    }
}
