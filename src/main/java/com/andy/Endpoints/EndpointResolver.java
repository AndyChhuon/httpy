package com.andy.Endpoints;

import com.andy.RequestParser.Records.ParsedRequest;
import com.andy.RequestParser.Records.ServerResponse;

public interface EndpointResolver {
    ServerResponse resolve(ParsedRequest parsedRequest);
}
