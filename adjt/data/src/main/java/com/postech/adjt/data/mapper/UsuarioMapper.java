package com.postech.adjt.data.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import com.postech.adjt.data.entity.EnderecoEntity;
import com.postech.adjt.data.entity.UsuarioEntity;
import com.postech.adjt.data.entity.TipoUsuarioEntity;
import com.postech.adjt.domain.model.Endereco;
import com.postech.adjt.domain.model.TipoUsuario;
import com.postech.adjt.domain.model.Usuario;


/**
 * Componente responsável por mapear objetos entre as entidades e o modelo de domínio.
 *
 * <p>
 * Realiza conversões bidirecionais entre {@link UsuarioEntity} e
 * {@link Usuario},
 * bem como entre {@link EnderecoEntity} e {@link Endereco}.
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

    private final TipoUsuarioMapper tipoUsuarioMapper;

    public UsuarioMapper(TipoUsuarioMapper tipoUsuarioMapper) {
        this.tipoUsuarioMapper = tipoUsuarioMapper;
    }

    /**
     * Converte uma entidade para um objeto de domínio
     *
     * @param entity A entidade a ser convertida
     * @return O objeto de domínio correspondente
     */
    public Usuario toDomain(UsuarioEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setId(entity.getId());
        usuario.setDataCriacao(entity.getDataCriacao());
        usuario.setDataAlteracao(entity.getDataAlteracao());
        usuario.setAtivo(entity.getAtivo());
        usuario.setNome(entity.getNome());
        usuario.setEmail(entity.getEmail());
        usuario.setSenha(entity.getSenha());

        TipoUsuario tipoUsuario = this.tipoUsuarioMapper.toDomain(entity.getTipoUsuario());
        usuario.setTipoUsuario(tipoUsuario);

        if (Objects.nonNull(entity.getEnderecos()) && !entity.getEnderecos().isEmpty()) {
            List<Endereco> enderecos = new ArrayList<>();
            for (EnderecoEntity endereco : entity.getEnderecos()) {
                enderecos.add(this.toEnderecoDomain(endereco));
            }
            usuario.setEnderecos(enderecos);
        }

        return usuario;
    }

    /**
     * Converte uma lista de entidades para uma lista de objetos de domínio
     *
     * @param entities A lista de entidades a ser convertida
     * @return A lista de objetos de domínio correspondente
     */
    public List<Usuario> toDomainList(List<UsuarioEntity> entities) {
        if (Objects.isNull(entities)) {
            return null;
        }

        List<Usuario> usuarios = new ArrayList<>();
        for (UsuarioEntity entity : entities) {
            usuarios.add(this.toDomain(entity));
        }

        return usuarios;
    }

    /**
     * Converte um objeto de domínio para uma entidade
     *
     * @param usuario O objeto de domínio a ser convertido
     * @return A entidade correspondente
     */
    public UsuarioEntity toEntity(Usuario usuario) {
        if (Objects.isNull(usuario)) {
            return null;
        }

        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(usuario.getId());
        entity.setDataCriacao(usuario.getDataCriacao());
        entity.setDataAlteracao(usuario.getDataAlteracao());
        entity.setAtivo(usuario.getAtivo());
        entity.setNome(usuario.getNome());
        entity.setEmail(usuario.getEmail());
        entity.setSenha(usuario.getSenha());

        TipoUsuarioEntity tipoUsuario = this.tipoUsuarioMapper.toEntity(usuario.getTipoUsuario());
        entity.setTipoUsuario(tipoUsuario);

        if (Objects.nonNull(usuario.getEnderecos()) && !usuario.getEnderecos().isEmpty()) {
            List<EnderecoEntity> enderecos = new ArrayList<>();
            for (Endereco endereco : usuario.getEnderecos()) {
                enderecos.add(this.toEnderecoEntity(endereco));
            }
            entity.setEnderecos(enderecos);
        }

        return entity;
    }

    /**
     * Converte uma entidade de endereço para um objeto de domínio
     *
     * @param entity A entidade de endereço a ser convertida
     * @return O objeto de domínio correspondente
     */
    public Endereco toEnderecoDomain(EnderecoEntity entity) {
        if (Objects.isNull(entity)) {
            return null;
        }

        Endereco endereco = new Endereco();
        endereco.setId(entity.getId());
        endereco.setDataCriacao(entity.getDataCriacao());
        endereco.setDataAlteracao(entity.getDataAlteracao());
        endereco.setAtivo(entity.getAtivo());
        endereco.setLogradouro(entity.getLogradouro());
        endereco.setNumero(entity.getNumero());
        endereco.setComplemento(entity.getComplemento());
        endereco.setBairro(entity.getBairro());
        endereco.setPontoReferencia(entity.getPontoReferencia());
        endereco.setCep(entity.getCep());
        endereco.setMunicipio(entity.getMunicipio());
        endereco.setUf(entity.getUf());
        endereco.setPrincipal(entity.getPrincipal());

        return endereco;
    }

    /**
     * Converte um objeto de domínio de endereço para uma entidade
     *
     * @param endereco O objeto de domínio de endereço a ser convertido
     * @return A entidade correspondente
     */
    public EnderecoEntity toEnderecoEntity(Endereco endereco) {
        if (Objects.isNull(endereco)) {
            return null;
        }

        EnderecoEntity entity = new EnderecoEntity();
        entity.setId(endereco.getId());
        entity.setDataCriacao(endereco.getDataCriacao());
        entity.setDataAlteracao(endereco.getDataAlteracao());
        entity.setAtivo(endereco.getAtivo());
        entity.setLogradouro(endereco.getLogradouro());
        entity.setNumero(endereco.getNumero());
        entity.setComplemento(endereco.getComplemento());
        entity.setBairro(endereco.getBairro());
        entity.setPontoReferencia(endereco.getPontoReferencia());
        entity.setCep(endereco.getCep());
        entity.setMunicipio(endereco.getMunicipio());
        entity.setUf(endereco.getUf());
        entity.setPrincipal(endereco.getPrincipal());

        return entity;
    }
}
