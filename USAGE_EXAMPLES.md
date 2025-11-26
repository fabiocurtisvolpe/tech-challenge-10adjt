# Exemplos de Uso - Clean Architecture

Este documento mostra exemplos práticos de como usar a estrutura Clean Architecture implementada.

---

## 1. Criar um Usuário

### A. Requisição HTTP

```bash
curl -X POST http://localhost:8080/api/usuario \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "senha": "senha123",
    "tipoUsuario": "CLIENTE"
  }'
```

### B. Fluxo Interno

#### 1) Controller Recebe Requisição
```java
// File: api/src/main/java/com/postech/adjt/api/controller/UsuarioController.java

@PostMapping
public ResponseEntity<UsuarioDTO> criar(@Valid @RequestBody UsuarioDTO dto) {
    // DTO tem validações JSR 380 (@NotBlank, etc)
    // Spring automaticamente valida
    
    // Converte DTO para Model (domínio)
    UsuarioDTO dto = {
        nome: "João Silva",
        email: "joao@example.com",
        senha: "senha123",
        tipoUsuario: "CLIENTE"
    }
    
    // ↓
    
    Usuario model = mapperDTO.dtoToModel(dto);
    // model agora é uma entidade de domínio pura
}
```

#### 2) Chama Use Case
```java
// File: domain/src/main/java/com/postech/adjt/domain/usecase/UsuarioUseCase.java

public Usuario criar(Usuario usuario) {
    // 1) Valida as regras de negócio
    UsuarioValidator.validarParaCriacao(usuario);
    
    // Valida:
    // - Nome não é nulo e tem mínimo 3 chars
    // - Email tem formato correto
    // - Senha tem mínimo 6 chars
    // - Tipo de usuário é válido
    
    // 2) Verifica se email já existe
    Optional<Usuario> usuarioExistente = usuarioRepository.obterPorEmail(usuario.getEmail());
    if (usuarioExistente.isPresent()) {
        throw new NotificacaoException("Usuário com email " + usuario.getEmail() + " já existe");
    }
    
    // 3) Delegue ao repositório (port)
    return usuarioRepository.criar(usuario);
    
    // IMPORTANTE: O Use Case NÃO conhece:
    // - Como dados são salvos (JPA? SQL? NoSQL?)
    // - Como email é verificado
    // - Detalhes de implementação
    // Apenas define REGRAS DE NEGÓCIO
}
```

#### 3) Gateway Implementa Persistência
```java
// File: data/src/main/java/com/postech/adjt/data/gateway/UsuarioGateway.java

@Override
public Usuario criar(Usuario usuario) {
    // Converte Model → Entity JPA
    UsuarioEntity entity = mapper.modelToEntity(usuario);
    
    // Usuario model:
    // {
    //   id: null,
    //   nome: "João Silva",
    //   email: "joao@example.com",
    //   senha: "senha123",
    //   tipoUsuario: TipoUsuarioEnum.CLIENTE
    // }
    
    // ↓ mapper.modelToEntity()
    
    // UsuarioEntity:
    // {
    //   id: null,
    //   nome: "João Silva",
    //   email: "joao@example.com",
    //   senha: "senha123",
    //   tipoUsuario: CLIENTE,
    //   ativo: true,
    //   dataCriacao: LocalDateTime.now(),
    //   dataAtualizacao: LocalDateTime.now()
    // }
    
    try {
        // Salva no banco de dados via JPA/Hibernate
        entity = repository.save(entity);
        
        // SQL executado:
        // INSERT INTO usuario (nome, email, senha, tipo_usuario, ativo, data_criacao, data_atualizacao)
        // VALUES ('João Silva', 'joao@example.com', 'senha123', 'CLIENTE', true, '2025-11-24 10:30:00', '2025-11-24 10:30:00')
        // RETURNING id, nome, email, ...
        
        // Retorna com ID preenchido:
        // UsuarioEntity {
        //   id: 1,
        //   nome: "João Silva",
        //   email: "joao@example.com",
        //   ...
        // }
        
    } catch (DataIntegrityViolationException e) {
        // Email já existe (constraint unique)
        throw new DuplicateEntityException(MensagemUtil.USUARIO_EMAIL_DUPLICADO);
    }
    
    // Converte Entity → Model (domínio)
    return mapper.entityToModel(entity);
    
    // Usuario model com ID:
    // {
    //   id: 1,
    //   nome: "João Silva",
    //   email: "joao@example.com",
    //   senha: "senha123",
    //   tipoUsuario: TipoUsuarioEnum.CLIENTE
    // }
}
```

#### 4) Presenter Converte para DTO Resposta
```java
// File: api/src/main/java/com/postech/adjt/api/presenter/UsuarioPresenter.java

public static UsuarioDTO toDTO(Usuario usuario) {
    // Usuario model:
    // {
    //   id: 1,
    //   nome: "João Silva",
    //   email: "joao@example.com",
    //   tipoUsuario: TipoUsuarioEnum.CLIENTE,
    //   ativo: true
    // }
    
    // ↓ conversion
    
    // UsuarioDTO (resposta HTTP):
    // {
    //   id: 1,
    //   nome: "João Silva",
    //   email: "joao@example.com",
    //   tipoUsuario: "CLIENTE",
    //   ativo: true,
    //   enderecos: []
    // }
    
    // NOTE: A senha NÃO é incluída na resposta
    // Apenas campos que devem ser expostos na API
}
```

#### 5) Controller Retorna Resposta HTTP
```java
return ResponseEntity.status(HttpStatus.CREATED)
    .body(UsuarioPresenter.toDTO(created));
```

### C. Resposta HTTP
```json
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "tipoUsuario": "CLIENTE",
  "ativo": true,
  "enderecos": []
}
```

---

## 2. Atualizar um Usuário

### Requisição
```bash
curl -X PUT http://localhost:8080/api/usuario/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva Atualizado",
    "email": "joao_novo@example.com",
    "tipoUsuario": "RESTAURANTE"
  }'
```

### Fluxo (similar ao criar, mas com validações adicionais)

```java
// domain/usecase/UsuarioUseCase.java

public Usuario atualizar(Usuario usuario) {
    // Valida dados
    UsuarioValidator.validarParaAtualizacao(usuario);
    
    // Verifica se usuário existe
    Optional<Usuario> usuarioExistente = usuarioRepository.obterPorId(usuario.getId());
    if (usuarioExistente.isEmpty()) {
        throw new NotificacaoException("Usuário com ID " + usuario.getId() + " não encontrado");
    }
    
    // Verifica se novo email não está sendo usado por outro usuário
    Optional<Usuario> outroUsuario = usuarioRepository.obterPorEmail(usuario.getEmail());
    if (outroUsuario.isPresent() && !outroUsuario.get().getId().equals(usuario.getId())) {
        throw new NotificacaoException("Email " + usuario.getEmail() + " já está em uso");
    }
    
    // Delegue ao gateway
    return usuarioRepository.atualizar(usuario);
}
```

---

## 3. Buscar Usuário por ID

### Requisição
```bash
curl -X GET http://localhost:8080/api/usuario/1 \
  -H "Authorization: Bearer <token_jwt>"
```

### Fluxo
```java
// api/controller/UsuarioController.java

@GetMapping("/{id}")
public ResponseEntity<UsuarioDTO> obterPorId(@PathVariable Integer id) {
    Optional<Usuario> usuario = useCase.obterPorId(id);
    return usuario
        .map(u -> ResponseEntity.ok(UsuarioPresenter.toDTO(u)))
        .orElse(ResponseEntity.notFound().build());
}

// domain/usecase/UsuarioUseCase.java

public Optional<Usuario> obterPorId(Integer id) {
    if (id == null || id <= 0) {
        throw new NotificacaoException("ID inválido");
    }
    return usuarioRepository.obterPorId(id);
}

// data/gateway/UsuarioGateway.java

@Override
public Optional<Usuario> obterPorId(Integer id) {
    return repository.findById(id)  // JPA Query
        .map(mapper::entityToModel); // Converte Entity → Model
}
```

### Resposta
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "tipoUsuario": "CLIENTE",
  "ativo": true,
  "enderecos": []
}
```

---

## 4. Listar Usuários Paginados

### Requisição
```bash
curl -X GET "http://localhost:8080/api/usuario?pagina=0&tamanho=10&ordenacao=nome"
```

### Fluxo
```java
// api/controller/UsuarioController.java

@GetMapping
public ResponseEntity<ResultadoPaginacaoDTO<UsuarioDTO>> listar(
        @RequestParam(defaultValue = "0") Integer pagina,
        @RequestParam(defaultValue = "10") Integer tamanho,
        @RequestParam(defaultValue = "id") String ordenacao) {
    
    ResultadoPaginacaoDTO<Usuario> resultado = useCase.listarPaginado(pagina, tamanho, ordenacao);
    
    // Converte cada Model → DTO
    ResultadoPaginacaoDTO<UsuarioDTO> response = new ResultadoPaginacaoDTO<>(
        resultado.getConteudo().stream()
            .map(UsuarioPresenter::toDTO)
            .toList(),
        resultado.getTotalElementos(),
        resultado.getTotalPaginas(),
        resultado.getPaginaAtual(),
        resultado.getTamanhoPagina()
    );
    
    return ResponseEntity.ok(response);
}

// domain/usecase/UsuarioUseCase.java

public ResultadoPaginacaoDTO<Usuario> listarPaginado(Integer pagina, Integer tamanho, String ordenacao) {
    if (pagina == null || pagina < 0) {
        throw new NotificacaoException("Número de página inválido");
    }
    if (tamanho == null || tamanho <= 0) {
        throw new NotificacaoException("Tamanho de página inválido");
    }
    
    return usuarioRepository.listarPaginado(pagina, tamanho, ordenacao);
}

// data/gateway/UsuarioGateway.java

@Override
public ResultadoPaginacaoDTO<Usuario> listarPaginado(Integer pagina, Integer tamanho, String ordenacao) {
    Sort sort = Sort.by(ordenacao != null ? ordenacao : "id");
    Pageable pageable = PageRequest.of(pagina, tamanho, sort);
    
    Page<UsuarioEntity> page = repository.findAll(pageable);
    
    // SQL gerado:
    // SELECT * FROM usuario
    // WHERE ativo = true
    // ORDER BY {ordenacao}
    // LIMIT {tamanho}
    // OFFSET {pagina * tamanho}
    
    return new ResultadoPaginacaoDTO<>(
        page.getContent()
            .stream()
            .map(mapper::entityToModel)
            .toList(),
        page.getTotalElements(),
        page.getTotalPages(),
        page.getNumber(),
        page.getSize()
    );
}
```

### Resposta
```json
HTTP/1.1 200 OK
Content-Type: application/json

{
  "conteudo": [
    {
      "id": 1,
      "nome": "João Silva",
      "email": "joao@example.com",
      "tipoUsuario": "CLIENTE",
      "ativo": true
    },
    {
      "id": 2,
      "nome": "Maria Santos",
      "email": "maria@example.com",
      "tipoUsuario": "RESTAURANTE",
      "ativo": true
    }
  ],
  "totalElementos": 2,
  "totalPaginas": 1,
  "paginaAtual": 0,
  "tamanhoPagina": 10
}
```

---

## 5. Desativar um Usuário

### Requisição
```bash
curl -X DELETE http://localhost:8080/api/usuario/1 \
  -H "Authorization: Bearer <token_jwt>"
```

### Fluxo
```java
// api/controller/UsuarioController.java

@DeleteMapping("/{id}")
public ResponseEntity<Void> desativar(@PathVariable Integer id) {
    useCase.desativar(id);
    return ResponseEntity.noContent().build();
}

// domain/usecase/UsuarioUseCase.java

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

// data/gateway/UsuarioGateway.java

@Override
public void desativar(Integer id) {
    UsuarioEntity entity = repository.findById(id)
        .orElseThrow(() -> new NotificacaoException("Usuário não encontrado"));
    
    entity.setAtivo(false);  // Soft delete
    repository.save(entity);
    
    // SQL gerado:
    // UPDATE usuario SET ativo = false, data_atualizacao = NOW()
    // WHERE id = {id}
}
```

### Resposta
```
HTTP/1.1 204 No Content
```

---

## Estrutura de Erro

Quando algo dá errado, a estrutura de tratamento de exceções funciona assim:

### Requisição com Email Duplicado
```bash
curl -X POST http://localhost:8080/api/usuario \
  -d '{"nome": "João", "email": "joao@example.com", "senha": "123456"}'
```

### Fluxo de Erro
```java
// 1) Validator detecta email duplicado
UsuarioValidator.validarParaCriacao(usuario); // ✓ Passa

// 2) UseCase verifica se email existe
Optional<Usuario> usuarioExistente = usuarioRepository.obterPorEmail(usuario.getEmail());
if (usuarioExistente.isPresent()) {
    throw new NotificacaoException("Usuário com email " + usuario.getEmail() + " já existe");
    // ✗ NotificacaoException (Business Rule Violation)
}

// 3) Exception é capturada por GlobalExceptionHandler
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(NotificacaoException.class)
    public ResponseEntity<ApiErrorResponse> handleNotificacaoException(NotificacaoException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
            ));
    }
}
```

### Resposta HTTP
```json
HTTP/1.1 400 Bad Request
Content-Type: application/json

{
  "status": 400,
  "message": "Usuário com email joao@example.com já existe",
  "timestamp": "2025-11-24T10:35:00"
}
```

---

## Validações em Ação

### Exemplo 1: Email Inválido
```java
// Requisição
{
  "nome": "João",
  "email": "email_invalido",  // ✗ Não tem @
  "senha": "senha123"
}

// Validator detecta
if (!EMAIL_PATTERN.matcher(email).matches()) {
    throw new NotificacaoException("Formato de email inválido: " + email);
}

// Resposta
{
  "status": 400,
  "message": "Formato de email inválido: email_invalido"
}
```

### Exemplo 2: Senha muito curta
```java
// Requisição
{
  "nome": "João",
  "email": "joao@example.com",
  "senha": "123"  // ✗ Menos de 6 caracteres
}

// Validator detecta
if (senha.length() < SENHA_MINIMA_LENGTH) {
    throw new NotificacaoException("Senha deve ter no mínimo 6 caracteres");
}

// Resposta
{
  "status": 400,
  "message": "Senha deve ter no mínimo 6 caracteres"
}
```

### Exemplo 3: Atualizar usuário que não existe
```java
// UseCase detecta
Optional<Usuario> usuarioExistente = usuarioRepository.obterPorId(usuario.getId());
if (usuarioExistente.isEmpty()) {
    throw new NotificacaoException("Usuário com ID 999 não encontrado");
}

// Resposta
{
  "status": 400,
  "message": "Usuário com ID 999 não encontrado"
}
```

---

## Testes Unitários

### Testar UseCase (sem dependências externas)

```java
public class UsuarioUseCaseTest {
    
    private UsuarioUseCase useCase;
    private UsuarioRepositoryPort mockRepository;
    
    @Before
    public void setup() {
        mockRepository = mock(UsuarioRepositoryPort.class);
        useCase = new UsuarioUseCase(mockRepository);
    }
    
    @Test
    public void testCriarUsuarioValido() {
        // Arrange
        Usuario usuario = new Usuario("João", "joao@example.com", "senha123", TipoUsuarioEnum.CLIENTE);
        Usuario usuarioCriado = new Usuario(1, "João", "joao@example.com", "senha123", TipoUsuarioEnum.CLIENTE);
        
        when(mockRepository.obterPorEmail("joao@example.com"))
            .thenReturn(Optional.empty());
        when(mockRepository.criar(usuario))
            .thenReturn(usuarioCriado);
        
        // Act
        Usuario resultado = useCase.criar(usuario);
        
        // Assert
        assertEquals(1, resultado.getId());
        assertEquals("João", resultado.getNome());
        verify(mockRepository).criar(usuario);
    }
    
    @Test
    public void testCriarUsuarioComEmailDuplicado() {
        // Arrange
        Usuario usuario = new Usuario("João", "joao@example.com", "senha123", TipoUsuarioEnum.CLIENTE);
        Usuario usuarioExistente = new Usuario(1, "João", "joao@example.com", "senha123", TipoUsuarioEnum.CLIENTE);
        
        when(mockRepository.obterPorEmail("joao@example.com"))
            .thenReturn(Optional.of(usuarioExistente));
        
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.criar(usuario);
        });
        verify(mockRepository, never()).criar(usuario);
    }
    
    @Test
    public void testCriarUsuarioComEmailInvalido() {
        // Arrange
        Usuario usuario = new Usuario("João", "email_invalido", "senha123", TipoUsuarioEnum.CLIENTE);
        
        // Act & Assert
        assertThrows(NotificacaoException.class, () -> {
            useCase.criar(usuario);
        });
        verify(mockRepository, never()).criar(usuario);
    }
}
```

---

## Resumo do Fluxo

```
┌─ HTTP Request
│  GET/POST/PUT/DELETE /api/usuario
│
├─ Controller valida JSR 380 (@Valid)
│
├─ Mapper DTO → Model (domínio)
│
├─ UseCase executa lógica de negócio
│  ├─ Validator verifica regras
│  ├─ Port (interface) é chamado
│  └─ Retorna Model
│
├─ Gateway implementa persistência
│  ├─ Mapper Model → Entity JPA
│  ├─ Repository.save/findBy/delete
│  └─ Mapper Entity → Model
│
├─ Presenter Model → DTO resposta
│
└─ HTTP Response
   200/201/204/4xx/5xx + JSON
```

---

**Estrutura pronta para produção! 🚀**
