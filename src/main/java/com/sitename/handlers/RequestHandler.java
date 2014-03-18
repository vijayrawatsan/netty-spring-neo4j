package com.sitename.handlers;

import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

public interface RequestHandler {

    public FullHttpResponse handleRequest(String requestUri, String requestPayload, HttpRequest request) throws Exception;
}
