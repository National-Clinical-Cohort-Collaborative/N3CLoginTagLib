package org.cd2h.n3c.enclave;

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
import org.cd2h.n3c.accessLevel.AccessLevel;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;
import org.cd2h.n3c.Sequence;

@SuppressWarnings("serial")

public class Enclave extends N3CLoginTagLibTagSupport {

	static Enclave currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Enclave.class);

	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	String email = null;
	boolean sftp = false;
	int level = 0;
	Date requested = null;
	Date approved = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			if (theRegistration!= null)
				parentEntities.addElement(theRegistration);
			AccessLevel theAccessLevel = (AccessLevel)findAncestorWithClass(this, AccessLevel.class);
			if (theAccessLevel!= null)
				parentEntities.addElement(theAccessLevel);

			if (theRegistration == null) {
			} else {
				email = theRegistration.getEmail();
			}
			if (theAccessLevel == null) {
			} else {
				level = theAccessLevel.getLevel();
			}

			EnclaveIterator theEnclaveIterator = (EnclaveIterator)findAncestorWithClass(this, EnclaveIterator.class);

			if (theEnclaveIterator != null) {
				email = theEnclaveIterator.getEmail();
			}

			if (theEnclaveIterator == null && theRegistration == null && theAccessLevel == null && email == null) {
				// no email was provided - the default is to assume that it is a new Enclave and to generate a new email
				email = null;
				log.debug("generating new Enclave " + email);
				insertEntity();
			} else if (theEnclaveIterator == null && theRegistration != null && theAccessLevel == null) {
				// an email was provided as an attribute - we need to load a Enclave from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select sftp,level,requested,approved from n3c_admin.enclave where email = ?");
				stmt.setString(1,email);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (sftp == false)
						sftp = rs.getBoolean(1);
					if (level == 0)
						level = rs.getInt(2);
					if (requested == null)
						requested = rs.getTimestamp(3);
					if (approved == null)
						approved = rs.getTimestamp(4);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theEnclaveIterator == null && theRegistration == null && theAccessLevel != null) {
				// an email was provided as an attribute - we need to load a Enclave from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select email,sftp,level,requested,approved from n3c_admin.enclave where");
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (email == null)
						email = rs.getString(1);
					if (sftp == false)
						sftp = rs.getBoolean(2);
					if (level == 0)
						level = rs.getInt(3);
					if (requested == null)
						requested = rs.getTimestamp(4);
					if (approved == null)
						approved = rs.getTimestamp(5);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or email was provided as an attribute - we need to load a Enclave from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select sftp,level,requested,approved from n3c_admin.enclave where email = ?");
				stmt.setString(1,email);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (sftp == false)
						sftp = rs.getBoolean(1);
					if (level == 0)
						level = rs.getInt(2);
					if (requested == null)
						requested = rs.getTimestamp(3);
					if (approved == null)
						approved = rs.getTimestamp(4);
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
				PreparedStatement stmt = getConnection().prepareStatement("update n3c_admin.enclave set sftp = ?, level = ?, requested = ?, approved = ? where email = ?");
				stmt.setBoolean(1,sftp);
				stmt.setInt(2,level);
				stmt.setTimestamp(3,requested == null ? null : new java.sql.Timestamp(requested.getTime()));
				stmt.setTimestamp(4,approved == null ? null : new java.sql.Timestamp(approved.getTime()));
				stmt.setString(5,email);
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
			PreparedStatement stmt = getConnection().prepareStatement("insert into n3c_admin.enclave(email,sftp,level,requested,approved) values (?,?,?,?,?)");
			stmt.setString(1,email);
			stmt.setBoolean(2,sftp);
			if (level == 0) {
				stmt.setNull(3, java.sql.Types.INTEGER);
			} else {
				stmt.setInt(3,level);
			}
			stmt.setTimestamp(4,requested == null ? null : new java.sql.Timestamp(requested.getTime()));
			stmt.setTimestamp(5,approved == null ? null : new java.sql.Timestamp(approved.getTime()));
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

	public boolean getSftp () {
		return sftp;
	}

	public void setSftp (boolean sftp) {
		this.sftp = sftp;
		commitNeeded = true;
	}

	public boolean getActualSftp () {
		return sftp;
	}

	public int getLevel () {
		return level;
	}

	public void setLevel (int level) {
		this.level = level;
		commitNeeded = true;
	}

	public int getActualLevel () {
		return level;
	}

	public Date getRequested () {
		return requested;
	}

	public void setRequested (Date requested) {
		this.requested = requested;
		commitNeeded = true;
	}

	public Date getActualRequested () {
		return requested;
	}

	public void setRequestedToNow ( ) {
		this.requested = new java.util.Date();
		commitNeeded = true;
	}

	public Date getApproved () {
		return approved;
	}

	public void setApproved (Date approved) {
		this.approved = approved;
		commitNeeded = true;
	}

	public Date getActualApproved () {
		return approved;
	}

	public void setApprovedToNow ( ) {
		this.approved = new java.util.Date();
		commitNeeded = true;
	}

	public static String emailValue() throws JspException {
		try {
			return currentInstance.getEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailValue()");
		}
	}

	public static boolean sftpValue() throws JspException {
		try {
			return currentInstance.getSftp();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function sftpValue()");
		}
	}

	public static int levelValue() throws JspException {
		try {
			return currentInstance.getLevel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function levelValue()");
		}
	}

	public static Date requestedValue() throws JspException {
		try {
			return currentInstance.getRequested();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function requestedValue()");
		}
	}

	public static Date approvedValue() throws JspException {
		try {
			return currentInstance.getApproved();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function approvedValue()");
		}
	}

	private void clearServiceState () {
		email = null;
		sftp = false;
		level = 0;
		requested = null;
		approved = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	}

}
