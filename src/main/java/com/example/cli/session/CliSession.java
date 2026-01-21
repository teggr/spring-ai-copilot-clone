package com.example.cli.session;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CliSession {
    
    private final Map<String, Object> attributes = new HashMap<>();
    
    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }
    
    public Object getAttribute(String key) {
        return attributes.get(key);
    }
    
    public void removeAttribute(String key) {
        attributes.remove(key);
    }
    
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }
    
    public void clear() {
        attributes.clear();
    }
}
