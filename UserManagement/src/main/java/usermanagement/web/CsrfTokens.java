package usermanagement.web;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Synchronizer-token CSRF protection: one random token per session, embedded
 * in every form as a hidden field and checked on each POST.
 */
public final class CsrfTokens {

    public static final String SESSION_ATTRIBUTE = "csrfToken";
    public static final String PARAMETER = "csrfToken";

    private static final SecureRandom RANDOM = new SecureRandom();

    private CsrfTokens() {
    }

    /** Creates the session token if it does not exist yet. */
    public static void ensureToken(HttpSession session) {
        if (session.getAttribute(SESSION_ATTRIBUTE) == null) {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            session.setAttribute(SESSION_ATTRIBUTE,
                    Base64.getUrlEncoder().withoutPadding().encodeToString(bytes));
        }
    }

    /** Constant-time comparison of the submitted token with the session token. */
    public static boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        Object expected = session.getAttribute(SESSION_ATTRIBUTE);
        String actual = request.getParameter(PARAMETER);
        if (expected == null || actual == null) {
            return false;
        }
        return MessageDigest.isEqual(
                expected.toString().getBytes(StandardCharsets.UTF_8),
                actual.getBytes(StandardCharsets.UTF_8));
    }
}
