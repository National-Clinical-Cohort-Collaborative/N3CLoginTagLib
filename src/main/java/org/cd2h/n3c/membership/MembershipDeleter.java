package org.cd2h.n3c.membership;


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
import org.cd2h.n3c.workstream.Workstream;

@SuppressWarnings("serial")

public class MembershipDeleter extends N3CLoginTagLibBodyTagSupport {
    String email = null;
    String label = null;
    Date joined = null;
	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();


    ResultSet rs = null;
    String var = null;
    int rsCount = 0;

    public int doStartTag() throws JspException {
		Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
		if (theRegistration!= null)
			parentEntities.addElement(theRegistration);
		Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
		if (theWorkstream!= null)
			parentEntities.addElement(theWorkstream);

		if (theRegistration == null) {
		} else {
			email = theRegistration.getEmail();
		}
		if (theWorkstream == null) {
		} else {
			label = theWorkstream.getLabel();
		}


        PreparedStatement stat;
        try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("DELETE from n3c_admin.membership where 1=1"
                                                        + (email == null ? "" : " and email = ?")
                                                        + (label == null ? "" : " and label = ?")
                                                        );
            if (email != null) stat.setString(webapp_keySeq++, email);
            if (label != null) stat.setString(webapp_keySeq++, label);
            stat.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            throw new JspTagException("Error: JDBC error generating Membership deleter");
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
