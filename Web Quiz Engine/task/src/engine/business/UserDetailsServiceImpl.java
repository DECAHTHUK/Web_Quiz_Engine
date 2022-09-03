package engine.business;

import engine.persistence.UserRepository;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepo;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepo = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Not found: " + email);
        }

        return new UserDetailsImpl(user);
    }

    public User findUserByEmail(String email) {
        return userRepo.findUserByEmail(email);
    }

    public void addNewQuizToUser(String email, long id) {
        User user = userRepo.findUserByEmail(email);
        user.addNewQuiz(id);
        userRepo.save(user);
    }

    public void addNewCompletedQuizToUser(String email, long id) {
        User user = userRepo.findUserByEmail(email);
        user.addNewCompletedQuiz(id);
        userRepo.save(user);
    }

    public Page<CompletedQuiz>  getAllCompletedQuizzes(String email, Integer pageNo, Integer pageSize) {
        Pageable paging = PageRequest.of(pageNo, pageSize);
        List<User> users = userRepo.findAll();
        User user = new User();
        for (User u : users) {
            if (Objects.equals(u.getEmail(), email)) {
                user = u;
            }
        }
        Map<Long, ArrayList<String>> found = user.getCompletedQuizzes();
        List<CompletedQuiz> lala = new ArrayList<>();
        for (Map.Entry<Long, ArrayList<String>> entry : found.entrySet()) {
            for (String a : entry.getValue()) {
                lala.add(new CompletedQuiz(entry.getKey(), a));
            }
        }
        lala.sort(Comparator.comparing(CompletedQuiz::getCompletedAt));
        Collections.reverse(lala);
        long pageOffset = paging.getOffset();
        long total = pageOffset + lala.size() + (lala.size() == pageSize ? pageSize : 0);
        System.out.println(pageNo + "   " + lala.size());
        Page<CompletedQuiz> out = new PageImpl<>(getSublist(lala, pageNo), paging, total);
        return out;
    }

    public List<CompletedQuiz> getSublist(List<CompletedQuiz> list, int pageNo) {
        System.out.println(pageNo + "   " + list.size());
        int sum = list.size();
        int count = 0;
        while (sum - 10 > 0) {
            if (count == pageNo) {
                return list.subList(count * 10, count * 10 + 10);
            }
            count++;
            sum -= 10;
        }
        return list.subList(count * 10, count * 10 + sum);
    }
}
