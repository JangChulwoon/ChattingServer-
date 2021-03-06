package org.study.chat;


import lombok.extern.slf4j.Slf4j;

import javax.naming.ldap.SortKey;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Slf4j
public class Server {

    static ConcurrentHashMap<Integer, BufferedWriter> outputStream = new ConcurrentHashMap();

    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket(9000);
        ExecutorService es = Executors.newFixedThreadPool(5);
        log.info("Ready server");
        while (true) {
            Socket client = server.accept();
            Future<String> f = es.submit(() -> {

                log.info("Accept client");
                // make I/O object
                InputStreamReader in = new InputStreamReader(client.getInputStream());
                OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
                BufferedReader br = new BufferedReader(in);
                BufferedWriter bw = new BufferedWriter(out);
                outputStream.put(client.getPort(), bw);
                // print and write
                for ( String line = br.readLine(); line != null; line = br.readLine()) {
                    log.info(Thread.currentThread().getName() + " :: " + line);
                    log.info(line);
                    if("".equals(line)){
                        br.close();
                        bw.close();
                        in.close();
                        out.close();
                        return "";
                    }
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
