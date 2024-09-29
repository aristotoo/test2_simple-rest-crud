package com.bogdan.chat.servlets;

import com.bogdan.chat.dto.ChatRoomDTO;
import com.bogdan.chat.service.ChatRoomService;
import com.bogdan.chat.service.impl.ChatRoomServiceImpl;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/chat/*")
public class ChatRoomRestServlet extends AbstractRestServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChatRoomRestServlet.class);
    private ChatRoomService chatRoomService;

    @Override
    public void init() {
        addPathToURLHandler();
        this.chatRoomService = new ChatRoomServiceImpl();
    }

    public void getAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        mapper.writeValue(response.getOutputStream(),chatRoomService.getAll());
    }

    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ChatRoomDTO chatRoomDTO = readValueFromRequest(request, ChatRoomDTO.class);
        mapper.writeValue(response.getOutputStream(), chatRoomService.save(chatRoomDTO));
    }

    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ChatRoomDTO chatRoomDTO = readValueFromRequest(request, ChatRoomDTO.class);
        mapper.writeValue(response.getOutputStream(), chatRoomService.update(chatRoomDTO));
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ChatRoomDTO chatRoomDTO = readValueFromRequest(request, ChatRoomDTO.class);
        mapper.writeValue(response.getOutputStream(), chatRoomService.delete(chatRoomDTO));
    }

    public void findById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] requestURI = request.getRequestURI().split("/");
        String id = requestURI[requestURI.length - 1];
        Optional<ChatRoomDTO> byId = chatRoomService.findById(Long.parseLong(id));
        if(byId.isPresent()){
            mapper.writeValue(response.getOutputStream(),byId.get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Object not found");
        }
    }
    private void addPathToURLHandler() {
        try {
            registerPath("/chat/all","GET",this.getClass()
                    .getMethod("getAll", HttpServletRequest.class, HttpServletResponse.class));
            registerPath("/chat/save","POST",this.getClass()
                    .getMethod("save",  HttpServletRequest.class, HttpServletResponse.class));
            registerPath("/chat/upd", "PUT",this.getClass()
                    .getMethod("update",  HttpServletRequest.class, HttpServletResponse.class));
            registerPath("/chat/delete","DELETE",this.getClass()
                    .getMethod("delete",  HttpServletRequest.class, HttpServletResponse.class));
            registerPath("/chat/(?<userId>\\d+)","GET",this.getClass()
                    .getMethod("findById",  HttpServletRequest.class, HttpServletResponse.class));
        } catch (NoSuchMethodException e) {
            LOGGER.debug(e.getMessage());
        }
    }
}