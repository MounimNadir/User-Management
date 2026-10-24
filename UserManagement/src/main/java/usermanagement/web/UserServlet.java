package usermanagement.web;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import usermanagement.dao.UserDAO;
import usermanagement.model.User;

/**
 * Controller of the MVC application. Routes:
 *
 *   GET  /        or /list   -> list all users
 *   GET  /new                -> empty form
 *   GET  /edit?id=N          -> form pre-filled with user N
 *   POST /insert             -> create a user
 *   POST /update             -> update a user
 *   POST /delete             -> delete a user
 *
 * State-changing operations are POST-only, protected by a CSRF token, and
 * followed by a redirect (Post/Redirect/Get) to avoid duplicate submissions.
 */
@WebServlet(urlPatterns = { "", "/list", "/new", "/edit", "/insert", "/update", "/delete" })
public class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String LIST_VIEW = "/WEB-INF/views/user-list.jsp";
    private static final String FORM_VIEW = "/WEB-INF/views/user-form.jsp";

    private transient UserDAO userDAO;

    @Override
    public void init() {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CsrfTokens.ensureToken(request.getSession());
        try {
            switch (request.getServletPath()) {
                case "/new" -> showForm(request, response, new User("", "", ""), Map.of());
                case "/edit" -> showEditForm(request, response);
                case "/insert", "/update", "/delete" ->
                        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                default -> listUsers(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!CsrfTokens.isValid(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or missing CSRF token");
            return;
        }
        try {
            switch (request.getServletPath()) {
                case "/insert" -> insertUser(request, response);
                case "/update" -> updateUser(request, response);
                case "/delete" -> deleteUser(request, response);
                default -> response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            }
        } catch (SQLException e) {
            throw new ServletException("Database error", e);
        }
    }

    // ---- Handlers -----------------------------------------------------------

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<User> users = userDAO.selectAllUsers();
        request.setAttribute("users", users);
        request.setAttribute("message", popFlash(request));
        request.getRequestDispatcher(LIST_VIEW).forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        Integer id = UserValidator.parseId(request.getParameter("id"));
        User user = (id == null) ? null : userDAO.selectUser(id);
        if (user == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        showForm(request, response, user, Map.of());
    }

    private void insertUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        User user = readUser(request);
        Map<String, String> errors = UserValidator.validate(user);
        if (!errors.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            showForm(request, response, user, errors);
            return;
        }
        userDAO.insertUser(user);
        redirectToList(request, response, "User created.");
    }

    private void updateUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        Integer id = UserValidator.parseId(request.getParameter("id"));
        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user id");
            return;
        }
        User user = readUser(request);
        user.setId(id);
        Map<String, String> errors = UserValidator.validate(user);
        if (!errors.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            showForm(request, response, user, errors);
            return;
        }
        if (!userDAO.updateUser(user)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        redirectToList(request, response, "User updated.");
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Integer id = UserValidator.parseId(request.getParameter("id"));
        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid user id");
            return;
        }
        if (!userDAO.deleteUser(id)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found");
            return;
        }
        redirectToList(request, response, "User deleted.");
    }

    // ---- Helpers ------------------------------------------------------------

    private static User readUser(HttpServletRequest request) {
        return new User(
                trim(request.getParameter("name")),
                trim(request.getParameter("email")),
                trim(request.getParameter("country")));
    }

    private static void showForm(HttpServletRequest request, HttpServletResponse response,
            User user, Map<String, String> errors) throws ServletException, IOException {
        request.setAttribute("user", user);
        request.setAttribute("errors", errors);
        request.getRequestDispatcher(FORM_VIEW).forward(request, response);
    }

    private static void redirectToList(HttpServletRequest request, HttpServletResponse response,
            String message) throws IOException {
        request.getSession().setAttribute("flash", message);
        response.sendRedirect(request.getContextPath() + "/list");
    }

    private static String popFlash(HttpServletRequest request) {
        Object flash = request.getSession().getAttribute("flash");
        request.getSession().removeAttribute("flash");
        return flash == null ? null : flash.toString();
    }

    private static String trim(String value) {
        return value == null ? "" : value.trim();
    }
}
