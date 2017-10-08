package org.study.chat;


import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Server {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket server = new ServerSocket(9000);
        log.info("Ready server");
        while (true) {
            Socket client = server.accept();
            log.info("Accept client");

            // make I/O object
            InputStreamReader in = new InputStreamReader(client.getInputStream());
            OutputStreamWriter out = new OutputStreamWriter(client.getOutputStream());
            BufferedReader br = new BufferedReader(in);
            BufferedWriter bw = new BufferedWriter(out);

            String line =  br.readLine();
                // print and write
            Thread.sleep(1000);
                log.info(line);
                bw.write(line);
                bw.newLine();
                bw.flush();
        }

    }
}
