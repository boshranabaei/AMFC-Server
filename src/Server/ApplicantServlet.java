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

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		
		String task = request.getParameter("task");
		Gson gson = new GsonBuilder().create();
		System.out.println(task);
		PrintWriter out = response.getWriter();
		
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
		out.close();
	}
}
