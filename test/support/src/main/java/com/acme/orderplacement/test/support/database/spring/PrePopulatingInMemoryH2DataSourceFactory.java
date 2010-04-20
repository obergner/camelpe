/**
 * 
 */
package com.acme.orderplacement.test.support.database.spring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * <p>
 * A <tt>Spring</tt> {@link FactoryBean <code>FactoryBean</code>} for producing
 * {@link DataSource <code>DataSource</code>}s to be used in integration test
 * scenarios. A <code>DataSource</code> - or rather the database it points to -
 * returned by this factory will be initialized using DDL statements found in a
 * configurable file and populated with test data found in another configurable
 * file.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public final class PrePopulatingInMemoryH2DataSourceFactory implements
		FactoryBean<DataSource> {

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final String H2_JDBC_DRIVER_CLASS = "org.h2.Driver";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private String databaseName;

	private Resource schemaLocation;

	private Resource dataLocation;

	/**
	 * The object created by this factory.
	 */
	private DataSource dataSource;

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * @param databaseName
	 *            the databaseName to set
	 */
	public void setDatabaseName(final String databaseName) {
		this.databaseName = databaseName;
	}

	/**
	 * @param schemaLocation
	 *            the schemaLocation to set
	 */
	public void setSchemaLocation(final Resource schemaLocation) {
		this.schemaLocation = schemaLocation;
	}

	/**
	 * @param dataLocation
	 *            the dataLocation to set
	 */
	public void setDataLocation(final Resource dataLocation) {
		this.dataLocation = dataLocation;
	}

	// ------------------------------------------------------------------------
	// org.springframework.beans.factory.FactoryBean
	// ------------------------------------------------------------------------

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public DataSource getObject() throws Exception {
		if (this.dataSource == null) {
			initDataSource();
		}

		return this.dataSource;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<?> getObjectType() {
		return DataSource.class;
	}

	/**
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton() {
		return true;
	}

	// ------------------------------------------------------------------------
	// Internal
	// ------------------------------------------------------------------------

	private void initDataSource() {
		// create the in-memory database source first
		this.dataSource = createDataSource();
		this.log.debug("Created in-memory H2 database [{}]", this.databaseName);
		// now populate the database by loading the schema and data
		populateDataSource();
		this.log
				.debug("Created schema from DDL file [{}]", this.schemaLocation);
		this.log.debug("Loaded data from [{}]", this.dataLocation);
	}

	private DataSource createDataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		// use the H2 JDBC driver
		dataSource.setDriverClassName(H2_JDBC_DRIVER_CLASS);
		// have it create an in-memory database that is not emptied when the
		// last connection is closed
		dataSource.setUrl("jdbc:h2:mem:" + this.databaseName
				+ ";DB_CLOSE_DELAY=-1");
		dataSource.setUsername("sa");
		dataSource.setPassword("");
		return dataSource;
	}

	private void populateDataSource() {
		final DatabasePopulator populator = new DatabasePopulator(
				this.dataSource);
		populator.populate();
	}

	// ------------------------------------------------------------------------
	// DatabasePopulator
	// ------------------------------------------------------------------------

	/**
	 * Populates a data source with data.
	 */
	private class DatabasePopulator {

		private final DataSource dataSource;

		/**
		 * Creates a new database populator.
		 * 
		 * @param dataSource
		 *            the data source that will be populated.
		 */
		public DatabasePopulator(final DataSource dataSource) {
			this.dataSource = dataSource;
		}

		/**
		 * Populate the database by creating the database schema from
		 * 'schema.sql' and inserting the data in 'testdata.sql'.
		 */
		public void populate() {
			Connection connection = null;
			try {
				connection = this.dataSource.getConnection();
				createDatabaseSchema(connection);
				insertTestData(connection);
			} catch (final SQLException e) {
				throw new RuntimeException(
						"SQL exception occurred acquiring connection", e);
			} finally {
				if (connection != null) {
					try {
						connection.close();
					} catch (final SQLException e) {
					}
				}
			}
		}

		// create the application's database schema (tables, indexes, etc.)
		private void createDatabaseSchema(final Connection connection) {
			try {
				final String sql = parseSqlIn(PrePopulatingInMemoryH2DataSourceFactory.this.schemaLocation);
				executeSql(sql, connection);
			} catch (final IOException e) {
				throw new RuntimeException(
						"I/O exception occurred accessing the database schema file",
						e);
			} catch (final SQLException e) {
				throw new RuntimeException(
						"SQL exception occurred exporting database schema", e);
			}
		}

		// populate the tables with data
		private void insertTestData(final Connection connection) {
			try {
				final String sql = parseSqlIn(PrePopulatingInMemoryH2DataSourceFactory.this.dataLocation);
				executeSql(sql, connection);
			} catch (final IOException e) {
				throw new RuntimeException(
						"I/O exception occurred accessing the data file", e);
			} catch (final SQLException e) {
				throw new RuntimeException(
						"SQL exception occurred loading data", e);
			}
		}

		// utility method to read a .sql txt input stream
		private String parseSqlIn(final Resource resource) throws IOException {
			InputStream is = null;
			try {
				is = resource.getInputStream();
				final BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				final StringWriter sw = new StringWriter();
				final BufferedWriter writer = new BufferedWriter(sw);

				for (int c = reader.read(); c != -1; c = reader.read()) {
					writer.write(c);
				}
				writer.flush();
				return sw.toString();

			} finally {
				if (is != null) {
					is.close();
				}
			}
		}

		// utility method to run the parsed sql
		private void executeSql(final String sql, final Connection connection)
				throws SQLException {
			final Statement statement = connection.createStatement();
			statement.execute(sql);
		}
	}
}
