package org.study.netty;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

@Slf4j
public class NonBlockingServer {

    private Map<SocketChannel, List<byte[]>> keepDataTrack = new HashMap<>();
    // ByteBuffer ?  ByteBuffer 만 Direct Buffer를 지원한다.  Kerner Buffer에 접근하기 위해 이걸 써야한다.
    // allocate 을 쓰면 그냥 일반 버퍼 , allocateDirect를 사용해야한다.
    private ByteBuffer buffer = ByteBuffer.allocate(2 * 1024);

    private void statrtEchoServer() {
        try (
                Selector selector = Selector.open();
                // nonblocking socket Channel
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()
        ) {
            if (serverSocketChannel.isOpen() && selector.isOpen()) {
                // default 값이 true 이기에, non-blocking을 하려면 false로 지정 해야한다.
                serverSocketChannel.configureBlocking(false);
                // 포트 지정 후, channel에 할당
                serverSocketChannel.bind(new InetSocketAddress("localhost", 9000));
                // 이벤트를 등록한다?
                serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                log.info("접속 대기.... ");

                while (true) {
                    selector.select(); // blocking
                    // selector 에서 등록된 채널 중, I/O 이벤트가 발생한 채널들의 목록을 조회한다.
                    Iterator<SelectionKey> keys = selector.selectedKeys().iterator();

                    while (keys.hasNext()) {
                        SelectionKey key = (SelectionKey) keys.next();
                        keys.remove();

                        if (!key.isValid()) {
                            continue;
                        }

                        if (key.isAcceptable()) {
                            this.acceptOp(key, selector);
                        } else if (key.isReadable()) {
                            this.readOP(key);
                        } else if (key.isWritable()) {
                            this.write(key);
                        }
                    }
                }
            } else {
                log.info("can't make Sever socket ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        Iterator<byte[]> its = channelData.iterator();

        while (its.hasNext()) {
            byte[] it = its.next();
            its.remove();
            socketChannel.write(ByteBuffer.wrap(it));
        }
        key.interestOps(SelectionKey.OP_READ);
    }

    private void acceptOp(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);

        log.info("Success Connection");

        keepDataTrack.put(socketChannel, new ArrayList<>());
        // 읽기 위한 이벤트 등록.
        serverSocketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void readOP(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            buffer.clear();
            int numRead = -1;
            try {
                // 쓰기전에 버퍼 비우고 씀
                numRead = socketChannel.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (numRead == -1) {
                this.keepDataTrack.remove(socketChannel);
                log.info("close Socket Connection " + socketChannel.getRemoteAddress());
                socketChannel.close();
                key.cancel();
                return;
            }
            byte[] data = new byte[numRead];
            System.arraycopy(buffer.array(), 0, data, 0, numRead);
            log.info(new String(data, "UTF-8") + "from" + socketChannel.getRemoteAddress());

            doEchoJob(key, data);
        } catch (IOException o) {
            o.printStackTrace();
        }
    }

    private void doEchoJob(SelectionKey key, byte[] data) {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        List<byte[]> channelData = keepDataTrack.get(socketChannel);
        channelData.add(data);

        key.interestOps(SelectionKey.OP_WRITE);
    }

    public static void main(String[] args) {
        NonBlockingServer main = new NonBlockingServer();
        main.statrtEchoServer();
    }


}
