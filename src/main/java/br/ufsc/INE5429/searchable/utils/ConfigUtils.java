package br.ufsc.INE5429.searchable.utils;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class ConfigUtils {
    public static String loadOrCreateKey() {
        Path keyFilePath = Paths.get("resources/key.pem");  // Ensure this path is correct for your environment

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
        keyGenerator.init(128);  // Using 128-bit AES key size
        SecretKey secretKey = keyGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
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
