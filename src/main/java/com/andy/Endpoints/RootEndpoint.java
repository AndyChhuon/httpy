package com.andy.Endpoints;

import com.andy.RequestParser.Records.ParsedRequest;

import java.util.Map;

public class RootEndpoint {
    public static String resolve(ParsedRequest parsedRequest){
        return "200 OK";
    }
}
