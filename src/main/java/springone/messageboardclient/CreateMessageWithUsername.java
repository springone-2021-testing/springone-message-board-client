package springone.messageboardclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMessageWithUsername {

    // For backward compatibility
    private String username;
    private String text;

}
