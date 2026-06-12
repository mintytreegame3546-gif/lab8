package server.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;

public final class PasswordHasher {
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final int SALT_BYTES = 16;

    public String hash(String password) {
        Objects.requireNonNull(password, "password must not be null");
        byte[] salt = new byte[SALT_BYTES];
        RANDOM.nextBytes(salt);
        return HexFormat.of().formatHex(salt) + ":" + hash(password, salt);
    }

    public boolean matches(String password, String storedHash) {
        Objects.requireNonNull(password, "password must not be null");
        if (storedHash == null || storedHash.isBlank()) return false;
        String[] parts = storedHash.split(":", 2);
        if (parts.length != 2) return legacyHash(password).equals(storedHash);
        byte[] salt = HexFormat.of().parseHex(parts[0]);
        return MessageDigest.isEqual(hash(password, salt).getBytes(StandardCharsets.UTF_8),
                parts[1].getBytes(StandardCharsets.UTF_8));
    }

    private String legacyHash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-224");
            return HexFormat.of().formatHex(digest.digest(password.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private String hash(String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(salt);
            byte[] hashed = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashed);
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 hashing is unavailable", e);
        }
    }
}
