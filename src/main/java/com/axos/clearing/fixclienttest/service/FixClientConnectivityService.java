package com.axos.clearing.fixclienttest.service;

import com.axos.clearing.fixclienttest.FixClientTestApplication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;

@ShellComponent
@RequiredArgsConstructor
@Slf4j
public class FixClientConnectivityService {

    private final FixClientTestApplication fixClientTestApplication;


    @ShellMethod(key = "setup", value = "Setup a connection to the specified host and port.")
    public void setup(final String host, final String port) {
        if (null != fixClientTestApplication.socket.get()) {
            log.warn("Socket already connected to host: {}, port: {}. Please 'disconnect' and 'setup' new connection",
                    fixClientTestApplication.socket.get().getInetAddress().getHostAddress(),
                    fixClientTestApplication.socket.get().getPort());
            return;
        }

        log.info("Setting up connection to host: {}, port: {}", host, port);
        try {
            final SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(host, Integer.parseInt(port)));
            socketChannel.configureBlocking(false);
            fixClientTestApplication.socket.set(socketChannel.socket());
        } catch (Exception e) {
            log.error("Error setting up connection to host: {}, port: {}", host, port, e);
            return;
        }
        log.info("Connection established to host: {}, port: {}", host, port);
    }

    @ShellMethod(key = "status", value = "Check the connection status.")
    public void status() {
        log.info("Connection status: {}", fixClientTestApplication.socket.get() != null ? "Connected" : "Not connected");
        if (null != fixClientTestApplication.socket.get()) {
            log.info("Connection {} bound", fixClientTestApplication.socket.get().isBound() ? "" : "not");
            log.info("Host: {}, port: {}",
                    fixClientTestApplication.socket.get().getInetAddress().getHostAddress(),
                    fixClientTestApplication.socket.get().getPort());
        }
    }

    @ShellMethod(key = "disconnect", value = "Disconnect from the current connection.")
    public void disconnect() {
        if (null != fixClientTestApplication.socket.get()) {
            try {
                fixClientTestApplication.socket.get().close();
            } catch (Exception e) {
                log.error("Error disconnecting from host: {}, port: {}",
                        fixClientTestApplication.socket.get().getInetAddress().getHostAddress(),
                        fixClientTestApplication.socket.get().getPort(), e);
                return;
            }
            log.info("Disconnected from host: {}, port: {}",
                    fixClientTestApplication.socket.get().getInetAddress().getHostAddress(),
                    fixClientTestApplication.socket.get().getPort());
        } else {
            log.warn("No connection to disconnect");
        }

        fixClientTestApplication.senderExecutor.shutdownNow();
        fixClientTestApplication.receiverExecutor.shutdownNow();
        fixClientTestApplication.socket.set(null);
    }

    public Socket getSocket() {
        if (null == fixClientTestApplication.socket.get() || fixClientTestApplication.socket.get().isClosed() || !fixClientTestApplication.socket.get().isConnected()) {
            log.error("Socket is not connected");
            return null;
        }
        return fixClientTestApplication.socket.get();
    }

    public void send(final String message) {
        if (null == fixClientTestApplication.socket.get()) {
            log.error("Socket is not connected");
            return;
        }
        fixClientTestApplication.sentQueue.add(message);
    }


}