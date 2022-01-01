package org.cd2h.n3c.membership;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Timestamp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;
import org.cd2h.n3c.N3CLoginTagLibBodyTagSupport;
import org.cd2h.n3c.workstream.Workstream;
import org.cd2h.n3c.registration.Registration;

@SuppressWarnings("serial")
public class MembershipIterator extends N3CLoginTagLibBodyTagSupport {
    String email = null;
    String label = null;
    Timestamp joined = null;
	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(MembershipIterator.class);


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
			log.error("JDBC error generating Membership iterator", e);
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
			log.error("JDBC error generating Membership iterator", e);
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
			log.error("JDBC error generating Membership iterator", e);
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
			log.error("JDBC error generating Membership iterator", e);
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
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (label == null ? "" : " and label = ?")
                                                        + (email == null ? "" : " and email = ?")
                                                        + generateLimitCriteria());
            if (label != null) stat.setString(webapp_keySeq++, label);
            if (email != null) stat.setString(webapp_keySeq++, email);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT n3c_admin.membership.email, n3c_admin.membership.label from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (label == null ? "" : " and label = ?")
                                                        + (email == null ? "" : " and email = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (label != null) stat.setString(webapp_keySeq++, label);
            if (email != null) stat.setString(webapp_keySeq++, email);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                email = rs.getString(1);
                label = rs.getString(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating Membership iterator: " + stat.toString(), e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Membership iterator: " + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Membership iterator: " + stat.toString(),e);
			}

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
          theBuffer.append(" and workstream.label = membership.label");
       if (useRegistration)
          theBuffer.append(" and registration.email = membership.email");

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

    public int doAfterBody() throws JspException {
        try {
            if ( rs != null && rs.next() ) {
                email = rs.getString(1);
                label = rs.getString(2);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across Membership", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across Membership" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across Membership",e);
			}

        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspTagException, JspException {
        try {
			if( pageContext != null ){
				Boolean error = (Boolean) pageContext.getAttribute("tagError");
				if( error != null && error ){

					freeConnection();
					clearServiceState();

					Exception e = null; // (Exception) pageContext.getAttribute("tagErrorException");
					String message = null; // (String) pageContext.getAttribute("tagErrorMessage");

					if(pageContext != null){
						e = (Exception) pageContext.getAttribute("tagErrorException");
						message = (String) pageContext.getAttribute("tagErrorMessage");

					}
					Tag parent = getParent();
					if(parent != null){
						return parent.doEndTag();
					}else if(e != null && message != null){
						throw new JspException(message,e);
					}else if(parent == null && pageContext != null){
						pageContext.removeAttribute("tagError");
						pageContext.removeAttribute("tagErrorException");
						pageContext.removeAttribute("tagErrorMessage");
					}
				}
			}

            if( rs != null ){
                rs.close();
            }

            if( stat != null ){
                stat.close();
            }

        } catch ( SQLException e ) {
            log.error("JDBC error ending Membership iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving email " + email);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending Membership iterator",e);
			}

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
