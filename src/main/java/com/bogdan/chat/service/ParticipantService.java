package com.bogdan.chat.service;

import com.bogdan.chat.dto.ParticipantDTO;

public interface ParticipantService {
    ParticipantDTO save(ParticipantDTO dto);
//    ParticipantDTO update(ParticipantDTO dto);
    ParticipantDTO delete(ParticipantDTO dto);
}
