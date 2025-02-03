package az.edu.turing.booking.util;

import az.edu.turing.booking.exception.BaseException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static az.edu.turing.booking.model.enums.ErrorEnum.INVALID_OPERATION;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserUtils {

    public static String hashPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new BaseException(INVALID_OPERATION, e.getMessage());
        }
        byte[] hashedBytes = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedBytes);
    }
}
