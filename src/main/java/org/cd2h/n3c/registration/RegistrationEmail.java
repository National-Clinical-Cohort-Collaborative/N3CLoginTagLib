package org.cd2h.n3c.registration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class RegistrationEmail extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RegistrationEmail.class);

	public int doStartTag() throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			if (!theRegistration.commitNeeded) {
				pageContext.getOut().print(theRegistration.getEmail());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for email tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for email tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getEmail() throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			return theRegistration.getEmail();
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for email tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for email tag ");
			}
		}
	}

	public void setEmail(String email) throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			theRegistration.setEmail(email);
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for email tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for email tag ");
			}
		}
	}

}
