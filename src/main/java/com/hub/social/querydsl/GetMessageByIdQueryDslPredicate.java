package com.hub.social.querydsl;

import java.util.UUID;

import com.hub.social.domain.GetMessageByIdQuery;
import com.hub.social.domain.Message;
import com.hub.social.domain.MessageRepository;
import com.querydsl.core.types.Predicate;

public class GetMessageByIdQueryDslPredicate extends QueryDslPredicate<Message> implements GetMessageByIdQuery {
    private UUID messageId;

    public GetMessageByIdQueryDslPredicate(MessageRepository repository) {
        super(repository);
    }

    @Override
    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
    }

    @Override
    protected Predicate buildPredicate() {
        return MessageQueryFilters.messageId(messageId);
    }
}
