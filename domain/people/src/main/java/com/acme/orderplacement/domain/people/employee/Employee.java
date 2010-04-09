/**
 * 
 */
package com.acme.orderplacement.domain.people.employee;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.acme.orderplacement.domain.people.contact.EmailAddress;
import com.acme.orderplacement.domain.people.customer.PrivateCustomer;
import com.acme.orderplacement.domain.people.person.Person;
import com.acme.orderplacement.domain.support.exception.IllegalDomainObjectStateException;
import com.acme.orderplacement.domain.support.meta.AbstractAuditableDomainObject;
import com.acme.orderplacement.domain.support.meta.AuditInfo;

/**
 * <p>
 * This <tt>Domain Class</tt> represents an <tt>employee</tt> of our fictitious
 * <strong>SOA Order Inc.</strong> company. It is a <tt>Role</tt> a
 * {@link Person <code>Person</code>} may assume.
 * </p>
 * <p>
 * Note that it is legal for a {@link Person <code>Person</code>} to be
 * simultaneously a {@link PrivateCustomer <code>PrivateCustomer</code>} as well
 * as an <code>Employee</code>.
 * </p>
 * <p>
 * <strong>Future Directions</strong> For now, an <code>Employee</code> is a
 * more or less isolated concept, i.e. it is <em>not</em> embedded in rich
 * structures reflecting our company's organizational make up, like
 * <tt>Departments</tt>, <tt>Department Head</tt> etc. This might change in the
 * long-term future. If this moment arrives, it would probably be appropriate to
 * move <code>Employee</code> to a new domain project, i.e.
 * &quot;com.acme.orderplacement.domain.company&quot;.
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Entity
@Table(schema = "PEOPLE", name = "EMPLOYEE")
@SequenceGenerator(name = "ID_SEQ_GEN", sequenceName = "PEOPLE.ID_SEQ_EMPLOYEE")
@org.hibernate.annotations.NamedQueries( {
		@org.hibernate.annotations.NamedQuery(name = Employee.Queries.BY_EMPLOYEE_NUMBER, query = "from com.acme.orderplacement.domain.people.employee.Employee employee where employee.employeeNumber = :employeeNumber"),
		@org.hibernate.annotations.NamedQuery(name = Employee.Queries.BY_EMPLOYEE_NUMBER_LIKE, query = "from com.acme.orderplacement.domain.people.employee.Employee employee where employee.employeeNumber like :employeeNumber"),
		@org.hibernate.annotations.NamedQuery(name = Employee.Queries.BY_WORK_EMAIL_ADDRESS, query = "from com.acme.orderplacement.domain.people.employee.Employee employee where employee.workEmailAddress = :workEmailAddress"),
		@org.hibernate.annotations.NamedQuery(name = Employee.Queries.BY_WORK_EMAIL_ADDRESS_LIKE, query = "from com.acme.orderplacement.domain.people.employee.Employee employee where employee.workEmailAddress like :workEmailAddress)"),
		@org.hibernate.annotations.NamedQuery(name = Employee.Queries.BY_DEPARTMENT, query = "from com.acme.orderplacement.domain.people.employee.Employee employee where employee.department = :department)") })
public class Employee extends AbstractAuditableDomainObject<Long> implements
		Serializable {

	// ------------------------------------------------------------------------
	// Inner class defining query names
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * An inner class defining the <i>names</i> of known HQL queries for
	 * <code>Employee</code>.
	 * </p>
	 * 
	 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
	 * 
	 */
	public final static class Queries {

		public static final String BY_EMPLOYEE_NUMBER = "employee.byEmployeeNumber";

		public static final String BY_EMPLOYEE_NUMBER_LIKE = "employee.byEmployeeNumberLike";

		public static final String BY_WORK_EMAIL_ADDRESS = "employee.byWorkEmailAddress";

		public static final String BY_WORK_EMAIL_ADDRESS_LIKE = "employee.byWorkEmailAddressLike";

		public static final String BY_DEPARTMENT = "employee.byDepartment";
	}

	// ------------------------------------------------------------------------
	// Fields
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = -3791938377243301311L;

	// ------------------------------------------------------------------------
	// Constructors
	// ------------------------------------------------------------------------

	/**
	 * 
	 */
	public Employee() {
		// Intentionally left blank
	}

	/**
	 * @param id
	 */
	public Employee(final Long id) {
		super(id);
	}

	/**
	 * @param version
	 */
	public Employee(final Integer version) {
		super(version);
	}

	/**
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Employee(final AuditInfo auditInfo) throws IllegalArgumentException {
		super(auditInfo);
	}

	/**
	 * @param id
	 * @param version
	 */
	public Employee(final Long id, final Integer version) {
		super(id, version);
	}

	/**
	 * @param id
	 * @param version
	 * @param auditInfo
	 * @throws IllegalArgumentException
	 */
	public Employee(final Long id, final Integer version,
			final AuditInfo auditInfo) throws IllegalArgumentException {
		super(id, version, auditInfo);
	}

	// ------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * This <code>Employee</code>'s unique <tt>Employee Number</tt>, i.e. a
	 * <tt>Business Key</tt> uniquely identifying him/her. This property may
	 * <strong>not</strong> be <code>null</code>.
	 * </p>
	 */
	@NotNull
	@Size(min = 5, max = 30)
	@Basic
	@Column(name = "EMPLOYEE_NUMBER", unique = true, nullable = false, length = 30)
	@org.hibernate.annotations.NaturalId(mutable = false)
	private String employeeNumber;

	/**
	 * @param employeeNumber
	 *            the employeeNumber to set
	 * @throws IllegalArgumentException
	 *             If <code>employeeNumber</code> is <code>null</code> or empty
	 */
	public void setEmployeeNumber(final String employeeNumber)
			throws IllegalArgumentException {
		Validate.notEmpty(employeeNumber, "employeeNumber");
		this.employeeNumber = employeeNumber;
	}

	/**
	 * @return the employeeNumber
	 */
	public String getEmployeeNumber() {
		return this.employeeNumber;
	}

	/**
	 * <p>
	 * The {@link Department <code>Department</code>} this <code>Employee</code>
	 * is a member of. Each <code>Employee</code> is a member of exactly one
	 * <code>Department</code>. Thus, this property may <strong>not</strong> be
	 * <code>null</code>.
	 * </p>
	 */
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "DEPARTMENT", unique = false, nullable = false, length = 30)
	private Department department;

	/**
	 * @param department
	 *            the department to set
	 * @throws IllegalArgumentException
	 *             If <code>department</code> is <code>null</code>
	 */
	public void setDepartment(final Department department)
			throws IllegalArgumentException {
		Validate.notNull(department, "department");
		this.department = department;
	}

	/**
	 * @return the department
	 */
	public Department getDepartment() {
		return this.department;
	}

	// ------------------------------------------------------------------------
	// Associations
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * This <code>Employee</code>'s {@link EmailAddress
	 * <code>EmailAddress</code>} at work. This may <strong>not</strong> be
	 * <code>null</code>.
	 * </p>
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "ID_WORK_EMAIL_ADDRESS", unique = false, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_EMPLOYEE_WORKEMAILADDR")
	private EmailAddress workEmailAddress;

	/**
	 * @param workEmailAddress
	 *            the workEmailAddress to set
	 * @throws IllegalArgumentException
	 *             If <code>workEmailAddress</code> is <code>null</code>
	 */
	public void setWorkEmailAddress(final EmailAddress workEmailAddress)
			throws IllegalArgumentException {
		Validate.notNull(workEmailAddress, "workEmailAddress");
		this.workEmailAddress = workEmailAddress;
	}

	/**
	 * @return the workEmailAddress
	 */
	public EmailAddress getWorkEmailAddress() {
		return this.workEmailAddress;
	}

	// ------------------------------------------------------------------------
	// Collaborations
	// ------------------------------------------------------------------------

	/**
	 * <p>
	 * This <code>Employee</code>'s parent {@link Person <code>Person</code>},
	 * i.e. the real human being that <i>acts</i> as an <code>Employee</code>
	 * when interacting with the <strong>SOA Order Application</strong>.
	 * </p>
	 * <p>
	 * The relationship between a <code>Person</code> and its associated
	 * <code>Employee</code> <tt>role</tt> is bidirectional. This quality has to
	 * be carefully managed.
	 * </p>
	 */
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "ID_PERSON", referencedColumnName = "ID", unique = true, nullable = false)
	@org.hibernate.annotations.ForeignKey(name = "FK_EMPLOYEE_PERSON")
	private Person parent;

	/**
	 * Getter of the property <tt>parent</tt>
	 * 
	 * @throws IllegalDomainObjectStateException
	 *             If {@link #parent <code>parent</code>} is <code>null</code>.
	 *             An <code>Employee</code> cannot live without its parent
	 *             {@link Person <code>Person</code>}.
	 * 
	 * @return Returns the parent.
	 */
	public Person getParent() throws IllegalDomainObjectStateException {
		if (this.parent == null) {
			throw new IllegalDomainObjectStateException(
					"An Employee MUST have an associated parent Person [parent = <null>]");
		}

		return this.parent;
	}

	/**
	 * Setter of the property <tt>parent</tt>
	 * 
	 * @param parent
	 *            The parent to set.
	 * @throws IllegalArgumentException
	 *             If <code>parent</code> is <code>null</code>
	 */
	public void setParent(final Person parent) throws IllegalArgumentException {
		Validate.notNull(parent, "parent");
		this.parent = parent;
	}

	/**
	 * <p>
	 * Test whether this <code>Employee</code> has a parent {@link Person
	 * <code>Person</code>}.
	 * </p>
	 * 
	 * @return
	 */
	public boolean hasParent() {

		return this.parent != null;
	}

	// ------------------------------------------------------------------------
	// Object infrastructure
	// ------------------------------------------------------------------------

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = super.hashCode();
		result = PRIME
				* result
				+ ((this.employeeNumber == null) ? 0 : this.employeeNumber
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
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Employee other = (Employee) obj;
		if (this.employeeNumber == null) {
			if (other.employeeNumber != null) {
				return false;
			}
		} else if (!this.employeeNumber.equals(other.employeeNumber)) {
			return false;
		}
		return true;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString()).append(
				"employeeNumber", this.employeeNumber).append("department",
				this.department).append("workEmailAddress",
				this.workEmailAddress).append("parent", this.parent).toString();
	}

}
