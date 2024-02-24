package me.diogo.openaijava.operation;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Component
@AllArgsConstructor
public class FileConverter {
    @Autowired
    private final ResourceLoader resourceLoader;

    public String readToString(final String filePath) throws IOException {
        final var resource = resourceLoader.getResource("classpath:" + filePath);

        try (var reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8)) {
            return FileCopyUtils.copyToString(reader);
        }
    }
}
