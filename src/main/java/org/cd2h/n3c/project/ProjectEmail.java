package org.cd2h.n3c.project;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class ProjectEmail extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			if (!theProject.commitNeeded) {
				pageContext.getOut().print(theProject.getEmail());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for email tag ");
		}
		return SKIP_BODY;
	}

	public String getEmail() throws JspTagException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			return theProject.getEmail();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for email tag ");
		}
	}

	public void setEmail(String email) throws JspTagException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			theProject.setEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for email tag ");
		}
	}

}
