package org.cd2h.n3c.binding;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;
import org.cd2h.n3c.N3CLoginTagLibBodyTagSupport;
import org.cd2h.n3c.project.Project;
import org.cd2h.n3c.domainTeam.DomainTeam;

@SuppressWarnings("serial")
public class BindingDeleter extends N3CLoginTagLibBodyTagSupport {
    String email = null;
    String uid = null;
    int nid = 0;
	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(BindingDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
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


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from n3c_admin.binding where 1=1"
                                                        + (email == null ? "" : " and email = ? ")
                                                        + (uid == null ? "" : " and uid = ? ")
                                                        + (nid == 0 ? "" : " and nid = ? ")
                                                        + (email == null ? "" : " and email = ? ")
                                                        + (uid == null ? "" : " and uid = ? ")
                                                        + (nid == 0 ? "" : " and nid = ? "));
            if (email != null) stat.setString(webapp_keySeq++, email);
            if (uid != null) stat.setString(webapp_keySeq++, uid);
            if (nid != 0) stat.setInt(webapp_keySeq++, nid);
			if (email != null) stat.setString(webapp_keySeq++, email);
			if (uid != null) stat.setString(webapp_keySeq++, uid);
			if (nid != 0) stat.setInt(webapp_keySeq++, nid);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Binding deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Binding deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Binding deleter",e);
			}

        } finally {
            freeConnection();
        }

        return SKIP_BODY;
    }

	public int doEndTag() throws JspException {

		clearServiceState();
		Boolean error = (Boolean) pageContext.getAttribute("tagError");
		if(error != null && error){

			freeConnection();

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
		return super.doEndTag();
	}

    private void clearServiceState() {
        email = null;
        uid = null;
        nid = 0;
        parentEntities = new Vector<N3CLoginTagLibTagSupport>();

        this.rs = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }



	public String getEmail () {
		return email;
	}

	public void setEmail (String email) {
		this.email = email;
	}

	public String getActualEmail () {
		return email;
	}

	public String getUid () {
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
}
