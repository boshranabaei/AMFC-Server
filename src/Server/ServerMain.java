package Server;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;

public class ServerMain {

	static String WIN_ADRESS="../../AMFC/WebContent/";
	static String LINUX_ADRESS="/root/AMFC/WebContent/";		
	static Map<String,Applicant> chosenApplicants = new HashMap<>();
	
	public static void main(String[] args) throws Exception {
		
		// Increase the input size
		System.setProperty("org.eclipse.jetty.server.Request.maxFormContentSize", "-1");
		System.setProperty("org.eclipse.jetty.servlet.MaxAge", "1");

		// Create a basic Jetty server object
		Server server = new Server(8080);
		

		// Resource handler for the admin web site
		ResourceHandler resource_handler1 = new ResourceHandler();
		resource_handler1.setDirectoriesListed(true);
		
		// Home page to show up
		resource_handler1.setWelcomeFiles(new String[] { "index.html" });

		// Resource handler for the public web site
		ResourceHandler resource_handler2 = new ResourceHandler();
		resource_handler2.setDirectoriesListed(true);
		
		// Home page to show up
		resource_handler2.setWelcomeFiles(new String[] { "index.html" });

		
		// The address of the content(. must be there)
		resource_handler1.setResourceBase(LINUX_ADRESS);
		ContextHandler contextHandler1 = new ContextHandler("/admin");
		contextHandler1.setHandler(resource_handler1);
		
		// The address of the content(. must be there)
		resource_handler2.setResourceBase(LINUX_ADRESS+"public/");
		ContextHandler contextHandler2 = new ContextHandler("/");
		contextHandler2.setHandler(resource_handler2);

		// Adding context handler for Servlets
		ServletContextHandler ServHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

		// ...Login Servlet
		ServHandler.addServlet(LoginServlet.class, "/login");

		// ...Settings Servlet
		ServHandler.addServlet(SettingsServlet.class, "/settings");

		// ...Applicant Servlet
		ServHandler.addServlet(ApplicantServlet.class, "/applicant");
		
		// Adding handlers to the server
		ServHandler.setBaseResource(Resource.newResource(LINUX_ADRESS));
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { contextHandler1, contextHandler2, ServHandler });
		server.setHandler(handlers);

		// Start things up! By using the server.join() the server thread will
		// join with the current thread
		server.start();
		server.dump();
		server.join();

	}
}
