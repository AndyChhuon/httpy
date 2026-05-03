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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

import static java.util.Map.entry;

public class Server {
    public static void main(String[] args) {
        final Map<String, EndpointResolver> endpointMapping = Map.ofEntries(entry("/", RootEndpoint::resolve), entry("/echo", EchoEndpoint::resolve), entry("/user-agent", UserAgentEndpoint::resolve));
        LongAdder count = new LongAdder();
        LongAdder totalNanos = new LongAdder();
        AtomicLong maxNanos = new AtomicLong();

        try (ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor(); ServerSocket socket = new ServerSocket(8080, 1024))  {
            System.out.println("Server running");

            scheduler.scheduleAtFixedRate(() -> {
                long n = count.sumThenReset();
                long total = totalNanos.sumThenReset();
                long max = maxNanos.getAndSet(0);

                if (n > 0){
                    System.out.printf("count=%d, mean=%d µs, max=%d µs\n", n, total/1000/n, max/1000);
                }
            }, 1, 1, TimeUnit.SECONDS);

            while (true) {
                Socket client = socket.accept();
                executor.execute(() -> {
                    try (client; InputStream in = client.getInputStream(); OutputStream out = client.getOutputStream()) {
                        long start = System.nanoTime();
                        BufferedInputStream buffer = new BufferedInputStream(in, 8192);
                        ParsedRequest parsedRequest = RequestParser.parse(buffer);
                        String request_target = parsedRequest.requestLine().request_target();
                        int subPathIndex = request_target.indexOf("/", request_target.indexOf("/") + 1);
                        String endpointPath = subPathIndex == -1 ? request_target : request_target.substring(0, subPathIndex);

                        EndpointResolver endpointResolver = endpointMapping.get(endpointPath);

                        if (endpointResolver != null) {
                            ServerResponse response = endpointResolver.resolve(parsedRequest);
                            out.write(response.toBytes());
                        } else {
                            out.write("HTTP/1.1 404 Not Found\r\n\r\n".getBytes());
                        }

                        out.flush();

                        long elapsed = System.nanoTime() - start;
                        count.increment();
                        totalNanos.add(elapsed);
                        maxNanos.accumulateAndGet(elapsed, Math::max);
                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                });

            }
        } catch (IOException e) {
            System.out.println("IOException:" + e.getMessage());
        }

    }
}
