package org.cd2h.n3c.membership;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class MembershipLabel extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(MembershipLabel.class);

	public int doStartTag() throws JspException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			if (!theMembership.commitNeeded) {
				pageContext.getOut().print(theMembership.getLabel());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Membership for label tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Membership for label tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Membership for label tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			return theMembership.getLabel();
		} catch (Exception e) {
			log.error("Can't find enclosing Membership for label tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Membership for label tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Membership for label tag ");
			}
		}
	}

	public void setLabel(String label) throws JspException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			theMembership.setLabel(label);
		} catch (Exception e) {
			log.error("Can't find enclosing Membership for label tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Membership for label tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Membership for label tag ");
			}
		}
	}

}
