package org.cd2h.n3c.project;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class ProjectResearchStatement extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			if (!theProject.commitNeeded) {
				pageContext.getOut().print(theProject.getResearchStatement());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for researchStatement tag ");
		}
		return SKIP_BODY;
	}

	public String getResearchStatement() throws JspTagException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			return theProject.getResearchStatement();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for researchStatement tag ");
		}
	}

	public void setResearchStatement(String researchStatement) throws JspTagException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			theProject.setResearchStatement(researchStatement);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Project for researchStatement tag ");
		}
	}

}
