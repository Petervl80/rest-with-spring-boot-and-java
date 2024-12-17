package br.com.petervl80.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.petervl80.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
