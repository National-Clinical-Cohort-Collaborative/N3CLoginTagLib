package org.cd2h.n3c.membership;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;
import org.cd2h.n3c.N3CLoginTagLibBodyTagSupport;
import org.cd2h.n3c.workstream.Workstream;
import org.cd2h.n3c.registration.Registration;

@SuppressWarnings("serial")
public class MembershipDeleter extends N3CLoginTagLibBodyTagSupport {
    String email = null;
    String label = null;
    Timestamp joined = null;
	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(MembershipDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
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


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from n3c_admin.membership where 1=1"
                                                        + (email == null ? "" : " and email = ? ")
                                                        + (label == null ? "" : " and label = ? ")
                                                        + (label == null ? "" : " and label = ? ")
                                                        + (email == null ? "" : " and email = ? "));
            if (email != null) stat.setString(webapp_keySeq++, email);
            if (label != null) stat.setString(webapp_keySeq++, label);
			if (label != null) stat.setString(webapp_keySeq++, label);
			if (email != null) stat.setString(webapp_keySeq++, email);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Membership deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Membership deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Membership deleter",e);
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
        label = null;
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

	public String getLabel () {
		return label;
	}

	public void setLabel (String label) {
		this.label = label;
	}

	public String getActualLabel () {
		return label;
	}
}
