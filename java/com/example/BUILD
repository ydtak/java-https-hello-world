java_library(
    name = "hello_lib",
    srcs = ["HelloWorld.java"],
    resources = ["keystore.jks"],
)

java_binary(
    name = "hello",
    main_class = "com.example.HelloWorld",
    runtime_deps = [":hello_lib"],
)
