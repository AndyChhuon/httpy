# HTTP Server From Scratch

A HTTP/1.1 server built in Java at the socket level using only the standard library, without Spring, Netty, or frameworks.

## Requirements

- Java 17+
- Maven

## Getting Started

Compile and run the server:

```bash
mvn compile exec:java -Dexec.mainClass=com.andy.Server
```

The server listens on `http://localhost:8080`.

## Testing

In a separate terminal:

```bash
curl -v http://localhost:8080
```
