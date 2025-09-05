package com.postech.adjt.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.postech.adjt.dto.FiltroGenericoDTO;
import com.postech.adjt.dto.UsuarioDTO;
import com.postech.adjt.exception.NotificacaoException;
import com.postech.adjt.mapper.TipoUsuarioMapper;
import com.postech.adjt.mapper.UsuarioMapper;
import com.postech.adjt.model.Endereco;
import com.postech.adjt.model.TipoUsuario;
import com.postech.adjt.model.Usuario;
import com.postech.adjt.repository.UsuarioRepository;
import com.postech.adjt.specification.SpecificationGenerico;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UsuarioMapper mapper;
    private final TipoUsuarioMapper tipoUsuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, UsuarioMapper mapper, TipoUsuarioMapper tipoUsuarioMapper,
            PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.mapper = mapper;
        this.tipoUsuarioMapper = tipoUsuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(rollbackFor = Exception.class)
    public UsuarioDTO criar(UsuarioDTO dto) {

        this.validarCriarAtualizar(dto);

        String senhaCodificada = passwordEncoder.encode(dto.getSenha());
        dto.setSenha(senhaCodificada);

        Usuario usuario = this.repository.save(this.mapper.toUsuario(dto));

        if (!dto.getEnderecos().isEmpty()) {

            dto.getEnderecos().forEach(endereco -> {
                usuario.adicionarEndereco(this.mapper.toEndereco(endereco));
            });

            Usuario usuarioEndereco = this.repository.save(usuario);
            return this.mapper.toUsuarioDTO(usuarioEndereco);
        }

        return this.mapper.toUsuarioDTO(usuario);
    }

    @Transactional(rollbackFor = Exception.class)
    public UsuarioDTO atualizar(Integer id, UsuarioDTO dto) {

        this.validarCriarAtualizar(dto);

        Optional<Usuario> entidade = this.repository.findById(id);
        if (entidade.isPresent()) {
            entidade.get().setNome(dto.getNome());
            entidade.get().setEmail(dto.getEmail());
            entidade.get().setLogin(dto.getLogin());

            String senhaCodificada = passwordEncoder.encode(dto.getSenha());
            entidade.get().setSenha(senhaCodificada);

            TipoUsuario tipoUsuario = this.tipoUsuarioMapper.toTipoUsuario(dto.getTipoUsuario());
            entidade.get().setTipoUsuario(tipoUsuario);

            if (!dto.getEnderecos().isEmpty()) {

                dto.getEnderecos().forEach(endereco -> {
                    entidade.get().adicionarEndereco(this.mapper.toEndereco(endereco));
                });

                Usuario usuarioEndereco = this.repository.save(entidade.get());
                return this.mapper.toUsuarioDTO(usuarioEndereco);
            }

            return this.mapper.toUsuarioDTO(entidade.get());
        }

        throw new NotificacaoException("Não foi possível executar a operação.");
    }

    @Transactional(rollbackFor = Exception.class)
    public UsuarioDTO buscar(Integer id) {

        Optional<Usuario> entidade = this.repository.findById(id);
        if (entidade.isPresent()) {
            return this.mapper.toUsuarioDTO(entidade.get());
        }

        throw new NotificacaoException("Tipo de Usuário não encontrado.");
    }

    @Transactional(rollbackFor = Exception.class)
    public List<UsuarioDTO> listar() {

        List<UsuarioDTO> list = new ArrayList<>();

        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        List<Usuario> usuarios = this.repository.findAll(sort);

        if (!usuarios.isEmpty()) {
            return usuarios.stream().map(this.mapper::toUsuarioDTO).collect(Collectors.toList());
        }

        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public Page<UsuarioDTO> listarPaginado(FiltroGenericoDTO filtro) {

        Sort sort = Sort.by(Sort.Direction.ASC, "nome");
        Pageable pageable = PageRequest.of(filtro.getPagina(), filtro.getTamanho(), sort);
        Specification<Usuario> spec = SpecificationGenerico.comFiltro(filtro);

        Page<Usuario> paginaUsuario = this.repository.findAll(spec, pageable);

        return paginaUsuario.map(this.mapper::toUsuarioDTO);
    }

    @Transactional(rollbackFor = Exception.class)
    public UsuarioDTO ativarInativar(Integer id) {

        Optional<Usuario> entidade = this.repository.findById(id);
        if (entidade.isPresent()) {
            boolean ativo = entidade.get().getAtivo();
            entidade.get().setAtivo(!ativo);
            Usuario usuario = this.repository.save(entidade.get());
            return this.mapper.toUsuarioDTO(usuario);
        }

        throw new NotificacaoException("Não foi possível executar a operação.");
    }

    private void validarCriarAtualizar(UsuarioDTO dto) {
        Optional<Usuario> usuario = this.repository.findByLogin(dto.getLogin());
        if ((usuario.isPresent()) && ((dto.getId() == null) || (dto.getId() != usuario.get().getId()))) {
            throw new NotificacaoException("Usuário já cadastrado.");
        }
    }
}
