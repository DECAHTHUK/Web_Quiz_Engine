package engine.presentation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.Mappers.QuizMapper;
import engine.business.Quiz;
import engine.business.QuizService;
import engine.business.UserDetailsServiceImpl;
import engine.dto.QuizDto;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import engine.response.GuessingResponce;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class QuizController {
    @Autowired
    QuizMapper quizMapper;

    @Autowired
    QuizService quizService;

    @Autowired
    UserDetailsServiceImpl userService;

    @PostMapping(value = "/quizzes", produces = MediaType.APPLICATION_JSON_VALUE)
    public Quiz postQuiz(@Valid @RequestBody QuizDto quiz, @AuthenticationPrincipal UserDetails details) {
        Quiz responce = quizService.save(quizMapper.toQuiz(quiz));
        userService.addNewQuizToUser(details.getUsername(), responce.getId());
        return responce;
    }

    @GetMapping(value = "/quizzes/{id}")
    public String getQuiz(@PathVariable long id) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String postAsString = objectMapper.writeValueAsString(quizService.getQuizById(id));
        return postAsString;
    }

    @GetMapping(value = "/quizzes")
    public String getAllQuizzes(@RequestParam int page) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String postAsString = objectMapper.writeValueAsString(quizService.findAll(page, 10, "id"));
        return postAsString;
    }

    @PostMapping(value = "/quizzes/{id}/solve", produces = MediaType.APPLICATION_JSON_VALUE)
    public GuessingResponce postAnswer(@PathVariable long id, @RequestBody Map<String, ArrayList<Integer>> answer,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        GuessingResponce responce = quizService.postAnswer(id, answer.get("answer"));
        if (responce.isSuccess()) {userService.addNewCompletedQuizToUser(userDetails.getUsername(), id);}
        System.out.println(id + "  " + userDetails.getUsername());
        return responce;
    }

    @GetMapping(value = "/quizzes/completed")
    public String getCompletedQuizzes(@RequestParam int page, @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String postAsString = objectMapper.writeValueAsString(userService.getAllCompletedQuizzes(userDetails.getUsername(), page, 9));
        System.out.println("fl;epofjopgjp  " + userDetails.getUsername());
        return postAsString;
    }

    @DeleteMapping("/quizzes/{id}")
    public void deleteQuiz(@PathVariable long id, @AuthenticationPrincipal UserDetails userDetails) {
        if (quizService.getQuizById(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        else if (!userService.findUserByEmail(userDetails.getUsername()).isIdValid(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        quizService.deleteQuizById(id);
        throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }
}
