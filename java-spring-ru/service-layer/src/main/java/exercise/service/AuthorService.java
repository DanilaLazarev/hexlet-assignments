package exercise.service;

import exercise.dto.AuthorCreateDTO;
import exercise.dto.AuthorDTO;
import exercise.dto.AuthorUpdateDTO;
import exercise.exception.ResourceNotFoundException;
import exercise.mapper.AuthorMapper;
import exercise.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorService {
    // BEGIN
    @Autowired
    private AuthorMapper  authorMapper;

    @Autowired
    private AuthorRepository authorRepository;

    public List<AuthorDTO> getAll(){
        return authorRepository.findAll().stream().map(authorMapper::map).toList();
    }

    public AuthorDTO findById(Long id){
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not Found"));
        return authorMapper.map(author);
    }

    public AuthorDTO create(AuthorCreateDTO dto){
        var author = authorMapper.map(dto);
        authorRepository.save(author);
        return authorMapper.map(author);
    }

    public AuthorDTO update(AuthorUpdateDTO dto,Long id){
        var author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author with id " + id + " not Found"));
        authorMapper.update(dto,author);
        authorRepository.save(author);
        return authorMapper.map(author);
    }

    public void delete(Long id){
        authorRepository.deleteById(id);
    }
    // END
}
