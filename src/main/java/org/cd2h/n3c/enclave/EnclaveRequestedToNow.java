package org.cd2h.n3c.enclave;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import java.util.Date;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")
public class EnclaveRequestedToNow extends N3CLoginTagLibTagSupport {

	public int doStartTag() throws JspException {
		try {
			Enclave theEnclave = (Enclave)findAncestorWithClass(this, Enclave.class);
			theEnclave.setRequestedToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Enclave for requested tag ");
		}
		return SKIP_BODY;
	}

	public Date getRequested() throws JspTagException {
		try {
			Enclave theEnclave = (Enclave)findAncestorWithClass(this, Enclave.class);
			return theEnclave.getRequested();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Enclave for requested tag ");
		}
	}

	public void setRequested( ) throws JspTagException {
		try {
			Enclave theEnclave = (Enclave)findAncestorWithClass(this, Enclave.class);
			theEnclave.setRequestedToNow( );
		} catch (Exception e) {
			e.printStackTrace();
			throw new JspTagException("Error: Can't find enclosing Enclave for requested tag ");
		}
	}

}
