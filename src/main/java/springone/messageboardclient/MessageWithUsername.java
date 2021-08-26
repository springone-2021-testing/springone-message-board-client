package springone.messageboardclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageWithUsername {

    // For backward compatibility
    private int id;
    private String username;
    private String text;

    Message getAsMessage() {
        String[] s = this.username.split("_");
        return new Message(this.getId(), s[0], s[1], this.getText());
    }

}
