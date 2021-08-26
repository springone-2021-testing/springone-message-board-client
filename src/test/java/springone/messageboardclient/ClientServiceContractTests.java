package springone.messageboardclient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.StubFinder;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.List;

@AutoConfigureStubRunner(ids = "springone:message-board-contracts:3.0.0",
        stubsMode = StubRunnerProperties.StubsMode.LOCAL)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE)
public class ClientServiceContractTests {

    @Autowired
    private ClientService service;

    @Autowired
    StubFinder stubFinder;

    @BeforeEach
    public void setup() {
        this.service.setBaseUrl("http://localhost:" + stubFinder.findStubUrl("message-board-contracts").getPort());
    }

    @Test
    void shouldAddMessage() {
        Result result = this.service.addMessage("Cora","Iberkleid","Welcome to everyone!");
        Assertions.assertEquals(result.getParameter(), "1");
        Assertions.assertEquals(result.getOperation(), "Create");
        Assertions.assertEquals(result.getStatus(), "Success");
    }

    @Test
    void shouldNotAddMessage() {
        Result result = this.service.addMessage("andy","", "I am here too!");
        Assertions.assertEquals(result.getParameter(), "0");
        Assertions.assertEquals(result.getOperation(), "Create");
        Assertions.assertEquals(result.getStatus(), "Failure: First and Last Name are required");
    }

    @Test
    void shouldGetMessages() {
        List<Message> messageList = this.service.getMessages();
        Assertions.assertEquals(messageList.get(0).getId(), 1);
        Assertions.assertEquals(messageList.get(0).getFirstname(), "Cora");
        Assertions.assertEquals(messageList.get(0).getLastname(), "Iberkleid");
        Assertions.assertEquals(messageList.get(0).getText(), "Welcome to everyone!");
    }

}
