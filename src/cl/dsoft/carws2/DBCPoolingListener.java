/**
 * 
 */
package cl.dsoft.carws2;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.sql.DataSource;

/**
 * @author lfhernandez
 *
 */
public class DBCPoolingListener implements ServletContextListener {

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		  try {
			    // Obtain our environment naming context
			    Context envCtx = (Context) new InitialContext().
			    lookup("java:comp/env");
			    
			    System.out.println("init!");

			    // Look up our data source
			    DataSource  ds = (DataSource) envCtx.lookup
			       ("jdbc/CarDB");
			    
			    System.out.println("DataSource: " + ds);

			    arg0.getServletContext().setAttribute
			      ("DBCPool", ds);
			   } catch(NamingException e){ e.printStackTrace();
			  }
	}

}
