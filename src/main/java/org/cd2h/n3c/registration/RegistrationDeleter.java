package org.cd2h.n3c.registration;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

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
    Date created = null;
    Date updated = null;
	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {



        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from n3c_admin.registration where 1=1"
                                                        + (email == null ? "" : " and email = ?")
                                                        );
            if (email != null) stat.setString(webapp_keySeq++, email);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Registration deleter");
        } finally {
            freeConnection();
        }

        return SKIP_BODY;
    }

	public int doEndTag() throws JspException {
		clearServiceState();
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
