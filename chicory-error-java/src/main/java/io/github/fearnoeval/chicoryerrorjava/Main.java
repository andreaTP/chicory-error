package io.github.fearnoeval.chicoryerrorjava;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.dylibso.chicory.runtime.HostFunction;
import com.dylibso.chicory.runtime.Instance;
import com.dylibso.chicory.runtime.Store;
import com.dylibso.chicory.wasm.Parser;
import com.dylibso.chicory.wasm.types.ValueType;

public class Main {
    private static final byte[] readAllBytes(final Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(final String[] args) {
        final var wasmPath = Paths.get("../chicory-error-rs/target/wasm32-unknown-unknown/release/chicory_error_rs.wasm");
        final var wasmBytes = Main.readAllBytes(wasmPath);

        runHostFunctionExample(wasmBytes, "logIt");
        runHostFunctionExample(wasmBytes, "hello_world_reversed");
    }

    /**
     * <li><a href="https://chicory.dev/docs/usage/host-functions">Chicory docs: Guests and Hosts</a></li>
     * <li>The only modifications are the call to {@link Parser#parse} and the call to {@link Instance#export}</li>
     */
    public static final void runHostFunctionExample(final byte[] wasmBytes, String guestFnName) {
        var func = new HostFunction(
            "console",
            "log",
            List.of(ValueType.I32, ValueType.I32),
            List.of(),
            (Instance instance, long... args) -> { // decompiled is: console_log(13, 0);
                var offset = (int) args[0];
                var len = (int) args[1];
                var message = instance.memory().readString(offset, len);
                System.out.println(message);
                return null;
            });

        // instantiate the store
        var store = new Store();
        // registers `console.log` in the store
        store.addFunction(func);
        var instance = store.instantiate("logger", Parser.parse(wasmBytes));
        var exportFn = instance.export(guestFnName);
        exportFn.apply();
    }
}
