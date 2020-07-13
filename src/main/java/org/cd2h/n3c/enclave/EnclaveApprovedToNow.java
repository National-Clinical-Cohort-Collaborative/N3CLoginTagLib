package org.cd2h.n3c.enclave;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.Date;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class EnclaveApprovedToNow extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Enclave theEnclave = (Enclave)findAncestorWithClass(this, Enclave.class);
			theEnclave.setApprovedToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Enclave for approved tag ");
		}
		return SKIP_BODY;
	}

	public Date getApproved() throws JspTagException {
		try {
			Enclave theEnclave = (Enclave)findAncestorWithClass(this, Enclave.class);
			return theEnclave.getApproved();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Enclave for approved tag ");
		}
	}

	public void setApproved( ) throws JspTagException {
		try {
			Enclave theEnclave = (Enclave)findAncestorWithClass(this, Enclave.class);
			theEnclave.setApprovedToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Enclave for approved tag ");
		}
	}

}
