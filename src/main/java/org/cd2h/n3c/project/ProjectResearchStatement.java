package org.cd2h.n3c.project;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class ProjectResearchStatement extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(ProjectResearchStatement.class);

	public int doStartTag() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			if (!theProject.commitNeeded) {
				pageContext.getOut().print(theProject.getResearchStatement());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Project for researchStatement tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for researchStatement tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for researchStatement tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getResearchStatement() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			return theProject.getResearchStatement();
		} catch (Exception e) {
			log.error("Can't find enclosing Project for researchStatement tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for researchStatement tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for researchStatement tag ");
			}
		}
	}

	public void setResearchStatement(String researchStatement) throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			theProject.setResearchStatement(researchStatement);
		} catch (Exception e) {
			log.error("Can't find enclosing Project for researchStatement tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for researchStatement tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for researchStatement tag ");
			}
		}
	}

}
