package com.postech.adjt.api.presenter;

import com.postech.adjt.api.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Usuario;

/**
 * Presenter para Usuário.
 * 
 * Responsável por converter dados do domínio (Model) para DTOs
 * que serão retornados pela API (Output Port).
 * 
 * Esta classe faz parte da camada de Interface Adapters (Presenters)
 * da arquitetura limpa, isolando o domínio da representação HTTP.
 * 
 * @author Fabio
 * @since 2025-11-24
 */
public class UsuarioPresenter {

    /**
     * Converte um usuário do domínio para DTO de resposta.
     *
     * @param usuario O usuário do domínio
     * @return DTO com dados do usuário
     */
    public static UsuarioDTO toDTO(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        dto.setTipoUsuario(usuario.getTipoUsuario());
        dto.setAtivo(usuario.getAtivo());
        
        if (usuario.getEnderecos() != null) {
            dto.setEnderecos(
                usuario.getEnderecos().stream()
                    .map(endereco -> {
                        var enderecoDTO = new com.postech.adjt.api.dto.EnderecoDTO();
                        enderecoDTO.setId(endereco.getId());
                        enderecoDTO.setLogradouro(endereco.getLogradouro());
                        enderecoDTO.setNumero(endereco.getNumero());
                        enderecoDTO.setComplemento(endereco.getComplemento());
                        enderecoDTO.setBairro(endereco.getBairro());
                        enderecoDTO.setPontoReferencia(endereco.getPontoReferencia());
                        enderecoDTO.setMunicipio(endereco.getMunicipio());
                        enderecoDTO.setUf(endereco.getUf());
                        enderecoDTO.setCep(endereco.getCep());
                        enderecoDTO.setPrincipal(endereco.getPrincipal());
                        return enderecoDTO;
                    })
                    .toList()
            );
        }

        return dto;
    }
}

