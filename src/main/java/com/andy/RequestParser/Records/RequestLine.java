package com.andy.RequestParser.Records;

public record RequestLine(HTTPMethod http_method, String request_target, String http_version) {
    public static RequestLine parse(String line){
        String[] parts = line.split(" ");
        if (parts.length != 3){
            throw new IllegalArgumentException("malformed request line: "+ line);
        }

        return new RequestLine(HTTPMethod.valueOf(parts[0]), parts[1], parts[2]);
    }
}
