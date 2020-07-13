package org.cd2h.n3c.workstream;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkstreamDescription extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			if (!theWorkstream.commitNeeded) {
				pageContext.getOut().print(theWorkstream.getDescription());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Workstream for description tag ");
		}
		return SKIP_BODY;
	}

	public String getDescription() throws JspTagException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			return theWorkstream.getDescription();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Workstream for description tag ");
		}
	}

	public void setDescription(String description) throws JspTagException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			theWorkstream.setDescription(description);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Workstream for description tag ");
		}
	}

}
