package com.andy;

import com.andy.Endpoints.EchoEndpoint;
import com.andy.Endpoints.EndpointResolver;
import com.andy.Endpoints.RootEndpoint;
import com.andy.Endpoints.UserAgentEndpoint;
import com.andy.RequestParser.Records.ParsedRequest;
import com.andy.RequestParser.Records.ServerResponse;
import com.andy.RequestParser.RequestParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import static java.util.Map.entry;

public class Server {
    public static void main(String[] args) {
        final Map<String, EndpointResolver> endpointMapping = Map.ofEntries(entry("/", RootEndpoint::resolve), entry("/echo", EchoEndpoint::resolve), entry("/user-agent", UserAgentEndpoint::resolve));

        try (ServerSocket socket = new ServerSocket(8080))  {
            System.out.println("Server running");
            while (true) {
                try (Socket client = socket.accept(); InputStream in = client.getInputStream(); OutputStream out = client.getOutputStream()) {
                    BufferedInputStream buffer = new BufferedInputStream(in, 8192);
                    ParsedRequest parsedRequest = RequestParser.parse(buffer);
                    String request_target = parsedRequest.requestLine().request_target();
                    int subPathIndex = request_target.indexOf("/", request_target.indexOf("/") + 1);
                    String endpointPath = subPathIndex == -1 ? request_target : request_target.substring(0, subPathIndex);

                    EndpointResolver endpointResolver = endpointMapping.get(endpointPath);

                    if (endpointResolver != null){
                        ServerResponse response = endpointResolver.resolve(parsedRequest);
                        out.write(response.toBytes());
                    } else {
                        out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
                    }

                    out.flush();
                    System.out.println("Wrote to client");

                }
            }

        } catch (IOException e){
            System.out.println("IOException:" + e.getMessage());
        }
    }
}
