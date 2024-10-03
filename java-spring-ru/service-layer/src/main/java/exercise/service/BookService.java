package exercise.service;

import exercise.dto.BookCreateDTO;
import exercise.dto.BookDTO;
import exercise.dto.BookUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.BookMapper;
import exercise.repository.AuthorRepository;
import exercise.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    // BEGIN

    @Autowired
    private BookMapper bookMapper;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public List<BookDTO> getAll(){
        return bookRepository.findAll().stream().map(bookMapper::map).toList();
    }

    public BookDTO findById(Long id){
        var book = bookRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        return bookMapper.map(book);
    }


    public ResponseEntity<BookDTO> create(BookCreateDTO dto){
        var book = bookMapper.map(dto);
        if(authorRepository.findById(dto.getAuthorId()).isPresent()){
            book.setAuthor(authorRepository.findById(dto.getAuthorId()).get());
            bookRepository.save(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(bookMapper.map(book));
        }
        else return ResponseEntity.badRequest().build();
    }

    public BookDTO update(BookUpdateDTO dto, Long id){
        var book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book with id " + id + " not found"));
        bookMapper.update(dto,book);
        bookRepository.save(book);
        return bookMapper.map(book);
    }

    public void delete(Long id){
        bookRepository.deleteById(id);
    }
    // END
}
