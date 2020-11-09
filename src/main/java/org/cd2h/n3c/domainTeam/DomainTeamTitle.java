package org.cd2h.n3c.domainTeam;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class DomainTeamTitle extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			if (!theDomainTeam.commitNeeded) {
				pageContext.getOut().print(theDomainTeam.getTitle());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DomainTeam for title tag ");
		}
		return SKIP_BODY;
	}

	public String getTitle() throws JspTagException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			return theDomainTeam.getTitle();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DomainTeam for title tag ");
		}
	}

	public void setTitle(String title) throws JspTagException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			theDomainTeam.setTitle(title);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing DomainTeam for title tag ");
		}
	}

}
