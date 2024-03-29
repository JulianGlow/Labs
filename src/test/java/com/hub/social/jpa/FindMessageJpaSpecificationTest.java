package com.hub.social.jpa;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.transaction.Transactional;

import com.hub.social.domain.FindMessageQuery;
import com.hub.social.domain.JpaTestConfiguration;
import com.hub.social.domain.Message;
import com.hub.social.domain.MessageRepository;
import com.hub.social.domain.User;
import com.hub.social.domain.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional
@ContextConfiguration(classes = JpaTestConfiguration.class)
public class FindMessageJpaSpecificationTest {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void authoredBy() throws Exception {
        User user = new User("username", "email@email.com");
        Message message = user.publish("Hello world");

        userRepository.save(user);

        FindMessageQuery query = new FindMessageJpaSpecification(messageRepository);
        query.setAuthor(Optional.of(user));

        assertEquals(message, query.findOne().get());
    }


    @Test
    public void authorName() throws Exception {
        User user = new User("username", "email@email.com");
        Message message = user.publish("Hello world");

        userRepository.save(user);

        FindMessageQuery query = new FindMessageJpaSpecification(messageRepository);
        query.setAuthorName(Optional.of(user.getUsername()));

        assertEquals(message, query.findOne().get());
    }

    @Test
    public void publishedBefore() throws Exception {
        User user = new User("username", "email@email.com");
        Message message = user.publish("Hello world");

        userRepository.save(user);

        FindMessageJpaSpecification query = new FindMessageJpaSpecification(messageRepository);
        query.setAuthor(Optional.of(user));
        query.setPublishedBefore(Optional.of(LocalDateTime.now().plusDays(1)));

        Optional<Message> maybeFound = query.findOne();

        assertEquals(message, maybeFound.get());
    }

    @Test
    public void publishedAfter() throws Exception {
        User user = new User("username", "email@email.com");
        Message message = user.publish("Hello world");

        userRepository.save(user);

        FindMessageJpaSpecification query = new FindMessageJpaSpecification(messageRepository);
        query.setAuthor(Optional.of(user));
        query.setPublishedAfter(Optional.of(LocalDateTime.now().minusDays(1)));

        Optional<Message> maybeFound = query.findOne();

        assertEquals(message, maybeFound.get());
    }

    @Test
    public void contains() {
        User user = new User("username", "email@email.com");
        user.publish("It's always sunny in Philadelphia");
        user.publish("Not matching");

        userRepository.save(user);

        FindMessageQuery query = new FindMessageJpaSpecification(messageRepository);
        query.setContains(Optional.of("SUNNY"));

        assertEquals(1, query.count());
        for (Message message : query.findAll()) {
            assertTrue(message.getBody().contains("sunny"));
        }
    }


    @Test
    public void shouldNotFindAny() {
        User user = new User("username", "email@email.com");
        user.publish("Hello world");

        userRepository.save(user);

        FindMessageQuery query = new FindMessageJpaSpecification(messageRepository);
        query.setAuthor(Optional.of(user));
        query.setPublishedBefore(Optional.of(LocalDateTime.now().minusDays(1)));
        Optional<Message> maybeFound = query.findOne();

        assertFalse(maybeFound.isPresent());
    }
}
