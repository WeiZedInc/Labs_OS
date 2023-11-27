package Client_Server_Piped;

import java.io.IOException;
import java.io.PipedOutputStream;

class Client {
    private PipedOutputStream outputStream;

    public Client(PipedOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void sendNumber(int number) {
        try {
            outputStream.write(number);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
