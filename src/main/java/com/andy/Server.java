package com.andy;

import com.andy.Endpoints.EndpointResolver;
import com.andy.Endpoints.RootEndpoint;
import com.andy.RequestParser.Records.ParsedRequest;
import com.andy.RequestParser.RequestParser;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;

import static java.util.Map.entry;

public class Server {
    public static void main(String[] args) {
        final Map<String, EndpointResolver> endpointMapping = Map.ofEntries(entry("/", RootEndpoint::resolve));

        try (ServerSocket socket = new ServerSocket(8080))  {
            System.out.println("Server running");
            while (true) {
                try (Socket client = socket.accept(); InputStream in = client.getInputStream(); OutputStream out = client.getOutputStream()) {
                    BufferedInputStream buffer = new BufferedInputStream(in, 8192);
                    ParsedRequest parsedRequest = RequestParser.parse(buffer);
                    EndpointResolver endpointResolver = endpointMapping.get(parsedRequest.requestLine().request_target());
                    if (endpointResolver != null){
                        String response = endpointResolver.resolve(parsedRequest);
                        out.write(String.format("HTTP/1.1 %s\r\n\r\n",response).getBytes());
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
