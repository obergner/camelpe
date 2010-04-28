/**
 * 
 */
package com.acme.orderplacement.persistence.testsupport.database.spring;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang.Validate;
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

	private static class CacheKey {

		private final String databaseName;

		private final Resource schemaLocation;

		private final Resource dataLocation;

		/**
		 * @param databaseName
		 * @param schemaLocation
		 * @param dataLocation
		 */
		CacheKey(final String databaseName, final Resource schemaLocation,
				final Resource dataLocation) throws IllegalArgumentException {
			Validate.notNull(databaseName, "databaseName");
			Validate.notNull(schemaLocation, "schemaLocation");
			Validate.notNull(dataLocation, "dataLocation");
			this.databaseName = databaseName;
			this.schemaLocation = schemaLocation;
			this.dataLocation = dataLocation;
		}

		/**
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((this.dataLocation == null) ? 0 : this.dataLocation
							.hashCode());
			result = prime
					* result
					+ ((this.databaseName == null) ? 0 : this.databaseName
							.hashCode());
			result = prime
					* result
					+ ((this.schemaLocation == null) ? 0 : this.schemaLocation
							.hashCode());
			return result;
		}

		/**
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(final Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final CacheKey other = (CacheKey) obj;
			if (this.dataLocation == null) {
				if (other.dataLocation != null) {
					return false;
				}
			} else if (!this.dataLocation.getDescription().equals(
					other.dataLocation.getDescription())) {
				return false;
			}
			if (this.databaseName == null) {
				if (other.databaseName != null) {
					return false;
				}
			} else if (!this.databaseName.equals(other.databaseName)) {
				return false;
			}
			if (this.schemaLocation == null) {
				if (other.schemaLocation != null) {
					return false;
				}
			} else if (!this.schemaLocation.getDescription().equals(
					other.schemaLocation.getDescription())) {
				return false;
			}
			return true;
		}

		/**
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "CacheKey [dataLocation=" + this.dataLocation
					+ ", databaseName=" + this.databaseName
					+ ", schemaLocation=" + this.schemaLocation + "]";
		}
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	private static final Map<CacheKey, DataSource> DATA_SOURCE_CACHE = new HashMap<CacheKey, DataSource>();

	private static final String H2_JDBC_DRIVER_CLASS = "org.h2.Driver";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private String databaseName;

	private Resource schemaLocation;

	private Resource dataLocation;

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
		final CacheKey cacheKey = new CacheKey(this.databaseName,
				this.schemaLocation, this.dataLocation);
		DataSource dataSource = DATA_SOURCE_CACHE.get(cacheKey);
		if (dataSource == null) {
			dataSource = initDataSource();
			DATA_SOURCE_CACHE.put(cacheKey, dataSource);
		}

		return dataSource;
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

	private DataSource initDataSource() {
		// create the in-memory database source first
		final DataSource dataSource = createDataSource();
		this.log.debug("Created in-memory H2 database [{}]", this.databaseName);
		// now populate the database by loading the schema and data
		populateDataSource(dataSource);
		this.log
				.debug("Created schema from DDL file [{}]", this.schemaLocation);
		this.log.debug("Loaded data from [{}]", this.dataLocation);

		return dataSource;
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

	private void populateDataSource(final DataSource dataSource) {
		final DatabasePopulator populator = new DatabasePopulator(dataSource);
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
