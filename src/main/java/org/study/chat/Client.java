package org.study.chat;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
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
            try {
                clientSocket.closeResource();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // send
        FutureTask sendTask = new CallbackFutureTask(() -> {
            clientSocket.send();
            return "";
        }, () -> {
            receiveTask.cancel(true);
        });

        es.execute(sendTask);
        es.execute(receiveTask);

        try {
            sendTask.get();
            receiveTask.get();
            es.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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