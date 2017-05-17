package Server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class ApplicantServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		String task = request.getParameter("task");
		Gson gson = new GsonBuilder().create();
		PrintWriter out = response.getWriter();

		if (task.equals("newPublicRegistration")) {
			String jsonString = request.getParameter("applicant");
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("1.txt", true));
		    writer.write(jsonString + "\n\n\n");
		    writer.close();
			
			Applicant newApplicant = gson.fromJson(jsonString, Applicant.class);
			newApplicant.photo = request.getParameter("photo[base64]");
			newApplicant.amfcPointOfContact = "self";
			newApplicant.approvalStatus = "pending";
			newApplicant.approximateAge = 0;
			if (MySQLBridge.msql.addApplicant(newApplicant)) {
				out.println("{\"mission\":\"accomplished\"}");
			} else {
				out.println("{\"mission\":\"unsuccessful\"}");
			}
			
		} else {
			HttpSession session = request.getSession();

			if (session.isNew()) {
				session.invalidate();
				out.println("{\"session\":\"denied\"}");
				
			} else if (Calendar.getInstance().getTimeInMillis() - session.getLastAccessedTime() > 12 * 30 * 1000) {
				session.invalidate();
				out.println("{\"session\":\"time out\"}");

			} else if (task.equals("acceptNewApplicant")) {
				String userIds = request.getParameter("userId");
				
				JSONParser parser = new JSONParser();
				JSONObject json=null;
				try {
					json = (JSONObject) parser.parse(userIds);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				JSONArray userIdsArray = (JSONArray) json.get("userIds");
				
				if (MySQLBridge.msql.acceptApplicant(userIdsArray)) {
					out.println("{\"mission\":\"accomplished\"}");
				} else {
					out.println("{\"mission\":\"unsuccessful\"}");
				}
				
			} else if (task.equals("rejectApplicant")) {
				String userIds = request.getParameter("userId");
				
				JSONParser parser = new JSONParser();
				JSONObject json=null;
				try {
					json = (JSONObject) parser.parse(userIds);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				JSONArray userIdsArray = (JSONArray) json.get("userIds");
				
				if (MySQLBridge.msql.rejectApplicant(userIdsArray)) {
					out.println("{\"mission\":\"accomplished\"}");
				} else {
					out.println("{\"mission\":\"unsuccessful\"}");
				}
				
			} else if (task.equals("newApplicant")) {
				String jsonString = request.getParameter("applicant");
				Applicant newApplicant = gson.fromJson(jsonString, Applicant.class);
				newApplicant.photo = request.getParameter("photo[base64]");
				newApplicant.approvalStatus = "approved";
				if (MySQLBridge.msql.addApplicant(newApplicant)) {
					out.println("{\"mission\":\"accomplished\"}");
				} else {
					out.println("{\"mission\":\"unsuccessful\"}");
				}
			
			} else if (task.equals("requestApplicants")) {
				Applicant[] allApplicants = MySQLBridge.msql.getApplicants("approved");
				out.println("{\"applicants\":" + gson.toJson(allApplicants) + "}");
			
			} else if (task.equals("requestNewApplicants")) {
				Applicant[] newApplicants = MySQLBridge.msql.getApplicants("pending");
				out.println("{\"applicants\":" + gson.toJson(newApplicants) + "}");
			
			} else if (task.equals("selectApplicant")) {
				ServerMain.chosenApplicants.put("...",
						MySQLBridge.msql.getApplicantById(Integer.parseInt(request.getParameter("userId")))[0]);
				out.println("{\"mission\":\"accomplished\"}");
			
			} else if (task.equals("getSelectedApplicant")) {
				out.println("{\"applicant\":" + gson.toJson(ServerMain.chosenApplicants.get("...")) + "}");
			
			} else if (task.equals("updateApplicant")) {
				String jsonString = request.getParameter("applicant");
				Applicant newApplicant = gson.fromJson(jsonString, Applicant.class);
				newApplicant.photo = request.getParameter("photo[base64]");
				if (MySQLBridge.msql.updateApplicant(newApplicant)) {
					out.println("{\"mission\":\"accomplished\"}");
				} else {
					out.println("{\"mission\":\"unsuccessful\"}");
				}
			
			} else if (task.equals("getPairings")) {
				int userId = Integer.parseInt(request.getParameter("userId"));
				int gender = Integer.parseInt(request.getParameter("gender"));
				Pairing[] pairings = MySQLBridge.msql.getPairingsById(userId, gender);
				out.println("{\"pairings\":" + gson.toJson(pairings) + "}");
			
			} else if (task.equals("getCandidates")) {
				int gender = Integer.parseInt(request.getParameter("gender"));
				int userId = Integer.parseInt(request.getParameter("userId"));
				Applicant[] candidates = MySQLBridge.msql.getCandidates(gender, userId);
				out.println("{\"candidates\":" + gson.toJson(candidates) + "}");
			
			} else if (task.equals("updatePairingStatus")) {
				int MUserId = Integer.parseInt(request.getParameter("MUserId"));
				int FUserId = Integer.parseInt(request.getParameter("FUserId"));
				String pairingStatus = request.getParameter("pairingStatus");

				if (MySQLBridge.msql.updatePairingStatus(MUserId, FUserId, pairingStatus))
					out.println("{\"mission\":\"accomplished\"}");
				else
					out.println("{\"mission\":\"unsuccessful\"}");
			
			} else if (task.equals("addPairing")) {
				int MUserId = Integer.parseInt(request.getParameter("MUserId"));
				int FUserId = Integer.parseInt(request.getParameter("FUserId"));
				String director = request.getParameter("director");
				if (MySQLBridge.msql.addPairing(MUserId, FUserId, director))
					out.println("{\"mission\":\"accomplished\"}");
				else
					out.println("{\"mission\":\"unsuccessful\"}");
			} else if (task.equals("removePairing")) {
				int MUserId = Integer.parseInt(request.getParameter("MUserId"));
				int FUserId = Integer.parseInt(request.getParameter("FUserId"));
				if (MySQLBridge.msql.removePairing(MUserId, FUserId))
					out.println("{\"mission\":\"accomplished\"}");
				else
					out.println("{\"mission\":\"unsuccessful\"}");
			
			} else if (task.equals("archiveApplicant")) {
				int userId = Integer.parseInt(request.getParameter("userId"));
				if (MySQLBridge.msql.archiveApplicant(userId))
					out.println("{\"mission\":\"accomplished\"}");
				else
					out.println("{\"mission\":\"unsuccessful\"}");
			
			} else if (task.equals("addComment")) {
				int MUserId = Integer.parseInt(request.getParameter("MUserId"));
				int FUserId = Integer.parseInt(request.getParameter("FUserId"));
				String note = request.getParameter("comment");
				if (MySQLBridge.msql.addNote(MUserId, FUserId, note))
					out.println("{\"mission\":\"accomplished\"}");
				else
					out.println("{\"mission\":\"unsuccessful\"}");
			}
		}
		out.close();
	}
}
