# chicory-error

## The skinny

- I took the example Chicory code from [the guests and hosts docs][guests-and-hosts-docs], reimplemented [the WASM portion][logger-wasm] in Rust, and it works!
  - For reference, [here's the original WAT code][logger-wasm-code]
- Upon reviewing [the code backing the count_vowels example][count-vowels-code], I saw that `alloc` instantiates a `Vec`, `mem::forget`s it so it's not dropped, and returns the "forgotten" pointer; `dealloc` simply "remembers" the memory and immediately drops it
  - The takeaway for my example is that memory allocation on the WASM side seems to "just work"
- To verify that memory allocation works as expected, I created a new function in Rust that:
  - Reverses the embedded `Hello, World!` string
  - Collects the reversal to a Rust `String`, which is where the allocation occurs
  - A byte array view of the `String` is attained
  - Using the byte array view, the pointer to it and length of it are passed to the `console.log` host function from the original example
- Upon invoking this from the Java side, the output is not as expected:

```
Hello, World!
Hello, World!
Hello, World!
Hello, World!
Hello, World!
Hello, World!
Hello, World!
Hello, World!
Hello, World!
Hello, World!
Hello, World!
library/std/src/alloc.rsbes faillibrary/std/src/panicking.rs,�n failed: psize >= size + min_overhead()� assertion failed: psize <= size + max_overhead()�



capacity overflow�library/alloc/src/raw_vec.rs�called `Option::unwrap()` on a `None` value00010203040506070809101112131415161718192021222324252627282930313233343536373839404142434445464748495051525354555657585960616263646566676869707172737475767778798081828384858687888990919293949596979899������������������������

                                                                                        $$,,44<<DDLLTT\\ddlltt||���������� �lroW ,olleH
```

## Notes

- _Most of_ the reversed string is visible at the end of the output

## Steps to reproduce

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

- [Chicory docs: Guests and Hosts][guests-and-hosts-docs]
- [Logger WASM][logger-wasm]
- [Logger WAT][logger-wasm-code]
- [`logIt` WAT](https://github.com/dylibso/chicory/blob/1a0ee7429b683014b1bc12c01c7d485498919b0c/wasm-corpus/src/main/resources/wat/host-function.wat)
- [Chicory example of alloc and dealloc][count-vowels-code]

[guests-and-hosts-docs]: https://chicory.dev/docs/usage/host-functions
[logger-wasm]: https://raw.githubusercontent.com/dylibso/chicory/1a0ee7429b683014b1bc12c01c7d485498919b0c/wasm-corpus/src/main/resources/compiled/host-function.wat.wasm
[logger-wasm-code]: https://github.com/dylibso/chicory/blob/1a0ee7429b683014b1bc12c01c7d485498919b0c/wasm-corpus/src/main/resources/rust/count_vowels.rs#L7-L19
[count-vowels-code]: https://github.com/dylibso/chicory/blob/1a0ee7429b683014b1bc12c01c7d485498919b0c/wasm-corpus/src/main/resources/rust/count_vowels.rs#L7-L19
