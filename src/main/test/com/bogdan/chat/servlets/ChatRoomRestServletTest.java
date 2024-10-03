package com.bogdan.chat.servlets;

import com.bogdan.chat.dto.ChatRoomDTO;
import com.bogdan.chat.dto.UserInfoDTO;
import com.bogdan.chat.service.ChatRoomService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChatRoomRestServletTest extends AbstractRestServletTest{
    @Mock
    private ChatRoomService service;
    @InjectMocks
    private ChatRoomRestServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ChatRoomDTO dto = new ChatRoomDTO.ChatRoomDTOBuilder()
            .setRoomId(1L)
            .setName("Test Chat")
            .setDescription("Test")
            .setCreateBy(new UserInfoDTO())
            .build();

    @BeforeEach
    void setUp() throws ServletException {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
    }

    @Test
    void testInit() throws NoSuchMethodException {
        servlet.init();
        verify(helper, times(5)).registerPath(
                Mockito.anyString(),
                Mockito.anyString(),
                Mockito.any(Method.class)
        );

        verify(helper).registerPath("/chat/all", "GET",
                servlet.getClass().getMethod("getAll", HttpServletRequest.class,
                HttpServletResponse.class));
        verify(helper).registerPath("/chat/save", "POST",
                servlet.getClass().getMethod("save", HttpServletRequest.class, HttpServletResponse.class));
        verify(helper).registerPath("/chat/upd", "PUT",
                servlet.getClass().getMethod("update", HttpServletRequest.class, HttpServletResponse.class));
        verify(helper).registerPath("/chat/delete", "DELETE",
                servlet.getClass().getMethod("delete", HttpServletRequest.class, HttpServletResponse.class));
        verify(helper).registerPath("/chat/(?<userId>\\d+)", "GET",
                servlet.getClass().getMethod("findById", HttpServletRequest.class, HttpServletResponse.class));
    }

    @Test
    void testFindById() throws Exception {
        Optional<ChatRoomDTO> result = Optional.of(dto);

        when(request.getRequestURI()).thenReturn("/chat/1");
        String id = "1";
        when(service.findById(Long.parseLong(id))).thenReturn(result);
        servlet.findById(request, response);

        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json");
        verify(service).findById(Long.parseLong(id));
    }

    @Test
    void testSave() throws Exception {
        ChatRoomDTO dto = new ChatRoomDTO.ChatRoomDTOBuilder()
                .setRoomId(1L)
                .setName("Test Chat")
                .setDescription("Test")
                .setCreateBy(new UserInfoDTO())
                .build();
        String requestBody = "{\"id\":1,\"name\":\"Test Chat,\"description\":\"Test\"}";
        when(request.getReader()).thenAnswer(invocation -> new BufferedReader(new StringReader(requestBody)));
        when(mapper.readValue(requestBody,ChatRoomDTO.class)).thenReturn(dto);
        when(service.save(dto)).thenReturn(dto);

        servlet.save(request, response);

        verify(service).save(Mockito.any(ChatRoomDTO.class));
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json");
    }

    @Test
    void testUpdate() throws Exception {
        String requestBody = "{\"id\":1,\"name\":\"Test Chat,\"description\":\"Test\"}";
        when(request.getReader()).thenAnswer(invocation -> new BufferedReader(new StringReader(requestBody)));
        when(mapper.readValue(requestBody,ChatRoomDTO.class)).thenReturn(dto);
        when(service.update(dto)).thenReturn(dto);

        servlet.update(request, response);

        verify(service).update(Mockito.any(ChatRoomDTO.class));
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json");
    }

    @Test
    void testDelete() throws Exception {
        String requestBody = "{\"id\":1,\"name\":\"Test Chat,\"description\":\"Test\"}";
        when(request.getReader()).thenAnswer(invocation -> new BufferedReader(new StringReader(requestBody)));
        when(mapper.readValue(requestBody,ChatRoomDTO.class)).thenReturn(dto);
        when(service.delete(dto)).thenReturn(dto);

        servlet.delete(request, response);

        verify(service).delete(Mockito.any(ChatRoomDTO.class));
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json");
    }

    @Test
    void testGetAll() throws Exception {
        when(service.getAll()).thenReturn(new ArrayList<>());

        servlet.getAll(request, response);

        verify(service).getAll();
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json");
    }
}
