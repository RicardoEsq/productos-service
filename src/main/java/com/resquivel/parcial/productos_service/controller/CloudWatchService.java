package com.resquivel.parcial.productos_service.controller;

import java.util.Collections;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.logs.AWSLogs;
import com.amazonaws.services.logs.AWSLogsClientBuilder;
import com.amazonaws.services.logs.model.CreateLogStreamRequest;
import com.amazonaws.services.logs.model.InputLogEvent;
import com.amazonaws.services.logs.model.PutLogEventsRequest;

@Service
public class CloudWatchService {

    @Value("${aws.cloudwatch.endpoint:http://localhost:4566}")
    private String endpoint;

    @Value("${aws.region:us-east-1}")
    private String region;

    private static final String LOG_GROUP = "producto-log-group";

    public void enviarLog(String mensaje) {
        AWSLogs awsLogs = AWSLogsClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials("test", "test")))
                .build();

        String streamName = "producto-" + UUID.randomUUID().toString().substring(0, 8);
        
        try {
            awsLogs.createLogStream(new CreateLogStreamRequest(LOG_GROUP, streamName));

            InputLogEvent evento = new InputLogEvent()
                    .withMessage(mensaje)
                    .withTimestamp(System.currentTimeMillis());

            PutLogEventsRequest request = new PutLogEventsRequest()
                    .withLogGroupName(LOG_GROUP)
                    .withLogStreamName(streamName)
                    .withLogEvents(Collections.singletonList(evento));

            awsLogs.putLogEvents(request);
        } catch (Exception e) {
            System.err.println("Error al enviar log a CloudWatch: " + e.getMessage());
        }
    }
}
