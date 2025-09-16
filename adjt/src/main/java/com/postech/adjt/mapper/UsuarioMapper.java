package com.postech.adjt.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.postech.adjt.dto.EnderecoDTO;
import com.postech.adjt.dto.TipoUsuarioDTO;
import com.postech.adjt.dto.usuario.UsuarioDTO;
import com.postech.adjt.model.Endereco;
import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.model.Usuario;

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
public class UsuarioMapper {

    /**
     * Mapper responsável pela conversão entre {@link TipoUsuario} e
     * {@link TipoUsuarioDTO}.
     */
    private final TipoUsuarioMapper tipoUsuarioMapper;

    /**
     * Construtor que injeta o mapper de tipo de usuário.
     *
     * @param tipoUsuarioMapper Mapper para {@link TipoUsuario}.
     */
    public UsuarioMapper(TipoUsuarioMapper tipoUsuarioMapper) {
        this.tipoUsuarioMapper = tipoUsuarioMapper;
    }

    /**
     * Converte uma entidade {@link Usuario} em um objeto {@link UsuarioDTO}.
     *
     * <p>
     * Inclui dados básicos, tipo de usuário e lista de endereços (se houver).
     * </p>
     *
     * @param entidade Entidade {@link Usuario} a ser convertida.
     * @return Objeto {@link UsuarioDTO} correspondente.
     */
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

        Usuario entidade = new Usuario();
        entidade.setId(dto.getId());
        entidade.setDataCriacao(dto.getDataAlteracao());
        entidade.setDataAlteracao(dto.getDataAlteracao());
        entidade.setAtivo(dto.getAtivo());
        entidade.setNome(dto.getNome());
        entidade.setEmail(dto.getEmail());
        entidade.setSenha(dto.getSenha());

        TipoUsuario tipoUsuario = this.tipoUsuarioMapper.toTipoUsuario(dto.getTipoUsuario());
        entidade.setTipoUsuario(tipoUsuario);

        return entidade;
    }

    /**
     * Converte uma entidade {@link Endereco} em um objeto {@link EnderecoDTO}.
     *
     * @param entidade Entidade {@link Endereco} a ser convertida.
     * @return Objeto {@link EnderecoDTO} correspondente.
     */
    public EnderecoDTO toEnderecoDTO(Endereco entidade) {
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

    /**
     * Converte um objeto {@link EnderecoDTO} em uma entidade {@link Endereco}.
     *
     * @param dto Objeto {@link EnderecoDTO} a ser convertido.
     * @return Entidade {@link Endereco} correspondente.
     */
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
