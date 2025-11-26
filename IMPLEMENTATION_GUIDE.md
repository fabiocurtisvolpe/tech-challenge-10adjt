# Guia de Implementação - Clean Architecture

## Template de Implementação de Feature

Este guia fornece templates prontos para implementar novas features seguindo a Clean Architecture.

---

## 1. Model (Entidade de Domínio)

**Localização**: `domain/src/main/java/com/postech/adjt/domain/model/`

```java
package com.postech.adjt.domain.model;

public class NomeEntidade extends BaseModel {

    private String campo1;
    private String campo2;

    // Construtores
    public NomeEntidade(String campo1, String campo2) {
        this.campo1 = campo1;
        this.campo2 = campo2;
    }

    public NomeEntidade(Integer id, Boolean ativo, String campo1, String campo2) {
        this.setId(id);
        this.setAtivo(ativo);
        this.campo1 = campo1;
        this.campo2 = campo2;
    }

    // Getters e Setters
    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }
}
```

---

## 2. Port (Interface)

**Localização**: `domain/src/main/java/com/postech/adjt/domain/ports/`

```java
package com.postech.adjt.domain.ports;

import java.util.Optional;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.model.NomeEntidade;

/**
 * Port para NomeEntidade.
 * 
 * Define o contrato que deve ser implementado na camada de dados.
 */
public interface NomeEntidadeRepositoryPort {

    NomeEntidade criar(NomeEntidade entidade);

    Optional<NomeEntidade> obterPorId(Integer id);

    NomeEntidade atualizar(NomeEntidade entidade);

    ResultadoPaginacaoDTO<NomeEntidade> listarPaginado(Integer pagina, Integer tamanho, String ordenacao);

    void desativar(Integer id);

    void ativar(Integer id);
}
```

---

## 3. Validator (Validações de Domínio)

**Localização**: `domain/src/main/java/com/postech/adjt/domain/validators/`

```java
package com.postech.adjt.domain.validators;

import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.model.NomeEntidade;

/**
 * Validador para NomeEntidade.
 */
public class NomeEntidadeValidator {

    public static void validarParaCriacao(NomeEntidade entidade) {
        if (entidade == null) {
            throw new NotificacaoException("Entidade não pode ser nula");
        }

        if (entidade.getCampo1() == null || entidade.getCampo1().trim().isEmpty()) {
            throw new NotificacaoException("Campo1 é obrigatório");
        }

        if (entidade.getCampo2() == null || entidade.getCampo2().trim().isEmpty()) {
            throw new NotificacaoException("Campo2 é obrigatório");
        }
    }

    public static void validarParaAtualizacao(NomeEntidade entidade) {
        if (entidade == null) {
            throw new NotificacaoException("Entidade não pode ser nula");
        }

        if (entidade.getId() == null) {
            throw new NotificacaoException("ID é obrigatório para atualização");
        }

        validarParaCriacao(entidade);
    }
}
```

---

## 4. Use Case (Lógica de Aplicação)

**Localização**: `domain/src/main/java/com/postech/adjt/domain/usecase/`

```java
package com.postech.adjt.domain.usecase;

import java.util.Optional;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.model.NomeEntidade;
import com.postech.adjt.domain.ports.NomeEntidadeRepositoryPort;
import com.postech.adjt.domain.validators.NomeEntidadeValidator;

/**
 * Use Case para NomeEntidade.
 */
public class NomeEntidadeUseCase {

    private final NomeEntidadeRepositoryPort repository;

    public NomeEntidadeUseCase(NomeEntidadeRepositoryPort repository) {
        this.repository = repository;
    }

    public NomeEntidade criar(NomeEntidade entidade) {
        NomeEntidadeValidator.validarParaCriacao(entidade);
        return repository.criar(entidade);
    }

    public Optional<NomeEntidade> obterPorId(Integer id) {
        if (id == null || id <= 0) {
            throw new NotificacaoException("ID inválido");
        }
        return repository.obterPorId(id);
    }

    public NomeEntidade atualizar(NomeEntidade entidade) {
        NomeEntidadeValidator.validarParaAtualizacao(entidade);
        
        Optional<NomeEntidade> existente = repository.obterPorId(entidade.getId());
        if (existente.isEmpty()) {
            throw new NotificacaoException("Entidade não encontrada");
        }
        
        return repository.atualizar(entidade);
    }

    public ResultadoPaginacaoDTO<NomeEntidade> listarPaginado(Integer pagina, Integer tamanho, String ordenacao) {
        if (pagina == null || pagina < 0) {
            throw new NotificacaoException("Página inválida");
        }
        if (tamanho == null || tamanho <= 0) {
            throw new NotificacaoException("Tamanho inválido");
        }
        
        return repository.listarPaginado(pagina, tamanho, ordenacao);
    }

    public void desativar(Integer id) {
        if (id == null || id <= 0) {
            throw new NotificacaoException("ID inválido");
        }
        
        Optional<NomeEntidade> entidade = repository.obterPorId(id);
        if (entidade.isEmpty()) {
            throw new NotificacaoException("Entidade não encontrada");
        }
        
        repository.desativar(id);
    }

    public void ativar(Integer id) {
        if (id == null || id <= 0) {
            throw new NotificacaoException("ID inválido");
        }
        
        Optional<NomeEntidade> entidade = repository.obterPorId(id);
        if (entidade.isEmpty()) {
            throw new NotificacaoException("Entidade não encontrada");
        }
        
        repository.ativar(id);
    }
}
```

---

## 5. JPA Entity

**Localização**: `data/src/main/java/com/postech/adjt/data/entity/`

```java
package com.postech.adjt.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "nome_entidade")
public class NomeEntidadeEntity extends BaseEntity {

    @Column(name = "campo_1", nullable = false)
    private String campo1;

    @Column(name = "campo_2", nullable = false)
    private String campo2;

    // Construtores
    public NomeEntidadeEntity() {
    }

    public NomeEntidadeEntity(String campo1, String campo2) {
        this.campo1 = campo1;
        this.campo2 = campo2;
    }

    // Getters e Setters
    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }
}
```

---

## 6. Repository JPA

**Localização**: `data/src/main/java/com/postech/adjt/data/repository/`

```java
package com.postech.adjt.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.postech.adjt.data.entity.NomeEntidadeEntity;

@Repository
public interface NomeEntidadeRepository extends JpaRepository<NomeEntidadeEntity, Integer> {
    // Adicionar métodos customizados conforme necessário
}
```

---

## 7. Mapper

**Localização**: `data/src/main/java/com/postech/adjt/data/mapper/`

```java
package com.postech.adjt.data.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.data.entity.NomeEntidadeEntity;
import com.postech.adjt.domain.model.NomeEntidade;

@Component
public class NomeEntidadeMapper {

    public NomeEntidade entityToModel(NomeEntidadeEntity entity) {
        if (entity == null) {
            return null;
        }

        return new NomeEntidade(
            entity.getId(),
            entity.getAtivo(),
            entity.getCampo1(),
            entity.getCampo2()
        );
    }

    public NomeEntidadeEntity modelToEntity(NomeEntidade model) {
        if (model == null) {
            return null;
        }

        NomeEntidadeEntity entity = new NomeEntidadeEntity(
            model.getCampo1(),
            model.getCampo2()
        );
        
        if (model.getId() != null) {
            entity.setId(model.getId());
        }
        
        entity.setAtivo(model.getAtivo());
        
        return entity;
    }
}
```

---

## 8. Gateway (Implementação do Port)

**Localização**: `data/src/main/java/com/postech/adjt/data/gateway/`

```java
package com.postech.adjt.data.gateway;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.postech.adjt.data.entity.NomeEntidadeEntity;
import com.postech.adjt.data.mapper.NomeEntidadeMapper;
import com.postech.adjt.data.repository.NomeEntidadeRepository;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.exception.NotificacaoException;
import com.postech.adjt.domain.model.NomeEntidade;
import com.postech.adjt.domain.ports.NomeEntidadeRepositoryPort;

/**
 * Gateway para NomeEntidade.
 */
@Component
public class NomeEntidadeGateway implements NomeEntidadeRepositoryPort {

    private final NomeEntidadeRepository repository;
    private final NomeEntidadeMapper mapper;

    public NomeEntidadeGateway(NomeEntidadeRepository repository, NomeEntidadeMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public NomeEntidade criar(NomeEntidade entidade) {
        NomeEntidadeEntity entity = mapper.modelToEntity(entidade);
        entity = repository.save(entity);
        return mapper.entityToModel(entity);
    }

    @Override
    public Optional<NomeEntidade> obterPorId(Integer id) {
        return repository.findById(id).map(mapper::entityToModel);
    }

    @Override
    public NomeEntidade atualizar(NomeEntidade entidade) {
        NomeEntidadeEntity entity = mapper.modelToEntity(entidade);
        entity = repository.save(entity);
        return mapper.entityToModel(entity);
    }

    @Override
    public ResultadoPaginacaoDTO<NomeEntidade> listarPaginado(Integer pagina, Integer tamanho, String ordenacao) {
        Sort sort = Sort.by(ordenacao != null ? ordenacao : "id");
        Pageable pageable = PageRequest.of(pagina, tamanho, sort);
        
        Page<NomeEntidadeEntity> page = repository.findAll(pageable);
        
        return new ResultadoPaginacaoDTO<>(
            page.getContent().stream().map(mapper::entityToModel).toList(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.getNumber(),
            page.getSize()
        );
    }

    @Override
    public void desativar(Integer id) {
        NomeEntidadeEntity entity = repository.findById(id)
            .orElseThrow(() -> new NotificacaoException("Entidade não encontrada"));
        entity.setAtivo(false);
        repository.save(entity);
    }

    @Override
    public void ativar(Integer id) {
        NomeEntidadeEntity entity = repository.findById(id)
            .orElseThrow(() -> new NotificacaoException("Entidade não encontrada"));
        entity.setAtivo(true);
        repository.save(entity);
    }
}
```

---

## 9. DTO (API)

**Localização**: `api/src/main/java/com/postech/adjt/api/dto/`

```java
package com.postech.adjt.api.dto;

import jakarta.validation.constraints.NotBlank;

public class NomeEntidadeDTO extends BaseDTO {

    @NotBlank(message = "Campo1 é obrigatório")
    private String campo1;

    @NotBlank(message = "Campo2 é obrigatório")
    private String campo2;

    // Construtores
    public NomeEntidadeDTO() {
    }

    public NomeEntidadeDTO(String campo1, String campo2) {
        this.campo1 = campo1;
        this.campo2 = campo2;
    }

    // Getters e Setters
    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }
}
```

---

## 10. Presenter

**Localização**: `api/src/main/java/com/postech/adjt/api/presenter/`

```java
package com.postech.adjt.api.presenter;

import com.postech.adjt.api.dto.NomeEntidadeDTO;
import com.postech.adjt.domain.model.NomeEntidade;

/**
 * Presenter para NomeEntidade.
 */
public class NomeEntidadePresenter {

    public static NomeEntidadeDTO toDTO(NomeEntidade entidade) {
        if (entidade == null) {
            return null;
        }

        NomeEntidadeDTO dto = new NomeEntidadeDTO();
        dto.setId(entidade.getId());
        dto.setAtivo(entidade.getAtivo());
        dto.setCampo1(entidade.getCampo1());
        dto.setCampo2(entidade.getCampo2());

        return dto;
    }
}
```

---

## 11. Controller REST

**Localização**: `api/src/main/java/com/postech/adjt/api/controller/`

```java
package com.postech.adjt.api.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.postech.adjt.api.dto.NomeEntidadeDTO;
import com.postech.adjt.api.mapper.NomeEntidadeMapperDTO;
import com.postech.adjt.api.presenter.NomeEntidadePresenter;
import com.postech.adjt.domain.dto.ResultadoPaginacaoDTO;
import com.postech.adjt.domain.model.NomeEntidade;
import com.postech.adjt.domain.usecase.NomeEntidadeUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/nome-entidade")
@Tag(name = "NomeEntidade", description = "Operações com NomeEntidade")
public class NomeEntidadeController {

    private final NomeEntidadeUseCase useCase;
    private final NomeEntidadeMapperDTO mapperDTO;

    public NomeEntidadeController(NomeEntidadeUseCase useCase, NomeEntidadeMapperDTO mapperDTO) {
        this.useCase = useCase;
        this.mapperDTO = mapperDTO;
    }

    @PostMapping
    @Operation(summary = "Criar nova entidade")
    public ResponseEntity<NomeEntidadeDTO> criar(@Valid @RequestBody NomeEntidadeDTO dto) {
        NomeEntidade model = mapperDTO.dtoToModel(dto);
        NomeEntidade created = useCase.criar(model);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(NomeEntidadePresenter.toDTO(created));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter entidade por ID")
    public ResponseEntity<NomeEntidadeDTO> obterPorId(@PathVariable Integer id) {
        Optional<NomeEntidade> entidade = useCase.obterPorId(id);
        return entidade.map(e -> ResponseEntity.ok(NomeEntidadePresenter.toDTO(e)))
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar entidade")
    public ResponseEntity<NomeEntidadeDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody NomeEntidadeDTO dto) {
        NomeEntidade model = mapperDTO.dtoToModel(dto);
        model.setId(id);
        NomeEntidade updated = useCase.atualizar(model);
        return ResponseEntity.ok(NomeEntidadePresenter.toDTO(updated));
    }

    @GetMapping
    @Operation(summary = "Listar entidades paginadas")
    public ResponseEntity<ResultadoPaginacaoDTO<NomeEntidadeDTO>> listar(
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho,
            @RequestParam(defaultValue = "id") String ordenacao) {
        
        ResultadoPaginacaoDTO<NomeEntidade> resultado = useCase.listarPaginado(pagina, tamanho, ordenacao);
        
        ResultadoPaginacaoDTO<NomeEntidadeDTO> response = new ResultadoPaginacaoDTO<>(
            resultado.getConteudo().stream()
                .map(NomeEntidadePresenter::toDTO)
                .toList(),
            resultado.getTotalElementos(),
            resultado.getTotalPaginas(),
            resultado.getPaginaAtual(),
            resultado.getTamanhoPagina()
        );
        
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar entidade")
    public ResponseEntity<Void> desativar(@PathVariable Integer id) {
        useCase.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
```

---

## 12. Mapper DTO

**Localização**: `api/src/main/java/com/postech/adjt/api/mapper/`

```java
package com.postech.adjt.api.mapper;

import org.springframework.stereotype.Component;

import com.postech.adjt.api.dto.NomeEntidadeDTO;
import com.postech.adjt.domain.model.NomeEntidade;

@Component
public class NomeEntidadeMapperDTO {

    public NomeEntidade dtoToModel(NomeEntidadeDTO dto) {
        if (dto == null) {
            return null;
        }

        NomeEntidade model = new NomeEntidade(
            dto.getCampo1(),
            dto.getCampo2()
        );
        
        if (dto.getId() != null) {
            model.setId(dto.getId());
        }
        
        return model;
    }

    public NomeEntidadeDTO modelToDTO(NomeEntidade model) {
        if (model == null) {
            return null;
        }

        NomeEntidadeDTO dto = new NomeEntidadeDTO(
            model.getCampo1(),
            model.getCampo2()
        );
        
        dto.setId(model.getId());
        dto.setAtivo(model.getAtivo());
        
        return dto;
    }
}
```

---

## 13. Configuração no Spring (Bean)

Adicione a seguinte anotação ao seu `AdjtApplication.java` ou crie uma classe de configuração:

```java
@Configuration
public class DomainConfig {

    @Bean
    public NomeEntidadeUseCase nomeEntidadeUseCase(NomeEntidadeRepositoryPort repository) {
        return new NomeEntidadeUseCase(repository);
    }
}
```

Ou no Controller, injete direto:

```java
@RestController
public class NomeEntidadeController {
    
    private final NomeEntidadeUseCase useCase;

    public NomeEntidadeController(NomeEntidadeGateway gateway) {
        this.useCase = new NomeEntidadeUseCase(gateway);
    }
}
```

---

## Ordem de Implementação Recomendada

1. ✅ Model (domínio)
2. ✅ Port (domínio)
3. ✅ Validator (domínio)
4. ✅ UseCase (domínio)
5. ✅ JPA Entity (dados)
6. ✅ Repository (dados)
7. ✅ Mapper (dados)
8. ✅ Gateway (dados)
9. ✅ DTO API (API)
10. ✅ Presenter (API)
11. ✅ Mapper DTO (API)
12. ✅ Controller (API)
13. ✅ Configuração/Beans

---

## Checklist de Implementação

- [ ] Model criado com lógica de domínio
- [ ] Port define contrato
- [ ] Validator encapsula validações
- [ ] UseCase orquestra operações
- [ ] JPA Entity mapeia tabela
- [ ] Repository fornece acesso JPA
- [ ] Mapper converte Entity ↔ Model
- [ ] Gateway implementa Port
- [ ] DTO API define contrato HTTP
- [ ] Presenter converte Model → DTO
- [ ] Mapper DTO converte DTO → Model
- [ ] Controller expõe endpoints
- [ ] Beans registrados no Spring
- [ ] Testes unitários escritos

---

**Última atualização**: 24/11/2025
