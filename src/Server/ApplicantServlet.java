package Server;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class ApplicantServlet extends HttpServlet {

	public void init() throws ServletException {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String task = request.getParameter("task");
		Gson gson = new GsonBuilder().create();
		System.out.println(task);
		PrintWriter out = response.getWriter();
//		System.out.println(task);
		if( task.equals("newApplicant")){
			String jsonString = request.getParameter("applicant");
			Applicant newApplicant = gson.fromJson(jsonString, Applicant.class);
			if(MySQLBridge.msql.addApplicant(newApplicant)){
				out.println("{\"mission\":\"accomplished\"}");
			}
			else{
				out.println("{\"mission\":\"unsuccessful\"}");
			}
		}
		else if(task.equals("requestApplicants")){
			Applicant [] allApplicants = MySQLBridge.msql.getApplicants();
			out.println("{\"applicants\":"+gson.toJson(allApplicants)+"}");
		}
		else if(task.equals("selectApplicant")){
			ServerMain.chosenApplicants.put("...", MySQLBridge.msql.getApplicantById(Integer.parseInt(request.getParameter("userId"))));
			out.println("{\"mission\":\"accomplished\"}");
		}
		else if(task.equals("getSelectedApplicant")){
			out.println("{\"applicant\":"+gson.toJson(ServerMain.chosenApplicants.get("..."))+"}");
			System.out.println(ServerMain.chosenApplicants.size());
		}
		else if(task.equals("updateApplicant")){
			String jsonString = request.getParameter("applicant");
			Applicant newApplicant = gson.fromJson(jsonString, Applicant.class);
			System.out.println(newApplicant.firstName);
			if(MySQLBridge.msql.updateApplicant(newApplicant)){
				out.println("{\"mission\":\"accomplished\"}");
			}
			else{
				out.println("{\"mission\":\"unsuccessful\"}");
			}
		}
		else if(task.equals("getPairings")){
			int userId = Integer.parseInt(request.getParameter("userId"));
			int gender = Integer.parseInt(request.getParameter("gender"));
			Pairing [] pairings = MySQLBridge.msql.getPairingsById(userId,gender);
			out.println("{\"pairings\":"+gson.toJson(pairings)+"}");
		}
		else if(task.equals("getCandidates")){
			int gender = Integer.parseInt(request.getParameter("gender"));
			Applicant [] candidates = MySQLBridge.msql.getCandidates(gender);
			out.println("{\"candidates\":"+gson.toJson(candidates)+"}");
		}
		out.close();
	}
}
