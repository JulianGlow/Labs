package com.hub.social.rest;

import java.util.UUID;
import javax.validation.constraints.NotNull;

import com.hub.social.domain.Message;
import com.hub.social.domain.MessageRepository;
import com.hub.social.domain.User;
import com.hub.social.domain.UserRepository;

public class DeleteMessageCommand {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @NotNull
    private UUID userId;

    @NotNull
    private UUID messageId;

    public DeleteMessageCommand(UserRepository userRepository, MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    public void execute() throws UserNotFoundException, MessageNotFoundException {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Message message = messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);

        user.unpublish(message);
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }
}
