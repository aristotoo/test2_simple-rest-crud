package com.bogdan.chat.service.impl;

import com.bogdan.chat.dao.ParticipantsDao;
import com.bogdan.chat.dao.factory.DaoProviderFactory;
import com.bogdan.chat.dao.impl.ParticipantsDaoImpl;
import com.bogdan.chat.dto.ParticipantDTO;
import com.bogdan.chat.mapping.Mapper;
import com.bogdan.chat.mapping.ParticipantMapper;
import com.bogdan.chat.model.Participation;
import com.bogdan.chat.service.ParticipantService;

public class ParticipantServiceImpl implements ParticipantService {
    private DaoProviderFactory factory;
    private Mapper<Participation,ParticipantDTO> mapper;
    private ParticipantsDao dao;

    public ParticipantServiceImpl(){
        factory = new DaoProviderFactory();
        this.dao = factory.getParticipantDao();
        this.mapper = new ParticipantMapper();
    }

    @Override
    public ParticipantDTO save(ParticipantDTO dto){
        Participation participation = mapper.create(dto);
        Participation save = dao.save(participation);
        return mapper.map(save);
    }

    @Override
    public ParticipantDTO delete(ParticipantDTO dto){
        Participation participation = mapper.create(dto);
        Participation delete = dao.delete(participation);
        return mapper.map(delete);
    }
}
