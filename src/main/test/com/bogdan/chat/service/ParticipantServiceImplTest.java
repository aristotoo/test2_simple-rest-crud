package com.bogdan.chat.service;

import com.bogdan.chat.dao.impl.ParticipantsDaoImpl;
import com.bogdan.chat.dto.ParticipantDTO;
import com.bogdan.chat.mapping.Mapper;
import com.bogdan.chat.model.Participation;
import com.bogdan.chat.service.impl.ParticipantServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParticipantServiceImplTest {
    @Mock
    private Mapper<Participation, ParticipantDTO> mapper;
    @Mock
    private ParticipantsDaoImpl dao;
    @InjectMocks
    private ParticipantServiceImpl service;

    private ParticipantDTO dto = new ParticipantDTO();
    private Participation participation = new Participation();

    @Test
    void testSave_shouldSaveParticipantAndReturnSavedParticipant() {
        when(mapper.create(dto)).thenReturn(participation);
        when(dao.save(participation)).thenReturn(participation);
        when(mapper.map(participation)).thenReturn(dto);

        ParticipantDTO save = service.save(dto);

        assertNotNull(save);
        verify(mapper, times(1)).create(dto);
        verify(dao, times(1)).save(participation);
        verify(mapper, times(1)).map(participation);

        assertEquals(dto, save);
    }

    @Test
    void testDeleteParticipant_shouldDeleteParticipantAndReturnDeletedParticipant() {
        when(mapper.create(dto)).thenReturn(participation);
        when(dao.delete(participation)).thenReturn(participation);
        when(mapper.map(participation)).thenReturn(dto);

        ParticipantDTO delete = service.delete(dto);

        assertNotNull(delete);
        verify(mapper, times(1)).create(dto);
        verify(dao, times(1)).delete(participation);
        verify(mapper, times(1)).map(participation);

        assertEquals(dto, delete);
    }
}

