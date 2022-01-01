package org.cd2h.n3c.workstream;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkstreamFullName extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(WorkstreamFullName.class);

	public int doStartTag() throws JspException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			if (!theWorkstream.commitNeeded) {
				pageContext.getOut().print(theWorkstream.getFullName());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Workstream for fullName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Workstream for fullName tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Workstream for fullName tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getFullName() throws JspException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			return theWorkstream.getFullName();
		} catch (Exception e) {
			log.error("Can't find enclosing Workstream for fullName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Workstream for fullName tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Workstream for fullName tag ");
			}
		}
	}

	public void setFullName(String fullName) throws JspException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			theWorkstream.setFullName(fullName);
		} catch (Exception e) {
			log.error("Can't find enclosing Workstream for fullName tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Workstream for fullName tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Workstream for fullName tag ");
			}
		}
	}

}
