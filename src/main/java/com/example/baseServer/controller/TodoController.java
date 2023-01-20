package com.example.baseServer.controller;

import javax.validation.Valid;

import com.example.baseServer.dto.ChangeStatusTodoDto;
import com.example.baseServer.dto.ChangeTextTodoDto;
import com.example.baseServer.dto.CreateTodoDto;
import com.example.baseServer.service.TodoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/v1/todo")
public class TodoController {

    @Autowired
    private TodoService todoService;

    @PostMapping
    public ResponseEntity create(@Valid @RequestBody CreateTodoDto dto) {
        return new ResponseEntity(todoService.create(dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.delete(id));
    }

    @DeleteMapping
    public ResponseEntity deleteReady() {
        return new ResponseEntity<>(todoService.deleteAllReady(), HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity patch(@RequestBody ChangeStatusTodoDto status) throws IOException {
        return new ResponseEntity(todoService.patch(status), HttpStatus.OK);
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity patchStatus(@RequestBody ChangeStatusTodoDto status, @PathVariable String id) {
        return new ResponseEntity(todoService.patchStatus(status, id), HttpStatus.OK);
    }

    @PatchMapping("/text/{id}")
    public ResponseEntity patchText(@Valid @RequestBody ChangeTextTodoDto text, @PathVariable String id) {
        return new ResponseEntity(todoService.patchText(text, id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity ResponseEntity(@RequestParam("page") Integer page,
                                         @RequestParam("perPage") Integer perPage,
                                         Boolean status) throws Exception {
        return new ResponseEntity(todoService.search(page, perPage, status), HttpStatus.OK);
    }
}
