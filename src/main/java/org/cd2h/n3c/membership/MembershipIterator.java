package org.cd2h.n3c.membership;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;
import org.cd2h.n3c.N3CLoginTagLibBodyTagSupport;
import org.cd2h.n3c.workstream.Workstream;
import org.cd2h.n3c.registration.Registration;

@SuppressWarnings("serial")

public class MembershipIterator extends N3CLoginTagLibBodyTagSupport {
    String email = null;
    String label = null;
    Date joined = null;
	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	private static final Log log =LogFactory.getLog(Membership.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useWorkstream = false;
   boolean useRegistration = false;

	public static String membershipCountByWorkstream(String label) throws JspTagException {
		int count = 0;
		MembershipIterator theIterator = new MembershipIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.membership where 1=1"
						+ " and label = ?"
						);

			stat.setString(1,label);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Membership iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean workstreamHasMembership(String label) throws JspTagException {
		return ! membershipCountByWorkstream(label).equals("0");
	}

	public static String membershipCountByRegistration(String email) throws JspTagException {
		int count = 0;
		MembershipIterator theIterator = new MembershipIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.membership where 1=1"
						+ " and email = ?"
						);

			stat.setString(1,email);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Membership iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean registrationHasMembership(String email) throws JspTagException {
		return ! membershipCountByRegistration(email).equals("0");
	}

	public static Boolean membershipExists (String email, String label) throws JspTagException {
		int count = 0;
		MembershipIterator theIterator = new MembershipIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.membership where 1=1"
						+ " and email = ?"
						+ " and label = ?"
						);

			stat.setString(1,email);
			stat.setString(2,label);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Membership iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean workstreamRegistrationExists (String label, String email) throws JspTagException {
		int count = 0;
		MembershipIterator theIterator = new MembershipIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.membership where 1=1"
						+ " and label = ?"
						+ " and email = ?"
						);

			stat.setString(1,label);
			stat.setString(2,email);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Membership iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

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


      try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT n3c_admin.membership.email, n3c_admin.membership.label from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (label == null ? "" : " and label = ?")
                                                        + (email == null ? "" : " and email = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (label != null) stat.setString(webapp_keySeq++, label);
            if (email != null) stat.setString(webapp_keySeq++, email);
            rs = stat.executeQuery();

            if (rs.next()) {
                email = rs.getString(1);
                label = rs.getString(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating Membership iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("n3c_admin.membership");
       if (useWorkstream)
          theBuffer.append(", n3c_admin.workstream");
       if (useRegistration)
          theBuffer.append(", n3c_admin.registration");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useWorkstream)
          theBuffer.append(" and workstream.label = membership.null");
       if (useRegistration)
          theBuffer.append(" and registration.email = membership.null");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "email,label";
        }
    }

    private String generateLimitCriteria() {
        if (limitCriteria > 0) {
            return " limit " + limitCriteria;
        } else {
            return "";
        }
    }

    public int doAfterBody() throws JspTagException {
        try {
            if (rs.next()) {
                email = rs.getString(1);
                label = rs.getString(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across Membership");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error ending Membership iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        email = null;
        label = null;
        parentEntities = new Vector<N3CLoginTagLibTagSupport>();

        this.rs = null;
        this.stat = null;
        this.sortCriteria = null;
        this.var = null;
        this.rsCount = 0;
    }

    public String getSortCriteria() {
        return sortCriteria;
    }

    public void setSortCriteria(String sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    public int getLimitCriteria() {
        return limitCriteria;
    }

    public void setLimitCriteria(int limitCriteria) {
        this.limitCriteria = limitCriteria;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }


   public boolean getUseWorkstream() {
        return useWorkstream;
    }

    public void setUseWorkstream(boolean useWorkstream) {
        this.useWorkstream = useWorkstream;
    }

   public boolean getUseRegistration() {
        return useRegistration;
    }

    public void setUseRegistration(boolean useRegistration) {
        this.useRegistration = useRegistration;
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
