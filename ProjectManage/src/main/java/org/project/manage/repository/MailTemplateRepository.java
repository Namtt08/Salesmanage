package org.project.manage.repository;

import java.util.Optional;

import org.project.manage.entities.MailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailTemplateRepository  extends JpaRepository<MailTemplate, Long> {

	Optional<MailTemplate> findByCode(String code);

}
