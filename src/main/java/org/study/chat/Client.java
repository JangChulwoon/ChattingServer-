package org.study.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@Slf4j
public class Client {
    final static String URL = "localhost";
    final static int PORT = 9000;

    public static void main(String[] args) throws IOException {
        ClientSocket clientSocket = new ClientSocket(URL, PORT);
        ExecutorService es = Executors.newFixedThreadPool(2);

        // receive
        FutureTask receiveTask = new CallbackFutureTask(() -> {
            clientSocket.receive();
            return "";
        }, () -> {
            //close ..
        });

        // send
        FutureTask sendTask = new CallbackFutureTask(() -> {
            clientSocket.send();
            return "";
        }, () -> {
            // close ..
            // How can I close Connection .. ?
        });


        es.execute(sendTask);
        es.execute(receiveTask);


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