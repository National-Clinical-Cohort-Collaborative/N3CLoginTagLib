package org.cd2h.n3c.workstream;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;


import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class Workstream extends N3CLoginTagLibTagSupport {

	static Workstream currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Logger log = LogManager.getLogger(Workstream.class);

	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	String label = null;
	String fullName = null;
	String description = null;

	private String var = null;

	private Workstream cachedWorkstream = null;

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
			log.error("JDBC error retrieving label " + label, e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving label " + label);
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error retrieving label " + label,e);
			}

		} finally {
			freeConnection();
		}

		if(pageContext != null){
			Workstream currentWorkstream = (Workstream) pageContext.getAttribute("tag_workstream");
			if(currentWorkstream != null){
				cachedWorkstream = currentWorkstream;
			}
			currentWorkstream = this;
			pageContext.setAttribute((var == null ? "tag_workstream" : var), currentWorkstream);
		}

		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;

		if(pageContext != null){
			if(this.cachedWorkstream != null){
				pageContext.setAttribute((var == null ? "tag_workstream" : var), this.cachedWorkstream);
			}else{
				pageContext.removeAttribute((var == null ? "tag_workstream" : var));
				this.cachedWorkstream = null;
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
				PreparedStatement stmt = getConnection().prepareStatement("update n3c_admin.workstream set full_name = ?, description = ? where label = ? ");
				stmt.setString( 1, fullName );
				stmt.setString( 2, description );
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
		if (fullName == null){
			fullName = "";
		}
		if (description == null){
			description = "";
		}
		PreparedStatement stmt = getConnection().prepareStatement("insert into n3c_admin.workstream(label,full_name,description) values (?,?,?)");
		stmt.setString(1,label);
		stmt.setString(2,fullName);
		stmt.setString(3,description);
		stmt.executeUpdate();
		stmt.close();
		freeConnection();
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

	public String getVar () {
		return var;
	}

	public void setVar (String var) {
		this.var = var;
	}

	public String getActualVar () {
		return var;
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
		this.var = null;

	}

}
