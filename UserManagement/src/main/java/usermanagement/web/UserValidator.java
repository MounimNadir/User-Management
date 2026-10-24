package usermanagement.web;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import usermanagement.model.User;

/** Server-side validation of user input (never trust client-side checks alone). */
public final class UserValidator {

    static final int NAME_MAX = 120;
    static final int EMAIL_MAX = 220;
    static final int COUNTRY_MAX = 120;

    // Pragmatic e-mail check: local@domain.tld, no spaces
    private static final Pattern EMAIL = Pattern.compile("^[^\\s@]+@[^\\s@]+\\.[^\\s@]{2,}$");

    private UserValidator() {
    }

    /** Returns field -> error message; an empty map means the user is valid. */
    public static Map<String, String> validate(User user) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (user.getName().isEmpty()) {
            errors.put("name", "Name is required.");
        } else if (user.getName().length() > NAME_MAX) {
            errors.put("name", "Name must be at most " + NAME_MAX + " characters.");
        }

        if (user.getEmail().isEmpty()) {
            errors.put("email", "E-mail is required.");
        } else if (user.getEmail().length() > EMAIL_MAX || !EMAIL.matcher(user.getEmail()).matches()) {
            errors.put("email", "Enter a valid e-mail address.");
        }

        if (user.getCountry().length() > COUNTRY_MAX) {
            errors.put("country", "Country must be at most " + COUNTRY_MAX + " characters.");
        }
        return errors;
    }

    /** Parses a positive integer id; returns {@code null} if missing or invalid. */
    public static Integer parseId(String raw) {
        if (raw == null) {
            return null;
        }
        try {
            int id = Integer.parseInt(raw.trim());
            return id > 0 ? id : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
