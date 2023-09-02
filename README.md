# Hello World HTTPS Server

## Overview
A simple barebones HTTPS Java server that returns Hello World built with Bazel.

Run the server:
```
bazel run //java/com/example:hello
```

Access the endpoint:
```
curl -k https://localhost/hello
```

## Running on Privileged Ports
If you try to bind to port 443 without sufficient permissions, you'll get a `java.net.BindException: Permission denied`. On UNIX-like systems (including Linux and macOS), ports below 1024 are privileged and require administrative rights to bind to.

If you're on a Linux machine, you can use `setcap` to give the Java runtime permission to bind to privileged ports.

```
sudo setcap 'cap_net_bind_service=+ep' /path/to/java
```

Replace `/path/to/java` with the path to your Java executable. You can find this by running `readlink -f $(which java)` to get the full path.
