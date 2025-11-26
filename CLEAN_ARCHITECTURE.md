# Arquitetura Limpa - Tech Challenge ADJT

## Visão Geral

Este projeto foi estruturado seguindo os princípios da **Clean Architecture**, conforme diagrama apresentado. A arquitetura é organizada em camadas concêntricas, onde cada camada tem responsabilidades bem definidas e as dependências apontam sempre para o centro.

## Estrutura de Camadas

### 1. **Entities (Centro - Núcleo do Domínio)**
**Localização**: `domain/src/main/java/com/postech/adjt/domain/model/`

Contém as entidades principais do negócio:
- `Usuario.java` - Entidade do usuário com lógica de domínio
- `Endereco.java` - Entidade de endereço

**Características**:
- Independentes de qualquer framework
- Encapsulam as regras de negócio mais críticas
- Não têm dependências externas

---

### 2. **Use Cases (Application Business Rules)**
**Localização**: `domain/src/main/java/com/postech/adjt/domain/usecase/`

Orquestram as operações do domínio e implementam a lógica de aplicação:
- `UsuarioUseCase.java` - Casos de uso relacionados a usuários
  - Criar usuário
  - Atualizar usuário
  - Listar/buscar usuários
  - Ativar/desativar usuários

**Características**:
- Contêm regras de negócio de aplicação
- Orquestram operações entre entidades
- Delegam persistência aos ports (repositórios)
- Validam regras antes de executar operações

---

### 3. **Ports (Interface Adapters - Abstração)**
**Localização**: `domain/src/main/java/com/postech/adjt/domain/ports/`

Define contratos que separam o domínio da implementação técnica:
- `UsuarioRepositoryPort.java` - Interface de persistência para usuários

**Características**:
- Define o que o domínio precisa (input/output ports)
- Inverte a dependência (domain → ports ← data)
- Permite trocar implementação sem afetar o domínio

---

### 4. **Validators (Validadores de Domínio)**
**Localização**: `domain/src/main/java/com/postech/adjt/domain/validators/`

Encapsulam as regras de validação do domínio:
- `UsuarioValidator.java` - Validações de regras de negócio
  - Validar formato de email
  - Validar tamanho mínimo de senha
  - Validar campos obrigatórios

**Características**:
- Centralizam lógica de validação
- Seguem princípio de responsabilidade única
- Podem ser reutilizados em diferentes contextos

---

### 5. **Gateways (Data Layer - Implementação de Ports)**
**Localização**: `data/src/main/java/com/postech/adjt/data/gateway/`

Implementam os ports definidos no domínio:
- `UsuarioGateway.java` - Implementação do UsuarioRepositoryPort

**Responsabilidades**:
- Adaptar operações do domínio para JPA/Hibernate
- Mapear entidades JPA para modelos de domínio
- Gerenciar transações com banco de dados

**Características**:
- Implementam a interface (port) do domínio
- Usam JPA/Hibernate para persistência
- Convertem entre UsuarioEntity ↔ Usuario (model)

---

### 6. **Entities (Camada de Dados)**
**Localização**: `data/src/main/java/com/postech/adjt/data/entity/`

Representam as tabelas do banco de dados:
- `UsuarioEntity.java` - Mapeamento JPA para tabela usuario
- `EnderecoEntity.java` - Mapeamento JPA para tabela endereco

**Características**:
- Anotações JPA (@Entity, @Column, etc)
- Específicas para persistência
- Diferentes dos modelos de domínio

---

### 7. **Repositories (Data Access)**
**Localização**: `data/src/main/java/com/postech/adjt/data/repository/`

Interfaces Spring Data JPA:
- `UsuarioRepository.java` - Acesso básico ao BD

**Características**:
- Herdam de JpaRepository
- Definem métodos de consulta
- Usados pelos Gateways

---

### 8. **Mappers (Conversão de Dados)**
**Localização**: `data/src/main/java/com/postech/adjt/data/mapper/`

Convertem entre diferentes representações:
- `UsuarioMapper.java` - Converte Entity ↔ Model

**Padrão**: 
```
UsuarioDTO → Mapper → Usuario (Model) → UsuarioGateway → UsuarioEntity
```

---

### 9. **Controllers (Web Layer - Interface Adapters)**
**Localização**: `api/src/main/java/com/postech/adjt/api/controller/`

Endpoints REST que recebem requisições:
- `UsuarioController.java` - Operações REST para usuários
- `LoginController.java` - Autenticação

**Fluxo**:
1. Recebe DTO da requisição
2. Converte para modelo (Model)
3. Chama Use Case
4. Recebe resultado
5. Converte para DTO resposta

---

### 10. **Presenters (Output Adapters)**
**Localização**: `api/src/main/java/com/postech/adjt/api/presenter/`

Convertém modelos de domínio para DTOs de resposta:
- `UsuarioPresenter.java` - Converte Usuario → UsuarioDTO

**Objetivo**:
- Isolar o domínio da representação HTTP
- Facilitar diferentes formatos de resposta

---

### 11. **DTOs (Data Transfer Objects)**

#### Domínio (`domain/src/main/java/com/postech/adjt/domain/dto/`)
- `UsuarioLoginDTO.java` - DTO de login
- `UsuarioTrocarSenhaDTO.java` - DTO de troca de senha
- `ResultadoPaginacaoDTO.java` - Resultado paginado genérico
- `FiltroGenericoDTO.java` - Filtros de busca

#### API (`api/src/main/java/com/postech/adjt/api/dto/`)
- `UsuarioDTO.java` - DTO de resposta HTTP
- `EnderecoDTO.java` - DTO de endereço
- `BaseDTO.java` - Base para todos os DTOs

**Separação**: DTOs do domínio são para comunicação interna, DTOs da API para HTTP.

---

### 12. **Config & Exception Handling**

**Configurações** (`api/src/main/java/com/postech/adjt/api/config/`):
- `SecurityConfig.java` - Spring Security
- `WebConfig.java` - Configurações web
- `OpenApiConfig.java` - Swagger/OpenAPI

**Tratamento de Exceções** (`api/src/main/java/com/postech/adjt/api/exception/`):
- `GlobalExceptionHandler.java` - Manipulador global de erros
- `CustomAuthenticationEntryPoint.java` - Autenticação falha
- `CustomAccessDeniedHandler.java` - Acesso negado

---

## Fluxo de Requisição

```
HTTP Request (UsuarioDTO)
        ↓
    Controller
        ↓
    Mapper (DTO → Model)
        ↓
    UseCase (UsuarioUseCase)
        ↓
    Validator (validarParaCriacao/Atualização)
        ↓
    RepositoryPort (interface)
        ↓
    Gateway (implementação)
        ↓
    Mapper (Entity ↔ Model)
        ↓
    Repository (JPA)
        ↓
    Database (PostgreSQL)
        ↓
    [Resposta segue caminho inverso]
        ↓
    Presenter
        ↓
    HTTP Response (UsuarioDTO)
```

---

## Implementação de Nova Feature

### Passo 1: Criar Port (domínio)
```java
// domain/src/main/java/com/postech/adjt/domain/ports/NovaFeaturePort.java
public interface NovaFeaturePort {
    // definir contrato
}
```

### Passo 2: Criar Use Case (domínio)
```java
// domain/src/main/java/com/postech/adjt/domain/usecase/NovaFeatureUseCase.java
public class NovaFeatureUseCase {
    // implementar lógica de negócio
}
```

### Passo 3: Criar Validator (domínio)
```java
// domain/src/main/java/com/postech/adjt/domain/validators/NovaFeatureValidator.java
public class NovaFeatureValidator {
    // validações de regra de negócio
}
```

### Passo 4: Criar Gateway (dados)
```java
// data/src/main/java/com/postech/adjt/data/gateway/NovaFeatureGateway.java
public class NovaFeatureGateway implements NovaFeaturePort {
    // implementar persistência
}
```

### Passo 5: Criar Controller (API)
```java
// api/src/main/java/com/postech/adjt/api/controller/NovaFeatureController.java
public class NovaFeatureController {
    // expor endpoints
}
```

### Passo 6: Criar DTOs
- `domain/src/main/java/com/postech/adjt/domain/dto/NovaFeatureDTO.java`
- `api/src/main/java/com/postech/adjt/api/dto/NovaFeatureDTO.java`

---

## Benefícios da Arquitetura Limpa

✅ **Independência de Framework**: Lógica de negócio não depende de Spring, JPA, etc.
✅ **Testabilidade**: Cada camada pode ser testada isoladamente
✅ **Manutenibilidade**: Código organizado e com responsabilidades claras
✅ **Escalabilidade**: Fácil adicionar novas features
✅ **Flexibilidade**: Trocar implementação sem afetar lógica de negócio
✅ **Clareza**: Fluxo de dados evidente e previsível

---

## Dependências Entre Camadas

```
API (Controllers, DTOs) 
    ↓
Domain (Use Cases, Models, Ports, Validators)
    ↓
Data (Gateways, Entities, Mappers)
    
Regra: Dependência sempre aponta para o centro
```

---

## Próximos Passos

1. Refatorar `UsuarioService` existente para usar o novo `UsuarioGateway`
2. Integrar `UsuarioUseCase` no `UsuarioController`
3. Criar testes unitários para cada camada
4. Aplicar padrão para outras entidades (Restaurante, Pedido, etc)

---

**Última atualização**: 24/11/2025
