package springone.messageboardclient;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class ClientService {

    private final RestTemplate restTemplate;

    @Setter
    @Value("${message-board.baseUrl:http://localhost:8080}")
    private String baseUrl;

    public ClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    Result addMessage(String firstname, String lastname, String text) {

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<CreateMessage> request = new HttpEntity<CreateMessage>(new CreateMessage(firstname, lastname, text), headers);
        return restTemplate.postForObject(baseUrl + "/message", request, Result.class);

    }

    public List<Message> getMessages() {

        // Dynamically create type for List<Message> - required to properly
        // parse the response for a composite class in an HTTP exchange
        var ptr = new ParameterizedTypeReference<List<Message>>() {
        };
        var exchange =
                this.restTemplate.exchange(baseUrl + "/message", HttpMethod.GET, null, ptr);
        return exchange.getBody();

    }

}
