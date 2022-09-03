package engine.persistence;

import engine.business.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long>,
        PagingAndSortingRepository<Quiz, Long> {
    @Override
    Quiz save(Quiz quiz);
    Quiz getQuizByText(String text);
    Quiz getQuizById(Long id);
    List<Quiz> findAll();
}