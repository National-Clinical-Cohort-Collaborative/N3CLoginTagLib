package org.cd2h.n3c.accessLevel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;
import org.cd2h.n3c.Sequence;

@SuppressWarnings("serial")

public class AccessLevel extends N3CLoginTagLibTagSupport {

	static AccessLevel currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(AccessLevel.class);

	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	int level = 0;
	String label = null;
	String description = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			AccessLevelIterator theAccessLevelIterator = (AccessLevelIterator)findAncestorWithClass(this, AccessLevelIterator.class);

			if (theAccessLevelIterator != null) {
				level = theAccessLevelIterator.getLevel();
			}

			if (theAccessLevelIterator == null && level == 0) {
				// no level was provided - the default is to assume that it is a new AccessLevel and to generate a new level
				level = Sequence.generateID();
				log.debug("generating new AccessLevel " + level);
				insertEntity();
			} else {
				// an iterator or level was provided as an attribute - we need to load a AccessLevel from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select label,description from n3c_admin.access_level where level = ?");
				stmt.setInt(1,level);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (label == null)
						label = rs.getString(1);
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
			throw new JspTagException("Error: JDBC error retrieving level " + level);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update n3c_admin.access_level set label = ?, description = ? where level = ?");
				stmt.setString(1,label);
				stmt.setString(2,description);
				stmt.setInt(3,level);
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
			if (level == 0) {
				level = Sequence.generateID();
				log.debug("generating new AccessLevel " + level);
			}

			if (label == null)
				label = "";
			if (description == null)
				description = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into n3c_admin.access_level(level,label,description) values (?,?,?)");
			stmt.setInt(1,level);
			stmt.setString(2,label);
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

	public int getLevel () {
		return level;
	}

	public void setLevel (int level) {
		this.level = level;
	}

	public int getActualLevel () {
		return level;
	}

	public String getLabel () {
		if (commitNeeded)
			return "";
		else
			return label;
	}

	public void setLabel (String label) {
		this.label = label;
		commitNeeded = true;
	}

	public String getActualLabel () {
		return label;
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

	public static int levelValue() throws JspException {
		try {
			return currentInstance.getLevel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function levelValue()");
		}
	}

	public static String labelValue() throws JspException {
		try {
			return currentInstance.getLabel();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function labelValue()");
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
		level = 0;
		label = null;
		description = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	}

}
