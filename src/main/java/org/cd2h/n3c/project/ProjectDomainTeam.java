package org.cd2h.n3c.project;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class ProjectDomainTeam extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			if (!theProject.commitNeeded) {
				pageContext.getOut().print(theProject.getDomainTeam());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for domainTeam tag ");
		}
		return SKIP_BODY;
	}

	public boolean getDomainTeam() throws JspTagException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			return theProject.getDomainTeam();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for domainTeam tag ");
		}
	}

	public void setDomainTeam(boolean domainTeam) throws JspTagException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			theProject.setDomainTeam(domainTeam);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for domainTeam tag ");
		}
	}

}
