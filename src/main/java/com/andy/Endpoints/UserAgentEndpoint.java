package com.andy.Endpoints;

import com.andy.RequestParser.Records.ParsedRequest;
import com.andy.RequestParser.Records.ServerResponse;
import com.andy.RequestParser.Records.StatusLine;

import java.util.HashMap;
import java.util.Map;

import static java.util.Map.entry;

public class UserAgentEndpoint {
    public static ServerResponse resolve(ParsedRequest parsedRequest){
        Map<String, String> headers = parsedRequest.headers();
        String userAgent = headers.get("user-agent");
        if (userAgent == null){
            return new ServerResponse(new StatusLine("HTTP/1.1", 400, "Bad Request"), new HashMap<>(), "");
        }

        HashMap<String,String> responseHeaders = new HashMap<>(Map.ofEntries(entry("Content-Type", "text/plain"), entry("Content-Length", String.valueOf(userAgent.length()))));

        return new ServerResponse(new StatusLine("HTTP/1.1", 200, "OK"), responseHeaders, userAgent);
    }
}
