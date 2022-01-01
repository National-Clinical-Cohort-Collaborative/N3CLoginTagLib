package org.cd2h.n3c.domainTeam;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class DomainTeamVid extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(DomainTeamVid.class);

	public int doStartTag() throws JspException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			if (!theDomainTeam.commitNeeded) {
				pageContext.getOut().print(theDomainTeam.getVid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing DomainTeam for vid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing DomainTeam for vid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing DomainTeam for vid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getVid() throws JspException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			return theDomainTeam.getVid();
		} catch (Exception e) {
			log.error("Can't find enclosing DomainTeam for vid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing DomainTeam for vid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing DomainTeam for vid tag ");
			}
		}
	}

	public void setVid(int vid) throws JspException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			theDomainTeam.setVid(vid);
		} catch (Exception e) {
			log.error("Can't find enclosing DomainTeam for vid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing DomainTeam for vid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing DomainTeam for vid tag ");
			}
		}
	}

}
