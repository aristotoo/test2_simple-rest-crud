package com.bogdan.chat.servlets;

import com.bogdan.chat.dto.UserDTO;
import com.bogdan.chat.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRestServletTest extends AbstractRestServletTest {
    @Mock
    private UserServiceImpl service;
    @InjectMocks
    private UserRestServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private UserDTO dto = new UserDTO.UserDTOBuilder()
            .setUserId(1L)
            .setName("John Doe")
            .build();

    @BeforeEach
    void setUp() {
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

        verify(helper).registerPath("/api/users/all","GET",servlet.getClass()
                .getMethod("getAll", HttpServletRequest.class, HttpServletResponse.class));
        verify(helper).registerPath("/api/users/save","POST",servlet.getClass()
                .getMethod("save",  HttpServletRequest.class, HttpServletResponse.class));
        verify(helper).registerPath("/api/users/upd", "PUT",servlet.getClass()
                .getMethod("update",  HttpServletRequest.class, HttpServletResponse.class));
        verify(helper).registerPath("/api/users/delete","DELETE",servlet.getClass()
                .getMethod("delete",  HttpServletRequest.class, HttpServletResponse.class));
        verify(helper).registerPath("/api/users/(?<userId>\\d+)","GET",servlet.getClass()
                .getMethod("findById",  HttpServletRequest.class, HttpServletResponse.class));
    }

    @Test
    void testFindById() throws Exception {
        Optional<UserDTO> result = Optional.of(dto);

        when(request.getRequestURI()).thenReturn("/api/users/1");
        String id = "1";
        when(service.findById(Long.parseLong(id))).thenReturn(result);
        servlet.findById(request, response);

        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json");
        verify(service).findById(Long.parseLong(id));
    }

    @Test
    void testSave() throws Exception {
        String requestBody = "{\"id\":1,\"name\":\"John Doe\"}";
        when(request.getReader()).thenAnswer(invocation -> new BufferedReader(new StringReader(requestBody)));
        when(mapper.readValue(requestBody,UserDTO.class)).thenReturn(dto);
        when(service.save(dto)).thenReturn(dto);

        servlet.save(request, response);

        verify(service).save(Mockito.any(UserDTO.class));
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json");
    }

    @Test
    void testUpdate() throws Exception {
        String requestBody = "{\"id\":1,\"name\":\"John Doe\"}";
        when(request.getReader()).thenAnswer(invocation -> new BufferedReader(new StringReader(requestBody)));
        when(mapper.readValue(requestBody,UserDTO.class)).thenReturn(dto);
        when(service.update(dto)).thenReturn(dto);

        servlet.update(request, response);

        verify(service).update(Mockito.any(UserDTO.class));
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setContentType("application/json");
    }

    @Test
    void testDelete() throws Exception {
        String requestBody = "{\"id\":1,\"name\":\"John Doe\"}";
        when(request.getReader()).thenAnswer(invocation -> new BufferedReader(new StringReader(requestBody)));
        when(mapper.readValue(requestBody,UserDTO.class)).thenReturn(dto);
        when(service.delete(dto)).thenReturn(dto);

        servlet.delete(request, response);

        verify(service).delete(Mockito.any(UserDTO.class));
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



