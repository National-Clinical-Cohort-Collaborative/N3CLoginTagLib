package org.cd2h.n3c.enclave;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;
import org.cd2h.n3c.N3CLoginTagLibBodyTagSupport;
import org.cd2h.n3c.registration.Registration;
import org.cd2h.n3c.accessLevel.AccessLevel;

@SuppressWarnings("serial")

public class EnclaveDeleter extends N3CLoginTagLibBodyTagSupport {
    String email = null;
    boolean sftp = false;
    int level = 0;
    Date requested = null;
    Date approved = null;
	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
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


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from n3c_admin.enclave where 1=1"
                                                        + (email == null ? "" : " and email = ?")
                                                        );
            if (email != null) stat.setString(webapp_keySeq++, email);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Enclave deleter");
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
