package com.endava.tteapp;

import org.springframework.boot.logging.LogLevel;

import java.time.LocalDateTime;

public class LoggerPrinter {
    private String applicationName;
    private String requestId;
    private String moduleName;
    private String additionalInfo;
    private LogLevel logLevel;
    private String message;
    private LocalDateTime timestamp;

    public LoggerPrinter(String applicationName, String requestId, String moduleName, String additionalInfo) {
        this.applicationName = applicationName;
        this.requestId = requestId;
        this.moduleName = moduleName;
        this.additionalInfo = additionalInfo;
        this.timestamp = LocalDateTime.now();
    }

    public void log(LogLevel logLevel, String message) {
        this.logLevel = logLevel;
        this.message = message;
        System.out.println(toString());
    }

    @Override
    public String toString() {
        return  "[" + timestamp + "] " +
                "[" + applicationName + "] " +
                "[" + requestId + "] " +
                "[" + moduleName + "] " +
                "[" + additionalInfo + "] " +
                "[" + logLevel + "] " +
                "[" + message + "]";
    }

}
