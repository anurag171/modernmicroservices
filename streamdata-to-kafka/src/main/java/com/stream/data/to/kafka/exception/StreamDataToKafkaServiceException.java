package com.stream.data.to.kafka.exception;

public class StreamDataToKafkaServiceException extends RuntimeException {

    public StreamDataToKafkaServiceException() {
        super();
    }

    public StreamDataToKafkaServiceException(String message) {
        super(message);
    }

    public StreamDataToKafkaServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
