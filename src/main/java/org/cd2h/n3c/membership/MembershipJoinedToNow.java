package org.cd2h.n3c.membership;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.Date;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class MembershipJoinedToNow extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			theMembership.setJoinedToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Membership for joined tag ");
		}
		return SKIP_BODY;
	}

	public Date getJoined() throws JspTagException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			return theMembership.getJoined();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Membership for joined tag ");
		}
	}

	public void setJoined( ) throws JspTagException {
		try {
			Membership theMembership = (Membership)findAncestorWithClass(this, Membership.class);
			theMembership.setJoinedToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Membership for joined tag ");
		}
	}

}
