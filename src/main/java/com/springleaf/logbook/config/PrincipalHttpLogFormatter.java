package com.springleaf.logbook.config;

import org.zalando.logbook.*;
import org.zalando.logbook.json.JsonHttpLogFormatter;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Map;

final class PrincipalHttpLogFormatter implements HttpLogFormatter {

    private final JsonHttpLogFormatter delegate;

    PrincipalHttpLogFormatter(final JsonHttpLogFormatter delegate) {
        this.delegate = delegate;
    }

    @Override
    public String format(Precorrelation precorrelation, HttpRequest request) throws IOException {
        final Map<String, Object> content = delegate.prepare(precorrelation, request);
        content.put("Date", getFormattedDate());
        content.remove("remote");
        content.remove("origin");
        content.remove("protocol");
        content.put("type", "REQUEST");
        return delegate.format(content);
    }

    @Override
    public String format(Correlation correlation, HttpResponse response) throws IOException {
        final Map<String, Object> content = delegate.prepare(correlation, response);
        content.remove("origin");
        content.put("type", "RESPONSE");
        return delegate.format(content);
    }

    private String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, d MMM yyyy HH:mm:ss z");
        ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
        String formattedDate = now.format(formatter);
        return formattedDate;
    }
}
