package com.bogdan.chat.servlets;

import com.bogdan.chat.dto.ChatRoomInfoDTO;
import com.bogdan.chat.dto.ParticipantDTO;
import com.bogdan.chat.dto.UserInfoDTO;
import com.bogdan.chat.service.impl.ParticipantServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ParticipantsRestServletTest extends AbstractRestServletTest {
    @Mock
    private ParticipantServiceImpl service;
    @InjectMocks
    private ParticipantsRestServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ParticipantDTO dto = new ParticipantDTO();

    @BeforeEach
    void setUp() {
        dto.setUserInfo(new UserInfoDTO());
        dto.setChatRoomInfo(new ChatRoomInfoDTO());
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
    }
    @Test
    void testInit() throws NoSuchMethodException {
        servlet.init();
        verify(helper, times(2)).registerPath(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(Method.class)
        );

        verify(helper).registerPath("/part/save","POST",
                servlet.getClass().getMethod("save",  HttpServletRequest.class, HttpServletResponse.class));
        verify(helper).registerPath("/part/delete","DELETE",
                servlet.getClass().getMethod("delete",  HttpServletRequest.class, HttpServletResponse.class));
    }

    @Test
    void testSave() throws IOException {
        String requestBody = "{\"userInfo\": {\"id\":null,\"username\":\"null\"},\"chatRoomInfo\": " +
                "{\"id\":null,\"name\":\"null\",\"description\":\"null\"}}";
        when(request.getReader()).thenAnswer(invocation -> new BufferedReader(new StringReader(requestBody)));
        when(mapper.readValue(requestBody, ParticipantDTO.class)).thenReturn(dto);
        when(service.save(any(ParticipantDTO.class))).thenReturn(dto);

        servlet.save(request,response);

        verify(service).save(Mockito.any(ParticipantDTO.class));
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json");
    }

    @Test
    void testDelete() throws IOException {
        String requestBody = "{\"userInfo\": {\"id\":1,\"username\":\"John Doe\"},\"chatRoomInfo\": " +
                "{\"id\":2,\"name\":\"General Chat\",\"description\":\"A general chat for all users\"}}";
        when(request.getReader()).thenAnswer(invocation -> new BufferedReader(new StringReader(requestBody)));
        when(mapper.readValue(requestBody, ParticipantDTO.class)).thenReturn(dto);
        when(service.delete(dto)).thenReturn(dto);

        servlet.delete(request,response);

        verify(service).delete(Mockito.any(ParticipantDTO.class));
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json");
    }

}
