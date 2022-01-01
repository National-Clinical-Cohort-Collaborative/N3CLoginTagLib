package org.cd2h.n3c.registration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import java.sql.Timestamp;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class RegistrationCreated extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RegistrationCreated.class);

	public int doStartTag() throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			if (!theRegistration.commitNeeded) {
				pageContext.getOut().print(theRegistration.getCreated());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for created tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for created tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for created tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getCreated() throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			return theRegistration.getCreated();
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for created tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for created tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for created tag ");
			}
		}
	}

	public void setCreated(Timestamp created) throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			theRegistration.setCreated(created);
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for created tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for created tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for created tag ");
			}
		}
	}

}
