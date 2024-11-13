#[link(wasm_import_module = "console")]
extern "C" {
    fn log(ptr: i32, size: i32);
}

/// Reimplementation of
/// [the original code in WAT](https://github.com/dylibso/chicory/blob/1a0ee7429b683014b1bc12c01c7d485498919b0c/wasm-corpus/src/main/resources/wat/host-function.wat)
#[no_mangle]
pub extern "C" fn logIt() {
    let hello_world_bytes = HELLO_WORLD.as_bytes();

    let hello_world_ptr = hello_world_bytes.as_ptr() as i32;
    let hello_world_len = hello_world_bytes.len() as i32;

    for _ in 0..10 {
        unsafe {
            log(
                hello_world_ptr,
                hello_world_len,
            )
        }
    }
}

#[no_mangle]
pub extern "C" fn hello_world_reversed() {
    let reversed: String = HELLO_WORLD.chars().rev().collect();
    let reversed_bytes = reversed.as_bytes();

    let ptr = reversed_bytes.as_ptr() as i32;
    let len = reversed_bytes.len() as i32;

    unsafe {
        log(
            ptr,
            len,
        )
    }
}

static HELLO_WORLD: &str = "Hello, World!";
