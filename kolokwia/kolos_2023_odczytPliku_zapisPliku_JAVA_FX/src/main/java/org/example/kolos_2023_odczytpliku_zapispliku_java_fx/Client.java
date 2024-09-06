package org.example.kolos_2023_odczytpliku_zapispliku_java_fx;

import java.io.*;
import java.net.Socket;

public class Client {

    public static Socket connectToServer(String address, int port) {
        try {
            Socket socket = new Socket(address, port);
            return socket;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void send(String path, Socket socket) {
        try {
            File file = new File(path);
            FileInputStream input = new FileInputStream(file);
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            byte[] buffer = new byte[8192];
            int count;
            output.writeLong(file.length());
            while ((count = input.read(buffer)) != -1)
                output.write(buffer, 0, count);

            output.flush();

            System.out.println("File sent.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void receive(Socket socket, String path) {
        try {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            FileOutputStream output = new FileOutputStream(path);

            byte[] buffer = new byte[8192];
            int count;
            int receivedSize = 0;
            long fileSize = input.readLong();

            while (receivedSize < fileSize) {
                count = input.read(buffer);
                output.write(buffer, 0, count);
                System.out.println(count);
                receivedSize += count;
            }
            output.close();

            System.out.println("File received.");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Socket socket = connectToServer("localhost", 5000);
        send("input.png", socket);
        receive(socket, "output.png");
    }
}