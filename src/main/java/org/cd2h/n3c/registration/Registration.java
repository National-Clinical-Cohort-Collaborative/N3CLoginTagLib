package org.cd2h.n3c.registration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")

public class Registration extends N3CLoginTagLibTagSupport {

	static Registration currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Registration.class);

	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	String email = null;
	String officialFirstName = null;
	String officialLastName = null;
	String firstName = null;
	String lastName = null;
	String institution = null;
	Date created = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			RegistrationIterator theRegistrationIterator = (RegistrationIterator)findAncestorWithClass(this, RegistrationIterator.class);

			if (theRegistrationIterator != null) {
				email = theRegistrationIterator.getEmail();
			}

			if (theRegistrationIterator == null && email == null) {
				// no email was provided - the default is to assume that it is a new Registration and to generate a new email
				email = null;
				log.debug("generating new Registration " + email);
				insertEntity();
			} else {
				// an iterator or email was provided as an attribute - we need to load a Registration from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select official_first_name,official_last_name,first_name,last_name,institution,created from n3c_admin.registration where email = ?");
				stmt.setString(1,email);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (officialFirstName == null)
						officialFirstName = rs.getString(1);
					if (officialLastName == null)
						officialLastName = rs.getString(2);
					if (firstName == null)
						firstName = rs.getString(3);
					if (lastName == null)
						lastName = rs.getString(4);
					if (institution == null)
						institution = rs.getString(5);
					if (created == null)
						created = rs.getTimestamp(6);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving email " + email);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update n3c_admin.registration set official_first_name = ?, official_last_name = ?, first_name = ?, last_name = ?, institution = ?, created = ? where email = ?");
				stmt.setString(1,officialFirstName);
				stmt.setString(2,officialLastName);
				stmt.setString(3,firstName);
				stmt.setString(4,lastName);
				stmt.setString(5,institution);
				stmt.setTimestamp(6,created == null ? null : new java.sql.Timestamp(created.getTime()));
				stmt.setString(7,email);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			if (officialFirstName == null)
				officialFirstName = "";
			if (officialLastName == null)
				officialLastName = "";
			if (firstName == null)
				firstName = "";
			if (lastName == null)
				lastName = "";
			if (institution == null)
				institution = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into n3c_admin.registration(email,official_first_name,official_last_name,first_name,last_name,institution,created) values (?,?,?,?,?,?,?)");
			stmt.setString(1,email);
			stmt.setString(2,officialFirstName);
			stmt.setString(3,officialLastName);
			stmt.setString(4,firstName);
			stmt.setString(5,lastName);
			stmt.setString(6,institution);
			stmt.setTimestamp(7,created == null ? null : new java.sql.Timestamp(created.getTime()));
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
	}

	public String getEmail () {
		if (commitNeeded)
			return "";
		else
			return email;
	}

	public void setEmail (String email) {
		this.email = email;
	}

	public String getActualEmail () {
		return email;
	}

	public String getOfficialFirstName () {
		if (commitNeeded)
			return "";
		else
			return officialFirstName;
	}

	public void setOfficialFirstName (String officialFirstName) {
		this.officialFirstName = officialFirstName;
		commitNeeded = true;
	}

	public String getActualOfficialFirstName () {
		return officialFirstName;
	}

	public String getOfficialLastName () {
		if (commitNeeded)
			return "";
		else
			return officialLastName;
	}

	public void setOfficialLastName (String officialLastName) {
		this.officialLastName = officialLastName;
		commitNeeded = true;
	}

	public String getActualOfficialLastName () {
		return officialLastName;
	}

	public String getFirstName () {
		if (commitNeeded)
			return "";
		else
			return firstName;
	}

	public void setFirstName (String firstName) {
		this.firstName = firstName;
		commitNeeded = true;
	}

	public String getActualFirstName () {
		return firstName;
	}

	public String getLastName () {
		if (commitNeeded)
			return "";
		else
			return lastName;
	}

	public void setLastName (String lastName) {
		this.lastName = lastName;
		commitNeeded = true;
	}

	public String getActualLastName () {
		return lastName;
	}

	public String getInstitution () {
		if (commitNeeded)
			return "";
		else
			return institution;
	}

	public void setInstitution (String institution) {
		this.institution = institution;
		commitNeeded = true;
	}

	public String getActualInstitution () {
		return institution;
	}

	public Date getCreated () {
		return created;
	}

	public void setCreated (Date created) {
		this.created = created;
		commitNeeded = true;
	}

	public Date getActualCreated () {
		return created;
	}

	public void setCreatedToNow ( ) {
		this.created = new java.util.Date();
		commitNeeded = true;
	}

	public static String emailValue() throws JspException {
		try {
			return currentInstance.getEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailValue()");
		}
	}

	public static String officialFirstNameValue() throws JspException {
		try {
			return currentInstance.getOfficialFirstName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function officialFirstNameValue()");
		}
	}

	public static String officialLastNameValue() throws JspException {
		try {
			return currentInstance.getOfficialLastName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function officialLastNameValue()");
		}
	}

	public static String firstNameValue() throws JspException {
		try {
			return currentInstance.getFirstName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function firstNameValue()");
		}
	}

	public static String lastNameValue() throws JspException {
		try {
			return currentInstance.getLastName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function lastNameValue()");
		}
	}

	public static String institutionValue() throws JspException {
		try {
			return currentInstance.getInstitution();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function institutionValue()");
		}
	}

	public static Date createdValue() throws JspException {
		try {
			return currentInstance.getCreated();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function createdValue()");
		}
	}

	private void clearServiceState () {
		email = null;
		officialFirstName = null;
		officialLastName = null;
		firstName = null;
		lastName = null;
		institution = null;
		created = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	}

}
