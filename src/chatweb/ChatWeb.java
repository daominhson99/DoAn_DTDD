package chatweb;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatWeb {

    private static Set<PrintWriter> clients = new HashSet<>();

    public static void main(String[] args) {
        System.out.println("Server đang chạy...");

        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client kết nối!");
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                clients.add(out);

                String message;
                while ((message = in.readLine()) != null) {
                    for (PrintWriter writer : clients) {
                        writer.println(message);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    clients.remove(out);
                }
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
