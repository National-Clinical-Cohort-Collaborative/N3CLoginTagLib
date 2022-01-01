package org.cd2h.n3c.workstream;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkstreamDescription extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(WorkstreamDescription.class);

	public int doStartTag() throws JspException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			if (!theWorkstream.commitNeeded) {
				pageContext.getOut().print(theWorkstream.getDescription());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Workstream for description tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Workstream for description tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Workstream for description tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getDescription() throws JspException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			return theWorkstream.getDescription();
		} catch (Exception e) {
			log.error("Can't find enclosing Workstream for description tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Workstream for description tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Workstream for description tag ");
			}
		}
	}

	public void setDescription(String description) throws JspException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			theWorkstream.setDescription(description);
		} catch (Exception e) {
			log.error("Can't find enclosing Workstream for description tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Workstream for description tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Workstream for description tag ");
			}
		}
	}

}
