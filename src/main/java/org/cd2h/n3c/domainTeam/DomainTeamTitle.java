package org.cd2h.n3c.domainTeam;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class DomainTeamTitle extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(DomainTeamTitle.class);

	public int doStartTag() throws JspException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			if (!theDomainTeam.commitNeeded) {
				pageContext.getOut().print(theDomainTeam.getTitle());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing DomainTeam for title tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing DomainTeam for title tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing DomainTeam for title tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getTitle() throws JspException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			return theDomainTeam.getTitle();
		} catch (Exception e) {
			log.error("Can't find enclosing DomainTeam for title tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing DomainTeam for title tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing DomainTeam for title tag ");
			}
		}
	}

	public void setTitle(String title) throws JspException {
		try {
			DomainTeam theDomainTeam = (DomainTeam)findAncestorWithClass(this, DomainTeam.class);
			theDomainTeam.setTitle(title);
		} catch (Exception e) {
			log.error("Can't find enclosing DomainTeam for title tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing DomainTeam for title tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing DomainTeam for title tag ");
			}
		}
	}

}
