//package com.sitename.config;
//
//import org.neo4j.graphdb.GraphDatabaseService;
//import org.springframework.context.annotation.AdviceMode;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
//import org.springframework.data.neo4j.config.Neo4jConfiguration;
//import org.springframework.data.neo4j.rest.SpringRestGraphDatabase;
//import org.springframework.data.neo4j.support.Neo4jTemplate;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//@Configuration
//@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
//@EnableNeo4jRepositories(basePackages = {"com.sitename.repository"})
//public class DataConfig extends Neo4jConfiguration {
//
//    @Bean(destroyMethod = "shutdown")
//    public GraphDatabaseService graphDatabaseService() {
//        return new SpringRestGraphDatabase("http://localhost:7474/db/data/");
//    }
//    @Bean
//    public Neo4jTemplate neo4jTemplate() {
//        return new Neo4jTemplate(graphDatabaseService());
//    }
//}