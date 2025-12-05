package com.postech.adjt.data.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entidade.EnderecoEntidade;
import com.postech.adjt.data.entidade.UsuarioEntidade;
import com.postech.adjt.domain.entidade.Endereco;
import com.postech.adjt.domain.entidade.Usuario;

/**
 * Componente responsável por mapear objetos entre as entidades e o modelo de
 * domínio.
 *
 * <p>
 * Realiza conversões bidirecionais entre {@link UsuarioEntidade} e
 * {@link Usuario},
 * bem como entre {@link EnderecoEntidade} e {@link Endereco}.
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

    public static UsuarioEntidade toEntity(Usuario usuario) {
        if (usuario == null)
            return null;


        UsuarioEntidade entidade = new UsuarioEntidade();
        entidade.setId(usuario.getId());
        entidade.setNome(usuario.getNome());
        entidade.setEmail(usuario.getEmail());
        entidade.setSenha(usuario.getSenha());
        entidade.setTipoUsuario(TipoUsuarioMapper.toEntity(usuario.getTipoUsuario()));
        entidade.setAtivo(usuario.getAtivo());
        entidade.setDataCriacao(usuario.getDataCriacao());
        entidade.setDataAlteracao(usuario.getDataAlteracao());

        if (usuario.getEnderecos() != null) {
            List<EnderecoEntidade> enderecos = usuario.getEnderecos().stream()
                    .map(e -> toEntityEndereco(e, entidade))
                    .collect(Collectors.toList());

            entidade.setEnderecos(enderecos);
        }

        return entidade;
    }

    private static EnderecoEntidade toEntityEndereco(Endereco endereco, UsuarioEntidade usuarioEntidade) {
        EnderecoEntidade entidade = new EnderecoEntidade();
        entidade.setId(endereco.getId());
        entidade.setLogradouro(endereco.getLogradouro());
        entidade.setNumero(endereco.getNumero());
        entidade.setComplemento(endereco.getComplemento());
        entidade.setBairro(endereco.getBairro());
        entidade.setPontoReferencia(endereco.getPontoReferencia());
        entidade.setCep(endereco.getCep());
        entidade.setMunicipio(endereco.getMunicipio());
        entidade.setUf(endereco.getUf());
        entidade.setPrincipal(endereco.getPrincipal());
        entidade.setAtivo(endereco.getAtivo());
        entidade.setDataCriacao(endereco.getDataCriacao());
        entidade.setDataAlteracao(endereco.getDataAlteracao());

        // vínculo obrigatório
        entidade.setUsuario(usuarioEntidade);

        return entidade;
    }

    public static Usuario toDomain(UsuarioEntidade entidade) {
        if (entidade == null)
            return null;

        Usuario usuario = Usuario.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .email(entidade.getEmail())
                .senha(entidade.getSenha())
                .tipoUsuario(TipoUsuarioMapper.toDomain(entidade.getTipoUsuario()))
                .enderecos(entidade.getEnderecos() != null
                        ? entidade.getEnderecos().stream()
                                .map(UsuarioMapper::toDomainEndereco)
                                .collect(Collectors.toList())
                        : null)
                .ativo(entidade.getAtivo())
                .dataCriacao(entidade.getDataCriacao())
                .dataAlteracao(entidade.getDataAlteracao())
                .build();

        return usuario;
    }

    private static Endereco toDomainEndereco(EnderecoEntidade entidade) {
        return Endereco.builder()
                .id(entidade.getId())
                .logradouro(entidade.getLogradouro())
                .numero(entidade.getNumero())
                .complemento(entidade.getComplemento())
                .bairro(entidade.getBairro())
                .pontoReferencia(entidade.getPontoReferencia())
                .cep(entidade.getCep())
                .municipio(entidade.getMunicipio())
                .uf(entidade.getUf())
                .principal(entidade.getPrincipal())
                .ativo(entidade.getAtivo())
                .build();
    }
}
