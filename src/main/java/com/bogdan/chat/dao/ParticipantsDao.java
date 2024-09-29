package com.bogdan.chat.dao;

import com.bogdan.chat.model.Participation;
import com.bogdan.chat.model.User;

import java.util.Set;

public interface ParticipantsDao {
    Set<User> getParticipants(long id);
    Participation save(Participation participation);
    Participation delete(Participation participation);
    Participation deleteAll(Participation participation);

}
