package com.budgets.modules.user.controllers;

import com.budgets.modules.security.config.JwtUtil;
import com.budgets.modules.security.util.Extration;
import com.budgets.modules.user.dto.*;
import com.budgets.modules.user.entities.User;
import com.budgets.modules.user.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.io.DecodingException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController<decodeToken> {

    private final UserService service;

    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UserRegisterDTO> create(@RequestBody User user) {
        try {
            UserRegisterDTO newUser = service.insert(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
        catch (DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping(value = "/auth/login")
    public ResponseEntity<UserRegisterDTO> login(@RequestBody UserLoginDTO user, HttpServletResponse request) {
        try {
            UserRegisterDTO byUser = service.findByUser(user);
            String token = jwtUtil.generationToken(byUser.getId());
            request.addHeader("Authorization", "Bearer "+token);
            return new ResponseEntity<>(byUser, HttpStatus.OK);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TokenDTO decodeToken(@RequestHeader("Authorization") String token) throws JsonProcessingException {
        Claims claims = jwtUtil.getClaims(token.replace("Bearer ", ""));
        Extration extration = new Extration();
        Long id = extration.extrationId(claims);
        return new TokenDTO(id);
    }

    @GetMapping()
    public ResponseEntity<UserOutputDTO> getUser(@RequestHeader("Authorization") String token) {
        try {
            TokenDTO tokenDTO = decodeToken(token);
            UserOutputDTO user = service.findById(tokenDTO.getId());
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping
    public ResponseEntity<UserOutputDTO> changePassword(@RequestHeader("Authorization") String token,
                                                    @RequestBody EditUserDTO user) {
        try {
            TokenDTO tokenDTO = decodeToken(token);
            UserOutputDTO userOutputDTO = service.update(tokenDTO.getId(), user);
            return new ResponseEntity<>(userOutputDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
