package com.sitename.handlers;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

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
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Please refer to the {@link CorsConfig} javadocs for information about all the
 * configuration options available.
 *
 * Below are some of configuration discussed in this example:
 * <h3>Support only a specific origin</h3>
 * To support a single origin instead of the wildcard use the following:
 * <pre>
 * CorsConfig corsConfig = CorsConfig.withOrigin("http://domain1.com")
 * </pre>
 *
 * <h3>Enable loading from the file system</h3>
 * To enable the server to handle an origin specified as 'null', which happens
 * when a web browser loads a file from the local file system use the following:
 * <pre>
 * corsConfig.isNullOriginAllowed()
 * </pre>
 *
 * <h3>Enable request headers</h3>
 * To enable additional request headers:
 * <pre>
 * corsConfig.allowedRequestHeaders("custom-request-header")
 * </pre>
 *
 * <h3>Expose response headers</h3>
 * By default a browser only exposes the following simple header:
 * <ul>
 * <li>Cache-Control</li>
 * <li>Content-Language</li>
 * <li>Content-Type</li>
 * <li>Expires</li>
 * <li>Last-Modified</li>
 * <li>Pragma</li>
 * </ul>
 * Any of the above response headers can be retreived by:
 * <pre>
 * xhr.getResponseHeader("Content-Type");
 * </pre>
 * If you need to get access to other headers this must be enabled by the server, for example:
 * <pre>
 * corsConfig.exposedHeaders("custom-response-header");
 * </pre>
 */
@Component
@Qualifier("httpServerInitializer")
public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {

    static final EventExecutorGroup eventExecutorGroup = new DefaultEventExecutorGroup(8);
    
    @Autowired
    private HttpServerHandler     httpServerHandler;
    
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        CorsConfig corsConfig = CorsConfig.anyOrigin().build();
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        pipeline.addLast("cors", new CorsHandler(corsConfig));
        pipeline.addLast(eventExecutorGroup, "handler", httpServerHandler);
    }

}