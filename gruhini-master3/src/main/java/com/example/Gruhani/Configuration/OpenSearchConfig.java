

/*package com.example.Gruhani.Configuration;




import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.opensearch.client.RestClient;
import org.opensearch.client.RestClientBuilder;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


@Configuration
@EnableAutoConfiguration
public class OpenSearchConfig {


      @Bean
      @Primary
      public RestClientBuilder restClientBuilder() {
          BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
          credentialsProvider.setCredentials(
                  new AuthScope("localhost", 9200),
                  new UsernamePasswordCredentials("admin", "@Vanshika21")
          );

          // IMPORTANT: use org.apache.http.HttpHost (not hc.core5)
          HttpHost host = new HttpHost("localhost", 9200, "https");

          RestClientBuilder builder = RestClient.builder(host);
          builder.setHttpClientConfigCallback(httpClientBuilder ->
                  httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
          );




        return builder;
    }


    @Bean
    public RestClient restClient(RestClientBuilder restClientBuilder) {
        return restClientBuilder.build();
    }

    @Bean
    public OpenSearchClient openSearchClient(RestClient restClient) {
        RestClientTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );
        return new OpenSearchClient(transport);
    }
}*/
/*
package com.example.Gruhani.Configuration;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class OpenSearchConfig {

    @Value("${opensearch.host:localhost}")
    private String host;

    @Value("${opensearch.port:9200}")
    private int port;

    @Value("${opensearch.username:admin}")
    private String username;

    @Value("${opensearch.password:@Vanshika21}")
    private String password;

    @Value("${opensearch.scheme:https}")
    private String scheme;

    @Bean
    @Primary
    public RestClientBuilder restClientBuilder() {
        try {
            BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    new AuthScope(host, port),
                    new UsernamePasswordCredentials(username, password)
            );

            HttpHost httpHost = new HttpHost(host, port, scheme);

            RestClientBuilder builder = RestClient.builder(httpHost);
            builder.setHttpClientConfigCallback(httpClientBuilder ->
                    httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider)
            );

            return builder;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create RestClientBuilder", e);
        }
    }

    @Bean
    @Primary
    public RestClient restClient() {
        return restClientBuilder().build();
    }

    @Bean
    @Primary
    public OpenSearchClient openSearchClient() {
        RestClientTransport transport = new RestClientTransport(
                restClient(),
                new JacksonJsonpMapper()
        );
        return new OpenSearchClient(transport);
    }
}*/