package com.api.restful.apirestfulspringbootjava.controllers;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.api.restful.apirestfulspringbootjava.models.User;
import com.api.restful.apirestfulspringbootjava.models.dto.UserCreateDTO;
import com.api.restful.apirestfulspringbootjava.models.dto.UserUpdateDTO;
import com.api.restful.apirestfulspringbootjava.security.UserSpringSecurity;
import com.api.restful.apirestfulspringbootjava.services.UserService;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    //@PathVariable vai dizer que a variável que está sendo recebida em @GetMapping("/{id}") é o long id de User
    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable long id){
        User obj=this.userService.findById(id);
        return ResponseEntity.ok().body(obj);
        //Retornar a resposta statusCode e o corpo da resposta com o objeto 
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO obj) {
        User user = this.userService.fromDTO(obj);
        User newUser = this.userService.create(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newUser.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO obj,@PathVariable long id){
        obj.setId(id);
        User user=this.userService.fromDTO(obj);
        this.userService.update(user);
        return ResponseEntity.noContent().build();

    }

@DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id){
        this.userService.delete(id);;
        return ResponseEntity.noContent().build();
}
public static UserSpringSecurity authenticated(){
    try {
        return(UserSpringSecurity)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    } catch (Exception e) {
       return null;
    }
}
}
