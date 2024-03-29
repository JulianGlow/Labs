package com.hub.social.domain;

import java.util.UUID;

public interface GetMessageByIdQuery extends Query<Message> {
    public void setMessageId(UUID messageId);
}
