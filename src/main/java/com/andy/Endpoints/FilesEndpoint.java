package com.andy.Endpoints;

import com.andy.RequestParser.Records.ParsedRequest;
import com.andy.RequestParser.Records.ServerResponse;
import com.andy.RequestParser.Records.StatusLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class FilesEndpoint {
    public static String path = "default";
    public static ServerResponse resolve(ParsedRequest parsedRequest){
        String request_target = parsedRequest.requestLine().request_target();
        int subPathIndex = request_target.indexOf("/", request_target.indexOf("/") + 1);
        String fileName = subPathIndex == -1 ? "" : request_target.substring(subPathIndex + 1);

        if (fileName.isBlank()){
            return new ServerResponse(new StatusLine("HTTP/1.1", 400, "Bad Request: Missing fileName"), new HashMap<>(), "");
        }

        if (!Files.isRegularFile(Path.of(path, fileName))) {
            return new ServerResponse(new StatusLine("HTTP/1.1", 404, "Not Found"), new HashMap<>(), "");
        }

        try {
            byte[] fileContents = Files.readAllBytes(Path.of(path, fileName));
            Map<String, String> headers = new HashMap<>(Map.ofEntries(entry("Content-Type", "application/octet-stream"), entry("Content-Length", String.valueOf(fileContents.length))));
            return new ServerResponse(new StatusLine("HTTP/1.1", 200, "OK"), headers, new String(fileContents));
        } catch (IOException e) {
            return new ServerResponse(new StatusLine("HTTP/1.1", 500, e.getMessage()), new HashMap<>(), "");
        }

    }
}
