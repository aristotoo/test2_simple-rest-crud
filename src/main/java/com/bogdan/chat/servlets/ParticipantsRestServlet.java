package com.bogdan.chat.servlets;

import com.bogdan.chat.dto.ParticipantDTO;
import com.bogdan.chat.service.ParticipantService;
import com.bogdan.chat.service.impl.ParticipantServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/part/*")
public class ParticipantsRestServlet extends AbstractRestServlet{
    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantsRestServlet.class);
    private ParticipantService service;

    public ParticipantsRestServlet(ParticipantService service, PathHelper helper, ObjectMapper mapper) {
        super(mapper,helper);
        this.service = service;
    }

    public ParticipantsRestServlet(){
        this.service = new ParticipantServiceImpl();
    }

    @Override
    public void init() {
        try {
            helper.registerPath("/part/save","POST",this.getClass()
                    .getMethod("save",  HttpServletRequest.class, HttpServletResponse.class));
            helper.registerPath("/part/delete","DELETE",this.getClass()
                    .getMethod("delete",  HttpServletRequest.class, HttpServletResponse.class));
        } catch (NoSuchMethodException e) {
            LOGGER.debug(e.getMessage());
        }
    }

    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ParticipantDTO participant = readValueFromRequest(request, ParticipantDTO.class);
        mapper.writeValue(response.getOutputStream(), service.save(participant));
    }


    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ParticipantDTO participant = readValueFromRequest(request, ParticipantDTO.class);
        mapper.writeValue(response.getOutputStream(), service.delete(participant));
    }
}
