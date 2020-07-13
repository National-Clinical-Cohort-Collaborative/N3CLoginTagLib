package org.cd2h.n3c.accessLevel;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class AccessLevelLevel extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			AccessLevel theAccessLevel = (AccessLevel)findAncestorWithClass(this, AccessLevel.class);
			if (!theAccessLevel.commitNeeded) {
				pageContext.getOut().print(theAccessLevel.getLevel());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AccessLevel for level tag ");
		}
		return SKIP_BODY;
	}

	public int getLevel() throws JspTagException {
		try {
			AccessLevel theAccessLevel = (AccessLevel)findAncestorWithClass(this, AccessLevel.class);
			return theAccessLevel.getLevel();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AccessLevel for level tag ");
		}
	}

	public void setLevel(int level) throws JspTagException {
		try {
			AccessLevel theAccessLevel = (AccessLevel)findAncestorWithClass(this, AccessLevel.class);
			theAccessLevel.setLevel(level);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing AccessLevel for level tag ");
		}
	}

}
