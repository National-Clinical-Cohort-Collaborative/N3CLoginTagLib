package org.cd2h.n3c.membership;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.cd2h.n3c.registration.Registration;
import org.cd2h.n3c.workstream.Workstream;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")

public class Membership extends N3CLoginTagLibTagSupport {

	static Membership currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Membership.class);

	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	String email = null;
	String label = null;
	Date joined = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			if (theRegistration!= null)
				parentEntities.addElement(theRegistration);
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			if (theWorkstream!= null)
				parentEntities.addElement(theWorkstream);

			if (theRegistration == null) {
			} else {
				email = theRegistration.getEmail();
			}
			if (theWorkstream == null) {
			} else {
				label = theWorkstream.getLabel();
			}

			MembershipIterator theMembershipIterator = (MembershipIterator)findAncestorWithClass(this, MembershipIterator.class);

			if (theMembershipIterator != null) {
				email = theMembershipIterator.getEmail();
				label = theMembershipIterator.getLabel();
			}

			if (theMembershipIterator == null && theRegistration == null && theWorkstream == null && email == null) {
				// no email was provided - the default is to assume that it is a new Membership and to generate a new email
				email = null;
				log.debug("generating new Membership " + email);
				insertEntity();
			} else if (theMembershipIterator == null && theRegistration != null && theWorkstream == null) {
				// an email was provided as an attribute - we need to load a Membership from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select label,joined from n3c_admin.membership where email = ?");
				stmt.setString(1,email);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (label == null)
						label = rs.getString(1);
					if (joined == null)
						joined = rs.getTimestamp(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theMembershipIterator == null && theRegistration == null && theWorkstream != null) {
				// an email was provided as an attribute - we need to load a Membership from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select email,joined from n3c_admin.membership where label = ?");
				stmt.setString(1,label);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (email == null)
						email = rs.getString(1);
					if (joined == null)
						joined = rs.getTimestamp(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or email was provided as an attribute - we need to load a Membership from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select joined from n3c_admin.membership where email = ? and label = ?");
				stmt.setString(1,email);
				stmt.setString(2,label);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (joined == null)
						joined = rs.getTimestamp(1);
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
				PreparedStatement stmt = getConnection().prepareStatement("update n3c_admin.membership set joined = ? where email = ? and label = ?");
				stmt.setTimestamp(1,joined == null ? null : new java.sql.Timestamp(joined.getTime()));
				stmt.setString(2,email);
				stmt.setString(3,label);
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
			PreparedStatement stmt = getConnection().prepareStatement("insert into n3c_admin.membership(email,label,joined) values (?,?,?)");
			stmt.setString(1,email);
			stmt.setString(2,label);
			stmt.setTimestamp(3,joined == null ? null : new java.sql.Timestamp(joined.getTime()));
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

	public String getLabel () {
		if (commitNeeded)
			return "";
		else
			return label;
	}

	public void setLabel (String label) {
		this.label = label;
	}

	public String getActualLabel () {
		return label;
	}

	public Date getJoined () {
		return joined;
	}

	public void setJoined (Date joined) {
		this.joined = joined;
		commitNeeded = true;
	}

	public Date getActualJoined () {
		return joined;
	}

	public void setJoinedToNow ( ) {
		this.joined = new java.util.Date();
		commitNeeded = true;
	}

	public static String emailValue() throws JspException {
		try {
			return currentInstance.getEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailValue()");
		}
	}

	public static String labelValue() throws JspException {
		try {
			return currentInstance.getLabel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function labelValue()");
		}
	}

	public static Date joinedValue() throws JspException {
		try {
			return currentInstance.getJoined();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function joinedValue()");
		}
	}

	private void clearServiceState () {
		email = null;
		label = null;
		joined = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	}

}
