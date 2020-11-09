package org.cd2h.n3c.binding;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import org.cd2h.n3c.project.Project;
import org.cd2h.n3c.domainTeam.DomainTeam;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;
import org.cd2h.n3c.Sequence;

@SuppressWarnings("serial")

public class Binding extends N3CLoginTagLibTagSupport {

	static Binding currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Binding.class);

	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	String email = null;
	String uid = null;
	int nid = 0;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			if (theProject!= null)
				parentEntities.addElement(theProject);
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			if (theDomainTeam!= null)
				parentEntities.addElement(theDomainTeam);

			if (theProject == null) {
			} else {
				email = theProject.getEmail();
				uid = theProject.getUid();
			}
			if (theDomainTeam == null) {
			} else {
				nid = theDomainTeam.getNid();
			}

			BindingIterator theBindingIterator = (BindingIterator)findAncestorWithClass(this, BindingIterator.class);

			if (theBindingIterator != null) {
				email = theBindingIterator.getEmail();
				uid = theBindingIterator.getUid();
				nid = theBindingIterator.getNid();
			}

			if (theBindingIterator == null && theProject == null && theDomainTeam == null && email == null) {
				// no email was provided - the default is to assume that it is a new Binding and to generate a new email
				email = null;
				log.debug("generating new Binding " + email);
				insertEntity();
			} else if (theBindingIterator == null && theProject != null && theDomainTeam == null) {
				// an email was provided as an attribute - we need to load a Binding from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select nid from n3c_admin.binding where email = ? and uid = ?");
				stmt.setString(1,email);
				stmt.setString(2,uid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (nid == 0)
						nid = rs.getInt(1);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else if (theBindingIterator == null && theProject == null && theDomainTeam != null) {
				// an email was provided as an attribute - we need to load a Binding from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select email,uid from n3c_admin.binding where nid = ?");
				stmt.setInt(1,nid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (email == null)
						email = rs.getString(1);
					if (uid == null)
						uid = rs.getString(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			} else {
				// an iterator or email was provided as an attribute - we need to load a Binding from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select 1 from n3c_admin.binding where email = ? and uid = ? and nid = ?");
				stmt.setString(1,email);
				stmt.setString(2,uid);
				stmt.setInt(3,nid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
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
				PreparedStatement stmt = getConnection().prepareStatement("update n3c_admin.binding set where email = ? and uid = ? and nid = ?");
				stmt.setString(1,email);
				stmt.setString(2,uid);
				stmt.setInt(3,nid);
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
			PreparedStatement stmt = getConnection().prepareStatement("insert into n3c_admin.binding(email,uid,nid) values (?,?,?)");
			stmt.setString(1,email);
			stmt.setString(2,uid);
			stmt.setInt(3,nid);
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

	public String getUid () {
		if (commitNeeded)
			return "";
		else
			return uid;
	}

	public void setUid (String uid) {
		this.uid = uid;
	}

	public String getActualUid () {
		return uid;
	}

	public int getNid () {
		return nid;
	}

	public void setNid (int nid) {
		this.nid = nid;
	}

	public int getActualNid () {
		return nid;
	}

	public static String emailValue() throws JspException {
		try {
			return currentInstance.getEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailValue()");
		}
	}

	public static String uidValue() throws JspException {
		try {
			return currentInstance.getUid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function uidValue()");
		}
	}

	public static int nidValue() throws JspException {
		try {
			return currentInstance.getNid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nidValue()");
		}
	}

	private void clearServiceState () {
		email = null;
		uid = null;
		nid = 0;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	}

}
