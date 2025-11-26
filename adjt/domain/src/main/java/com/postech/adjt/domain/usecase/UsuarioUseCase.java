package com.postech.adjt.domain.usecase;

import java.util.Optional;

import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.dto.filtro.FiltroGenericoDTO;
import com.postech.adjt.domain.entidade.Usuario;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.ports.UsuarioRepositoryPort;

/**
 * Use Case para operações com Usuários.
 * 
 * Implementa a lógica de aplicação relacionada aos usuários,
 * orquestrando as operações do domínio e delegando persistência
 * ao repositório (port).
 * 
 * @author Fabio
 * @since 2025-11-24
 */
public class UsuarioUseCase {

    private final UsuarioRepositoryPort usuarioRepository;

    public UsuarioUseCase(UsuarioRepositoryPort usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cria um novo usuário validando as regras de negócio.
     *
     * @param usuario O usuário a criar
     * @return O usuário criado
     */
    public Usuario criar(Usuario usuario) {
        
        // Verifica se email já existe
        Optional<Usuario> usuarioExistente = usuarioRepository.obterPorEmail(usuario.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new NotificacaoException("Usuário com email " + usuario.getEmail() + " já existe");
        }

        return usuarioRepository.criar(usuario);
    }

    /**
     * Obtém um usuário pelo ID.
     *
     * @param id O ID do usuário
     * @return Optional contendo o usuário
     */
    public Optional<Usuario> obterPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new NotificacaoException("ID inválido");
        }
        return usuarioRepository.obterPorId(id);
    }

    /**
     * Obtém um usuário pelo email.
     *
     * @param email O email do usuário
     * @return Optional contendo o usuário
     */
    public Optional<Usuario> obterPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new NotificacaoException("Email não pode estar vazio");
        }
        return usuarioRepository.obterPorEmail(email);
    }

    /**
     * Atualiza um usuário existente.
     *
     * @param usuario O usuário com dados atualizados
     * @return O usuário atualizado
     */
    public Usuario atualizar(Usuario usuario) {
    

        // Verifica se usuário existe
        Optional<Usuario> usuarioExistente = usuarioRepository.obterPorId(usuario.getId());
        if (usuarioExistente.isEmpty()) {
            throw new NotificacaoException("Usuário com ID " + usuario.getId() + " não encontrado");
        }

        // Verifica se email já existe para outro usuário
        Optional<Usuario> outroUsuario = usuarioRepository.obterPorEmail(usuario.getEmail());
        if (outroUsuario.isPresent() && !outroUsuario.get().getId().equals(usuario.getId())) {
            throw new NotificacaoException("Email " + usuario.getEmail() + " já está em uso");
        }

        return usuarioRepository.atualizar(usuario);
    }

    /**
     * Lista usuários paginados.
     *
     * @param pagina    Número da página
     * @param tamanho   Tamanho da página
     * @param ordenacao Campo para ordenação
     * @return Resultado paginado
     */
    public ResultadoPaginacaoDTO<Usuario> listarPaginado(Integer pagina, Integer tamanho, String ordenacao) {
        if (pagina == null || pagina < 0) {
            throw new NotificacaoException("Número de página inválido");
        }
        if (tamanho == null || tamanho <= 0) {
            throw new NotificacaoException("Tamanho de página inválido");
        }

        return usuarioRepository.listarPaginado(pagina, tamanho, ordenacao);
    }

    /**
     * Busca usuários com filtros.
     *
     * @param filtro  Filtros a aplicar
     * @param pagina  Número da página
     * @param tamanho Tamanho da página
     * @return Resultado paginado
     */
    public ResultadoPaginacaoDTO<Usuario> buscarComFiltro(FiltroGenericoDTO filtro, Integer pagina, Integer tamanho) {
        if (filtro == null) {
            throw new NotificacaoException("Filtro não pode ser nulo");
        }

        return usuarioRepository.buscarComFiltro(filtro, pagina, tamanho);
    }

    /**
     * Desativa um usuário.
     *
     * @param id O ID do usuário
     */
    public void desativar(Integer id) {
        if (id == null || id <= 0) {
            throw new NotificacaoException("ID inválido");
        }

        Optional<Usuario> usuario = usuarioRepository.obterPorId(id);
        if (usuario.isEmpty()) {
            throw new NotificacaoException("Usuário com ID " + id + " não encontrado");
        }

        usuarioRepository.desativar(id);
    }

    /**
     * Ativa um usuário.
     *
     * @param id O ID do usuário
     */
    public void ativar(Integer id) {
        if (id == null || id <= 0) {
            throw new NotificacaoException("ID inválido");
        }

        Optional<Usuario> usuario = usuarioRepository.obterPorId(id);
        if (usuario.isEmpty()) {
            throw new NotificacaoException("Usuário com ID " + id + " não encontrado");
        }

        usuarioRepository.ativar(id);
    }
}
