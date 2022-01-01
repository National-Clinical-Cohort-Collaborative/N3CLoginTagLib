package org.cd2h.n3c.project;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class ProjectEmail extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(ProjectEmail.class);

	public int doStartTag() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			if (!theProject.commitNeeded) {
				pageContext.getOut().print(theProject.getEmail());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Project for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for email tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for email tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getEmail() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			return theProject.getEmail();
		} catch (Exception e) {
			log.error("Can't find enclosing Project for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for email tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for email tag ");
			}
		}
	}

	public void setEmail(String email) throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			theProject.setEmail(email);
		} catch (Exception e) {
			log.error("Can't find enclosing Project for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for email tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for email tag ");
			}
		}
	}

}
