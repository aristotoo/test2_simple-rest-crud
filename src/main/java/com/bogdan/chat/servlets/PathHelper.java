package com.bogdan.chat.servlets;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathHelper {
    private final Map<Pattern, Map<String, Method>> methodMap = new HashMap<>();

    public Map<String,Method> getExecutableMethodMapFromPath(String uri){
        for(Map.Entry<Pattern,Map<String,Method>> entry : methodMap.entrySet()){
            Pattern pattern = entry.getKey();
            Matcher matcher = pattern.matcher(uri);
            if(matcher.matches()){
                return entry.getValue();
            }
        }
        return Collections.emptyMap();
    }

    public void registerPath(String path, String method, Method handlerMethod) {
        Map<String,Method> pathMap = methodMap.computeIfAbsent(Pattern.compile(path), k -> new HashMap<>());
        pathMap.put(method.toLowerCase(),handlerMethod);
    }
}
