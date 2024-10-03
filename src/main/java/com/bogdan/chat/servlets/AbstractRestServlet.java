package com.bogdan.chat.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Collectors;

public class AbstractRestServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestServlet.class);

    protected ObjectMapper mapper;
    protected PathHelper helper;

    public AbstractRestServlet(ObjectMapper mapper, PathHelper helper) {
        this.mapper = mapper;
        this.helper = helper;
    }

    public AbstractRestServlet(){
        this.mapper = new ObjectMapper();
        this.helper = new PathHelper();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String uri = request.getRequestURI();
        String method = request.getMethod();

        Map<String,Method> pathMap = helper.getExecutableMethodMapFromPath(uri);
        if(pathMap != null){
            Method handlerMethod = pathMap.get(method.toLowerCase());
            if(handlerMethod != null){
                try{
                    handlerMethod.invoke(this,request,response);
                } catch (IllegalAccessException | InvocationTargetException ex){
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    LOGGER.debug("ERROR: {}",ex.getMessage());
                }
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        response.getWriter().write("Resource not found");
    }
    protected <T> T readValueFromRequest(HttpServletRequest request, Class<T> tClass) {
        T t = null;
        try {
            t = mapper.readValue(request.getReader()
                    .lines()
                    .collect(Collectors.joining()), tClass);
        } catch (IOException e){
            LOGGER.debug(e.getMessage());
        }
        return t;
    }
}

