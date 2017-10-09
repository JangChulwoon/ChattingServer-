package org.study.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class ClientSocket {
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
        return !"".equals(text) && text != null ;
    }


}


