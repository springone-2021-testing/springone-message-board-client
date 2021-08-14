package com.example.message.board.springone;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class MessageBoardClient {


    private final RestTemplate restTemplate;
    private final String hostname;
    private final int port;

    public MessageBoardClient(RestTemplate restTemplate,
                     @Value("${message-board.host}") String hostname,
                     @Value("${message-board.port}") int port) {
        this.restTemplate = restTemplate;
        this.hostname = hostname;
        this.port = port;
    }


    public Result addMessage(String user, String text) {
//        throw new UnsupportedOperationException("Not yet implemented");

//        return this.restTemplate.getForObject("http://" + this.hostname +
//                ":" + this.port + "/cats/{name}", Cat.class, name);


        HttpEntity<CreateMessage> request = new HttpEntity<>(new CreateMessage(user,text));
        return restTemplate.postForObject("http://" + this.hostname +
                ":" + this.port + "/message", request, Result.class);

    }
}
