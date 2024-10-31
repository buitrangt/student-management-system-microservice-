package org.aibles.auth.utils.service;

import java.util.Map;

public interface LoginService {
    Map<String, Object> login(String username, String password);
}
