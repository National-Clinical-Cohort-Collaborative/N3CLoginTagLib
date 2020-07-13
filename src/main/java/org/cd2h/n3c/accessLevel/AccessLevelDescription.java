package org.cd2h.n3c.accessLevel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class AccessLevelDescription extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			AccessLevel theAccessLevel = (AccessLevel)findAncestorWithClass(this, AccessLevel.class);
			if (!theAccessLevel.commitNeeded) {
				pageContext.getOut().print(theAccessLevel.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AccessLevel for description tag ");
		}
		return SKIP_BODY;
	}

	public String getDescription() throws JspTagException {
		try {
			AccessLevel theAccessLevel = (AccessLevel)findAncestorWithClass(this, AccessLevel.class);
			return theAccessLevel.getDescription();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AccessLevel for description tag ");
		}
	}

	public void setDescription(String description) throws JspTagException {
		try {
			AccessLevel theAccessLevel = (AccessLevel)findAncestorWithClass(this, AccessLevel.class);
			theAccessLevel.setDescription(description);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AccessLevel for description tag ");
		}
	}

}
