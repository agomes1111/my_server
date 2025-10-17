package com.my.server.domain.data;

public interface ServiceApp {
    String getHttpVerb();
    String getPath();
    void setPath(String path);
    void setHttpVerb(String path);
}
