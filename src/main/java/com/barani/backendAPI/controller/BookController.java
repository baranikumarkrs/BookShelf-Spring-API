package com.barani.backendAPI.controller;

import com.barani.backendAPI.model.Book;
import com.barani.backendAPI.repo.BookRepo;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class BookController {

    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/getAllBooks")
    public ResponseEntity<List<Book>> getAllBooks(){
        try {
            return ResponseEntity.ok(bookRepo.findAll());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/getBookById/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId){
        try {
            Book book = bookRepo.findById(bookId).orElseThrow(NoSuchElementException::new);
            return ResponseEntity.ok(book);
        } catch (NoSuchElementException ex){
            return ResponseEntity.notFound().build();
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/getBooksByNameContaining/{bookName}")
    public ResponseEntity<List<Book>> getBooksByNameContaining(@PathVariable String bookName){
        return null;
    }

//    @PostMapping("/storeBookCover")
//    public ResponseEntity<BookThumbnail> storeBookThumbnail(@RequestParam("cover") MultipartFile imgFile){
//        if(imgFile == null)
//            return ResponseEntity.badRequest().build();
//        try {
//            BookThumbnail thumbnail = new BookThumbnail();
//            thumbnail.setThumbnailImage(new Binary(BsonBinarySubType.BINARY, imgFile.getBytes()));
//            BookThumbnail savedData = bookThumbnailRepo.save(thumbnail);
//            return ResponseEntity.ok(savedData);
//        } catch (Exception e){
//            return ResponseEntity.internalServerError().build();
//        }
//    }

//    @GetMapping("getBookCoverById")
//    public ResponseEntity<BookThumbnail> getBookThumbnailById(@RequestParam("id") String id){
//
//        if(id == null || id.isEmpty())
//            return ResponseEntity.badRequest().build();
//
//        Optional<BookThumbnail> data = bookThumbnailRepo.findById(id);
//
//        return data.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//
//    }

    @PostMapping(value = "/addBook")
    public ResponseEntity<Book> addBook(@RequestParam("bookName") String name, @RequestParam("bookAuthor") String author
            , @RequestParam("bookCover") MultipartFile imgFile){

        if(name.isEmpty() || author.isEmpty())
            return ResponseEntity.badRequest().build();

        try {

            Book book = new Book(name,author);

            if(imgFile != null)
                book.setThumbnailImage(new Binary(BsonBinarySubType.BINARY, imgFile.getBytes()));

            return ResponseEntity.ok(bookRepo.save(book));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @PostMapping("/updateBookById")
    public ResponseEntity<Book> updateBookById(@RequestBody Book book){

        Optional<Book> oldBook = bookRepo.findById(Long.valueOf(book.getId()));

        if(oldBook.isPresent()){
            Book updatedBook = oldBook.get();
            updatedBook.setName(book.getName());
            updatedBook.setAuthor(book.getAuthor());
            return ResponseEntity.ok(bookRepo.save(updatedBook));
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    @DeleteMapping("/deleteBookById/{bookId}")
    public ResponseEntity<HttpStatus> deleteBookById(@PathVariable Long bookId){
        bookRepo.deleteById(bookId);
        return ResponseEntity.ok().build();
    }

}
