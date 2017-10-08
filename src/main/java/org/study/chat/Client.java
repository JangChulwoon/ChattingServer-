package org.study.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.Socket;

@Slf4j
public class Client {
    public static void main(String[] args) throws IOException {
        String url = "localhost";
        int port = 9000;
        Socket socket = new Socket(url,port);

        log.info(""+socket.getPort());
    }
}
