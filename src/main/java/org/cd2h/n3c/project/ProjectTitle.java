package org.cd2h.n3c.project;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class ProjectTitle extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(ProjectTitle.class);

	public int doStartTag() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			if (!theProject.commitNeeded) {
				pageContext.getOut().print(theProject.getTitle());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Project for title tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for title tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for title tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getTitle() throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			return theProject.getTitle();
		} catch (Exception e) {
			log.error("Can't find enclosing Project for title tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for title tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for title tag ");
			}
		}
	}

	public void setTitle(String title) throws JspException {
		try {
			Project theProject = (Project)findAncestorWithClass(this, Project.class);
			theProject.setTitle(title);
		} catch (Exception e) {
			log.error("Can't find enclosing Project for title tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Project for title tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Project for title tag ");
			}
		}
	}

}
