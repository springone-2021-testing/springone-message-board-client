package com.example.message.board.springone;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.NONE)
@AutoConfigureStubRunner(ids = {"springone-demo:message-board:+:stubs:8080"},
		stubsMode = StubRunnerProperties.StubsMode.LOCAL)
public class SpringoneApplicationTests {

	@Autowired
	private MessageBoardClient client;

	@Test
	void clientShouldAddMessageToBoard() {
		var result = this.client.addMessage("Cora","Welcome to everyone!");
		Assertions.assertEquals(result.getParameter(), "1");
		Assertions.assertEquals(result.getOperation(), "Create");
		Assertions.assertEquals(result.getStatus(), "Success");
	}
}
