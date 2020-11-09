package org.cd2h.n3c.binding;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class BindingUid extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			if (!theBinding.commitNeeded) {
				pageContext.getOut().print(theBinding.getUid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Binding for uid tag ");
		}
		return SKIP_BODY;
	}

	public String getUid() throws JspTagException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			return theBinding.getUid();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Binding for uid tag ");
		}
	}

	public void setUid(String uid) throws JspTagException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			theBinding.setUid(uid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Binding for uid tag ");
		}
	}

}
