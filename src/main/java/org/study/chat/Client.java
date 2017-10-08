package org.study.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

@Slf4j
public class Client {
    final static String URL = "localhost";
    final static int PORT = 9000;

    public static void main(String[] args) throws IOException {
        ClientSocket clientSocket = new ClientSocket(URL, PORT);
        ExecutorService es = Executors.newFixedThreadPool(2);
        // send
        Future send =  es.submit(() -> {
            clientSocket.send();
            return "";
        });

        // receive
        Future<String> receive = es.submit(() -> {
            clientSocket.receive();
            return "";
        });

        FutureTask futureTask = new FutureTask(() -> {
            clientSocket.send();
            return "";
        });

        // How Can I implement End-Code ....?
        // Callback?
    }

    static class ClientSocket {
        private Socket socket;
        private InputStreamReader in;
        private OutputStreamWriter out;
        private BufferedReader br;
        private BufferedReader keyboard;
        private BufferedWriter bw;

        public ClientSocket(String url, int port) throws IOException {
            this.socket = new Socket(url, port);
            this.in = new InputStreamReader(socket.getInputStream());
            this.out = new OutputStreamWriter(socket.getOutputStream());
            this.br = new BufferedReader(in);
            this.keyboard = new BufferedReader(new InputStreamReader(System.in));
            this.bw = new BufferedWriter(out);
        }

        public void receive() throws IOException {
            // br.readLine() :: Blocking

            for (String text = br.readLine(); isVaildText(text); text = br.readLine()) {
                log.info(text);
            }
        }

        public void send() throws IOException {
            // keyboard.readLine() :: Blocking
            for (String text = keyboard.readLine(); isVaildText(text); text = keyboard.readLine()) {
                bw.write(text);
                bw.newLine();
                bw.flush();
            }
        }
        private boolean isVaildText(String text) {
            log.info("Here :: "+text);
            return !"".equals(text) && text != null ;
        }

    }

}

/*        ExecutorService es = Executors.newFixedThreadPool(10);
        for(int i  =0 ; i< 10; i++){
            int count = i;
            es.submit(() -> {
                        Socket socket = new Socket(url, port);
                        InputStreamReader in = new InputStreamReader(socket.getInputStream());
                        OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
                        BufferedReader br = new BufferedReader(in);
                        //BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
                        BufferedWriter bw = new BufferedWriter(out);

                        String line = ""+count;
                        String print = null;
                        bw.write(line);
                        bw.newLine();
                        bw.flush();
                        log.info(br.readLine());
                        return "";
                    }
            );
        }

        es.shutdown();*/