package com.github.petervl80.integrationtests.dto.wrapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.petervl80.integrationtests.dto.BookDTO;

import java.io.Serializable;
import java.util.List;

public class BookEmbeddedDTO implements Serializable {

    @JsonProperty("books")
    private List<BookDTO> books;

    public BookEmbeddedDTO() {
    }

    public List<BookDTO> getBooks() {
        return books;
    }

    public void setBooks(List<BookDTO> books) {
        this.books = books;
    }
}
