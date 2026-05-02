package com.andy.RequestParser.Records;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public record ServerResponse(StatusLine statusLine, Map<String,String> headers, String responseBody) {
    public byte[] toBytes(){
        String statusLine = String.format("%s %d %s", statusLine().http_version(), statusLine().status_code(), statusLine().reason_phrase());
        String headers = headers().entrySet().stream().map(entry -> entry.getKey() + ": " + entry.getValue()).collect(Collectors.joining("\r\n", "", "\r\n"));

        return String.format("%s\r\n%s\r\n%s", statusLine, headers, responseBody).getBytes();
    }
}
