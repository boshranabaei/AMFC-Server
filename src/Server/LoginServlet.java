package Server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String task = request.getParameter("task");
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession();

		if (task.equals("authenticate")) {
			String username = request.getParameter("userIDInput");
			String password = request.getParameter("passwordInput");
			if (MySQLBridge.msql.authenticate(username, password)) {
				out.println("{\"isValid\":true}");
			} else {
				out.println("{\"isValid\":false}");
			}

		}else if (task.equals("logout")){
			// The session is tied to a single browser, and all frames/tabs
			// opened in this browser share the same session. If you exit
			// the
			// browser, you lose the session.
			session.invalidate();
			out.println("{\"mission\":\"accomplished\"}");
		}	
		out.close();
	}
}