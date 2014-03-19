package com.sitename.handlers;

import static io.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static io.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONNECTION;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders.Values;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;

public class HttpServerHandler extends SimpleChannelInboundHandler<Object> {

    private Map<String, RequestHandler> singletonHandlerMap = new HashMap<String, RequestHandler>();

    private List<RequestHandler>        controllers;
    
    private ApplicationContext applicationContext;

    public HttpServerHandler(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    
    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // Get or post without content
        if(msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;

            if(is100ContinueExpected(req)) {
                ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
            }

            String uri = req.getUri();

            if("/favicon.ico".equals(uri)) {
                FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
                sendResponse(ctx, (FullHttpRequest) req, res);
                return;
            }

            QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
            String path = queryStringDecoder.path();
            
            RequestHandler requestHandler = lookupUrlHandler(path);
            FullHttpResponse response = requestHandler.handleRequest(uri, null, req);
            
            sendResponse(ctx, req, response);
        }
    }

    private void sendResponse(ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse response) {
        boolean keepAlive = isKeepAlive(req);
        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, Values.KEEP_ALIVE);
            ctx.write(response);
        }
    }

    private RequestHandler getHandler(String path) {
        RequestHandler result = singletonHandlerMap.get(path);
        if(result != null) {
            return result;
        }
        for(RequestHandler handler : controllers) {
            if(path.matches(handler.getClass().getAnnotation(Controller.class).value())) {
                singletonHandlerMap.put(path, handler);
                result = handler;
                break;
            }
        }
        return result;
    }
    
    public RequestHandler lookupUrlHandler(String url) {
        RequestHandler handler = singletonHandlerMap.get(url);
        if(handler != null) {
            return handler;
        }
        String[] beanDefinitionNames = this.applicationContext.getBeanDefinitionNames();
        for(String beanName : beanDefinitionNames) {
            Object obj = this.applicationContext.getBean(beanName);
            if(obj == null) {
                continue;// spring is mad giving null all over places
            }
            Controller controller = obj.getClass().getAnnotation(Controller.class);
            if(controller != null) {
                if(url.matches(controller.value())) {
                    handler = (RequestHandler) obj;
                    singletonHandlerMap.put(url, handler);
                    return handler;
                }
            }
        }

        return null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}