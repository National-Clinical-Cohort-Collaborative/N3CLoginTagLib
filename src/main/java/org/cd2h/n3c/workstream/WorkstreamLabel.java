package org.cd2h.n3c.workstream;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class WorkstreamLabel extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			if (!theWorkstream.commitNeeded) {
				pageContext.getOut().print(theWorkstream.getLabel());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Workstream for label tag ");
		}
		return SKIP_BODY;
	}

	public String getLabel() throws JspTagException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			return theWorkstream.getLabel();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Workstream for label tag ");
		}
	}

	public void setLabel(String label) throws JspTagException {
		try {
			Workstream theWorkstream = (Workstream)findAncestorWithClass(this, Workstream.class);
			theWorkstream.setLabel(label);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Workstream for label tag ");
		}
	}

}
