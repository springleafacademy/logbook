package com.springleaf.logbook.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;
import org.zalando.logbook.json.JsonHttpLogFormatter;

import static java.util.regex.Pattern.compile;
import static org.zalando.logbook.Conditions.contentType;
import static org.zalando.logbook.HeaderFilters.authorization;
import static org.zalando.logbook.QueryFilters.accessToken;
import static org.zalando.logbook.QueryFilters.replaceQuery;
import static org.zalando.logbook.json.JsonPathBodyFilters.jsonPath;

@Configuration
public class LogBookConfig {

    @Bean
    public Logbook logbook(){
        return Logbook.builder()
                .requestFilter(RequestFilters.replaceBody(message -> contentType("audio/*").test(message) ? "mmh mmh mmh mmh" : null))
                .responseFilter(ResponseFilters.replaceBody(message -> contentType("*/*-stream").test(message) ? "It just keeps going and going..." : null))
                .queryFilter(accessToken())
                .queryFilter(replaceQuery("product_price", "<secret>"))
                .headerFilter(authorization())
//                .headerFilter(eachHeader("X-Secret"::equalsIgnoreCase, "<secret>"))
                .bodyFilter(jsonPath("$.studentName").replace("unknown"))
//                .bodyFilter(jsonPath("$.address").replace("X"))
//                .bodyFilter(jsonPath("$.name").replace(compile("^(\\w).+"), "$1."))
//                .bodyFilter(jsonPath("$.friends.*.name").replace(compile("^(\\w).+"), "$1."))
//                .bodyFilter(jsonPath("$.grades.*").replace(1.0))
//                .bodyFilter(jsonPath("$.studentName").delete())//remove a parameter from body
                .sink(new DefaultSink(
                        new PrincipalHttpLogFormatter(new JsonHttpLogFormatter()),
                        new StreamHttpLogWriter()
                ))
//                .bodyFilter(new CompactingJsonBodyFilter())
                .build();
    }
}
