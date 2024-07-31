package com.edufocus.edufocus.user.model.entity;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {

    // Hash a password
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verify a password
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}