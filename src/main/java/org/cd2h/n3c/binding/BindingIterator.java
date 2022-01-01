package org.cd2h.n3c.binding;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;
import org.cd2h.n3c.N3CLoginTagLibBodyTagSupport;
import org.cd2h.n3c.project.Project;
import org.cd2h.n3c.domainTeam.DomainTeam;

@SuppressWarnings("serial")
public class BindingIterator extends N3CLoginTagLibBodyTagSupport {
    String email = null;
    String uid = null;
    int nid = 0;
	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	private static final Logger log = LogManager.getLogger(BindingIterator.class);


    PreparedStatement stat = null;
    ResultSet rs = null;
    String sortCriteria = null;
    int limitCriteria = 0;
    String var = null;
    int rsCount = 0;

   boolean useProject = false;
   boolean useDomainTeam = false;

	public static String bindingCountByProject(String email, String uid) throws JspTagException {
		int count = 0;
		BindingIterator theIterator = new BindingIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.binding where 1=1"
						+ " and email = ?"
						+ " and uid = ?"
						);

			stat.setString(1,email);
			stat.setString(2,uid);
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Binding iterator", e);
			throw new JspTagException("Error: JDBC error generating Binding iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean projectHasBinding(String email, String uid) throws JspTagException {
		return ! bindingCountByProject(email, uid).equals("0");
	}

	public static String bindingCountByDomainTeam(String nid) throws JspTagException {
		int count = 0;
		BindingIterator theIterator = new BindingIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.binding where 1=1"
						+ " and nid = ?"
						);

			stat.setInt(1,Integer.parseInt(nid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Binding iterator", e);
			throw new JspTagException("Error: JDBC error generating Binding iterator");
		} finally {
			theIterator.freeConnection();
		}
		return "" + count;
	}

	public static Boolean domainTeamHasBinding(String nid) throws JspTagException {
		return ! bindingCountByDomainTeam(nid).equals("0");
	}

	public static Boolean bindingExists (String email, String uid, String nid) throws JspTagException {
		int count = 0;
		BindingIterator theIterator = new BindingIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.binding where 1=1"
						+ " and email = ?"
						+ " and uid = ?"
						+ " and nid = ?"
						);

			stat.setString(1,email);
			stat.setString(2,uid);
			stat.setInt(3,Integer.parseInt(nid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Binding iterator", e);
			throw new JspTagException("Error: JDBC error generating Binding iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

	public static Boolean projectDomainTeamExists (String email, String uid, String nid) throws JspTagException {
		int count = 0;
		BindingIterator theIterator = new BindingIterator();
		try {
			PreparedStatement stat = theIterator.getConnection().prepareStatement("SELECT count(*) from n3c_admin.binding where 1=1"
						+ " and email = ?"
						+ " and uid = ?"
						+ " and nid = ?"
						);

			stat.setString(1,email);
			stat.setString(2,uid);
			stat.setInt(3,Integer.parseInt(nid));
			ResultSet crs = stat.executeQuery();

			if (crs.next()) {
				count = crs.getInt(1);
			}
			stat.close();
		} catch (SQLException e) {
			log.error("JDBC error generating Binding iterator", e);
			throw new JspTagException("Error: JDBC error generating Binding iterator");
		} finally {
			theIterator.freeConnection();
		}
		return count > 0;
	}

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


      try {
            //run count query  
            int webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT count(*) from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (email == null ? "" : " and email = ?")
                                                        + (uid == null ? "" : " and uid = ?")
                                                        + (nid == 0 ? "" : " and nid = ?")
                                                        + generateLimitCriteria());
            if (email != null) stat.setString(webapp_keySeq++, email);
            if (uid != null) stat.setString(webapp_keySeq++, uid);
            if (nid != 0) stat.setInt(webapp_keySeq++, nid);
            rs = stat.executeQuery();

            if (rs.next()) {
                pageContext.setAttribute(var+"Total", rs.getInt(1));
            }


            //run select id query  
            webapp_keySeq = 1;
            stat = getConnection().prepareStatement("SELECT n3c_admin.binding.email, n3c_admin.binding.uid, n3c_admin.binding.nid from " + generateFromClause() + " where 1=1"
                                                        + generateJoinCriteria()
                                                        + (email == null ? "" : " and email = ?")
                                                        + (uid == null ? "" : " and uid = ?")
                                                        + (nid == 0 ? "" : " and nid = ?")
                                                        + " order by " + generateSortCriteria()  +  generateLimitCriteria());
            if (email != null) stat.setString(webapp_keySeq++, email);
            if (uid != null) stat.setString(webapp_keySeq++, uid);
            if (nid != 0) stat.setInt(webapp_keySeq++, nid);
            rs = stat.executeQuery();

            if ( rs != null && rs.next() ) {
                email = rs.getString(1);
                uid = rs.getString(2);
                nid = rs.getInt(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_INCLUDE;
            }
        } catch (SQLException e) {
            log.error("JDBC error generating Binding iterator: " + stat.toString(), e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: JDBC error generating Binding iterator: " + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error generating Binding iterator: " + stat.toString(),e);
			}

        }

        return SKIP_BODY;
    }

    private String generateFromClause() {
       StringBuffer theBuffer = new StringBuffer("n3c_admin.binding");
       if (useProject)
          theBuffer.append(", n3c_admin.project");
       if (useDomainTeam)
          theBuffer.append(", n3c_admin.domain_team");

      return theBuffer.toString();
    }

    private String generateJoinCriteria() {
       StringBuffer theBuffer = new StringBuffer();
       if (useProject)
          theBuffer.append(" and project.email = binding.email");
       if (useProject)
          theBuffer.append(" and project.uid = binding.uid");
       if (useDomainTeam)
          theBuffer.append(" and domain_team.nid = binding.nid");

      return theBuffer.toString();
    }

    private String generateSortCriteria() {
        if (sortCriteria != null) {
            return sortCriteria;
        } else {
            return "email,uid,nid";
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
                uid = rs.getString(2);
                nid = rs.getInt(3);
                pageContext.setAttribute(var, ++rsCount);
                return EVAL_BODY_AGAIN;
            }
        } catch (SQLException e) {
            log.error("JDBC error iterating across Binding", e);

			freeConnection();
			clearServiceState();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error iterating across Binding" + stat.toString());
				return parent.doEndTag();
			}else{
				throw new JspException("JDBC error iterating across Binding",e);
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
            log.error("JDBC error ending Binding iterator",e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "JDBC error retrieving email " + email);
				return parent.doEndTag();
			}else{
				throw new JspException("Error: JDBC error ending Binding iterator",e);
			}

        } finally {
            clearServiceState();
            freeConnection();
        }
        return super.doEndTag();
    }

    private void clearServiceState() {
        email = null;
        uid = null;
        nid = 0;
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


   public boolean getUseProject() {
        return useProject;
    }

    public void setUseProject(boolean useProject) {
        this.useProject = useProject;
    }

   public boolean getUseDomainTeam() {
        return useDomainTeam;
    }

    public void setUseDomainTeam(boolean useDomainTeam) {
        this.useDomainTeam = useDomainTeam;
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
