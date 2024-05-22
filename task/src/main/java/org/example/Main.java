package org.example;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.io.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Task 1
        System.out.println("Task №1");
        try (Connection connection = new ConnectionImpl())
        {
            String[] messages = new String[]{"Четыре", "Пять", "Шесть"};
            Session session = connection.createSession(true);
            Producer producer = session.createProducer(session.createDestination("queue"));
            for (String message : messages) {
                producer.send(message);
                Thread.sleep(2000);
            }
            session.close();
        } catch (DummyException | InterruptedException e) {
            e.printStackTrace();
        }

        // Task 2
        System.out.println("\n" + "Task №2");
        try (Connection connection = new ConnectionImpl();
             Reader fileReader = new FileReader("src/main/resources/messages.dat");
             BufferedReader reader = new BufferedReader(fileReader))
        {
            connection.start();
            Session session = connection.createSession(true);
            Producer producer = session.createProducer(session.createDestination("queue"));

            List<String> messages = reader.lines().toList();

            Thread th = new Thread(new RunnableTask());
            th.start();
            int i = 0;
            do {
                i = i % messages.size();
                producer.send(messages.get(i));
                i++;
                Thread.sleep(2000);
            } while (th.isAlive());

            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}