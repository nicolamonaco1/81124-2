package it.unibs.pajc;

import java.net.ServerSocket;
import java.net.Socket;

public class WAServer {
    public static void main(String[] args) {
        System.out.print("Avvio del server...");

        try(
            ServerSocket server = new ServerSocket(1234);
            ){
            while(true){
                System.out.println("Attesa del Client...");
                Socket client = server.accept();

                WAProtocolProcessor p = new WAProtocolProcessor(client);
                (new Thread(p)).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }


        System.out.println("Chiusura del server...");
    }
}
