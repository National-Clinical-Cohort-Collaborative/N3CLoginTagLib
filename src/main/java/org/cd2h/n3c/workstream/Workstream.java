package org.cd2h.n3c.workstream;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")

public class Workstream extends N3CLoginTagLibTagSupport {

	static Workstream currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Workstream.class);

	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	String label = null;
	String fullName = null;
	String description = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			WorkstreamIterator theWorkstreamIterator = (WorkstreamIterator)findAncestorWithClass(this, WorkstreamIterator.class);

			if (theWorkstreamIterator != null) {
				label = theWorkstreamIterator.getLabel();
			}

			if (theWorkstreamIterator == null && label == null) {
				// no label was provided - the default is to assume that it is a new Workstream and to generate a new label
				label = null;
				log.debug("generating new Workstream " + label);
				insertEntity();
			} else {
				// an iterator or label was provided as an attribute - we need to load a Workstream from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select full_name,description from n3c_admin.workstream where label = ?");
				stmt.setString(1,label);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (fullName == null)
						fullName = rs.getString(1);
					if (description == null)
						description = rs.getString(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving label " + label);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update n3c_admin.workstream set full_name = ?, description = ? where label = ?");
				stmt.setString(1,fullName);
				stmt.setString(2,description);
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
			if (fullName == null)
				fullName = "";
			if (description == null)
				description = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into n3c_admin.workstream(label,full_name,description) values (?,?,?)");
			stmt.setString(1,label);
			stmt.setString(2,fullName);
			stmt.setString(3,description);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
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

	public String getFullName () {
		if (commitNeeded)
			return "";
		else
			return fullName;
	}

	public void setFullName (String fullName) {
		this.fullName = fullName;
		commitNeeded = true;
	}

	public String getActualFullName () {
		return fullName;
	}

	public String getDescription () {
		if (commitNeeded)
			return "";
		else
			return description;
	}

	public void setDescription (String description) {
		this.description = description;
		commitNeeded = true;
	}

	public String getActualDescription () {
		return description;
	}

	public static String labelValue() throws JspException {
		try {
			return currentInstance.getLabel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function labelValue()");
		}
	}

	public static String fullNameValue() throws JspException {
		try {
			return currentInstance.getFullName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function fullNameValue()");
		}
	}

	public static String descriptionValue() throws JspException {
		try {
			return currentInstance.getDescription();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function descriptionValue()");
		}
	}

	private void clearServiceState () {
		label = null;
		fullName = null;
		description = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	}

}
