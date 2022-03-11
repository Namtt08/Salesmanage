package org.project.manage.services;

import org.project.manage.dto.MailDto;

public interface EmailService {

	public void sendEmail(MailDto mail);
}
