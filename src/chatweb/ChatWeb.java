package chatweb;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatWeb {

    private static final int PORT = 1234;
    private static Set<DataOutputStream> clients = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Server đang chạy...");

        try (ServerSocket server = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = server.accept();
                System.out.println("Client connected");

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                clients.add(out);

                new Thread(new ClientHandler(socket, out)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private DataInputStream in;
        private DataOutputStream out;

        ClientHandler(Socket socket, DataOutputStream out) throws IOException {
            this.in = new DataInputStream(socket.getInputStream());
            this.out = out;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String type = in.readUTF();

                    if (type.equals("TEXT")) {
                        String msg = in.readUTF();
                        broadcastText(msg);
                    }

                    if (type.equals("IMAGE")) {
                        String sender = in.readUTF();
                        String fileName = in.readUTF();
                        int length = in.readInt();
                        byte[] data = new byte[length];
                        in.readFully(data);
                        broadcastImage(sender, fileName, data);
                    }
                }
            } catch (IOException e) {
                clients.remove(out);
            }
        }
    }

    static void broadcastText(String msg) throws IOException {
        for (DataOutputStream dos : clients) {
            dos.writeUTF("TEXT");
            dos.writeUTF(msg);
            dos.flush();
        }
    }

    static void broadcastImage(String sender, String fileName, byte[] data) throws IOException {
        for (DataOutputStream dos : clients) {
            dos.writeUTF("IMAGE");
            dos.writeUTF(sender);
            dos.writeUTF(fileName);
            dos.writeInt(data.length);
            dos.write(data);
            dos.flush();
        }
    }
}
