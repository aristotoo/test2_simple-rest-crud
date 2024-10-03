package com.bogdan.chat.servlets;

import com.bogdan.chat.dto.UserDTO;

import com.bogdan.chat.service.UserService;
import com.bogdan.chat.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/api/*")
public class UserRestServlet extends AbstractRestServlet {
    private UserService userService;

    public UserRestServlet(ObjectMapper mapper, PathHelper helper, UserService userService) {
        super(mapper, helper);
        this.userService = userService;
    }
    public UserRestServlet(){
        this.userService = new UserServiceImpl();
    }

    @Override
    public void init() {
        try {
            super.helper.registerPath("/api/users/all","GET",
                    this.getClass().getMethod("getAll", HttpServletRequest.class, HttpServletResponse.class));
            super.helper.registerPath("/api/users/save","POST",
                    this.getClass().getMethod("save",  HttpServletRequest.class, HttpServletResponse.class));
            super.helper.registerPath("/api/users/upd", "PUT",this.getClass()
                    .getMethod("update",  HttpServletRequest.class, HttpServletResponse.class));
            super.helper.registerPath("/api/users/delete","DELETE",this.getClass()
                    .getMethod("delete",  HttpServletRequest.class, HttpServletResponse.class));
            super.helper.registerPath("/api/users/(?<userId>\\d+)","GET",this.getClass()
                    .getMethod("findById",  HttpServletRequest.class, HttpServletResponse.class));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void findById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = getPathVariable(request);
        Optional<UserDTO> byId = userService.findById(Long.parseLong(id));
        if(byId.isPresent()){
            mapper.writeValue(response.getOutputStream(),byId.get());
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("Object not found");
        }
    }
    private String getPathVariable(HttpServletRequest request) {
        String[] requestURI = request.getRequestURI().split("/");
        return requestURI[requestURI.length - 1];
    }

    public void getAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
        mapper.writeValue(response.getOutputStream(),userService.getAll());
    }

    public void save(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDTO userDTO = super.readValueFromRequest(request,UserDTO.class);
        mapper.writeValue(response.getOutputStream(), userService.save(userDTO));
    }

    public void update(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDTO userDTO = super.readValueFromRequest(request,UserDTO.class);
        mapper.writeValue(response.getOutputStream(), userService.update(userDTO));
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDTO userDTO = super.readValueFromRequest(request, UserDTO.class);
        mapper.writeValue(response.getOutputStream(), userService.delete(userDTO));
    }
}