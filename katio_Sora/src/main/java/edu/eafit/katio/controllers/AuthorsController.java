package edu.eafit.katio.controllers;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import edu.eafit.katio.models.Authors;
import edu.eafit.katio.repository.AuthorRepository;
import edu.eafit.katio.services.AuthorService;



@RestController
@RequestMapping("/katio/authors")
@CrossOrigin(origins = "*")
public class AuthorsController {
    @Autowired
    private AuthorRepository _authorRepository;


    // Traer todos
    @GetMapping("/getall")
    public ResponseEntity<Iterable<Authors>> getAllAuthors() {
        var authors = _authorRepository.findAll();
        return new ResponseEntity<Iterable<Authors>>(authors, HttpStatus.OK);
    }
    
    // Buscar por id
    @GetMapping("/getById")
    public ResponseEntity<Iterable<Authors>> getAuthorById(@RequestParam("id") Integer id) 
    {
        var authorById = new AuthorService(_authorRepository).getAuthorById(id);
        return new ResponseEntity<Iterable<Authors>>(authorById, HttpStatus.OK);
    }

    // Buscar por nombre
    @GetMapping("/getByName")
    public ResponseEntity<Iterable<Authors>> getAuthorByName(@RequestParam("name") String name) 
    {
        var authorByName = new AuthorService(_authorRepository).getAuthorByName(name);
        return new ResponseEntity<Iterable<Authors>>(authorByName, HttpStatus.OK);
    }

    // Buscar por pais
    @GetMapping("/getByCountry")
    public ResponseEntity<Iterable<Authors>> getAuthorByCountry(@RequestParam("country") String country) {
        var authorByCountry = new AuthorService(_authorRepository).getAuthorByCountry(country);
        return new ResponseEntity<Iterable<Authors>>(authorByCountry, HttpStatus.OK);
    }

    // Traer Autores por rango de fecha de nacimiento
    @GetMapping("/getByDate/{startDate}/{endDate}")
    public ResponseEntity<List<Authors>> getAuthorsByDate(
        @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
        @PathVariable("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        var response = new AuthorService(_authorRepository).getAuthorsByDateRange(startDate, endDate);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Crear autor
    @PostMapping("/add")
    public ResponseEntity<Authors> addAuthors(@RequestBody Authors authors) {
        try {
            Authors createAuthor = new AuthorService(_authorRepository) .addAuthors(authors);
            return new ResponseEntity<>(createAuthor, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Editar autor
    @PutMapping("/update/{name}")
    public ResponseEntity<Object> updateAuthors(@PathVariable("name") String name, @RequestBody Authors updateAuthors) {
        Authors updatedAuthors = new AuthorService(_authorRepository) .updateAuthor(name, updateAuthors);
        if (updatedAuthors != null) {
            return new ResponseEntity<>(updatedAuthors, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Autor no Encontrado", HttpStatus.NOT_FOUND);
        }
    }

   
}