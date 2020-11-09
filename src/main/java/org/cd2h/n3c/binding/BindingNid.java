package org.cd2h.n3c.binding;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class BindingNid extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			if (!theBinding.commitNeeded) {
				pageContext.getOut().print(theBinding.getNid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Binding for nid tag ");
		}
		return SKIP_BODY;
	}

	public int getNid() throws JspTagException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			return theBinding.getNid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Binding for nid tag ");
		}
	}

	public void setNid(int nid) throws JspTagException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			theBinding.setNid(nid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Binding for nid tag ");
		}
	}

}
