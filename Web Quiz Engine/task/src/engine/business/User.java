package engine.business;

import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@Entity
@Table(name = "User")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotBlank
    @Pattern(regexp = ".+@.+\\..+")
    private String email;

    @NotBlank
    @Length(min = 5)
    private String password;

    @ElementCollection
    private List<Long> quizzesList;

    @ElementCollection
    private Map<Long, ArrayList<String>> completedQuizzes = new TreeMap<Long, ArrayList<String>>();

    public void addNewQuiz(Long idd) {
        quizzesList.add(idd);
    }

    public void addNewCompletedQuiz(Long idd) {
        if (!completedQuizzes.containsKey(idd)) {
            completedQuizzes.put(idd, new ArrayList<>());
        }
        completedQuizzes.get(idd).add(LocalDateTime.now().toString());
    }

    public boolean isIdValid(Long id) {
        for (Long idd : quizzesList) {
            if (Objects.equals(idd, id)) {
                return true;
            }
        }
        return false;
    }
}
