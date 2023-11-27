package Client_Server_Piped;

import java.io.IOException;
import java.io.PipedInputStream;
import java.util.concurrent.CompletableFuture;

class Server {
    private PipedInputStream[] inputs;

    public Server(PipedInputStream[] inputs) {
        this.inputs = inputs;
    }

    public CompletableFuture<Integer> sumNumbers() {
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> readInput(inputs[0]));
        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> readInput(inputs[1]));

        return future1.thenCombine(future2, Integer::sum);
    }

    private int readInput(PipedInputStream input) {
        try {
            return input.read();
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }
}