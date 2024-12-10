package br.ufsc.INE5429.searchable.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class ConfigUtils {
    public static String loadOrCreateKey() {
        Path keyFilePath = getResourcePath("key.pem");
        if (Files.exists(keyFilePath)) {
            try {
                byte[] keyBytes = Files.readAllBytes(keyFilePath);
                return new String(keyBytes);
            } catch (IOException e) {
                throw new RuntimeException("Failed to read the key file", e);
            }
        } else {
            try {
                String generatedKey = generateNewKey();
                saveKeyToFile(generatedKey, keyFilePath);
                return generatedKey;
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate or save the key", e);
            }
        }
    }

    private static String generateNewKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    private static Path getResourcePath(String resource) {
        InputStream inputStream = ConfigUtils.class.getClassLoader().getResourceAsStream(resource);

        if (inputStream == null) {
            throw new RuntimeException("Resource " + resource + " not found.");
        }

        try {
            Path tempFile = Files.createTempFile(resource, ".tmp");
            Files.copy(inputStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
            return tempFile.toAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource from classpath", e);
        }
    }

    private static void saveKeyToFile(String key, Path filePath) {
        try {
            // Ensure the parent directories exist
            Files.createDirectories(filePath.getParent());
            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                writer.write(key);
            }
        } catch (IOException e) {
            System.err.println("Failed to save the key to file: " + e.getMessage());
            throw new RuntimeException("Failed to save the key to file", e);
        }
    }
}
