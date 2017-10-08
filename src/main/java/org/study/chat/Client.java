package org.study.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

@Slf4j
public class Client {
    public static void main(String[] args) throws IOException {
        String url = "localhost";
        int port = 9000;
        Socket socket = new Socket(url,port);
        while(true){
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
        }
    }
}
