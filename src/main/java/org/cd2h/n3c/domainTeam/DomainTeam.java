package org.cd2h.n3c.domainTeam;

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

public class DomainTeam extends N3CLoginTagLibTagSupport {

	static DomainTeam currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(DomainTeam.class);

	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	int nid = 0;
	int vid = 0;
	String title = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			DomainTeamIterator theDomainTeamIterator = (DomainTeamIterator)findAncestorWithClass(this, DomainTeamIterator.class);

			if (theDomainTeamIterator != null) {
				nid = theDomainTeamIterator.getNid();
			}

			if (theDomainTeamIterator == null && nid == 0) {
				// no nid was provided - the default is to assume that it is a new DomainTeam and to generate a new nid
				nid = Sequence.generateID();
				log.debug("generating new DomainTeam " + nid);
				insertEntity();
			} else {
				// an iterator or nid was provided as an attribute - we need to load a DomainTeam from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select vid,title from n3c_admin.domain_team where nid = ?");
				stmt.setInt(1,nid);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (vid == 0)
						vid = rs.getInt(1);
					if (title == null)
						title = rs.getString(2);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving nid " + nid);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update n3c_admin.domain_team set vid = ?, title = ? where nid = ?");
				stmt.setInt(1,vid);
				stmt.setString(2,title);
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
			if (nid == 0) {
				nid = Sequence.generateID();
				log.debug("generating new DomainTeam " + nid);
			}

			if (title == null)
				title = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into n3c_admin.domain_team(nid,vid,title) values (?,?,?)");
			stmt.setInt(1,nid);
			stmt.setInt(2,vid);
			stmt.setString(3,title);
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
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

	public int getVid () {
		return vid;
	}

	public void setVid (int vid) {
		this.vid = vid;
		commitNeeded = true;
	}

	public int getActualVid () {
		return vid;
	}

	public String getTitle () {
		if (commitNeeded)
			return "";
		else
			return title;
	}

	public void setTitle (String title) {
		this.title = title;
		commitNeeded = true;
	}

	public String getActualTitle () {
		return title;
	}

	public static int nidValue() throws JspException {
		try {
			return currentInstance.getNid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function nidValue()");
		}
	}

	public static int vidValue() throws JspException {
		try {
			return currentInstance.getVid();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function vidValue()");
		}
	}

	public static String titleValue() throws JspException {
		try {
			return currentInstance.getTitle();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function titleValue()");
		}
	}

	private void clearServiceState () {
		nid = 0;
		vid = 0;
		title = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	}

}
