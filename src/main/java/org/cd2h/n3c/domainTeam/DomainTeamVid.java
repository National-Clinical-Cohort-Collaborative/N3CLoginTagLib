package org.cd2h.n3c.domainTeam;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class DomainTeamVid extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			if (!theDomainTeam.commitNeeded) {
				pageContext.getOut().print(theDomainTeam.getVid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DomainTeam for vid tag ");
		}
		return SKIP_BODY;
	}

	public int getVid() throws JspTagException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			return theDomainTeam.getVid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DomainTeam for vid tag ");
		}
	}

	public void setVid(int vid) throws JspTagException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			theDomainTeam.setVid(vid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DomainTeam for vid tag ");
		}
	}

}
