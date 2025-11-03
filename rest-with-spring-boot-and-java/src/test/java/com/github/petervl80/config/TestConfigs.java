package com.github.petervl80.config;

public interface TestConfigs {
    int SERVER_PORT = 8888;

    String HEADER_PARAM_AUTHORIZATION = "Authorization";
    String HEADER_PARAM_ORIGIN = "Origin";

    String ORIGIN_LOCALHOST = "http://localhost:8080";
    String ORIGIN_INVALID_LOCALHOST = "http://localhost:1000";
}
