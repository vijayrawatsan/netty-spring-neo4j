package com.sitename.netty.controller;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;

import java.util.Date;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;

import com.sitename.data.domain.User;
import com.sitename.data.service.UserService;
import com.sitename.handlers.RequestHandler;

@Controller("/(v1|v2|v3)/users.*")
public class UserController implements RequestHandler {

    @Override
    public FullHttpResponse handleRequest(String requestUri, String requestPayload, HttpRequest request) throws Exception {
        
        if(request.getMethod().equals(HttpMethod.GET)) {
            if(requestUri.matches("/v1/users1.*")) {
                User user = new User();
                user.setFriends(null);
                user.setFullName("Vijay Rawat");
                user.setLastLogin(new Date());
                user.setLogin("vijayrawatsan");
                
                return getFullHttpResponse(user);
            }
            else  if(requestUri.matches("/v1/users.*")) {
                return getFullHttpResponse(UserService.getInstance().findAll());
            }
        }
        return null;
    }
    
    private FullHttpResponse getFullHttpResponse(Object content) throws Exception {
        //TODO - autowire this
        ObjectMapper mapper = new ObjectMapper();
        byte[] json = mapper.writeValueAsBytes(content);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json));
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
        return response;
    }

}
