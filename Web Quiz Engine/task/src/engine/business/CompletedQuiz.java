package engine.business;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CompletedQuiz {
    private long id;
    private String completedAt;
}
