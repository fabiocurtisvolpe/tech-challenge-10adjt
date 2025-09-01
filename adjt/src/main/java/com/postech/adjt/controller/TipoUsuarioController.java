package com.postech.adjt.controller;

import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.service.TipoUsuarioService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/api/tipo-usuario")
public class TipoUsuarioController {

    protected final TipoUsuarioService service;

    public TipoUsuarioController(TipoUsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public TipoUsuarioDTO criar(@RequestBody TipoUsuarioDTO dto) {    
        return this.service.criar(dto);
    }

    @PutMapping("/{id}")
    public TipoUsuarioDTO atualizar(@PathVariable Integer id, @RequestBody TipoUsuarioDTO dto) {
        return this.service.atualizar(id, dto);
    }

}
