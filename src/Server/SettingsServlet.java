package Server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class SettingsServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String username = request.getParameter("username");
		String oldPassword = request.getParameter("oldPassword");
		String newPassword = request.getParameter("newPassword");

		PrintWriter out = response.getWriter();
		if (MySQLBridge.msql.changePassword(username, oldPassword, newPassword)) {
			out.println("{\"mission\":\"accomplished\"}");
		} else {
			out.println("{\"mission\":\"unsuccessful\"}");
		}
		out.close();
	}
	
}
