package com.andy.RequestParser.Records;

public record StatusLine(String http_version, int status_code, String reason_phrase) {
}
