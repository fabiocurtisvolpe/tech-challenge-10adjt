# Estrutura de Diretórios - Clean Architecture

```
tech-challenge-10adjt/
│
├── CLEAN_ARCHITECTURE.md          ← Documentação da arquitetura
├── IMPLEMENTATION_GUIDE.md        ← Guia com templates de implementação
├── ADJT.postman_collection.json   ← Coleção Postman
├── pom.xml                        ← POM parent
├── README.md
│
└── adjt/
    │
    ├── pom.xml                   ← Módulos do projeto
    ├── mvnw / mvnw.cmd          ← Maven wrapper
    │
    ├─────────────────────────────────────────────────────────────────
    │  DOMAIN MODULE (Enterprise & Application Business Rules)
    ├─────────────────────────────────────────────────────────────────
    │
    ├── domain/
    │   ├── pom.xml
    │   │
    │   └── src/main/java/com/postech/adjt/domain/
    │       │
    │       ├── model/                     ← Entities (núcleo do domínio)
    │       │   ├── BaseModel.java
    │       │   ├── Usuario.java           ✓ Entidade de usuário
    │       │   └── Endereco.java          ✓ Entidade de endereço
    │       │
    │       ├── usecase/                   ← Use Cases (Application Rules)
    │       │   └── UsuarioUseCase.java    ✓ NOVO - Orquestra operações de usuário
    │       │
    │       ├── ports/                     ← Port Interfaces (Input/Output)
    │       │   └── UsuarioRepositoryPort.java  ✓ NOVO - Contrato de repositório
    │       │
    │       ├── validators/                ← Validadores de Domínio
    │       │   └── UsuarioValidator.java  ✓ NOVO - Validações de regra de negócio
    │       │
    │       ├── exception/                 ← Exceções do Domínio
    │       │   └── NotificacaoException.java
    │       │
    │       ├── enums/                     ← Enumerações
    │       │   ├── TipoUsuarioEnum.java
    │       │   └── FiltroOperadorEnum.java
    │       │
    │       ├── dto/                       ← DTOs de Domínio (interno)
    │       │   ├── UsuarioLoginDTO.java
    │       │   ├── UsuarioTrocarSenhaDTO.java
    │       │   ├── ResultadoPaginacaoDTO.java
    │       │   └── filtro/
    │       │       ├── FiltroGenericoDTO.java
    │       │       └── FiltroCampoDTO.java
    │       │
    │       ├── constants/                 ← Constantes
    │       │   └── MensagemUtil.java
    │       │
    │       └── service/                   ← Base Service
    │           └── BaseService.java
    │
    ├─────────────────────────────────────────────────────────────────
    │  DATA MODULE (Frameworks & Drivers + Gateway Implementation)
    ├─────────────────────────────────────────────────────────────────
    │
    ├── data/
    │   ├── pom.xml
    │   │
    │   └── src/main/java/com/postech/adjt/data/
    │       │
    │       ├── gateway/                   ← Gateways (Port Implementations)
    │       │   └── UsuarioGateway.java    ✓ NOVO - Implementa UsuarioRepositoryPort
    │       │
    │       ├── entity/                    ← JPA Entities (BD Mapping)
    │       │   ├── BaseEntity.java
    │       │   ├── UsuarioEntity.java     ✓ Mapeamento JPA de usuario
    │       │   ├── EnderecoEntity.java    ✓ Mapeamento JPA de endereco
    │       │   └── CustomRevEntity.java
    │       │
    │       ├── repository/                ← JPA Repositories
    │       │   └── UsuarioRepository.java ✓ Spring Data JPA
    │       │
    │       ├── mapper/                    ← Entity ↔ Model Mappers
    │       │   └── UsuarioMapper.java     ✓ Converte Entity ↔ Model
    │       │
    │       ├── specification/             ← JPA Specifications
    │       │   └── SpecificationGenerico.java
    │       │
    │       ├── converter/                 ← JPA Converters
    │       │   └── TipoUsuarioEnumConverter.java
    │       │
    │       ├── exception/                 ← Exceções de Dados
    │       │   └── DuplicateEntityException.java
    │       │
    │       └── resources/                 ← Recursos de Dados
    │           └── db/migration/          ← Flyway Migrations
    │               ├── V1__tabelas_usuario_tipo_usuario.sql
    │               ├── V2__dados_padrao.sql
    │               ├── V3__uq_usuario_tipo_usuario.sql
    │               ├── V4__custom_rev_model.sql
    │               ├── V5__pode_ser_excluido_eh_dono_restaurante.sql
    │               ├── V6__drop_tipo_usuario.sql
    │               └── V7__uq_usuario_email.sql
    │
    ├─────────────────────────────────────────────────────────────────
    │  API MODULE (Interface Adapters + Web Layer)
    ├─────────────────────────────────────────────────────────────────
    │
    ├── api/
    │   ├── pom.xml
    │   │
    │   └── src/main/java/com/postech/adjt/api/
    │       │
    │       ├── controller/                ← REST Controllers
    │       │   ├── UsuarioController.java ✓ Endpoint /api/usuario
    │       │   └── LoginController.java   ✓ Endpoint /api/login
    │       │
    │       ├── presenter/                 ← Output Adapters
    │       │   └── UsuarioPresenter.java  ✓ NOVO - Model → DTO
    │       │
    │       ├── dto/                       ← DTOs de API (HTTP)
    │       │   ├── BaseDTO.java
    │       │   ├── UsuarioDTO.java        ✓ DTO de Resposta HTTP
    │       │   └── EnderecoDTO.java       ✓ DTO de Endereço
    │       │
    │       ├── mapper/                    ← DTO Mappers
    │       │   └── UsuarioMapperDTO.java  ✓ Converte DTO ↔ Model
    │       │
    │       ├── config/                    ← Configurações
    │       │   ├── OpenApiConfig.java     ✓ Swagger/OpenAPI
    │       │   ├── WebConfig.java         ✓ Config web
    │       │   └── SecurityConfig.java    ✓ Spring Security
    │       │
    │       ├── exception/                 ← Tratamento de Exceções HTTP
    │       │   ├── GlobalExceptionHandler.java
    │       │   ├── CustomAuthenticationEntryPoint.java
    │       │   └── CustomAccessDeniedHandler.java
    │       │
    │       ├── jwt/                       ← Segurança JWT
    │       │   ├── config/
    │       │   │   └── UserAuthenticated.java
    │       │   ├── service/
    │       │   │   ├── JwtService.java
    │       │   │   └── AppUserDetailsService.java
    │       │   ├── filter/
    │       │   │   └── JwtAuthenticationFilter.java
    │       │   ├── util/
    │       │   │   └── UsuarioLogadoUtil.java
    │       │   └── model/
    │       │       ├── LoginRequest.java
    │       │       └── LoginResponse.java
    │       │
    │       ├── AdjtApplication.java       ✓ Main Application
    │       │
    │       └── resources/
    │           └── application.properties
    │
    └── local/
        └── docker-compose.yml             ← Ambiente local (PostgreSQL)

```

---

## Fluxo de Dados (Clean Architecture)

### Requisição HTTP → Resposta JSON

```
┌─────────────────────────────────────────────────────────────────┐
│ CLIENTE HTTP                                                    │
│ POST /api/usuario                                               │
│ { "nome": "João", "email": "joao@example.com" }                │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ API LAYER (Interface Adapter)                                   │
│ UsuarioController::criar(UsuarioDTO)                            │
│  └─ Recebe DTO com validações JSR 380                           │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ MAPPER DTO (Input Adapter)                                      │
│ UsuarioMapperDTO::dtoToModel(UsuarioDTO)                        │
│  └─ Converte DTO → Usuario (Model)                              │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ DOMAIN LAYER (Application Rules)                                │
│ UsuarioUseCase::criar(Usuario)                                  │
│  ├─ UsuarioValidator::validarParaCriacao(usuario)               │
│  ├─ Check: email já existe?                                     │
│  └─ Delega ao repositório (port)                                │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ PORT INTERFACE (Domain)                                         │
│ UsuarioRepositoryPort::criar(usuario)                           │
│  └─ Contrato abstrato                                           │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ DATA LAYER (Frameworks & Drivers)                               │
│ UsuarioGateway::criar(usuario)                                  │
│  ├─ Mapper::modelToEntity(usuario)                              │
│  ├─ Repository::save(entity)                                    │
│  └─ Mapper::entityToModel(entity) → Usuario (model)             │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ PERSISTENCE (Framework)                                         │
│ JPA/Hibernate → SQL → PostgreSQL                                │
│ INSERT INTO usuario (nome, email, senha, ...) VALUES (...)      │
└────────────────┬────────────────────────────────────────────────┘
                 │
              [BD Response]
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ Retorno: Usuario (model) com ID preenchido                      │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ PRESENTER (Output Adapter)                                      │
│ UsuarioPresenter::toDTO(usuario)                                │
│  └─ Converte Usuario (Model) → UsuarioDTO                       │
└────────────────┬────────────────────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────────────────────┐
│ CONTROLLER (Response)                                           │
│ HTTP 201 CREATED                                                │
│ { "id": 1, "nome": "João", "email": "joao@example.com" }       │
└─────────────────────────────────────────────────────────────────┘
```

---

## Camadas e Dependências

```
┌────────────────────────────────────────────────────┐
│  FRAMEWORKS & DRIVERS                              │
│  ┌──────────────────────────────────────────────┐  │
│  │ - Spring Boot / Spring Data JPA              │  │
│  │ - Hibernate / PostgreSQL                     │  │
│  │ - JWT / Spring Security                      │  │
│  │ - Flyway (Migrations)                        │  │
│  └──────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────┘
                         ▲
                         │ depende
                         │
┌────────────────────────────────────────────────────┐
│  INTERFACE ADAPTERS                                │
│  ┌──────────────────────────────────────────────┐  │
│  │ Controllers, Presenters, DTOs, Mappers       │  │
│  │ (Conversão e Adaptação)                      │  │
│  └──────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────┘
                         ▲
                         │ depende
                         │
┌────────────────────────────────────────────────────┐
│  APPLICATION BUSINESS RULES                        │
│  ┌──────────────────────────────────────────────┐  │
│  │ Use Cases (Orquestração de negócio)          │  │
│  │ Gateways (Implementação de Ports)            │  │
│  │ Mappers (Entity ↔ Model)                     │  │
│  └──────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────┘
                         ▲
                         │ depende
                         │
┌────────────────────────────────────────────────────┐
│  ENTERPRISE BUSINESS RULES                         │
│  ┌──────────────────────────────────────────────┐  │
│  │ Entities (Models)                            │  │
│  │ Ports (Interfaces)                           │  │
│  │ Validators (Validações)                      │  │
│  │ Exceptions (Domínio)                         │  │
│  └──────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────┘

  REGRA FUNDAMENTAL:
  As setas apontam para o centro (inversão de dependência)
  O domínio é independente de frameworks e tecnologia
```

---

## O que mudou? (Novo vs Antigo)

### ANTES (sem estrutura clara)
```
- UsuarioService (em data) continha tudo:
  ├─ Lógica de negócio
  ├─ Validações
  ├─ Acesso ao repositório
  └─ Conversão Entity ↔ DTO
  
- Acoplamento com JPA/Spring na lógica de negócio
```

### DEPOIS (Clean Architecture)
```
- Separação de responsabilidades clara:
  ├─ Domain: UsuarioUseCase + UsuarioValidator (apenas regras)
  ├─ Data: UsuarioGateway (implementação de persistência)
  ├─ Data: UsuarioMapper (conversão Entity ↔ Model)
  ├─ API: UsuarioPresenter (conversão Model → DTO)
  └─ API: UsuarioController (endpoints HTTP)

- Domain totalmente independente de JPA/Spring
- Fácil de testar, manter e evoluir
```

---

## Próximas Tarefas

1. **Refatorar UsuarioService** para usar o novo padrão
   - Mover lógica para UsuarioUseCase
   - Manter UsuarioService como gateway se necessário (ou deprecar)

2. **Criar mais Entities** seguindo o padrão:
   - Restaurante (Restaurante + RestauranteValidator + RestauranteUseCase)
   - Pedido (Pedido + PedidoValidator + PedidoUseCase)
   - Menu (Menu + MenuValidator + MenuUseCase)

3. **Criar testes unitários**:
   - Testar UseCases sem dependências externas
   - Testar Validators isoladamente
   - Testar Gateways com mocks

4. **Documentação de API**:
   - Swagger/OpenAPI completo
   - Exemplos de requisição/resposta

---

**Estrutura pronta para evolução! 🚀**
