package springone.messageboardclient;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ClientService {

    private final RestTemplate restTemplate;

    private CircuitBreakerFactory circuitBreakerFactory;

    @Setter
    @Value("${message-board.baseUrl:http://localhost:8080}")
    private String baseUrl;

    public ClientService(RestTemplate restTemplate, CircuitBreakerFactory circuitBreakerFactory) {
        this.restTemplate = restTemplate;
        this.circuitBreakerFactory = circuitBreakerFactory;
    }

    Result addMessage(String firstname, String lastname, String text) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<CreateMessage> request = new HttpEntity<CreateMessage>(new CreateMessage(firstname, lastname, text), headers);

        return circuitBreakerFactory.create("addMessageCircuitBreaker").run(() ->
                        this.restTemplate.postForObject(
                                baseUrl + "/message",
                                request,
                                Result.class),
                throwable -> {
                    // For backward compatibility
                    String username = firstname + (lastname.trim().isBlank() ? "" : "_") + lastname;
                    HttpEntity<CreateMessageWithUsername> backCompatibleRequest = new HttpEntity<CreateMessageWithUsername>(new CreateMessageWithUsername(username, text), headers);
                    return this.restTemplate.postForObject(
                            baseUrl + "/message",
                            backCompatibleRequest,
                            Result.class);
                });

    }

    public List<Message> getMessages() {

        // Dynamically create type for List<Message> - required to properly
        // parse the response for a composite class in an HTTP exchange
        var ptr = new ParameterizedTypeReference<List<Message>>() {
        };

        return circuitBreakerFactory.create("getMessagesCircuitBreaker").run(() ->
                    this.restTemplate.exchange(
                            baseUrl + "/message",
                            HttpMethod.GET,
                            null,
                            ptr)
                            .getBody(),
            throwable -> {
                // For back compatibility
                var ptrWithUsername = new ParameterizedTypeReference<List<MessageWithUsername>>() {
                };
                List<MessageWithUsername> result = this.restTemplate.exchange(
                        baseUrl + "/message",
                        HttpMethod.GET,
                        null,
                        ptrWithUsername)
                        .getBody();

                List<Message> result2 = new ArrayList<Message>();
                for (MessageWithUsername message : result) {
                    result2.add(message.getAsMessage());
                }
                return result2;

            });

    }

}
