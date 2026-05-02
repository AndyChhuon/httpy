package com.andy.RequestParser.Records;

import java.util.Map;

public record ParsedRequest(RequestLine requestLine, Map<String,String> headers, String body) {
}
