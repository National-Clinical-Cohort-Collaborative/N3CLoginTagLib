package org.cd2h.n3c.binding;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class BindingNid extends N3CLoginTagLibTagSupport {

	private static final Logger log = LogManager.getLogger(BindingNid.class);

	public int doStartTag() throws JspException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			if (!theBinding.commitNeeded) {
				pageContext.getOut().print(theBinding.getNid());
			}
		} catch (Exception e) {
			log.error("Can't find enclosing Binding for nid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Binding for nid tag ");
				return parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Binding for nid tag ");
			}

		}
		return SKIP_BODY;
	}

	public int getNid() throws JspException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			return theBinding.getNid();
		} catch (Exception e) {
			log.error("Can't find enclosing Binding for nid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Binding for nid tag ");
				parent.doEndTag();
				return 0;
			}else{
				throw new JspTagException("Error: Can't find enclosing Binding for nid tag ");
			}
		}
	}

	public void setNid(int nid) throws JspException {
		try {
			Binding theBinding = (Binding)findAncestorWithClass(this, Binding.class);
			theBinding.setNid(nid);
		} catch (Exception e) {
			log.error("Can't find enclosing Binding for nid tag ", e);
			freeConnection();
			Tag parent = getParent();
			if(parent != null){
				pageContext.setAttribute("tagError", true);
				pageContext.setAttribute("tagErrorException", e);
				pageContext.setAttribute("tagErrorMessage", "Error: Can't find enclosing Binding for nid tag ");
				parent.doEndTag();
			}else{
				throw new JspTagException("Error: Can't find enclosing Binding for nid tag ");
			}
		}
	}

}
