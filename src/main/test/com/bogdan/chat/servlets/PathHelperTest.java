package com.bogdan.chat.servlets;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PathHelperTest {
    @InjectMocks
    private PathHelper pathHelper;

    @Test
    void testRegisterPath() throws NoSuchMethodException {
        // Подготовка метода для регистрации
        Method handlerMethod = PathHelperTest.class.getDeclaredMethod("dummyHandler");

        // Регистрация пути
        pathHelper.registerPath("/api/test", "GET", handlerMethod);

        // Проверка, что метод зарегистрирован правильно
        Map<String, Method> executableMethodMap = pathHelper.getExecutableMethodMapFromPath("/api/test");
        assertEquals(1, executableMethodMap.size());
        assertTrue(executableMethodMap.containsKey("get"));
        assertEquals(handlerMethod, executableMethodMap.get("get"));
    }

    // Заглушка для метода
    public void dummyHandler() {
        // Метод-заглушка, ничего не делает
    }
}
