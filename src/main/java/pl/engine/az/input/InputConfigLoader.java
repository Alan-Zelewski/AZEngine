package pl.engine.az.input;

import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class InputConfigLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private InputConfigLoader() {
    }

    public static InputConfig load(String resource) {
        try (InputStream stream = InputConfigLoader.class.getResourceAsStream(resource)) {
            if (stream == null) {
                throw new IllegalArgumentException("Resource not found on classpath " + resource);
            }
            return MAPPER.readValue(stream, InputConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Cannot load input config from resource: " + resource, e);
        }
    }
}
