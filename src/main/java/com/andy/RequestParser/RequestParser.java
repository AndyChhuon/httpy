package com.andy.RequestParser;

import com.andy.RequestParser.Records.ParsedRequest;
import com.andy.RequestParser.Records.RequestLine;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RequestParser {
    public static ParsedRequest parse(BufferedInputStream buffer) throws IOException {
        RequestLine requestLine = RequestLine.parse(readLine(buffer));

        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while (!(headerLine = readLine(buffer)).isEmpty()){
            String[] parts = headerLine.split(": ");
            if (parts.length == 2){
                headers.put(parts[0].toLowerCase(), parts[1]);
            }
        }

        int contentLength;
        String body = "";
        if ((contentLength = Integer.parseInt(headers.getOrDefault("content-length","0"))) > 0){
            body = new String(buffer.readNBytes(contentLength), StandardCharsets.UTF_8);
        }

        return new ParsedRequest(requestLine, headers, body);
    }

    public static String readLine(BufferedInputStream buffer) throws IOException {
        char prev = 0;
        int curr;
        StringBuilder stringBuilder = new StringBuilder();

        while ((curr = buffer.read()) != -1){
            char currChar = (char) curr;

            if (currChar == '\n' && prev == '\r'){
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                break;
            }
            stringBuilder.append(currChar);
            prev = currChar;
        }

        return stringBuilder.toString();
    }
}
