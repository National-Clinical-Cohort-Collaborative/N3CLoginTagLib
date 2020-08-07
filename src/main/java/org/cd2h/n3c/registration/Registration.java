package org.cd2h.n3c.registration;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.cd2h.n3c.N3CLoginTagLibTagSupport;

@SuppressWarnings("serial")

public class Registration extends N3CLoginTagLibTagSupport {

	static Registration currentInstance = null;
	boolean commitNeeded = false;
	boolean newRecord = false;

	private static final Log log =LogFactory.getLog(Registration.class);

	Vector<N3CLoginTagLibTagSupport> parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	String email = null;
	String officialFirstName = null;
	String officialLastName = null;
	String officialFullName = null;
	String officialInstitution = null;
	String firstName = null;
	String lastName = null;
	String institution = null;
	String orcidId = null;
	String gsuiteEmail = null;
	String slackId = null;
	String githubId = null;
	String twitterId = null;
	String expertise = null;
	String therapeuticArea = null;
	String assistantEmail = null;
	boolean enclave = false;
	boolean workstreams = false;
	Date created = null;
	Date updated = null;
	Date emailed = null;

	public int doStartTag() throws JspException {
		currentInstance = this;
		try {


			RegistrationIterator theRegistrationIterator = (RegistrationIterator)findAncestorWithClass(this, RegistrationIterator.class);

			if (theRegistrationIterator != null) {
				email = theRegistrationIterator.getEmail();
			}

			if (theRegistrationIterator == null && email == null) {
				// no email was provided - the default is to assume that it is a new Registration and to generate a new email
				email = null;
				log.debug("generating new Registration " + email);
				insertEntity();
			} else {
				// an iterator or email was provided as an attribute - we need to load a Registration from the database
				boolean found = false;
				PreparedStatement stmt = getConnection().prepareStatement("select official_first_name,official_last_name,official_full_name,official_institution,first_name,last_name,institution,orcid_id,gsuite_email,slack_id,github_id,twitter_id,expertise,therapeutic_area,assistant_email,enclave,workstreams,created,updated,emailed from n3c_admin.registration where email = ?");
				stmt.setString(1,email);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					if (officialFirstName == null)
						officialFirstName = rs.getString(1);
					if (officialLastName == null)
						officialLastName = rs.getString(2);
					if (officialFullName == null)
						officialFullName = rs.getString(3);
					if (officialInstitution == null)
						officialInstitution = rs.getString(4);
					if (firstName == null)
						firstName = rs.getString(5);
					if (lastName == null)
						lastName = rs.getString(6);
					if (institution == null)
						institution = rs.getString(7);
					if (orcidId == null)
						orcidId = rs.getString(8);
					if (gsuiteEmail == null)
						gsuiteEmail = rs.getString(9);
					if (slackId == null)
						slackId = rs.getString(10);
					if (githubId == null)
						githubId = rs.getString(11);
					if (twitterId == null)
						twitterId = rs.getString(12);
					if (expertise == null)
						expertise = rs.getString(13);
					if (therapeuticArea == null)
						therapeuticArea = rs.getString(14);
					if (assistantEmail == null)
						assistantEmail = rs.getString(15);
					if (enclave == false)
						enclave = rs.getBoolean(16);
					if (workstreams == false)
						workstreams = rs.getBoolean(17);
					if (created == null)
						created = rs.getTimestamp(18);
					if (updated == null)
						updated = rs.getTimestamp(19);
					if (emailed == null)
						emailed = rs.getTimestamp(20);
					found = true;
				}
				stmt.close();

				if (!found) {
					insertEntity();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: JDBC error retrieving email " + email);
		} finally {
			freeConnection();
		}
		return EVAL_PAGE;
	}

	public int doEndTag() throws JspException {
		currentInstance = null;
		try {
			if (commitNeeded) {
				PreparedStatement stmt = getConnection().prepareStatement("update n3c_admin.registration set official_first_name = ?, official_last_name = ?, official_full_name = ?, official_institution = ?, first_name = ?, last_name = ?, institution = ?, orcid_id = ?, gsuite_email = ?, slack_id = ?, github_id = ?, twitter_id = ?, expertise = ?, therapeutic_area = ?, assistant_email = ?, enclave = ?, workstreams = ?, created = ?, updated = ?, emailed = ? where email = ?");
				stmt.setString(1,officialFirstName);
				stmt.setString(2,officialLastName);
				stmt.setString(3,officialFullName);
				stmt.setString(4,officialInstitution);
				stmt.setString(5,firstName);
				stmt.setString(6,lastName);
				stmt.setString(7,institution);
				stmt.setString(8,orcidId);
				stmt.setString(9,gsuiteEmail);
				stmt.setString(10,slackId);
				stmt.setString(11,githubId);
				stmt.setString(12,twitterId);
				stmt.setString(13,expertise);
				stmt.setString(14,therapeuticArea);
				stmt.setString(15,assistantEmail);
				stmt.setBoolean(16,enclave);
				stmt.setBoolean(17,workstreams);
				stmt.setTimestamp(18,created == null ? null : new java.sql.Timestamp(created.getTime()));
				stmt.setTimestamp(19,updated == null ? null : new java.sql.Timestamp(updated.getTime()));
				stmt.setTimestamp(20,emailed == null ? null : new java.sql.Timestamp(emailed.getTime()));
				stmt.setString(21,email);
				stmt.executeUpdate();
				stmt.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			clearServiceState();
			freeConnection();
		}
		return super.doEndTag();
	}

	public void insertEntity() throws JspException {
		try {
			if (officialFirstName == null)
				officialFirstName = "";
			if (officialLastName == null)
				officialLastName = "";
			if (officialFullName == null)
				officialFullName = "";
			if (officialInstitution == null)
				officialInstitution = "";
			if (firstName == null)
				firstName = "";
			if (lastName == null)
				lastName = "";
			if (institution == null)
				institution = "";
			if (orcidId == null)
				orcidId = "";
			if (gsuiteEmail == null)
				gsuiteEmail = "";
			if (slackId == null)
				slackId = "";
			if (githubId == null)
				githubId = "";
			if (twitterId == null)
				twitterId = "";
			if (expertise == null)
				expertise = "";
			if (therapeuticArea == null)
				therapeuticArea = "";
			if (assistantEmail == null)
				assistantEmail = "";
			PreparedStatement stmt = getConnection().prepareStatement("insert into n3c_admin.registration(email,official_first_name,official_last_name,official_full_name,official_institution,first_name,last_name,institution,orcid_id,gsuite_email,slack_id,github_id,twitter_id,expertise,therapeutic_area,assistant_email,enclave,workstreams,created,updated,emailed) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			stmt.setString(1,email);
			stmt.setString(2,officialFirstName);
			stmt.setString(3,officialLastName);
			stmt.setString(4,officialFullName);
			stmt.setString(5,officialInstitution);
			stmt.setString(6,firstName);
			stmt.setString(7,lastName);
			stmt.setString(8,institution);
			stmt.setString(9,orcidId);
			stmt.setString(10,gsuiteEmail);
			stmt.setString(11,slackId);
			stmt.setString(12,githubId);
			stmt.setString(13,twitterId);
			stmt.setString(14,expertise);
			stmt.setString(15,therapeuticArea);
			stmt.setString(16,assistantEmail);
			stmt.setBoolean(17,enclave);
			stmt.setBoolean(18,workstreams);
			stmt.setTimestamp(19,created == null ? null : new java.sql.Timestamp(created.getTime()));
			stmt.setTimestamp(20,updated == null ? null : new java.sql.Timestamp(updated.getTime()));
			stmt.setTimestamp(21,emailed == null ? null : new java.sql.Timestamp(emailed.getTime()));
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new JspTagException("Error: IOException while writing to the user");
		} finally {
			freeConnection();
		}
	}

	public String getEmail () {
		if (commitNeeded)
			return "";
		else
			return email;
	}

	public void setEmail (String email) {
		this.email = email;
	}

	public String getActualEmail () {
		return email;
	}

	public String getOfficialFirstName () {
		if (commitNeeded)
			return "";
		else
			return officialFirstName;
	}

	public void setOfficialFirstName (String officialFirstName) {
		this.officialFirstName = officialFirstName;
		commitNeeded = true;
	}

	public String getActualOfficialFirstName () {
		return officialFirstName;
	}

	public String getOfficialLastName () {
		if (commitNeeded)
			return "";
		else
			return officialLastName;
	}

	public void setOfficialLastName (String officialLastName) {
		this.officialLastName = officialLastName;
		commitNeeded = true;
	}

	public String getActualOfficialLastName () {
		return officialLastName;
	}

	public String getOfficialFullName () {
		if (commitNeeded)
			return "";
		else
			return officialFullName;
	}

	public void setOfficialFullName (String officialFullName) {
		this.officialFullName = officialFullName;
		commitNeeded = true;
	}

	public String getActualOfficialFullName () {
		return officialFullName;
	}

	public String getOfficialInstitution () {
		if (commitNeeded)
			return "";
		else
			return officialInstitution;
	}

	public void setOfficialInstitution (String officialInstitution) {
		this.officialInstitution = officialInstitution;
		commitNeeded = true;
	}

	public String getActualOfficialInstitution () {
		return officialInstitution;
	}

	public String getFirstName () {
		if (commitNeeded)
			return "";
		else
			return firstName;
	}

	public void setFirstName (String firstName) {
		this.firstName = firstName;
		commitNeeded = true;
	}

	public String getActualFirstName () {
		return firstName;
	}

	public String getLastName () {
		if (commitNeeded)
			return "";
		else
			return lastName;
	}

	public void setLastName (String lastName) {
		this.lastName = lastName;
		commitNeeded = true;
	}

	public String getActualLastName () {
		return lastName;
	}

	public String getInstitution () {
		if (commitNeeded)
			return "";
		else
			return institution;
	}

	public void setInstitution (String institution) {
		this.institution = institution;
		commitNeeded = true;
	}

	public String getActualInstitution () {
		return institution;
	}

	public String getOrcidId () {
		if (commitNeeded)
			return "";
		else
			return orcidId;
	}

	public void setOrcidId (String orcidId) {
		this.orcidId = orcidId;
		commitNeeded = true;
	}

	public String getActualOrcidId () {
		return orcidId;
	}

	public String getGsuiteEmail () {
		if (commitNeeded)
			return "";
		else
			return gsuiteEmail;
	}

	public void setGsuiteEmail (String gsuiteEmail) {
		this.gsuiteEmail = gsuiteEmail;
		commitNeeded = true;
	}

	public String getActualGsuiteEmail () {
		return gsuiteEmail;
	}

	public String getSlackId () {
		if (commitNeeded)
			return "";
		else
			return slackId;
	}

	public void setSlackId (String slackId) {
		this.slackId = slackId;
		commitNeeded = true;
	}

	public String getActualSlackId () {
		return slackId;
	}

	public String getGithubId () {
		if (commitNeeded)
			return "";
		else
			return githubId;
	}

	public void setGithubId (String githubId) {
		this.githubId = githubId;
		commitNeeded = true;
	}

	public String getActualGithubId () {
		return githubId;
	}

	public String getTwitterId () {
		if (commitNeeded)
			return "";
		else
			return twitterId;
	}

	public void setTwitterId (String twitterId) {
		this.twitterId = twitterId;
		commitNeeded = true;
	}

	public String getActualTwitterId () {
		return twitterId;
	}

	public String getExpertise () {
		if (commitNeeded)
			return "";
		else
			return expertise;
	}

	public void setExpertise (String expertise) {
		this.expertise = expertise;
		commitNeeded = true;
	}

	public String getActualExpertise () {
		return expertise;
	}

	public String getTherapeuticArea () {
		if (commitNeeded)
			return "";
		else
			return therapeuticArea;
	}

	public void setTherapeuticArea (String therapeuticArea) {
		this.therapeuticArea = therapeuticArea;
		commitNeeded = true;
	}

	public String getActualTherapeuticArea () {
		return therapeuticArea;
	}

	public String getAssistantEmail () {
		if (commitNeeded)
			return "";
		else
			return assistantEmail;
	}

	public void setAssistantEmail (String assistantEmail) {
		this.assistantEmail = assistantEmail;
		commitNeeded = true;
	}

	public String getActualAssistantEmail () {
		return assistantEmail;
	}

	public boolean getEnclave () {
		return enclave;
	}

	public void setEnclave (boolean enclave) {
		this.enclave = enclave;
		commitNeeded = true;
	}

	public boolean getActualEnclave () {
		return enclave;
	}

	public boolean getWorkstreams () {
		return workstreams;
	}

	public void setWorkstreams (boolean workstreams) {
		this.workstreams = workstreams;
		commitNeeded = true;
	}

	public boolean getActualWorkstreams () {
		return workstreams;
	}

	public Date getCreated () {
		return created;
	}

	public void setCreated (Date created) {
		this.created = created;
		commitNeeded = true;
	}

	public Date getActualCreated () {
		return created;
	}

	public void setCreatedToNow ( ) {
		this.created = new java.util.Date();
		commitNeeded = true;
	}

	public Date getUpdated () {
		return updated;
	}

	public void setUpdated (Date updated) {
		this.updated = updated;
		commitNeeded = true;
	}

	public Date getActualUpdated () {
		return updated;
	}

	public void setUpdatedToNow ( ) {
		this.updated = new java.util.Date();
		commitNeeded = true;
	}

	public Date getEmailed () {
		return emailed;
	}

	public void setEmailed (Date emailed) {
		this.emailed = emailed;
		commitNeeded = true;
	}

	public Date getActualEmailed () {
		return emailed;
	}

	public void setEmailedToNow ( ) {
		this.emailed = new java.util.Date();
		commitNeeded = true;
	}

	public static String emailValue() throws JspException {
		try {
			return currentInstance.getEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailValue()");
		}
	}

	public static String officialFirstNameValue() throws JspException {
		try {
			return currentInstance.getOfficialFirstName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function officialFirstNameValue()");
		}
	}

	public static String officialLastNameValue() throws JspException {
		try {
			return currentInstance.getOfficialLastName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function officialLastNameValue()");
		}
	}

	public static String officialFullNameValue() throws JspException {
		try {
			return currentInstance.getOfficialFullName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function officialFullNameValue()");
		}
	}

	public static String officialInstitutionValue() throws JspException {
		try {
			return currentInstance.getOfficialInstitution();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function officialInstitutionValue()");
		}
	}

	public static String firstNameValue() throws JspException {
		try {
			return currentInstance.getFirstName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function firstNameValue()");
		}
	}

	public static String lastNameValue() throws JspException {
		try {
			return currentInstance.getLastName();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function lastNameValue()");
		}
	}

	public static String institutionValue() throws JspException {
		try {
			return currentInstance.getInstitution();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function institutionValue()");
		}
	}

	public static String orcidIdValue() throws JspException {
		try {
			return currentInstance.getOrcidId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function orcidIdValue()");
		}
	}

	public static String gsuiteEmailValue() throws JspException {
		try {
			return currentInstance.getGsuiteEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function gsuiteEmailValue()");
		}
	}

	public static String slackIdValue() throws JspException {
		try {
			return currentInstance.getSlackId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function slackIdValue()");
		}
	}

	public static String githubIdValue() throws JspException {
		try {
			return currentInstance.getGithubId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function githubIdValue()");
		}
	}

	public static String twitterIdValue() throws JspException {
		try {
			return currentInstance.getTwitterId();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function twitterIdValue()");
		}
	}

	public static String expertiseValue() throws JspException {
		try {
			return currentInstance.getExpertise();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function expertiseValue()");
		}
	}

	public static String therapeuticAreaValue() throws JspException {
		try {
			return currentInstance.getTherapeuticArea();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function therapeuticAreaValue()");
		}
	}

	public static String assistantEmailValue() throws JspException {
		try {
			return currentInstance.getAssistantEmail();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function assistantEmailValue()");
		}
	}

	public static boolean enclaveValue() throws JspException {
		try {
			return currentInstance.getEnclave();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function enclaveValue()");
		}
	}

	public static boolean workstreamsValue() throws JspException {
		try {
			return currentInstance.getWorkstreams();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function workstreamsValue()");
		}
	}

	public static Date createdValue() throws JspException {
		try {
			return currentInstance.getCreated();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function createdValue()");
		}
	}

	public static Date updatedValue() throws JspException {
		try {
			return currentInstance.getUpdated();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function updatedValue()");
		}
	}

	public static Date emailedValue() throws JspException {
		try {
			return currentInstance.getEmailed();
		} catch (Exception e) {
			 throw new JspTagException("Error in tag function emailedValue()");
		}
	}

	private void clearServiceState () {
		email = null;
		officialFirstName = null;
		officialLastName = null;
		officialFullName = null;
		officialInstitution = null;
		firstName = null;
		lastName = null;
		institution = null;
		orcidId = null;
		gsuiteEmail = null;
		slackId = null;
		githubId = null;
		twitterId = null;
		expertise = null;
		therapeuticArea = null;
		assistantEmail = null;
		enclave = false;
		workstreams = false;
		created = null;
		updated = null;
		emailed = null;
		newRecord = false;
		commitNeeded = false;
		parentEntities = new Vector<N3CLoginTagLibTagSupport>();

	}

}
