package com.andy.Endpoints;

import com.andy.RequestParser.Records.ParsedRequest;
import com.andy.RequestParser.Records.ServerResponse;
import com.andy.RequestParser.Records.StatusLine;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class EchoEndpoint {
    public static ServerResponse resolve(ParsedRequest parsedRequest){
        String request_target = parsedRequest.requestLine().request_target();
        int subPathIndex = request_target.indexOf("/", request_target.indexOf("/") + 1);
        String subPath = subPathIndex == -1 ? "" : request_target.substring(subPathIndex + 1);

        HashMap<String,String> headers = new HashMap<>(Map.ofEntries(entry("Content-Type", "text/plain"), entry("Content-Length", String.valueOf(subPath.length()))));

        return new ServerResponse(new StatusLine("HTTP/1.1", 200, "OK"), headers, subPath);
    }
}
