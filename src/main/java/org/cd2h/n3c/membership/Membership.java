package org.cd2h.n3c.membership;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.cd2h.n3c.workstream.Workstream;
import org.cd2h.n3c.registration.Registration;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class Membership extends N3CLoginTagLibTagSupport {

	static Membership currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Membership.class);

	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	String email = null;
	String label = null;
	Timestamp joined = null;

	private String var = null;

	private Membership cachedMembership = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			if (theWorkstream!= null)
				parentEntities.addElement(theWorkstream);
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			if (theRegistration!= null)
				parentEntities.addElement(theRegistration);

			if (theWorkstream == null) {
			} else {
				label = theWorkstream.getLabel();
			}
			if (theRegistration == null) {
			} else {
				email = theRegistration.getEmail();
			}

			MembershipIterator theMembershipIterator = (MembershipIterator)findAncestorWithClass(this, MembershipIterator.class);

			if (theMembershipIterator != null) {
				email = theMembershipIterator.getEmail();
				label = theMembershipIterator.getLabel();
			}

			if (theMembershipIterator == null && theWorkstream == null && theRegistration == null && email == null) {
				// no email was provided - the default is to assume that it is a new Membership and to generate a new email
				email = null;
				insertEntity();
			} else if (theMembershipIterator == null && theWorkstream != null && theRegistration == null) {
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
			} else if (theMembershipIterator == null && theWorkstream == null && theRegistration != null) {
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
			log.error("JDBC error retrieving email " + email, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving email " + email);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving email " + email,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Membership currentMembership = (Membership) pageContext.getAttribute("tag_membership");
			if(currentMembership != null){
				cachedMembership = currentMembership;
			}
			currentMembership = this;
			pageContext.setAttribute((var == null ? "tag_membership" : var), currentMembership);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedMembership != null){
				pageContext.setAttribute((var == null ? "tag_membership" : var), this.cachedMembership);
			}else{
				pageContext.removeAttribute((var == null ? "tag_membership" : var));
				this.cachedMembership = null;
			}
		}

		try {
			Boolean error = null; // (Boolean) pageContext.getAttribute("tagError");
			if(pageContext != null){
				error = (Boolean) pageContext.getAttribute("tagError");
			}

			if(error != null && error){

				freeConnection();
				clearServiceState();

				Exception e = (Exception) pageContext.getAttribute("tagErrorException");
				String message = (String) pageContext.getAttribute("tagErrorMessage");

				Tag parent = getParent();
				if(parent != null){
					return parent.doEndTag();
				}else if(e != null && message != null){
					throw new JspException(message,e);
				}else if(parent == null){
					pageContext.removeAttribute("tagError");
					pageContext.removeAttribute("tagErrorException");
					pageContext.removeAttribute("tagErrorMessage");
				}
			}
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update n3c_admin.membership set joined = ? where email = ?  and label = ? ");
				stmt.setTimestamp( 1, joined );
				stmt.setString(2,email);
				stmt.setString(3,label);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			log.error("Error: IOException while writing to the user", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: IOException while writing to the user");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: IOException while writing to the user");
			}

		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException, SQLException {
		PreparedStatement stmt = getConnection().prepareStatement("insert into n3c_admin.membership(email,label,joined) values (?,?,?)");
		stmt.setString(1,email);
		stmt.setString(2,label);
		stmt.setTimestamp(3,joined);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
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

	public Timestamp getJoined () {
		return joined;
	}

	public void setJoined (Timestamp joined) {
		this.joined = joined;
		commitNeeded = true;
	}

	public Timestamp getActualJoined () {
		return joined;
	}

	public void setJoinedToNow ( ) {
		this.joined = new java.sql.Timestamp(new java.util.Date().getTime());
		commitNeeded = true;
	}

	public String getVar () {
		return var;
	}

	public void setVar (String var) {
		this.var = var;
	}

	public String getActualVar () {
		return var;
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

	public static Timestamp joinedValue() throws JspException {
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
		this.var = null;

	}

}
