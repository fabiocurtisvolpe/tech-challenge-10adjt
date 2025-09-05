package com.postech.adjt.controller;

import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.dto.UsuarioDTO;
import com.postech.adjt.service.UsuarioService;

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
@RequestMapping("/api/usuario")
public class UsuarioController {

    protected final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping("/criar")
    public UsuarioDTO criar(@RequestBody UsuarioDTO dto) {
        return this.service.criar(dto);
    }

    @PutMapping("/{id}")
    public UsuarioDTO atualizar(@PathVariable Integer id, @RequestBody UsuarioDTO dto) {
        return this.service.atualizar(id, dto);
    }

    @GetMapping("/{id}")
    public UsuarioDTO buscar(@PathVariable Integer id) {
        return this.service.buscar(id);
    }

    @GetMapping("/listar")
    public List<UsuarioDTO> listar() {
        return this.service.listar();
    }

    @PostMapping("/paginado")
    public Page<UsuarioDTO> paginado(@RequestBody FiltroGenericoDTO filtro) {
        return this.service.listarPaginado(filtro);
    }

    @DeleteMapping("/{id}")
    public UsuarioDTO ativarInativar(@PathVariable Integer id) {
        return this.service.ativarInativar(id);
    }
}
