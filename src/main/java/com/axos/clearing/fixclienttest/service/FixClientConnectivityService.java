package com.axos.clearing.fixclienttest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class FixClientConnectivityService {

    private final AtomicReference<Socket> socket = new AtomicReference<>();
    private final ExecutorService messageHandlerExecutor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<String> sentQueue = new LinkedBlockingQueue<>();

    @ShellMethod(key = "setup", value = "Setup a connection to the specified host and port.")
    public void setup(final String host, final String port) {
        if (null != socket.get()) {
            log.warn("Socket already connected to host: {}, port: {}. Please 'disconnect' and 'setup' new connection",
                    socket.get().getInetAddress().getHostAddress(),
                    socket.get().getPort());
            return;
        }

        log.info("Setting up connection to host: {}, port: {}", host, port);
        try {
            socket.set(new Socket(host, Integer.parseInt(port)));
            runExecutorLoop();
        } catch (Exception e) {
            log.error("Error setting up connection to host: {}, port: {}", host, port, e);
            return;
        }
        log.info("Connection established to host: {}, port: {}", host, port);
    }

    private void runExecutorLoop() {
        messageHandlerExecutor.execute(() -> {
            while (true) {
                try {
                    byte[] responseBuffer = new byte[4096];
                    final InputStream inputStream = socket.get().getInputStream();
                    int bytesRead = inputStream.read(responseBuffer);
                    if (bytesRead > 0) {
                        String response = new String(responseBuffer, 0, bytesRead, StandardCharsets.US_ASCII);
                        log.info("RECEIVE <<< {}", response);
                    }
                } catch (IOException e) {
                    log.error("Error reading from socket", e);
                }

                if (!sentQueue.isEmpty()) {
                    try {
                        final String senderMsg = sentQueue.take();
                        final OutputStream outputStream = socket.get().getOutputStream();
                        outputStream.write(senderMsg.getBytes(StandardCharsets.US_ASCII));
                        outputStream.flush();
                        log.info("SENT >>> {}", senderMsg);
                    } catch (InterruptedException | IOException e) {
                        log.error("Error sending message", e);
                    }
                }
            }
        });
    }

    @ShellMethod(key = "status", value = "Check the connection status.")
    public void status() {
        log.info("Connection status: {}", socket.get() != null ? "Connected" : "Not connected");
        if (null != socket.get()) {
            log.info("Connection {} bound", socket.get().isBound() ? "" : "not");
            log.info("Host: {}, port: {}",
                    socket.get().getInetAddress().getHostAddress(),
                    socket.get().getPort());
        }
    }

    @ShellMethod(key = "disconnect", value = "Disconnect from the current connection.")
    public void disconnect() {
        if (null != socket.get()) {
            try {
                socket.get().close();
            } catch (Exception e) {
                log.error("Error disconnecting from host: {}, port: {}",
                        socket.get().getInetAddress().getHostAddress(),
                        socket.get().getPort(), e);
                return;
            }
            log.info("Disconnected from host: {}, port: {}",
                    socket.get().getInetAddress().getHostAddress(),
                    socket.get().getPort());
        } else {
            log.warn("No connection to disconnect");
        }

        messageHandlerExecutor.shutdownNow();
        socket.set(null);
    }

    public Socket getSocket() {
        if (null == socket.get() || socket.get().isClosed() || !socket.get().isConnected()) {
            log.error("Socket is not connected");
            return null;
        }
        return socket.get();
    }

    public void send(final String message) {
        if (null == socket.get()) {
            log.error("Socket is not connected");
            return;
        }
        sentQueue.add(message);
    }
}