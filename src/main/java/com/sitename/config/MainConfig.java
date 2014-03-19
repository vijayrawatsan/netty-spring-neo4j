package com.sitename.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sitename.handlers.HttpServerInitializer;

@Configuration
@PropertySource(value = { "classpath:netty-server.properties" })
@ComponentScan(basePackages = "com.sitename")
@EnableTransactionManagement(mode=AdviceMode.PROXY)
@EnableNeo4jRepositories(basePackages = {"com.sitename.repository"})
public class MainConfig extends Neo4jConfiguration {

    @Value("${boss.thread.count}")
    private int                   bossCount;

    @Value("${worker.thread.count}")
    private int                   workerCount;

    @Value("${tcp.port}")
    private int                   tcpPort;

    @Value("${so.keepalive}")
    private boolean               keepAlive;

    @Value("${so.backlog}")
    private int                   backlog;

    @Autowired
    @Qualifier("httpServerInitializer")
    private HttpServerInitializer httpServerInitializer;
    @Autowired
    private ApplicationContext  applicationContext;

    @Bean(destroyMethod = "shutdown")
    public GraphDatabaseService graphDatabaseService() {
        return new SpringRestGraphDatabase("http://localhost:7474/db/data/");
    }
    @Bean
    public Neo4jTemplate neo4jTemplate() {
        Neo4jTemplate neo4jTemplate = new Neo4jTemplate(graphDatabaseService());
        neo4jTemplate.setApplicationContext(applicationContext);
        System.out.println("yoyo");
        return neo4jTemplate;
    }
    
    @SuppressWarnings("unchecked")
    @Bean(name = "serverBootstrap")
    public ServerBootstrap bootstrap() {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup(), workerGroup()).channel(NioServerSocketChannel.class).childHandler(httpServerInitializer);
        Map<ChannelOption<?>, Object> tcpChannelOptions = tcpChannelOptions();
        Set<ChannelOption<?>> keySet = tcpChannelOptions.keySet();
        for(@SuppressWarnings("rawtypes")
        ChannelOption option : keySet) {
            b.option(option, tcpChannelOptions.get(option));
        }
        return b;
    }

    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossCount);
    }

    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerCount);
    }

    @Bean(name = "tcpSocketAddress")
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(tcpPort);
    }

    @Bean(name = "tcpChannelOptions")
    public Map<ChannelOption<?>, Object> tcpChannelOptions() {
        Map<ChannelOption<?>, Object> options = new HashMap<ChannelOption<?>, Object>();
        options.put(ChannelOption.SO_KEEPALIVE, keepAlive);
        options.put(ChannelOption.SO_BACKLOG, backlog);
        return options;
    }

    /**
     * Necessary to make the Value annotations work.
     * 
     * @return
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}