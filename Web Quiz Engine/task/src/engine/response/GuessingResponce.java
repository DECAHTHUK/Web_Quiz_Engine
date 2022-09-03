package engine.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GuessingResponce {
    private boolean success;
    private String feedback;
}
