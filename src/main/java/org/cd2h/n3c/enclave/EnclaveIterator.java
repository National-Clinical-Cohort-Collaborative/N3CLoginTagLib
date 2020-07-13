package org.cd2h.n3c.enclave;


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
import org.cd2h.n3c.registration.Registration;
import org.cd2h.n3c.accessLevel.AccessLevel;

@SuppressWarnings("serial")

public class EnclaveIterator extends N3CLoginTagLibBodyTagSupport {
    String email = null;
    boolean sftp = false;
    int level = 0;
    Date requested = null;
    Date approved = null;
	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	private static final Log log =LogFactory.getLog(Enclave.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useRegistration = false;
   boolean useAccessLevel = false;

	public static String enclaveCountByRegistration(String email) throws JspTagException {
		int count = 0;
		EnclaveIterator theIterator = new EnclaveIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.enclave where 1=1"
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
			throw new JspTagException("Error: JDBC error generating Enclave iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean registrationHasEnclave(String email) throws JspTagException {
		return ! enclaveCountByRegistration(email).equals("0");
	}

	public static String enclaveCountByAccessLevel(String level) throws JspTagException {
		int count = 0;
		EnclaveIterator theIterator = new EnclaveIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.enclave where 1=1"
						+ " and level = ?"
						);

			stat.setInt(1,Integer.parseInt(level));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Enclave iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean accessLevelHasEnclave(String level) throws JspTagException {
		return ! enclaveCountByAccessLevel(level).equals("0");
	}

	public static Boolean enclaveExists (String email) throws JspTagException {
		int count = 0;
		EnclaveIterator theIterator = new EnclaveIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.enclave where 1=1"
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
			throw new JspTagException("Error: JDBC error generating Enclave iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean registrationAccessLevelExists (String email, String level) throws JspTagException {
		int count = 0;
		EnclaveIterator theIterator = new EnclaveIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.enclave where 1=1"
						+ " and email = ?"
						+ " and level = ?"
						);

			stat.setString(1,email);
			stat.setInt(2,Integer.parseInt(level));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error generating Enclave iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

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


      try {
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT n3c_admin.enclave.email from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (email == null ? "" : " and email = ?")
                                                        + (level == 0 ? "" : " and level = ?")
                                                        + " order by " + generateSortCriteria() + generateLimitCriteria());
            if (email != null) stat.setString(webapp_keySeq++, email);
            if (level != 0) stat.setInt(webapp_keySeq++, level);
            rs = stat.executeQuery();

            if (rs.next()) {
                email = rs.getString(1);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error generating Enclave iterator: " + stat.toString());
        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("n3c_admin.enclave");
       if (useRegistration)
          theBuffer.append(", n3c_admin.registration");
       if (useAccessLevel)
          theBuffer.append(", n3c_admin.access_level");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useRegistration)
          theBuffer.append(" and registration.email = enclave.null");
       if (useAccessLevel)
          theBuffer.append(" and access_level.level = enclave.null");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "email";
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
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            clearServiceState();
            freeConnection();
            throw new JspTagException("Error: JDBC error iterating across Enclave");
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
            rs.close();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new JspTagException("Error: JDBC error ending Enclave iterator");
        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        email = null;
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


   public boolean getUseRegistration() {
        return useRegistration;
    }

    public void setUseRegistration(boolean useRegistration) {
        this.useRegistration = useRegistration;
    }

   public boolean getUseAccessLevel() {
        return useAccessLevel;
    }

    public void setUseAccessLevel(boolean useAccessLevel) {
        this.useAccessLevel = useAccessLevel;
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
