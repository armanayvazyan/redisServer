package client;

import lombok.SneakyThrows;

import java.io.*;
import java.net.Socket;

public class Client {

    @SneakyThrows({IOException.class})
    public static void main(String[] args) {
        Socket s = new Socket("localhost", 3333);
        DataInputStream din = new DataInputStream(s.getInputStream());
        DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str = "", str2 = "";
        while (!str.equals("stop")) {
            str = br.readLine();
            dout.writeUTF(str);
            dout.flush();
            str2 = din.readUTF();
            System.out.println("redis>: " + str2);
        }

        dout.close();
        s.close();
    }


    @SneakyThrows(IOException.class)
    private static void receiveResponse(BufferedReader in) {
        System.out.println("\nResponse/>");
        char[] buf = new char[32 * 1024];
        int read = in.read(buf);
        String line = new String(buf, 0, read);
        System.out.print(line);
    }

    @SneakyThrows(IOException.class)
    private static void sendMessage(BufferedWriter out, String request) {
        out.write(request);
        out.flush();
    }
}
