package org.example;

import ru.pflb.mq.dummy.exception.DummyException;
import ru.pflb.mq.dummy.implementation.ConnectionImpl;
import ru.pflb.mq.dummy.interfaces.Connection;
import ru.pflb.mq.dummy.interfaces.Producer;
import ru.pflb.mq.dummy.interfaces.Session;

public class Main {
    public static void main(String[] args) {
        sendingMessagesFromList();
    }

    public static int sendingMessagesFromList() {
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
        return 0;
    }
}