package com.postech.adjt.controller;

import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.service.TipoUsuarioService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/{id}")
    public TipoUsuarioDTO buscar(@PathVariable Integer id) {
        return this.service.buscar(id);
    }

    @GetMapping("/listar")
    public List<TipoUsuarioDTO> listar() {
        return this.service.listar();
    }

    @PostMapping("/paginado")
    public Page<TipoUsuarioDTO> paginado(@RequestBody FiltroGenericoDTO filtro) {
        return this.service.listarPaginado(filtro);
    }

    @DeleteMapping("/{id}")
    public TipoUsuarioDTO ativarInativar(@PathVariable Integer id) {
        return this.service.ativarInativar(id);
    }
}
