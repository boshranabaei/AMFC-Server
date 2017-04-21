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
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.session.JDBCSessionManager;
import org.eclipse.jetty.server.session.JDBCSessionIdManager;

public class ServerMain {

	static String WIN_ADRESS="../../AMFC/WebContent/";
	static String LINUX_ADRESS="../AMFC/AMFC/WebContent/";		
	static Map<String,Applicant> chosenApplicants = new HashMap<>();
	static JDBCSessionIdManager idMan;
	static SessionManager sessionMan;
	
	public static void main(String[] args) throws Exception {
		
		// Increase the input size
		System.setProperty("org.eclipse.jetty.server.Request.maxFormContentSize", "-1");

		// Create a basic Jetty server object
		Server server = new Server(8080);

		// =======Session Management=======
		// There are two components to session management in Jetty:
		// 1-The session ID manager ensures that session IDs are unique
		idMan = new JDBCSessionIdManager(server);
		idMan.setDriverInfo("org.sqlite.JDBC", "jdbc:sqlite:C:/5-Java/JettyServer/AMFC-Server/db/amfc.db");

		// 2-The session manager handles the session lifecycle
		// (create/update/invalidate/expire)
		sessionMan = new JDBCSessionManager();
		sessionMan.setIdManager(idMan);
		sessionMan.setMaxInactiveInterval(0);
//		sessionMan.setSessionIdManager(server.getSessionIdManager());
		
		server.setSessionIdManager(idMan);
		

		// Resource handler for the web site
		ResourceHandler resource_handler1 = new ResourceHandler();
		resource_handler1.setDirectoriesListed(true);
		
		// Home page to show up
		resource_handler1.setWelcomeFiles(new String[] { "index.html" });

		// The address of the content(. must be there)
		resource_handler1.setResourceBase(WIN_ADRESS);
		ContextHandler contextHandler = new ContextHandler("/");
		contextHandler.setHandler(resource_handler1);

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
		handlers.setHandlers(new Handler[] { contextHandler, ServHandler });
		server.setHandler(handlers);

		// Start things up! By using the server.join() the server thread will
		// join with the current thread
		server.start();
		server.dump();
		server.join();

	}
}
