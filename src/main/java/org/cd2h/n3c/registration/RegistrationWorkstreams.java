package org.cd2h.n3c.registration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class RegistrationWorkstreams extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RegistrationWorkstreams.class);

	public int doStartTag() throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			if (!theRegistration.commitNeeded) {
				pageContext.getOut().print(theRegistration.getWorkstreams());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for workstreams tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for workstreams tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for workstreams tag ");
			}

		}
		return SKIP_BODY;
	}

	public boolean getWorkstreams() throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			return theRegistration.getWorkstreams();
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for workstreams tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for workstreams tag ");
				parent.doEndTag();
				return false;
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for workstreams tag ");
			}
		}
	}

	public void setWorkstreams(boolean workstreams) throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			theRegistration.setWorkstreams(workstreams);
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for workstreams tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for workstreams tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for workstreams tag ");
			}
		}
	}

}
