package engine.business;

import engine.persistence.QuizRepository;
import engine.response.GuessingResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizService {
    private final QuizRepository quizRepository;

    @Autowired
    public QuizService(QuizRepository quizRepository) {this.quizRepository = quizRepository;}

    public Quiz save(Quiz toSave) {
        return quizRepository.save(toSave);
    }

    public Quiz getQuizById(Long id) {
        Quiz quiz = quizRepository.getQuizById(id);
        if (quiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return quiz;
    }

    public boolean isEmpty() {
        return quizRepository.count() == 0;
    }

    public Page<Quiz> findAll(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return quizRepository.findAll(paging);
    }

    public GuessingResponce postAnswer(long id, ArrayList<Integer> answer) {
        Quiz quiz = quizRepository.getQuizById(id);
        if (quiz == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        answer.sort(Integer::compareTo);
        if (answer.equals(quiz.getAnswer())) {
            return new GuessingResponce(true, "Congratulations, you're right!");
        }
        else {
            return new GuessingResponce(false, "Wrong answer! Please, try again.");
        }
    }

    public void deleteQuizById(Long id) {quizRepository.delete(getQuizById(id));}
}
