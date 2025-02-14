package com.axos.clearing.fixclienttest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

@SpringBootTest
@Slf4j
public class FixServerTest {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        try (final ServerSocket serverSocket = new ServerSocket(12568)) {

            while (true) {
                final Socket socket = serverSocket.accept();
                log.info("New client connected: {}", socket);

                new Thread(() -> {
                    try (final BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                         final BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                        String msg;
                        while (null != (msg = in.readLine())) {
                            System.out.println("ECHO RECEIVE <<< "+ msg);
                            out.write("ECHO SENT >>>  " + msg);
                            out.flush();
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
