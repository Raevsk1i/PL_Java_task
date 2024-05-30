package org.example;

import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        sendingMessagesFromFile(args[0]);
    }

    public static int sendingMessagesFromFile(String filePath) {
        try (Connection connection = new ConnectionImpl();
             Reader fileReader = new FileReader(filePath);
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
        return 0;
    }
}