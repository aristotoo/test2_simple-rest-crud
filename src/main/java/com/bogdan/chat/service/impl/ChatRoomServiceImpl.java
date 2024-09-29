package com.bogdan.chat.service.impl;

import com.bogdan.chat.dao.ChatRoomDao;
import com.bogdan.chat.dao.ParticipantsDao;
import com.bogdan.chat.dao.factory.DaoProviderFactory;
import com.bogdan.chat.dto.ChatRoomDTO;
import com.bogdan.chat.mapping.ChatRoomMapper;
import com.bogdan.chat.mapping.Mapper;
import com.bogdan.chat.model.ChatRoom;
import com.bogdan.chat.model.Participation;
import com.bogdan.chat.service.ChatRoomService;

import java.util.List;
import java.util.Optional;

public class ChatRoomServiceImpl implements ChatRoomService {

    private DaoProviderFactory daoFactory;
    private ChatRoomDao dao;
    private ParticipantsDao participantsDao;
    private Mapper<ChatRoom,ChatRoomDTO> mapper;

    public ChatRoomServiceImpl(){
        this.daoFactory = new DaoProviderFactory();
        this.dao = daoFactory.getChatDao();
        this.participantsDao = daoFactory.getParticipantDao();
        this.mapper = new ChatRoomMapper();
    }
    @Override
    public Optional<ChatRoomDTO> findById(Long id) {
        Optional<ChatRoom> byId = dao.findById(id);
        Optional<ChatRoomDTO> result = Optional.empty();
        if(byId.isPresent()){
            ChatRoom chatRoom = byId.get();
            chatRoom.addParticipants(participantsDao.getParticipants(id));
            result = Optional.of(mapper.map(chatRoom));
        }
        return result;
    }

    @Override
    public ChatRoomDTO save(ChatRoomDTO chatRoomDTO) {
        ChatRoom save = dao.save(mapper.create(chatRoomDTO));
        return mapper.map(save);
    }

    @Override
    public ChatRoomDTO update(ChatRoomDTO chatRoomDTO) {
        ChatRoom update = dao.update(mapper.create(chatRoomDTO));
        return mapper.map(update);
    }

    @Override
    public ChatRoomDTO delete(ChatRoomDTO chatRoomDTO) {
        ChatRoom chatRoom = mapper.create(chatRoomDTO);
        Participation participation = new Participation.ParticipationBuilder()
                .setChatRoom(chatRoom)
                .build();
        participantsDao.deleteAll(participation);
        ChatRoom delete = dao.delete(chatRoom);
        return mapper.map(delete);
    }

    @Override
    public List<ChatRoomDTO> getAll() {
        List<ChatRoom> chats = dao.getAll();
        if(!chats.isEmpty()){
            for(ChatRoom chatRoom : chats){
                chatRoom.addParticipants(participantsDao.getParticipants(chatRoom.getRoomId()));
            }
        }
        return chats.stream()
                .map(mapper::map)
                .toList();
    }
}
