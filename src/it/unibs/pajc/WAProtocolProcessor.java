package it.unibs.pajc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

public class WAProtocolProcessor implements Runnable{

    protected Socket client;
    protected BufferedReader in;
    protected PrintWriter out;

    public WAProtocolProcessor(Socket client) {
        this.client = client;
    }

    public void run(){
        try{
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            login();
        }catch(IOException e){
            e.printStackTrace();
        }finally{
            try{
                in.close();
                out.close();
            }catch(Exception e){

            }
        }
    }

    protected String nome;
    protected static HashMap<String, WAProtocolProcessor> clientMap = new HashMap<>();

    protected void login() throws IOException {

        sendMsg(null, "Benvenuto nel server WAPP! ");
        sendMsg(null, "Inserisci il tuo nome: (minimo 3 lettere)");

        while(nome == null) {
            nome = in.readLine();
            if (nome.length() < 3) {
                sendMsg(null, "Inserisci un nome valido: (minimo 3 lettere)");
                nome = null;
            }

            synchronized (clientMap) {
                if (clientMap.containsKey(nome)) {
                    sendMsg(null,"Il nome inserito è già presente. Reiseriscilo");
                    nome = null;
                }else {
                    clientMap.put(nome, this);
                }
            }
        }

        sendMsg(null, String.format("Benvenuto %s\n", nome));
    }

    protected void sendMsg(WAProtocolProcessor sender, String msg){
        String senderName = sender != null ? sender.nome : "*";
        out.printf("[%s] %s\n", senderName, msg);
        out.flush();
    }
}
