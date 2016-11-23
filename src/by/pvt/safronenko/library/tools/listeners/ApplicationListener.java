package by.pvt.safronenko.library.tools.listeners;

import by.pvt.safronenko.library.tools.datasource.DataSource;
import by.pvt.safronenko.library.tools.datasource.DataSourceHolder;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Application listener.
 */
public class ApplicationListener implements ServletContextListener {

	// Logger.
	private static final Logger logger = Logger.getLogger(ApplicationListener.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		// Executed when application is deployed/started by container.

		ServletContext servletContext = servletContextEvent.getServletContext();
		String jdbcDriver = servletContext.getInitParameter("jdbcDriver");
		String jdbcUrl = servletContext.getInitParameter("jdbcUrl");
		String jdbcUser = servletContext.getInitParameter("jdbcUser");
		String jdbcPassword = servletContext.getInitParameter("jdbcPassword");

		logger.info(String.format("Connection pool to %s with %s / password len: %d ...", jdbcUrl, jdbcUser,
				jdbcPassword.length()));

		try {
			// Create and remember new entity manager.
			DataSourceHolder.setDataSource(new DataSource(jdbcDriver, jdbcUrl, jdbcUser, jdbcPassword));
			logger.info("Done with connection pool.");
		} catch (RuntimeException e) {
			// Log and re-throw.
			logger.error("Problem while creating entity manager.", e);
			throw e;
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// Executed when application is un-deployed/stopped by container.

		try {
			// Get entity manager.
			DataSource em = DataSourceHolder.getDataSource();
			// Forget it.
			DataSourceHolder.setDataSource(null);
			// Shutdown.
			em.shutdown();
		} catch (Exception e) {
			logger.error("Problem while shutting down entity manager.", e);
		}
	}
}
