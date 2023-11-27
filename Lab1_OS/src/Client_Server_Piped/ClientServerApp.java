package Client_Server_Piped;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class ClientServerApp {
    public static void main(String[] args) {
        try {
            PipedOutputStream[] outputs = {new PipedOutputStream(), new PipedOutputStream()};
            PipedInputStream[] inputs = {new PipedInputStream(outputs[0]), new PipedInputStream(outputs[1])};

            Server server = new Server(inputs);
            Client client1 = new Client(outputs[0]);
            Client client2 = new Client(outputs[1]);

            client1.sendNumber(22);
            client2.sendNumber(20);

            server.sumNumbers().thenAccept(sum -> System.out.println("Sum: " + sum)).join();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}