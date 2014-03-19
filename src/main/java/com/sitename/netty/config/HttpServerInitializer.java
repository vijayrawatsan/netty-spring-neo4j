package com.sitename.netty.config;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.cors.CorsConfig;
import io.netty.handler.codec.http.cors.CorsHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("httpServerInitializer")
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    final EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(8);
    
    @Autowired
    private HttpServerHandler httpServerHandler;
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        CorsConfig corsConfig = CorsConfig.anyOrigin().build();
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        pipeline.addLast("cors", new CorsHandler(corsConfig));
        //TODO 
        //Since we will be making a blocking call to neo4j we should use eventExecutorGroup 
        //But not 100% sure, if execution of handler will be ordered or not.
        pipeline.addLast(eventExecutorGroup, "handler", httpServerHandler);
        //pipeline.addLast("handler", httpServerHandler);
    }

}