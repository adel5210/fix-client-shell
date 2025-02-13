package com.axos.clearing.fixclienttest.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.lang.reflect.Method;

/**
 * @author Adel.Albediwy
 */
@RequiredArgsConstructor
@Slf4j
@ShellComponent
public class FixCommandHelpService {
    private final FixClientConnectivityService fixClientConnectivityService;
    private final FixClientMessageService fixClientMessageService;
    @ShellMethod(key = "help")
    public void help() {
        logCommands(fixClientConnectivityService.getClass());
        logCommands(fixClientMessageService.getClass());
    }

    private void logCommands(Class<?> serviceClass) {
        for (Method method : serviceClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(ShellMethod.class)) {
                ShellMethod shellMethod = method.getAnnotation(ShellMethod.class);
                String command = String.join(", ", shellMethod.key());
                String description = shellMethod.value();
                log.info("Command: {}, Description: {}", command, description);
            }
        }
    }
}
