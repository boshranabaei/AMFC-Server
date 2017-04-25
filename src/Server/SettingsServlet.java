package Server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class SettingsServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String task = request.getParameter("task");
		PrintWriter out = response.getWriter();
		String username = request.getParameter("username");

		HttpSession session = request.getSession();

		if (session.isNew()) {
			session.invalidate();
			out.println("{\"session\":\"denied\"}");
			
		} else if (Calendar.getInstance().getTimeInMillis() - session.getLastAccessedTime() > 12 * 30 * 1000) {
			session.invalidate();
			out.println("{\"session\":\"time out\"}");
			
		} else if (task.equals("changePassword")) {
			String oldPassword = request.getParameter("oldPassword");
			String newPassword = request.getParameter("newPassword");

			if (MySQLBridge.msql.changePassword(username, oldPassword, newPassword)) {
				out.println("{\"mission\":\"accomplished\"}");
			} else {
				out.println("{\"mission\":\"unsuccessful\"}");
			}
			
		} else if (task.equals("updateProfile")) {
			String firstName = request.getParameter("firstName");
			String lastName = request.getParameter("lastName");
			String email = request.getParameter("email");
			String phoneNumber = request.getParameter("phoneNumber");

			if (MySQLBridge.msql.updateProfile(username, firstName, lastName, email, phoneNumber)) {
				out.println("{\"mission\":\"accomplished\"}");
			} else {
				out.println("{\"mission\":\"unsuccessful\"}");
			}
			
		} else if (task.equals("requestAdmins")) {
			Admin[] allAdmins = MySQLBridge.msql.getAdmins();
			Gson gson = new GsonBuilder().create();
			out.println("{\"admins\":" + gson.toJson(allAdmins) + "}");
		}
		out.close();
	}
}
