/**
 * 
 */
package com.acme.orderplacement.persistence.support.jpa.hibernate.spring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceProvider;

import org.hibernate.cfg.Environment;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.DerbyDialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.InformixDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.SybaseASE15Dialect;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.ejb.HibernateEntityManagerFactory;
import org.hibernate.event.PostDeleteEventListener;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEventListener;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;

import com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean;

/**
 * <p>
 * A custom <a href="http://www.springsource.org"><tt>Spring</tt></a>
 * {@link JpaVendorAdapter <code>JpaVendorAdapter</code>} using a
 * {@link ConfigurableHibernatePersistenceProviderBean
 * <code>ConfigurableHibernatePersistenceProviderBean</code>} as its
 * {@link javax.persistence.PersistenceProvider
 * <code>javax.persistence.PersistenceProvider</code>} implementation. It
 * therefore allows for configuring the underlying <a
 * href="http://www.hibernate.org"><tt>Hibernate</tt></a> <tt>JPA</tt>
 * implementation in a vendor specific manner.
 * </p>
 * <p>
 * <strong>Configuration options</strong> This <code>JpaVendorAdapter</code>
 * delegate to a <code>ConfigurableHibernatePersistenceProviderBean</code> and
 * therefore offers the same configuration options beyond those offered by the
 * &quot;standard&quot;
 * {@link org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
 * <code>org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter</code>}.
 * </p>
 * 
 * @see ConfigurableHibernatePersistenceProviderBean
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
public class ConfigurableHibernateJpaVendorAdapter extends
		AbstractJpaVendorAdapter {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final ConfigurableHibernatePersistenceProviderBean persistenceProvider = new ConfigurableHibernatePersistenceProviderBean();

	private final JpaDialect jpaDialect = new HibernateJpaDialect();

	// -------------------------------------------------------------------------
	// org.springframework.orm.jpa.JpaVendorAdapter
	// -------------------------------------------------------------------------

	/**
	 * @see org.springframework.orm.jpa.JpaVendorAdapter#getPersistenceProvider()
	 */
	public PersistenceProvider getPersistenceProvider() {
		return this.persistenceProvider;
	}

	// -------------------------------------------------------------------------
	// org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter
	// -------------------------------------------------------------------------

	/**
	 * @see org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter#getJpaDialect()
	 */
	@Override
	public JpaDialect getJpaDialect() {
		return this.jpaDialect;
	}

	/**
	 * @see org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter#getEntityManagerFactoryInterface()
	 */
	@Override
	public Class<? extends EntityManagerFactory> getEntityManagerFactoryInterface() {
		return HibernateEntityManagerFactory.class;
	}

	/**
	 * @see org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter#getEntityManagerInterface()
	 */
	@Override
	public Class<? extends EntityManager> getEntityManagerInterface() {
		return HibernateEntityManager.class;
	}

	/**
	 * @see org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter#getPersistenceProviderRootPackage()
	 */
	@Override
	public String getPersistenceProviderRootPackage() {
		return "org.hibernate";
	}

	/**
	 * @see org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter#getJpaPropertyMap()
	 */
	@Override
	public Map<String, ?> getJpaPropertyMap() {
		final Map<String, Object> jpaProperties = new HashMap<String, Object>();

		if (getDatabasePlatform() != null) {
			jpaProperties.put(Environment.DIALECT, getDatabasePlatform());
		} else if (getDatabase() != null) {
			final Class<? extends Dialect> databaseDialectClass = determineDatabaseDialectClass(getDatabase());
			if (databaseDialectClass != null) {
				jpaProperties.put(Environment.DIALECT, databaseDialectClass
						.getName());
			}
		}

		if (isGenerateDdl()) {
			jpaProperties.put(Environment.HBM2DDL_AUTO, "update");
		}
		if (isShowSql()) {
			jpaProperties.put(Environment.SHOW_SQL, "true");
		}

		return jpaProperties;
	}

	// -------------------------------------------------------------------------
	// com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean
	// -------------------------------------------------------------------------

	/**
	 * @param postDeleteEventListeners
	 * @throws IllegalArgumentException
	 * @see com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostDeleteEventListeners(java.util.List)
	 */
	public void setAdditionalPostDeleteEventListeners(
			final List<PostDeleteEventListener> postDeleteEventListeners)
			throws IllegalArgumentException {
		this.persistenceProvider
				.setAdditionalPostDeleteEventListeners(postDeleteEventListeners);
	}

	/**
	 * @param postDeleteEventListeners
	 * @throws IllegalArgumentException
	 * @see com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostDeleteEventListeners(org.hibernate.event.PostDeleteEventListener[])
	 */
	public void setAdditionalPostDeleteEventListeners(
			final PostDeleteEventListener... postDeleteEventListeners)
			throws IllegalArgumentException {
		this.persistenceProvider
				.setAdditionalPostDeleteEventListeners(postDeleteEventListeners);
	}

	/**
	 * @param postInsertEventListeners
	 * @throws IllegalArgumentException
	 * @see com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostInsertEventListeners(java.util.List)
	 */
	public void setAdditionalPostInsertEventListeners(
			final List<PostInsertEventListener> postInsertEventListeners)
			throws IllegalArgumentException {
		this.persistenceProvider
				.setAdditionalPostInsertEventListeners(postInsertEventListeners);
	}

	/**
	 * @param postInsertEventListeners
	 * @throws IllegalArgumentException
	 * @see com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostInsertEventListeners(org.hibernate.event.PostInsertEventListener[])
	 */
	public void setAdditionalPostInsertEventListeners(
			final PostInsertEventListener... postInsertEventListeners)
			throws IllegalArgumentException {
		this.persistenceProvider
				.setAdditionalPostInsertEventListeners(postInsertEventListeners);
	}

	/**
	 * @param postUpdateEventListeners
	 * @throws IllegalArgumentException
	 * @see com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostUpdateEventListeners(java.util.List)
	 */
	public void setAdditionalPostUpdateEventListeners(
			final List<PostUpdateEventListener> postUpdateEventListeners)
			throws IllegalArgumentException {
		this.persistenceProvider
				.setAdditionalPostUpdateEventListeners(postUpdateEventListeners);
	}

	/**
	 * @param postUpdateEventListeners
	 * @throws IllegalArgumentException
	 * @see com.acme.orderplacement.persistence.support.jpa.hibernate.ConfigurableHibernatePersistenceProviderBean#setAdditionalPostUpdateEventListeners(org.hibernate.event.PostUpdateEventListener[])
	 */
	public void setAdditionalPostUpdateEventListeners(
			final PostUpdateEventListener... postUpdateEventListeners)
			throws IllegalArgumentException {
		this.persistenceProvider
				.setAdditionalPostUpdateEventListeners(postUpdateEventListeners);
	}

	// -------------------------------------------------------------------------
	// Protected
	// -------------------------------------------------------------------------

	/**
	 * Determine the Hibernate database dialect class for the given target
	 * database.
	 * 
	 * @param database
	 *            the target database
	 * @return the Hibernate database dialect class, or
	 *         <code>null<code> if none found
	 */
	protected Class<? extends Dialect> determineDatabaseDialectClass(
			final Database database) {
		switch (database) {
		case DB2:
			return DB2Dialect.class;
		case DERBY:
			return DerbyDialect.class;
		case H2:
			return H2Dialect.class;
		case HSQL:
			return HSQLDialect.class;
		case INFORMIX:
			return InformixDialect.class;
		case MYSQL:
			return MySQLDialect.class;
		case ORACLE:
			return Oracle10gDialect.class;
		case POSTGRESQL:
			return PostgreSQLDialect.class;
		case SQL_SERVER:
			return SQLServerDialect.class;
		case SYBASE:
			return SybaseASE15Dialect.class;
		default:
			return null;
		}
	}
}
