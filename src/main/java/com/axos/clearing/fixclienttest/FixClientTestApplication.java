package com.axos.clearing.fixclienttest;

import io.micrometer.common.util.StringUtils;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class FixClientTestApplication {

    public final AtomicReference<Socket> socket = new AtomicReference<>();
    public final ExecutorService senderExecutor = Executors.newSingleThreadExecutor();
    public final ExecutorService receiverExecutor = Executors.newSingleThreadExecutor();
    public final List<String> sentQueue = new LinkedList<>();
    public final AtomicReference<String> senderCompID = new AtomicReference<>();
    public final AtomicReference<String> targetCompID = new AtomicReference<>();
    public final AtomicInteger msgSeqNum = new AtomicInteger(0);
    public final AtomicReference<quickfix.fix42.Message> message42 = new AtomicReference<>(new quickfix.fix42.Message());
    public final AtomicReference<quickfix.fix44.Message> message44 = new AtomicReference<>(new quickfix.fix44.Message());
    public final AtomicInteger fixType = new AtomicInteger();

    private final Runnable runSender=() -> {
        while (true){
            try {
                if(socket.get() == null|| socket.get().isClosed() || !socket.get().isConnected()) continue;
                if (sentQueue.isEmpty()) continue;

                final String senderMsg = sentQueue.remove(0);
                if (senderMsg == null) continue;

                log.info("Sending message: {}", senderMsg);

                final SocketChannel socketChannel = socket.get().getChannel();
                ByteBuffer buffer = ByteBuffer.wrap((senderMsg + "\n").getBytes(StandardCharsets.US_ASCII));
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
                log.info("SENT >>> [{}]", senderMsg);
            } catch (Exception e) {
                log.error("Error sending message", e);
            }
        }
    };


    private final Runnable runReceiver = () -> {
        while (true) {
            try {
                if(socket.get() == null|| socket.get().isClosed() || !socket.get().isConnected()) continue;

                final SocketChannel socketChannel = socket.get().getChannel();
                final ByteBuffer responseBuffer = ByteBuffer.allocate(4096);
                int bytesRead = socketChannel.read(responseBuffer);
                if (bytesRead > 0) {
                    responseBuffer.flip();
                    final String response = StandardCharsets.US_ASCII.decode(responseBuffer).toString();
                    if (StringUtils.isNotBlank(response)) {
                        log.info("RECEIVE <<< [{}]", response);
                    }
                }
            } catch (Exception e) {
                log.error("Error reading from socket", e);
            }
        }
    };


    public static void main(String[] args) {
        SpringApplication.run(FixClientTestApplication.class, args);
    }

    @PostConstruct
    public void run() throws Exception {
        log.info("FixClientConnectivityService started.............");
        senderExecutor.execute(runSender);
        receiverExecutor.execute(runReceiver);
    }
}
