# chicory-error

```sh
# - Build the Rust
cd chicory-error-rs
cargo build --target wasm32-unknown-unknown --release
cd ..

# - Establish that the compiled WASM exists
ls -l chicory-error-rs/target/wasm32-unknown-unknown/release/chicory_error_rs.wasm

# - Build and run the Java code
cd chicory-error-java
mvn clean package && java -jar target/chickory-error-java-0.0.1-SNAPSHOT.jar
```

## Resources

- [Chicory docs: Guests and Hosts](https://chicory.dev/docs/usage/host-functions)
- [Logger WASM](https://raw.githubusercontent.com/dylibso/chicory/1a0ee7429b683014b1bc12c01c7d485498919b0c/wasm-corpus/src/main/resources/compiled/host-function.wat.wasm)
- [`logIt` WAT](https://github.com/dylibso/chicory/blob/1a0ee7429b683014b1bc12c01c7d485498919b0c/wasm-corpus/src/main/resources/wat/host-function.wat)
