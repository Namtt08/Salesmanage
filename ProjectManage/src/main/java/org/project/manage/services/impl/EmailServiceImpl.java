package org.project.manage.services.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.project.manage.dto.FileAttachment;
import org.project.manage.dto.MailDto;
import org.project.manage.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class EmailServiceImpl implements EmailService {

	@Autowired
	JavaMailSender mailSender;

	@Value("${mail.from}")
	private String mailFrom;

	@Override
	public void sendEmail(MailDto mail) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();

		try {

			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

			mimeMessageHelper.setSubject(mail.getMailSubject());
			mimeMessageHelper.setFrom(new InternetAddress(mailFrom));
			mimeMessageHelper.setTo(mail.getMailTo());
			mimeMessageHelper.setText(mail.getMailContent());
			if (StringUtils.isNotBlank(mail.getMailCc())) {
				mimeMessageHelper.setCc(mail.getMailCc());
			}
			if (StringUtils.isNotBlank(mail.getMailBcc())) {
				mimeMessageHelper.setBcc(mail.getMailBcc());
			}
			if (mail.getAttachments() != null) {
				for (FileAttachment fileAttachment : mail.getAttachments()) {
					FileSystemResource file = new FileSystemResource(new File(fileAttachment.getFilePath()));
					mimeMessageHelper.addAttachment(fileAttachment.getFileName(), file);
				}
			}
			mailSender.send(mimeMessageHelper.getMimeMessage());

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}