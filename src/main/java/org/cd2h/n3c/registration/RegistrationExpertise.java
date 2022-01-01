package org.cd2h.n3c.registration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class RegistrationExpertise extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RegistrationExpertise.class);

	public int doStartTag() throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			if (!theRegistration.commitNeeded) {
				pageContext.getOut().print(theRegistration.getExpertise());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for expertise tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for expertise tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for expertise tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getExpertise() throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			return theRegistration.getExpertise();
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for expertise tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for expertise tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for expertise tag ");
			}
		}
	}

	public void setExpertise(String expertise) throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			theRegistration.setExpertise(expertise);
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for expertise tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for expertise tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for expertise tag ");
			}
		}
	}

}
