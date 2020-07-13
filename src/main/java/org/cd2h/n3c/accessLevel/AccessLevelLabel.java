package org.cd2h.n3c.accessLevel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class AccessLevelLabel extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			AccessLevel theAccessLevel = (AccessLevel)findAncestorWithClass(this, AccessLevel.class);
			if (!theAccessLevel.commitNeeded) {
				pageContext.getOut().print(theAccessLevel.getLabel());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AccessLevel for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			AccessLevel theAccessLevel = (AccessLevel)findAncestorWithClass(this, AccessLevel.class);
			return theAccessLevel.getLabel();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AccessLevel for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			AccessLevel theAccessLevel = (AccessLevel)findAncestorWithClass(this, AccessLevel.class);
			theAccessLevel.setLabel(label);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AccessLevel for label tag ");
		}
	}

}
