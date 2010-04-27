/**
 * 
 */
package com.acme.orderplacement.persistence.testsupport.scontext;

import javax.sql.DataSource;

/**
 * <p>
 * TODO: Insert short summary for PerJvmDataSourceHolder
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
class PerJvmDataSourceHolder {

	static final PerJvmDataSourceHolder INSTANCE = new PerJvmDataSourceHolder();

	private DataSource dataSource;

	/**
	 * 
	 */
	private PerJvmDataSourceHolder() {
		// Intentionally left blank
	}

	boolean holdsDataSource() {
		return this.dataSource != null;
	}

	/**
	 * @param dataSource
	 * @throws IllegalStateException
	 */
	void setDataSource(final DataSource dataSource)
			throws IllegalStateException {
		if (this.dataSource != null) {
			throw new IllegalStateException("DataSource has already been set");
		}
		this.dataSource = dataSource;
	}

	/**
	 * @return the dataSource
	 */
	DataSource getDataSource() {
		return this.dataSource;
	}

}
