package org.cd2h.n3c.membership;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class MembershipEmail extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(MembershipEmail.class);

	public int doStartTag() throws JspException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			if (!theMembership.commitNeeded) {
				pageContext.getOut().print(theMembership.getEmail());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Membership for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Membership for email tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Membership for email tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getEmail() throws JspException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			return theMembership.getEmail();
		} catch (Exception e) {
			log.error("Can't find enclosing Membership for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Membership for email tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Membership for email tag ");
			}
		}
	}

	public void setEmail(String email) throws JspException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			theMembership.setEmail(email);
		} catch (Exception e) {
			log.error("Can't find enclosing Membership for email tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Membership for email tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Membership for email tag ");
			}
		}
	}

}
