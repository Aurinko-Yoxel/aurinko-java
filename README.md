# Aurinko.io Java SDK

A lightweight, fluent Java client for interacting with the **Aurinko.io Unified API**. 
This SDK simplifies authentication, connection management, and data parsing.

## Features

* **Fluent API**: Clean and readable builder-like syntax for HTTP requests.
* **Auto-Closeable**: Implements `AutoCloseable` for safe and automatic resource management using `try-with-resources`.
* **Flexible Execution**: Supports both immediate execution and prepared requests.
* **Type-Safe Parsing**: Automatically maps JSON responses directly to your Java POJOs (Plain Old Java Objects).

## Requirements

* Java 25 or higher
* **Aurinko.io Platform Access**: A valid account, instance URL, and appropriate credentials (such as an Access Token, App Auth credentials, or session identifiers depending on your authentication flow).

## Installation

Add the dependency to your project (adjust based on your build tool once the artifact is published).

### Maven
Add the JitPack repository to your pom.xml:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Add the dependency (replace Tag with a specific release tag or commit hash):
```xml
<dependencies>
    <dependency>
        <groupId>com.github.YourGithubUsername</groupId>
        <artifactId>aurinko-java-sdk</artifactId>
        <version>Tag</version>
    </dependency>
</dependencies>
```

## Usage Example

The following example demonstrates how to initialize the service using account authentication,
retrieve information about account via the Aurinko API, and automatically manage resources.

```java

import com.yoxel.aurinko.bean.AurApplication;
import java.io.IOException;

/**
 *
 */
public class AurinkoGetAppDemo {

    public static void main(String[] args) throws IOException {

        try (final var svc = AurinkoService.createWithAccountAuth(
                "http://localhost:9000",
                "access_token"
        )) {
            final var accountInfo = svc.accounts.getMe();

            System.out.println(accountInfo);
        }
    }
}
```

## Architecture Notes

* **`AurinkoService`**: The main entry point. Must be closed after use (manually or via `try-with-resources`).

