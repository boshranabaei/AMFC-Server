package Server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class ApplicantServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String username = request.getParameter("userIDInput");
		String password = request.getParameter("passwordInput");

		PrintWriter out = response.getWriter();
		if (MySQLBridge.msql.authenticate(username, password)) {
			out.println("{\"isValid\":true}");
		} else {
			out.println("{\"isValid\":false}");
		}
		out.close();
	}
}
