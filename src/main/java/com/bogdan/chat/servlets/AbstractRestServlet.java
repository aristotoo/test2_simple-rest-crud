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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AbstractRestServlet extends HttpServlet {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRestServlet.class);
    private final Map<Pattern, Map<String, Method>> methodMap = new HashMap<>();
    protected ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String uri = request.getRequestURI();
        String method = request.getMethod();

        Map<String,Method> pathMap = getExecutableMethodMapFromPath(uri);
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

    private Map<String,Method> getExecutableMethodMapFromPath(String uri){
        for(Map.Entry<Pattern,Map<String,Method>> entry : methodMap.entrySet()){
            Pattern pattern = entry.getKey();
            Matcher matcher = pattern.matcher(uri);
            if(matcher.matches()){
                return entry.getValue();
            }
        }
        return Collections.emptyMap();
    }

    protected void registerPath(String path, String method, Method handlerMethod) {
        Map<String,Method> pathMap = methodMap.computeIfAbsent(Pattern.compile(path), k -> new HashMap<>());
        pathMap.put(method.toLowerCase(),handlerMethod);
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

