package Server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class LoginServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String username = request.getParameter("userIDInput");
		String password = request.getParameter("passwordInput");

		PrintWriter out = response.getWriter();
		
		System.out.println(MySQLBridge.msql.authenticate(username, password));
		if (MySQLBridge.msql.authenticate(username, password)) {
			out.println("{\"isValid\":true}");
			
		} else {
			out.println("{\"isValid\":false}");
		}
		out.close();
	}
	
	/*
	 * 
	 * if (MySQLBridge.msql.authenticate(username, password)) {
			response.sendRedirect("napp.html");
		} else {
			response.sendRedirect("");
		}
		*/
	 
}
