package Server;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.resource.Resource;

public class ServerMain {

	static String WIN_ADRESS="../../AMFC/WebContent/";
	static String LINUX_ADRESS="../AMFC/AMFC/WebContent/";		
	
	public static void main(String[] args) throws Exception {
		
		// Create a basic Jetty server object
		Server server = new Server(8080);

		// Resource handler for the web site
		ResourceHandler resource_handler1 = new ResourceHandler();
		resource_handler1.setDirectoriesListed(true);
		
		// Home page to show up
		resource_handler1.setWelcomeFiles(new String[] { "index.html" });

		// The address of the content(. must be there)
		resource_handler1.setResourceBase(WIN_ADRESS);
		ContextHandler contextHandler1 = new ContextHandler("/");
		contextHandler1.setHandler(resource_handler1);

		// Adding context handler for Servlets
		ServletContextHandler ServHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

		// ...Login Servlet
		ServHandler.addServlet(LoginServlet.class, "/login");
		ServHandler.setBaseResource(Resource.newResource(WIN_ADRESS));

		// ...Settings Servlet
		ServHandler.addServlet(SettingsServlet.class, "/settings");
		ServHandler.setBaseResource(Resource.newResource(WIN_ADRESS));

		// ...Applicant Servlet
		ServHandler.addServlet(ApplicantServlet.class, "/applicant");
		ServHandler.setBaseResource(Resource.newResource(WIN_ADRESS));
		
		// Adding handlers to the server
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] { contextHandler1, ServHandler });
		server.setHandler(handlers);

		// Start things up! By using the server.join() the server thread will
		// join with the current thread
		server.start();
		server.dump();
		server.join();

	}
}
