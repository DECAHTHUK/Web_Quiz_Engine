package engine.Mappers;

import engine.business.Quiz;
import engine.dto.QuizDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class QuizMapper {
    public Quiz toQuiz(QuizDto quizDto) {
        Quiz out = new Quiz();
        out.setTitle(quizDto.getTitle());
        out.setText(quizDto.getText());
        out.setOptions(quizDto.getOptions());
        if(quizDto.getAnswer() == null) {
            out.setAnswer(new ArrayList<>());
        } else {
            out.setAnswer(quizDto.getAnswer());
            out.getAnswer().sort(Integer::compareTo);
        }
        return out;
    }
}
