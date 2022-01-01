package org.cd2h.n3c.binding;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class BindingUid extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(BindingUid.class);

	public int doStartTag() throws JspException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			if (!theBinding.commitNeeded) {
				pageContext.getOut().print(theBinding.getUid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Binding for uid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Binding for uid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Binding for uid tag ");
			}

		}
		return SKIP_BODY;
	}

	public String getUid() throws JspException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			return theBinding.getUid();
		} catch (Exception e) {
			log.error("Can't find enclosing Binding for uid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Binding for uid tag ");
				parent.doEndTag();
				return null;
			}else{
				throw new JspTagException("Error: Can't find enclosing Binding for uid tag ");
			}
		}
	}

	public void setUid(String uid) throws JspException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			theBinding.setUid(uid);
		} catch (Exception e) {
			log.error("Can't find enclosing Binding for uid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Binding for uid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Binding for uid tag ");
			}
		}
	}

}
