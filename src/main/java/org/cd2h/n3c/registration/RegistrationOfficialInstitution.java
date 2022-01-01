package org.cd2h.n3c.registration;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class RegistrationOfficialInstitution extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(RegistrationOfficialInstitution.class);

	public int doStartTag() throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			if (!theRegistration.commitNeeded) {
				pageContext.getOut().print(theRegistration.getOfficialInstitution());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for officialInstitution tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for officialInstitution tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for officialInstitution tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getOfficialInstitution() throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			return theRegistration.getOfficialInstitution();
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for officialInstitution tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for officialInstitution tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for officialInstitution tag ");
			}
		}
	}

	public void setOfficialInstitution(String officialInstitution) throws JspException {
		try {
			Registration theRegistration = (Registration)findAncestorWithClass(this, Registration.class);
			theRegistration.setOfficialInstitution(officialInstitution);
		} catch (Exception e) {
			log.error("Can't find enclosing Registration for officialInstitution tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Registration for officialInstitution tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Registration for officialInstitution tag ");
			}
		}
	}

}
