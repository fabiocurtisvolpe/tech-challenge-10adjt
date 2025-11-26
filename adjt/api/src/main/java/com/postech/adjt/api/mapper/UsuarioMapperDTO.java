package com.postech.adjt.api.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.dto.EnderecoDTO;
import com.postech.adjt.api.dto.UsuarioDTO;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;

/**
 * Componente responsável por mapear objetos entre as entidades do modelo
 * e os DTOs utilizados na camada de apresentação.
 *
 * <p>
 * Realiza conversões bidirecionais entre {@link Usuario} e {@link UsuarioDTO},
 * bem como entre {@link Endereco} e {@link EnderecoDTO}.
 * </p>
 *
 * <p>
 * Utiliza {@link TipoUsuarioMapper} para mapear o tipo de usuário associado.
 * </p>
 *
 * @author Fabio
 * @since 2025-09-08
 */
@Component
public class UsuarioMapperDTO {

    /**
     * Converte uma model {@link Usuario} em um objeto {@link UsuarioDTO}.
     *
     * <p>
     * Inclui dados básicos, tipo de usuário e lista de endereços (se houver).
     * </p>
     *
     * @param model Model {@link Usuario} a ser convertida.
     * @return Objeto {@link UsuarioDTO} correspondente.
     */
    public UsuarioDTO toUsuarioDTO(Usuario model) {
        if (Objects.isNull(model)) {
            return null;
        }

        List<EnderecoDTO> listEnderecoDTO = new ArrayList<>();

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(model.getId());
        dto.setNome(model.getNome());
        dto.setEmail(model.getEmail());
        dto.setTipoUsuario(model.getTipoUsuario());

        if (!model.getEnderecos().isEmpty()) {
            model.getEnderecos().forEach(endereco -> {
                listEnderecoDTO.add(this.toEnderecoDTO(endereco));
            });

            dto.setEnderecos(listEnderecoDTO);
        }

        return dto;
    }

    /**
     * Converte um objeto {@link UsuarioDTO} em uma entidade {@link Usuario}.
     *
     * <p>
     * Inclui dados básicos e tipo de usuário. Endereços não são mapeados neste
     * método.
     * </p>
     *
     * @param dto Objeto {@link UsuarioDTO} a ser convertido.
     * @return Entidade {@link Usuario} correspondente.
     */
    public Usuario toUsuario(UsuarioDTO dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        List<Endereco> enderecos = new ArrayList<>();

        if (Objects.nonNull(dto.getEnderecos()) && !dto.getEnderecos().isEmpty()) {
            for (EnderecoDTO endereco : dto.getEnderecos()) {
                enderecos.add(this.toEndereco(endereco));
            }
        }

        return new Usuario(dto.getId(), dto.getNome(), dto.getEmail(),
                dto.getSenha(), dto.getTipoUsuario(), enderecos);
    }

    /**
     * Converte um model {@link Endereco} em um objeto {@link EnderecoDTO}.
     *
     * @param model Model {@link Endereco} a ser convertida.
     * @return Objeto {@link EnderecoDTO} correspondente.
     */
    public EnderecoDTO toEnderecoDTO(Endereco model) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setId(model.getId());
        dto.setDataCriacao(model.getDataAlteracao());
        dto.setDataAlteracao(model.getDataAlteracao());
        dto.setAtivo(model.getAtivo());
        dto.setLogradouro(model.getLogradouro());
        dto.setNumero(model.getNumero());
        dto.setComplemento(model.getComplemento());
        dto.setBairro(model.getBairro());
        dto.setPontoReferencia(model.getPontoReferencia());
        dto.setCep(model.getCep());
        dto.setMunicipio(model.getMunicipio());
        dto.setUf(model.getUf());
        dto.setPrincipal(model.getPrincipal());

        return dto;
    }

    /**
     * Converte um objeto {@link EnderecoDTO} em uma entidade {@link Endereco}.
     *
     * @param dto Objeto {@link EnderecoDTO} a ser convertido.
     * @return Entidade {@link Endereco} correspondente.
     */
    public Endereco toEndereco(EnderecoDTO dto) {
        return new Endereco(dto.getId(), dto.getLogradouro(),
                dto.getNumero(), dto.getComplemento(), dto.getBairro(),
                dto.getPontoReferencia(), dto.getCep(), dto.getMunicipio(),
                dto.getUf(), dto.getPrincipal(), null);
    }
}
