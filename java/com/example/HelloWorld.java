package com.example;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsConfigurator;
import com.sun.net.httpserver.HttpsServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;

public class HelloWorld {

    // Create keystore using the following command:
    // keytool -genkeypair -keyalg RSA \
    // -keysize 2048 \
    // -keystore keystore.jks \
    // -validity 3650
    private static final String KEYSTORE_PASSWORD = "keystore_password";
    private static final String KEYSTORE_PATH = "com/example/keystore.jks";

    public static void main(String[] args) {
        try {
            // Load the keystore
            char[] password = KEYSTORE_PASSWORD.toCharArray();
            KeyStore keystore = KeyStore.getInstance("JKS");
            InputStream keystoreStream = HelloWorld.class.getClassLoader().getResourceAsStream(KEYSTORE_PATH);
            if (keystoreStream == null) {
                System.err.println("Keystore not found in classpath.");
                return;
            }
            keystore.load(keystoreStream, password);
            System.out.println("Keystore loaded successfully.");

            // Set up the key manager factory
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keystore, password);
            System.out.println("KeyManagerFactory initialized.");

            // Set up the SSL context
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, null);
            System.out.println("SSL context initialized.");

            // Create and configure HTTPS server
            HttpsServer server = HttpsServer.create(new InetSocketAddress(443), 0);
            server.setHttpsConfigurator(new HttpsConfigurator(sslContext));
            server.createContext("/hello", new MyHandler());
            server.setExecutor(null);

            System.out.println("Server started at https://localhost:443/");
            server.start();
        } catch (Exception e) {
            System.err.println("Server failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "Hello World\n";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}