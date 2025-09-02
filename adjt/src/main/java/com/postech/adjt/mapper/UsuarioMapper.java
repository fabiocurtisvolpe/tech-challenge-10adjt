package com.postech.adjt.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.postech.adjt.dto.EnderecoDTO;
import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.dto.UsuarioDTO;
import com.postech.adjt.model.Endereco;
import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.model.Usuario;

@Component
public class UsuarioMapper {

    private final TipoUsuarioMapper tipoUsuarioMapper;

    public UsuarioMapper(TipoUsuarioMapper tipoUsuarioMapper) {
        this.tipoUsuarioMapper = tipoUsuarioMapper;
    }

    public UsuarioDTO toUsuarioDTO(Usuario entidade) {
        if (Objects.isNull(entidade)) {
            return null;
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entidade.getId());
        dto.setDataCriacao(entidade.getDataAlteracao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setAtivo(entidade.getAtivo());
        dto.setNome(entidade.getNome());
        dto.setEmail(entidade.getEmail());
        dto.setLogin(entidade.getLogin());
        dto.setSenha(entidade.getSenha());

        TipoUsuarioDTO tipoUsuarioDTO = this.tipoUsuarioMapper.toTipoUsuarioDTO(entidade.getTipoUsuario());
        dto.setTipoUsuario(tipoUsuarioDTO);

        if (!entidade.getEnderecos().isEmpty()) {
            List<EnderecoDTO> listEnderecoDTO = new ArrayList<>();
            entidade.getEnderecos().forEach(endereco -> {
                listEnderecoDTO.add(this.toEnderecoDTO(endereco));
            });

            dto.setEnderecos(listEnderecoDTO);
        }

        return dto;
    }

    public Usuario toUsuario(UsuarioDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        Usuario entidade = new Usuario();
        entidade.setId(dto.getId());
        entidade.setDataCriacao(dto.getDataAlteracao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setAtivo(dto.getAtivo());
        entidade.setNome(dto.getNome());
        entidade.setEmail(dto.getEmail());
        entidade.setLogin(dto.getLogin());
        entidade.setSenha(dto.getSenha());

        TipoUsuario tipoUsuario = this.tipoUsuarioMapper.toTipoUsuario(dto.getTipoUsuario());
        entidade.setTipoUsuario(tipoUsuario);

        if (!dto.getEnderecos().isEmpty()) {
            List<Endereco> listEndereco = new ArrayList<>();
            dto.getEnderecos().forEach(endereco -> {
                listEndereco.add(this.toEndereco(endereco));
            });

            entidade.setEnderecos(listEndereco);
        }

        return entidade;
    }

    private EnderecoDTO toEnderecoDTO(Endereco entidade) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(entidade.getId());
        dto.setDataCriacao(entidade.getDataAlteracao());
        dto.setDataAlteracao(entidade.getDataAlteracao());
        dto.setAtivo(entidade.getAtivo());
        dto.setLogradouro(entidade.getLogradouro());
        dto.setNumero(entidade.getNumero());
        dto.setComplemento(entidade.getComplemento());
        dto.setBairro(entidade.getBairro());
        dto.setPontoReferencia(entidade.getPontoReferencia());
        dto.setCep(entidade.getCep());
        dto.setMunicipio(entidade.getMunicipio());
        dto.setUf(entidade.getUf());
        dto.setPrincipal(entidade.getPrincipal());

        return dto;
    }

    public Endereco toEndereco(EnderecoDTO dto) {
        Endereco entidade = new Endereco();
        entidade.setId(dto.getId());
        entidade.setDataCriacao(dto.getDataAlteracao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setAtivo(dto.getAtivo());
        entidade.setLogradouro(dto.getLogradouro());
        entidade.setNumero(dto.getNumero());
        entidade.setComplemento(dto.getComplemento());
        entidade.setBairro(dto.getBairro());
        entidade.setPontoReferencia(dto.getPontoReferencia());
        entidade.setCep(dto.getCep());
        entidade.setMunicipio(dto.getMunicipio());
        entidade.setUf(dto.getUf());
        entidade.setPrincipal(dto.getPrincipal());

        return entidade;
    }
}
