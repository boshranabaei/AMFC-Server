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
		String username = request.getParameter("userIDInput");
		String password = request.getParameter("passwordInput");

		PrintWriter out = response.getWriter();

		
		if (MySQLBridge.msql.authenticate(username, password)) {
			if (!ServerMain.sessionMan.isStarting())
				try {
					ServerMain.sessionMan.start();
				} catch (Exception e) {
					e.printStackTrace();
				}

			HttpSession session = ServerMain.sessionMan.newHttpSession(request);
			String sessionId = session.getId();
			out.println("{\"isValid\":true,\"sessionId\":\"" + sessionId + "\"}");

//		} else {
//			out.println("{\"isValid\":false}");
//		}
		out.close();
	}

	/*
	 * 
	 * if (MySQLBridge.msql.authenticate(username, password)) {
	 * response.sendRedirect("napp.html"); } else { response.sendRedirect(""); }
	 */

}
