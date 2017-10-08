package org.study.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Client {
    public static void main(String[] args) throws IOException {
        String url = "localhost";
        int port = 9000;
        /*while(true){
            InputStreamReader in = new InputStreamReader(socket.getInputStream());
            OutputStreamWriter out = new OutputStreamWriter(socket.getOutputStream());
            BufferedReader br = new BufferedReader(in);
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            BufferedWriter bw = new BufferedWriter(out);

            String line = null;
            String print = null;
            while((line = keyboard.readLine()) !=null){
                bw.write(line);
                bw.newLine();
                bw.flush();
                log.info(br.readLine());
            }
        }*/

        ExecutorService es = Executors.newFixedThreadPool(10);
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

        es.shutdown();
    }
}
