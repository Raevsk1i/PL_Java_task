package org.example;

import java.util.Scanner;

public class RunnableTask implements Runnable {
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            if (scanner.hasNextLine()) {
                break;
            }
        }
    }
}
