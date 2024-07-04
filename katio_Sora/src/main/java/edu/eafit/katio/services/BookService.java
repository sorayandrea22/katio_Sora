package edu.eafit.katio.services;

import java.util.ArrayList;
import java.util.Optional;

import java.util.List;
import java.util.Date;

import edu.eafit.katio.dtos.BooksByAuthor;
import edu.eafit.katio.dtos.GenreByBook;
import edu.eafit.katio.dtos.GenreInsertdto;
import edu.eafit.katio.interfaces.BaseBookService;
import edu.eafit.katio.models.Books;
import edu.eafit.katio.repository.BookRepository;
import edu.eafit.katio.repository.BooksByAuthorRepository;
import edu.eafit.katio.repository.GenreByBookRepository;

public class BookService implements BaseBookService {
    
    private BookRepository _bookRepository;
    private BooksByAuthorRepository _BooksByAuthorRepository;
    private GenreByBookRepository _GenreByBookRepository;

    public BookService(BookRepository bookRepository){
        _bookRepository = bookRepository;
    }

    public BookService(BooksByAuthorRepository booksByAuthorRepository){
    _BooksByAuthorRepository = booksByAuthorRepository;
    }

    public BookService(BookRepository bookRepository, GenreByBookRepository genreByBookRepository){
        _bookRepository = bookRepository;
        _GenreByBookRepository = genreByBookRepository;
    }

    // Traer todos los Libros
    @Override
    public Iterable<Books> getAllBooks() {
        var bookList = _bookRepository.findAll();
        return bookList;
    }

    // Traer Libros por Id
    @Override
    public List<Books> getBooksById(Integer Id) {
        var bookList = _bookRepository.findById(Id);
        return bookList;
    }

    // Traer Libros por Nombre
    @Override
    public List<Books> getBooksByName(String Name) {
        var bookList = _bookRepository.findByName(Name);
        return bookList;
    }

    //Traer Libros por Editorial
    @Override
    public List<Books> getBooksByEdition(String Edition) {
        var bookList = _bookRepository.findByEdition(Edition);
        return bookList;
    }

    //Traer Libros por Genero
    @Override
    public List<Books> getBooksByGenre(String Genre) {
        var bookList = _bookRepository.findByGenre(Genre);
        return bookList;
    }

    // Traer libros por rango de fechas de publicación
    public List<Books> getBooksByDateRange(Date startDate, Date endDate) {
        var bookList = _bookRepository.findByPublishedDateBetween(startDate, endDate);
        return bookList;
    }

    // Editar un Libro
    @Override
    public Books updateBook(String name, Books books) {
        Optional<Books> oldBook = _bookRepository.findByNameEdit(name);
        if(oldBook.isPresent() && !oldBook.get().getName().isBlank()) 
        {
            oldBook.get().setName(books.getName());
            oldBook.get().setISBN10(books.getISBN10());
            oldBook.get().setISBN13(books.getISBN13());
            oldBook.get().setPublished(books.getPublished());
            oldBook.get().setEdition(books.getEdition());
            oldBook.get().setGenre(books.getGenre());
            oldBook.get().setDeweyIndex(books.getDeweyIndex());

            return _bookRepository.saveAndFlush(oldBook.get());
        } else {
            return null;
        }
    }
    
    // Crear Libro
    @Override
    public Books addBooks(Books books) {

        Optional<Books> existingBook = _bookRepository.findByNameAdd(books.getName());
        if (existingBook.isPresent()) {
            throw new RuntimeException("El libro ya existe");
        }
        return _bookRepository.saveAndFlush(books);
    }

    // Traer libros por Id del autor
    @Override
    public List<BooksByAuthor> getAllBooksByAuthorId(Integer idAuthor) {
       List<BooksByAuthor> bookList = new ArrayList<BooksByAuthor>();
       bookList = _BooksByAuthorRepository.findByAuthorId(idAuthor);
       return bookList;
    }

    // Traer libros por el nombre del autor
    @Override 
    public List<BooksByAuthor> getAllBooksByAuthorName(String nameAuthor) {
        List<BooksByAuthor> bookList = new ArrayList<BooksByAuthor>();
        bookList = _BooksByAuthorRepository.findByAuthorName(nameAuthor);
        return bookList;
    }

    // Traer libros por el nombre y/o apellido del autor
    @Override
    public List<BooksByAuthor> getAllBooksByAuthor(String nameAuthor, String lastNameAuthor) {
        List<BooksByAuthor> bookList = new ArrayList<BooksByAuthor>();

        if (lastNameAuthor.length() > 0 && nameAuthor.length() <= 0) 
        {
            bookList = _BooksByAuthorRepository.findByAuthorLastName(lastNameAuthor);
        }
        else if (lastNameAuthor.length() <= 0 && nameAuthor.length() > 0) 
        {
            bookList = _BooksByAuthorRepository.findByAuthorName(nameAuthor);
        }
        else 
        {
            bookList = _BooksByAuthorRepository.findByAuthorFullName(lastNameAuthor, nameAuthor);
        }
        return bookList;
    }
    
    @Override
    public boolean addGenre(GenreInsertdto genreInsertdto) {
        var book = _bookRepository.findById(genreInsertdto.getBookId());
        if(book.isEmpty()){
            return false;
        }

        for (var item : genreInsertdto.getGenreList()) {
            var genre = new GenreByBook();
            genre.setBookid(book.get().getId());
            genre.setGenreId(item.getId());
            _GenreByBookRepository.saveAndFlush(genre);
        }

        return true;
    }

}
