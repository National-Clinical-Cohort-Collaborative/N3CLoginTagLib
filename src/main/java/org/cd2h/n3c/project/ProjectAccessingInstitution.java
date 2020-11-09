package org.cd2h.n3c.project;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class ProjectAccessingInstitution extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			if (!theProject.commitNeeded) {
				pageContext.getOut().print(theProject.getAccessingInstitution());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for accessingInstitution tag ");
		}
		return SKIP_BODY;
	}

	public String getAccessingInstitution() throws JspTagException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			return theProject.getAccessingInstitution();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for accessingInstitution tag ");
		}
	}

	public void setAccessingInstitution(String accessingInstitution) throws JspTagException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			theProject.setAccessingInstitution(accessingInstitution);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for accessingInstitution tag ");
		}
	}

}
