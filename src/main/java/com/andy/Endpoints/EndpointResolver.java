package com.andy.Endpoints;

import com.andy.RequestParser.Records.ParsedRequest;

import java.util.Map;

public interface EndpointResolver {
    String resolve(ParsedRequest parsedRequest);
}
