package org.cd2h.n3c.membership;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Timestamp;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class MembershipJoinedToNow extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(MembershipJoinedToNow.class);


	public int doStartTag() throws JspException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			theMembership.setJoinedToNow( );
		} catch (Exception e) {
			log.error(" Can't find enclosing Membership for joined tag ", e);
			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Membership for joined tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Membership for joined tag ");
			}

		}
		return SKIP_BODY;
	}

	public Timestamp getJoined() throws JspException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			return theMembership.getJoined();
		} catch (Exception e) {

			log.error("Can't find enclosing Membership for joined tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Membership for joined tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Membership for joined tag ");
			}

		}
	}

	public void setJoined() throws JspException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			theMembership.setJoinedToNow( );
		} catch (Exception e) {

			log.error("Can't find enclosing Membership for joined tag ", e);

			freeConnection();

			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Can't find enclosing Membership for joined tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Membership for joined tag ");
			}

		}
	}
}