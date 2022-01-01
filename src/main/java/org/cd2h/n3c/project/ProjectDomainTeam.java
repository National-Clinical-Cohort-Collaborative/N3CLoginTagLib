package org.cd2h.n3c.project;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class ProjectDomainTeam extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(ProjectDomainTeam.class);

	public int doStartTag() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			if (!theProject.commitNeeded) {
				pageContext.getOut().print(theProject.getDomainTeam());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Project for domainTeam tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for domainTeam tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for domainTeam tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getDomainTeam() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			return theProject.getDomainTeam();
		} catch (Exception e) {
			log.error("Can't find enclosing Project for domainTeam tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for domainTeam tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for domainTeam tag ");
			}
		}
	}

	public void setDomainTeam(boolean domainTeam) throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			theProject.setDomainTeam(domainTeam);
		} catch (Exception e) {
			log.error("Can't find enclosing Project for domainTeam tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for domainTeam tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for domainTeam tag ");
			}
		}
	}

}
