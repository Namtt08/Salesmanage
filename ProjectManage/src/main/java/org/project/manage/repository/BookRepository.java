package org.project.manage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.project.manage.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}