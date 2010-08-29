/**
 * 
 */
package com.acme.orderplacement.jee.framework.common.jndi;

import java.lang.ref.WeakReference;
import java.util.Enumeration;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameAlreadyBoundException;
import javax.naming.NameClassPair;
import javax.naming.NameNotFoundException;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.NotContextException;

import org.apache.commons.lang.Validate;

/**
 * <p>
 * A <code>NestedJndiBinding</code> encapsulates binding an arbitrary
 * {@link #getResource() <code>Resource</code>} into a {@link #getRootContext()
 * <tt>JNDI tree</tt>} under an arbitrary {@link #getJndiName()
 * <tt>JNDI name</tt>}. Contrary to
 * {@link javax.naming.Context#bind(Name, Object)
 * <code>javax.naming.Context.bind(Name, Object)</code>} calling {@link #bind()
 * <code>bind()</code>} on a <code>NestedJndiBinding</code> will create all
 * intermediate sub contexts.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 *         TODO: CURRENTLY NOT USED
 */
public class NestedJndiBinding {

	// -------------------------------------------------------------------------
	// Fields
	// -------------------------------------------------------------------------

	private final String jndiName;

	private final WeakReference<Object> resource;

	private final WeakReference<Context> rootContext;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * @param jndiName
	 * @param resource
	 * @param rootContext
	 * @throws IllegalArgumentException
	 */
	public NestedJndiBinding(final String jndiName, final Object resource,
			final Context rootContext) throws IllegalArgumentException {
		Validate.notEmpty(jndiName, "jndiName");
		Validate.notNull(resource, "resource");
		Validate.notNull(rootContext, "rootContext");
		this.jndiName = jndiName;
		this.resource = new WeakReference<Object>(resource);
		this.rootContext = new WeakReference<Context>(rootContext);
	}

	// -------------------------------------------------------------------------
	// Properties
	// -------------------------------------------------------------------------

	/**
	 * @return the jndiName
	 */
	public final String getJndiName() {
		return this.jndiName;
	}

	/**
	 * @return the resource
	 */
	public final Object getResource() {
		return this.resource.get();
	}

	/**
	 * @return the rootContext
	 */
	public final Context getRootContext() {
		return this.rootContext.get();
	}

	// -------------------------------------------------------------------------
	// API
	// -------------------------------------------------------------------------

	/**
	 * @throws NamingException
	 * @throws IllegalStateException
	 */
	public void bind() throws NamingException, IllegalStateException {
		final Context localRootContext = getRootContext();
		if (localRootContext == null) {

			throw new IllegalStateException(
					"No root context present (may already have been garbage collected)");
		}

		final Object localResource = getResource();
		if (localResource == null) {

			throw new IllegalStateException(
					"No resource to bind (may already have been garbage collected)");
		}

		final Enumeration<String> components = localRootContext.getNameParser(
				getJndiName()).parse(getJndiName()).getAll();
		Context lastSubcontext = localRootContext;
		while (components.hasMoreElements()) {
			final String comp = components.nextElement();
			if (components.hasMoreElements()) {
				/*
				 * We haven't reached our leaf yet, so we create an intermediate
				 * sub context.
				 */
				lastSubcontext = createSubcontextIfNotExists(lastSubcontext,
						comp);
			} else {
				lastSubcontext.bind(comp, localResource);
			}
		}
	}

	/**
	 * @throws NamingException
	 */
	public void unbind() throws NamingException {
		try {
			final Context rootContext = getRootContext();
			if (rootContext == null) {

				return;
			}
			final Name parsedJndiName = rootContext
					.getNameParser(getJndiName()).parse(getJndiName());
			rootContext.unbind(getJndiName());
			parsedJndiName.remove(parsedJndiName.size() - 1);
			while (parsedJndiName.size() > 0) {
				final String lastRemainingComponent = (String) parsedJndiName
						.remove(parsedJndiName.size() - 1);
				final Context lastButOneContext = (Context) rootContext
						.lookup(parsedJndiName);
				if (contextIsEmptyOrContainsOnly(lastButOneContext,
						lastRemainingComponent)) {
					lastButOneContext.destroySubcontext(lastRemainingComponent);
				} else {
					break;
				}
			}
		} catch (final NameNotFoundException e) {
			/*
			 * Ignore
			 */
		}
	}

	// -------------------------------------------------------------------------
	// equals(), hashCode(), toString()
	// -------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.jndiName == null) ? 0 : this.jndiName.hashCode());
		result = prime * result
				+ ((this.resource == null) ? 0 : this.resource.hashCode());
		result = prime
				* result
				+ ((this.rootContext == null) ? 0 : this.rootContext.hashCode());
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
		final NestedJndiBinding other = (NestedJndiBinding) obj;
		if (this.jndiName == null) {
			if (other.jndiName != null) {
				return false;
			}
		} else if (!this.jndiName.equals(other.jndiName)) {
			return false;
		}
		if (this.resource == null) {
			if (other.resource != null) {
				return false;
			}
		} else if (!this.resource.equals(other.resource)) {
			return false;
		}
		if (this.rootContext == null) {
			if (other.rootContext != null) {
				return false;
			}
		} else if (!this.rootContext.equals(other.rootContext)) {
			return false;
		}
		return true;
	}

	/**
	 * @autogenerated by CodeHaggis (http://sourceforge.net/projects/haggis)
	 * @overwrite toString()
	 * @return String returns this object in a String
	 */
	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("NestedJndiBinding::[");
		sb.append(super.toString() + " ");
		sb.append(" jndiName:=");
		sb.append(this.jndiName);
		sb.append(" resource:=");
		sb.append(this.resource);
		sb.append(" rootContext:=");
		sb.append(this.rootContext);
		sb.append("]");
		return sb.toString();
	}

	// -------------------------------------------------------------------------
	// Internal
	// -------------------------------------------------------------------------

	private Context createSubcontextIfNotExists(final Context lastSubcontext,
			final String comp) throws NamingException {
		try {
			return lastSubcontext.createSubcontext(comp);
		} catch (final NameAlreadyBoundException e) {
			final Object alreadyBound = lastSubcontext.lookup(comp);
			if (!(alreadyBound instanceof Context)) {

				throw new NotContextException(String.format(
						"Expected a subcontext to be bound to [%1$s] under the JNDI name [%2$s] "
								+ "yet got a [%3$s]", lastSubcontext
								.getNameInNamespace(), comp, alreadyBound
								.getClass().getName()));
			}

			return Context.class.cast(alreadyBound);
		}
	}

	private boolean contextIsEmptyOrContainsOnly(final Context ctx,
			final String component) throws NamingException {
		final NamingEnumeration<NameClassPair> entries = ctx.list("");
		int numEntries = 0;
		boolean found = false;
		while (entries.hasMoreElements()) {
			numEntries++;
			if (numEntries > 1) {
				break;
			}
			final NameClassPair currentEntry = entries.nextElement();
			currentEntry.setRelative(true);
			if (currentEntry.getName().equals(component)) {
				found = true;
			}
		}

		return ((numEntries == 0) || ((numEntries == 1) && found));
	}
}
