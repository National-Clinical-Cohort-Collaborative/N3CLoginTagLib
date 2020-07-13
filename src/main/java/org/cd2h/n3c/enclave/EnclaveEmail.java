package org.cd2h.n3c.enclave;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class EnclaveEmail extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Enclave theEnclave = (Enclave)findAncestorWithClass(this, Enclave.class);
			if (!theEnclave.commitNeeded) {
				pageContext.getOut().print(theEnclave.getEmail());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Enclave for email tag ");
		}
		return SKIP_BODY;
	}

	public String getEmail() throws JspTagException {
		try {
			Enclave theEnclave = (Enclave)findAncestorWithClass(this, Enclave.class);
			return theEnclave.getEmail();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Enclave for email tag ");
		}
	}

	public void setEmail(String email) throws JspTagException {
		try {
			Enclave theEnclave = (Enclave)findAncestorWithClass(this, Enclave.class);
			theEnclave.setEmail(email);
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Enclave for email tag ");
		}
	}

}
