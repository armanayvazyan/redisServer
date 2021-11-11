package server;

import lombok.SneakyThrows;
import server.exceptions.WrongCommandException;
import server.exceptions.WrongNumberOfArgumentsException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Server {

    @SneakyThrows({IOException.class, InterruptedException.class})
    static void start() {
        ServerSocket ss = new ServerSocket(3333);
        Socket s = ss.accept();
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());

        String command = "";
        while (!command.equals("exit")) {
            command = din.readUTF();

            ExecutorService service = Executors.newSingleThreadExecutor();

            String finalCommand = command;
            String serverResponse;
            try {
                Future<String> future = service.submit(() -> new CommandProcessor(finalCommand).call());
                serverResponse = future.get();
            } catch ( ExecutionException e) {
                serverResponse = e.getMessage();
            }
            dout.writeUTF(serverResponse);
            dout.flush();

        }

        din.close();
        s.close();
        ss.close();
    }

    public static void main(String[] args) {
        start();
    }
}
