package org.study.chat;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Server {

    static ConcurrentHashMap<Integer, BufferedWriter> outputStream = new ConcurrentHashMap();

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket(9000);
        ExecutorService es = Executors.newFixedThreadPool(5);
        log.info("Ready server");
        while (true) {
            Socket client = server.accept();
            es.submit(() -> {
                log.info("Accept client");

                // make I/O object
                InputStreamReader in = new InputStreamReader(client.getInputStream());
                OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
                BufferedReader br = new BufferedReader(in);
                BufferedWriter bw = new BufferedWriter(out);
                outputStream.put(client.getPort(), bw);
                String line = br.readLine();
                // print and write
                for (; line != null; line = br.readLine()) {
                    Thread.sleep(5000);
                    log.info(Thread.currentThread().getName() + " :: " + line);
                    for (Integer key : outputStream.keySet()) {
                        outputStream.get(key).write(line);
                        outputStream.get(key).newLine();
                        outputStream.get(key).flush();
                    }
                }
                return "";
            });
        }

    }
}
