package com.bogdan.chat.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractRestServletTest {
    @Mock
    protected ObjectMapper mapper;
    @Mock
    protected PathHelper helper;
    @InjectMocks
    protected AbstractRestServlet servlet;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;
    @Test
     void testServiceResourceNotFound() throws Exception {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);
        // Настраиваем запрос, который не соответствует ни одному методу
        when(request.getRequestURI()).thenReturn("/invalid/resource");
        when(request.getMethod()).thenReturn("GET");
        when(servlet.helper.getExecutableMethodMapFromPath("/invalid/resource")).thenReturn(Collections.emptyMap());

        // Вызываем метод service
        servlet.service(request, response);

        // Проверяем, что ответ содержит статус 404 и сообщение "Resource not found"
        verify(response).setStatus(HttpServletResponse.SC_NOT_FOUND);
        verify(writer).write("Resource not found");
    }

    // @Test
    //  void testServiceInternalError() throws Exception {
    //     request = mock(HttpServletRequest.class);
    //     response = mock(HttpServletResponse.class);
    //     // Настраиваем запрос
    //     when(request.getRequestURI()).thenReturn("/part/save");
    //     when(request.getMethod()).thenReturn("POST");
    //
    //     Method handlerMethod = ParticipantsRestServlet.class.getDeclaredMethod("save", HttpServletRequest.class, HttpServletResponse.class);
    //     Map<String, Method> pathMap = new HashMap<>();
    //     pathMap.put("post", handlerMethod);
    //     when(helper.getExecutableMethodMapFromPath("/part/save")).thenReturn(pathMap);
    //
    //     // Имитация выброса исключения в методе обработчика
    //     doThrow(new IllegalAccessException("Test exception"))
    //             .when(mock(Method.class)).invoke(this,request,response);
    //     // Вызов метода service
    //     servlet.service(request, response);
    //
    //     // Проверка на статус 500
    //     verify(response).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    //     verify(writer, never()).write(anyString());
    // }
    //
    // // Заглушка для метода обработки
    // public void dummyHandler(HttpServletRequest request, HttpServletResponse response) throws Exception {
    //     response.getWriter().write("{}"); // Возврат пустого JSON
    // }
}