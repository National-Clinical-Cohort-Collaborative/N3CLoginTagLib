package org.cd2h.n3c.registration;


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

@SuppressWarnings("serial")
public class RegistrationDeleter extends N3CLoginTagLibBodyTagSupport {
    String email = null;
    String officialFirstName = null;
    String officialLastName = null;
    String officialFullName = null;
    String officialInstitution = null;
    String firstName = null;
    String lastName = null;
    String institution = null;
    String orcidId = null;
    String gsuiteEmail = null;
    String slackId = null;
    String githubId = null;
    String twitterId = null;
    String expertise = null;
    String therapeuticArea = null;
    String assistantEmail = null;
    boolean enclave = false;
    boolean workstreams = false;
    Timestamp created = null;
    Timestamp updated = null;
    Timestamp emailed = null;
	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(RegistrationDeleter.class);


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {



        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from n3c_admin.registration where 1=1"
                                                        + (email == null ? "" : " and email = ? "));
            if (email != null) stat.setString(webapp_keySeq++, email);
            stat.execute();

			webapp_keySeq = 1;
        } catch (SQLException e) {
            log.error("JDBC error generating Registration deleter", e);

			clearServiceState();
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Registration deleter");
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Registration deleter",e);
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
}
