package com.github.petervl80.service;

import com.github.petervl80.data.dto.BookDTO;
import com.github.petervl80.exception.RequiredObjectIsNullException;
import com.github.petervl80.model.Book;
import com.github.petervl80.repository.BookRepository;
import com.github.petervl80.unittests.mapper.mocks.MockBook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    MockBook input;

    @InjectMocks
    private BookService service;

    @Mock
    BookRepository repository;

    @BeforeEach
    void setUp() {
        input = new MockBook();
    }

    @Test
    void findById() {
        Book book = input.mockEntity(1);
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        var result = service.findById(1L);
        assertBook(result, "/api/books/v1/1");
    }

    @Test
    void create() {
        Book book = input.mockEntity(1);

        book.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.save(book)).thenReturn(book);

        var result = service.create(dto);

        assertBook(result, "/api/books/v1/1");
    }

    @Test
    void testCreateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> service.create(null));
        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void update() {
        Book book = input.mockEntity(1);

        book.setId(1L);

        BookDTO dto = input.mockDTO(1);

        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(book)).thenReturn(book);

        var result = service.update(dto);

        assertBook(result, "/api/books/v1/" + result.getId());
    }

    @Test
    void testUpdateWithNullPerson() {
        Exception exception = assertThrows(RequiredObjectIsNullException.class,
                () -> service.update(null));
        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void delete() {
        Book book = input.mockEntity(1);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        service.delete(1L);

        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Book.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findAll() {
        List<Book> list = input.mockEntityList();
        when(repository.findAll()).thenReturn(list);
        List<BookDTO> books = service.findAll();

        assertNotNull(books);
        assertEquals(14, books.size());

        var person1 = books.get(1);
        assertBook(person1, "/api/books/v1/" + person1.getId());

        var person4 = books.get(4);
        assertBook(person4, "/api/books/v1/" + person4.getId());

        var person7 = books.get(7);
        assertBook(person7, "/api/books/v1/" + person7.getId());
    }

    private static void assertBook(BookDTO result, String result1) {
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getLaunchDate());
        assertNotNull(result.getPrice());
        assertNotNull(result.getTitle());
        assertNotNull(result.getLinks());

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("self")
                        && link.getHref().endsWith(result1)
                        && Objects.equals(link.getType(), "GET")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("findAll")
                        && link.getHref().endsWith("/api/books/v1")
                        && Objects.equals(link.getType(), "GET")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("create")
                        && link.getHref().endsWith("/api/books/v1")
                        && Objects.equals(link.getType(), "POST")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("update")
                        && link.getHref().endsWith("/api/books/v1")
                        && Objects.equals(link.getType(), "PUT")));

        assertTrue(result.getLinks().stream()
                .anyMatch(link -> link.getRel().value().equals("delete")
                        && link.getHref().endsWith(result1)
                        && Objects.equals(link.getType(), "DELETE")));
    }
}