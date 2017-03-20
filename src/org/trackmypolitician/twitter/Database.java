package org.trackmypolitician.twitter;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;

import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * A wrapper class to help with database servers
 */
public class Database {

	private final JdbcTemplate jdbcClient;

	/**
	 * Constructs a {@link Database} object to aid communication with a server
	 * at the given address.
	 * 
	 * @param uri
	 *            Address of the database server
	 * @throws URISyntaxException
	 *             If uri is invalid
	 * @throws SQLException
	 */
	public Database(String uri) throws URISyntaxException, SQLException {
		// Helps breakdown URL string
		URI dbUri = new URI(uri);

		// PostgreSQL Driver
		PGSimpleDataSource dataSource = new PGSimpleDataSource();
		dataSource.setServerName(dbUri.getHost());
		dataSource.setPortNumber(dbUri.getPort());
		dataSource.setDatabaseName(dbUri.getPath().substring(1)); 

		// Get user data
		String[] userData = dbUri.getUserInfo().split(":");
		dataSource.setUser(userData[0]);
		dataSource.setPassword(userData[1]);

		jdbcClient = new JdbcTemplate(dataSource);
		
		// TODO: For testing only
		dataSource.getConnection();
	}
}
