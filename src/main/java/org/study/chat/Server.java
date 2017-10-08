package org.study.chat;


import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(9000);
        log.info("Ready server");
        Socket client  = server.accept();
        log.info("Accept client");
        log.info(""+client.getPort());
    }
}
