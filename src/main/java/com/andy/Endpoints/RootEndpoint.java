package com.andy.Endpoints;

import com.andy.RequestParser.Records.ParsedRequest;
import com.andy.RequestParser.Records.ServerResponse;
import com.andy.RequestParser.Records.StatusLine;

import java.util.HashMap;

public class RootEndpoint {
    public static ServerResponse resolve(ParsedRequest parsedRequest){
        return new ServerResponse(new StatusLine("HTTP/1.1", 200, "OK"), new HashMap<>(), "");
    }
}
