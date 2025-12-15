# Wallet Service - Microserviço de Gerenciamento de Carteiras

Um microserviço robusto e seguro para gerenciamento de carteiras/contas bancárias, desenvolvido com Spring Boot 3.x e arquitetura moderna. Integra-se com o User Service através de JWT para validação de usuários e operações financeiras com segurança.

## 📋 Descrição do Projeto

O **Wallet Service** é responsável por:

- Criar e gerenciar contas bancárias dos usuários
- Validar existência de usuários através do User Service (com autenticação JWT)
- Gerenciar saldo e operações financeiras (depósitos e saques)
- Gerar números únicos de conta no padrão ACC-XXXXXXXXXX
- Persistir dados em banco de dados relacional
- Suportar múltiplos tipos de conta (Corrente e Poupança)
- Implementar segurança robusta com tokens JWT
- Fornecer documentação automática com Swagger/OpenAPI

Este microserviço foi desenvolvido seguindo boas práticas de desenvolvimento, incluindo validação robusta, transações gerenciadas, tratamento global de exceções e comunicação segura com outros microsserviços.

## 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Propósito |
|---|---|---|
| **Java** | 17+ | Linguagem de programação |
| **Spring Boot** | 3.x | Framework principal |
| **Spring Data JPA** | - | Persistência de dados |
| **Spring Security** | - | Autenticação e autorização |
| **WebClient** | - | Comunicação HTTP com User Service |
| **JWT (Auth0)** | 4.4.0 | Geração e validação de tokens |
| **Lombok** | - | Redução de boilerplate |
| **MapStruct** | 1.5+ | Mapeamento entre DTOs e entidades |
| **Jakarta Validation** | - | Validação de dados |
| **SpringDoc OpenAPI** | - | Documentação automática Swagger |
| **PostgreSQL/MySQL** | - | Banco de dados relacional |

## 📁 Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/fintech/wallet_service/
│   │   ├── config/
│   │   │   ├── interfaces/
│   │   │   │   └── ContaControllerOpenApi.java      # Documentação OpenAPI
│   │   │   ├── mapper/
│   │   │   │   └── ContaMapper.java                 # Mapeamento DTO <-> Entity
│   │   │   ├── security/
│   │   │   │   ├── JwtTokenProvider.java            # Provider de tokens JWT
│   │   │   │   ├── JwtAuthenticationFilter.java     # Filtro JWT
│   │   │   │   └── SecurityConfig.java              # Configuração de segurança
│   │   │   ├── service/
│   │   │   │   └── UsuarioServiceCliente.java       # Cliente WebClient para User Service
│   │   │   └── swagger/
│   │   │       └── SwaggerConfig.java               # Configuração OpenAPI/Swagger
│   │   ├── controller/
│   │   │   └── ContaController.java                 # Endpoints da API
│   │   ├── service/
│   │   │   └── ContaService.java                    # Lógica de negócio
│   │   ├── repository/
│   │   │   └── ContaRepository.java                 # Acesso aos dados
│   │   ├── entity/
│   │   │   ├── Conta.java                           # Entidade JPA
│   │   │   └── TipoConta.java                       # Enum de tipos de conta
│   │   ├── dto/
│   │   │   ├── ContaRequestDTO.java
│   │   │   ├── ContaResponseDTO.java
│   │   │   ├── ContaResumoResponseDTO.java
│   │   │   ├── SaldoResponseDTO.java
│   │   │   └── TransicaoRequestDTO.java
│   │   ├── exception/
│   │   │   ├── ContaNaoEncontradaException.java
│   │   │   └── UsuarioNaoEncontradoException.java
│   │   └── Exceptionhandler/
│   │       ├── ApiExceptionHandler.java             # Handler global de exceções
│   │       ├── ApiError.java                        # Estrutura de erro
│   │       └── ProblemType.java                     # Tipos de problemas
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/fintech/wallet_service/
```

## 🚀 Como Executar

### Pré-requisitos

- Java 17 ou superior instalado
- Maven 3.8.0 ou superior
- PostgreSQL 12 ou superior
- Git
- User Service rodando em `http://localhost:8081`

### Configuração Inicial

1. **Clone o repositório**
   ```bash
   git clone <seu-repositorio>
   cd wallet-service
   ```

2. **Configure o banco de dados** (application.properties)
   ```properties
   # Datasource
   spring.datasource.url=jdbc:postgresql://localhost:5432/wallet_db
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=false
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL10Dialect
   
   # Aplicação
   spring.application.name=wallet-service
   server.port=8080
   
   # JWT Secret
   jwt.secret=sua-chave-secreta-muito-segura
   
   # User Service (WebClient)
   user-service.url=http://localhost:8081
   
   # Swagger
   springdoc.api-docs.path=/v3/api-docs
   springdoc.swagger-ui.path=/swagger-ui.html
   ```

3. **Instale as dependências**
   ```bash
   mvn clean install
   ```

4. **Execute a aplicação**
   ```bash
   mvn spring-boot:run
   ```

A aplicação estará disponível em:
- **API**: `http://localhost:8080/api/v1/carteiras`
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8080/v3/api-docs`

### Usando Docker Compose

```yaml
services:
  wallet_service:
    image: 'postgres:latest'
    environment:
      - 'POSTGRES_DB=wallet_service'
      - 'POSTGRES_PASSWORD=admin'
      - 'POSTGRES_USER=postgres'
    ports:
      - '5434:5432'
```

Execute com:
```bash
docker-compose up -d
```

## 🔐 Autenticação e Segurança

### Fluxo de Autenticação JWT

1. **Login no User Service**: Obtenha um token JWT autenticando-se em `/api/v1/usuarios/login`
2. **Usar Token**: Inclua o token no header `Authorization: Bearer <seu-token-jwt>` em todas as requisições ao Wallet Service
3. **Validação**: O `JwtAuthenticationFilter` valida o token em cada requisição
4. **Acesso**: Se válido, a requisição é processada; caso contrário, retorna 401 Unauthorized

### Componentes de Segurança

#### 1. JwtTokenProvider
Responsável por extrair, validar e decodificar tokens JWT.

```java
// Extrai token do header Authorization
String token = jwtTokenProvider.extrairToken("Bearer eyJhbGc...");

// Valida se token é válido
boolean isValid = jwtTokenProvider.validarToken(token);

// Obtém ID do usuário do token
String usuarioId = jwtTokenProvider.obterUsuarioIdDoToken(token);
```

#### 2. JwtAuthenticationFilter
Filtro que intercepta requisições e valida o token.

```
Requisição HTTP
    ↓
JwtAuthenticationFilter
    ↓
Extrai token do header
    ↓
Valida token com JwtTokenProvider
    ↓
Define autenticação no SecurityContext
    ↓
Próximo filtro/Controller
```

#### 3. SecurityConfig
Configuração de segurança que define:
- CSRF desabilitado (API stateless)
- Session stateless
- Endpoints públicos: `/swagger-ui/**`, `/v3/api-docs/**`
- Endpoints protegidos: `/api/v1/carteiras/**` (requer autenticação)
- Filtro JWT adicionado antes de `UsernamePasswordAuthenticationFilter`

### Validação com User Service

O `UsuarioServiceCliente` utiliza `WebClient` para comunicar com o User Service:

```java
// Verifica se usuário existe
boolean existe = usuarioServiceCliente.usuarioExiste(usuarioId);

// Obtém token do contexto de segurança
String token = obterTokenDoContexto(); // Extrai do SecurityContextHolder

// Faz requisição com token
webClient.get()
    .uri("/api/v1/usuarios/{usuarioId}", usuarioId)
    .header("Authorization", token)
    .retrieve()
    .toBodilessEntity()
    .block();
```

## 📡 Endpoints da API

### ⚠️ Autenticação Obrigatória
Todos os endpoints requerem autenticação JWT, exceto o acesso ao Swagger.

**Header obrigatório:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 1. Criar Nova Conta
**POST** `/api/v1/carteiras`

Cria uma nova conta bancária para um usuário. Valida automaticamente se o usuário existe consultando o User Service com o token JWT.

**Request:**
```json
{
  "usuarioId": 1,
  "tipo": "CORRENTE"
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "usuarioId": 1,
  "numeroConta": "ACC-1234567890",
  "saldo": 0.00,
  "tipo": "CORRENTE",
  "dataCriacao": "2024-12-15T10:30:00-03:00",
  "dataAtualizacao": "2024-12-15T10:30:00-03:00",
  "ativa": true
}
```

**Validações:**
- `usuarioId`: obrigatório e deve existir no User Service
- `tipo`: obrigatório (CORRENTE ou POUPANCA)

**Possíveis Erros:**
- `400 Bad Request`: Dados inválidos ou validação falhou
- `401 Unauthorized`: Token inválido ou expirado
- `404 Not Found`: Usuário não encontrado no User Service
- `500 Internal Server Error`: Erro ao comunicar com User Service

### 2. Buscar Conta por ID
**GET** `/api/v1/carteiras/{id}`

Recupera os dados completos de uma conta específica.

**Path Parameter:**
- `id` (Long): ID da conta - exemplo: `1`

**Response:** `200 OK`
```json
{
  "id": 1,
  "usuarioId": 1,
  "numeroConta": "ACC-1234567890",
  "saldo": 1500.50,
  "tipo": "CORRENTE",
  "dataCriacao": "2024-12-15T10:30:00-03:00",
  "dataAtualizacao": "2024-12-15T14:45:00-03:00",
  "ativa": true
}
```

**Possíveis Erros:**
- `401 Unauthorized`: Token inválido ou expirado
- `404 Not Found`: Conta não encontrada

### 3. Listar Contas do Usuário
**GET** `/api/v1/carteiras/usuario/{usuarioId}`

Lista todas as contas de um usuário específico. Valida se o usuário existe no User Service.

**Path Parameter:**
- `usuarioId` (Long): ID do usuário - exemplo: `1`

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "usuarioId": 1,
    "numeroConta": "ACC-1234567890",
    "tipo": "CORRENTE"
  },
  {
    "id": 2,
    "usuarioId": 1,
    "numeroConta": "ACC-0987654321",
    "tipo": "POUPANCA"
  }
]
```

**Possíveis Erros:**
- `401 Unauthorized`: Token inválido ou expirado
- `404 Not Found`: Usuário não existe no User Service

### 4. Consultar Saldo
**GET** `/api/v1/carteiras/{id}/saldo`

Retorna o saldo atual de uma conta.

**Path Parameter:**
- `id` (Long): ID da conta - exemplo: `1`

**Response:** `200 OK`
```json
{
  "id": 1,
  "numeroConta": "ACC-1234567890",
  "saldo": 1500.50,
  "tipo": "CORRENTE",
  "dataConsulta": "2024-12-15T16:45:00-03:00"
}
```

**Possíveis Erros:**
- `401 Unauthorized`: Token inválido ou expirado
- `404 Not Found`: Conta não encontrada

### 5. Depositar
**PUT** `/api/v1/carteiras/{id}/depositar`

Realiza um depósito na conta, aumentando o saldo.

**Path Parameter:**
- `id` (Long): ID da conta - exemplo: `1`

**Request:**
```json
{
  "valor": 500.00
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "numeroConta": "ACC-1234567890",
  "saldo": 2000.50,
  "tipo": "CORRENTE",
  "dataConsulta": "2024-12-15T17:00:00-03:00"
}
```

**Validações:**
- `valor`: obrigatório e deve ser maior que zero
- Valor é adicionado ao saldo atual

**Possíveis Erros:**
- `400 Bad Request`: Valor inválido ou menor/igual a zero
- `401 Unauthorized`: Token inválido ou expirado
- `404 Not Found`: Conta não encontrada

### 6. Sacar
**PUT** `/api/v1/carteiras/{id}/sacar`

Realiza um saque na conta, diminuindo o saldo.

**Path Parameter:**
- `id` (Long): ID da conta - exemplo: `1`

**Request:**
```json
{
  "valor": 200.00
}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "numeroConta": "ACC-1234567890",
  "saldo": 1800.50,
  "tipo": "CORRENTE",
  "dataConsulta": "2024-12-15T17:05:00-03:00"
}
```

**Validações:**
- `valor`: obrigatório e deve ser maior que zero
- Valor é subtraído do saldo atual
- ⚠️ **Nota**: Validação de saldo insuficiente pode ser implementada futuramente

**Possíveis Erros:**
- `400 Bad Request`: Valor inválido ou menor/igual a zero
- `401 Unauthorized`: Token inválido ou expirado
- `404 Not Found`: Conta não encontrada

## 🔄 Integração com User Service

### Fluxo de Comunicação

```
┌──────────────────────┐
│   Cliente            │
│ (com JWT Token)      │
└──────────┬───────────┘
           │
           │ POST /api/v1/carteiras
           │ Authorization: Bearer {token}
           ▼
┌──────────────────────────────┐
│   ContaController            │
└──────────┬───────────────────┘
           │
           │ criarConta(dto)
           ▼
┌──────────────────────────────┐
│   ContaService               │
│ usuarioExiste(usuarioId)?    │
└──────────┬───────────────────┘
           │
           │ usuarioExiste(1)
           │ Token extraído do SecurityContext
           ▼
┌──────────────────────────────┐
│   UsuarioServiceCliente      │
│ (WebClient)                  │
└──────────┬───────────────────┘
           │
           │ GET /api/v1/usuarios/1
           │ Authorization: Bearer {token}
           ▼
┌──────────────────────────────┐
│   User Service               │
│ (Microserviço externo)       │
└──────────┬───────────────────┘
           │
           │ Usuário existe?
           │ 200 OK ou 404 Not Found
           ▼
┌──────────────────────────────┐
│   UsuarioServiceCliente      │
│ Retorna: true/false          │
└──────────┬───────────────────┘
           │
           ├─ Se true: cria conta
           │
           ├─ Se false: lança UsuarioNaoEncontradoException
           │
           └─ Se erro: lança RuntimeException
```

### Cliente WebClient

**Configuração:**
```java
@Component
public class UsuarioServiceCliente {
    private final WebClient webClient;
    
    public UsuarioServiceCliente(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8080").build();
    }
    
    public boolean usuarioExiste(Long usuarioId) {
        String token = obterTokenDoContexto();
        webClient.get()
            .uri("/api/v1/usuarios/{usuarioId}", usuarioId)
            .header("Authorization", token)
            .retrieve()
            .toBodilessEntity()
            .block();
        return true; // Se chegou aqui, usuário existe
    }
    
    private String obterTokenDoContexto() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Bearer " + auth.getCredentials().toString();
    }
}
```

**Tratamento de Erros:**
- `WebClientResponseException.NotFound` → retorna `false`
- `WebClientResponseException.Forbidden` → lança `RuntimeException` (token inválido)
- Outras exceções → lança `RuntimeException` com detalhes

## 📊 Tratamento de Exceções

### Handler Global de Exceções

O `ApiExceptionHandler` padroniza todas as respostas de erro:

```json
{
  "timestamp": "2024-12-15T17:30:00-03:00",
  "status": 404,
  "type": "https://fintech.com.br/recurso-nao-encontrado",
  "title": "Recurso não encontrado",
  "detail": "Não foi encontrado uma conta com o ID: 1",
  "userMessage": "A conta solicitada não foi encontrada.",
  "fields": null
}
```

### Tipos de Erro Tratados

| Exceção | Status | Type | Mensagem |
|---------|--------|------|----------|
| `ContaNaoEncontradaException` | 404 | `recurso-nao-encontrado` | Conta não encontrada |
| `UsuarioNaoEncontradoException` | 404 | `recurso-nao-encontrado` | Usuário não encontrado |
| `MethodArgumentNotValidException` | 400 | `dados-invalidos` | Validação de campo falhou |
| `HttpMessageNotReadableException` | 400 | `mensagem-incompreensivel` | JSON inválido |
| `MethodArgumentTypeMismatchException` | 400 | `parametro-invalido` | Tipo de parâmetro inválido |
| `WebClientResponseException` | 4xx/5xx | `erro-de-sistema` | Erro ao comunicar com serviço externo |
| `IllegalArgumentException` | 400 | `dados-invalidos` | Argumento inválido |
| `ArithmeticException` | 400 | `dados-invalidos` | Operação matemática inválida |
| `NoResourceFoundException` | 404 | `recurso-nao-encontrado` | Endpoint não encontrado |
| Exceção genérica | 500 | `erro-de-sistema` | Erro interno inesperado |

### Resposta com Validação de Campo

```json
{
  "timestamp": "2024-12-15T17:30:00-03:00",
  "status": 400,
  "type": "https://fintech.com.br/dados-invalidos",
  "title": "Dados inválidos",
  "detail": "Um ou mais campos estão inválidos. Corrija e tente novamente.",
  "userMessage": "Um ou mais campos estão inválidos. Corrija e tente novamente.",
  "fields": [
    {
      "name": "usuarioId",
      "userMessage": "O id do usuario é obrigatório"
    },
    {
      "name": "tipo",
      "userMessage": "Tipo de conta é obrigatório"
    }
  ]
}
```

## 📝 DTOs - Data Transfer Objects

### ContaRequestDTO
Dados para criação de uma conta.

```java
public record ContaRequestDTO(
    Long usuarioId,    // obrigatório
    String tipo        // obrigatório: CORRENTE ou POUPANCA
) {}
```

### ContaResponseDTO
Representação completa de uma conta.

```java
public record ContaResponseDTO(
    Long id,
    Long usuarioId,
    String numeroConta,      // formato: ACC-XXXXXXXXXX
    BigDecimal saldo,
    String tipo,
    OffsetDateTime dataCriacao,
    OffsetDateTime dataAtualizacao,
    Boolean ativa
) {}
```

### ContaResumoResponseDTO
Resumo simplificado de uma conta.

```java
public record ContaResumoResponseDTO(
    Long id,
    Long usuarioId,
    String numeroConta,
    String tipo
) {}
```

### SaldoResponseDTO
Dados de retorno da consulta/operação de saldo.

```java
public record SaldoResponseDTO(
    Long id,
    String numeroConta,
    BigDecimal saldo,
    String tipo,
    OffsetDateTime dataConsulta
) {}
```

### TransicaoRequestDTO
Dados para operações de depósito e saque.

```java
public record TransicaoRequestDTO(
    @Positive(message = "Valor de deposito tem que ser maior que ZERO")
    BigDecimal valor
) {}
```

## 🏗️ Arquitetura em Camadas

### Fluxo de uma Requisição

```
1. HTTP Request
   ↓
2. SecurityConfig
   - CSRF desabilitado
   - Session stateless
   ↓
3. JwtAuthenticationFilter
   - Extrai token do header
   - Valida com JwtTokenProvider
   - Define autenticação no SecurityContext
   ↓
4. ContaController
   - Recebe requisição HTTP
   - Valida com Jakarta Validation
   - Chama ContaService
   ↓
5. ContaService
   - Lógica de negócio
   - Chama UsuarioServiceCliente (se necessário)
   - Chama ContaRepository
   ↓
6. ContaRepository (JPA)
   - Executa query no banco de dados
   - Retorna entidade Conta
   ↓
7. ContaMapper (MapStruct)
   - Converte Conta para ContaResponseDTO
   ↓
8. ContaController
   - Retorna ResponseEntity com status HTTP
   ↓
9. HTTP Response (JSON)
```

### Transações

Todas as operações de modificação usam `@Transactional`:

```java
@Transactional
public ContaResponseDTO criarConta(ContaRequestDTO dto) {
    // Operação ACID garantida
    // Rollback automático em exceção
}
```

## 🎯 Boas Práticas Implementadas

### 1. Segurança
- ✅ JWT para autenticação stateless
- ✅ Validação de token em cada requisição
- ✅ Integração segura com User Service
- ✅ Filtro de segurança personalizado
- ✅ Tratamento de token expirado/inválido

### 2. Validação de Dados
- ✅ Jakarta Validation em DTOs
- ✅ Validação de existência de usuário
- ✅ Validação de valores monetários com BigDecimal
- ✅ Mensagens de erro claras e em português

### 3. Persistência
- ✅ JPA/Hibernate como ORM
- ✅ Transações gerenciadas automaticamente
- ✅ Timestamps automáticos (CreationTimestamp, UpdateTimestamp)
- ✅ Enums para tipos de conta

### 4. Tratamento de Erros
- ✅ Handler global de exceções (@ControllerAdvice)
- ✅ Exceções customizadas com status HTTP
- ✅ Respostas estruturadas com ProblemType
- ✅ Detalhes técnicos e mensagens para usuário

### 5. Documentação
- ✅ Swagger/OpenAPI automático
- ✅ Anotações @Operation em endpoints
- ✅ Exemplos de requisição/resposta
- ✅ Descrição de parâmetros

### 6. Comunicação entre Serviços
- ✅ WebClient para requisições HTTP
- ✅ Tratamento de erro 404, 403, etc.
- ✅ Propagação de token JWT
- ✅ Timeout e retry configuráveis

### 7. Padrões de Código
- ✅ Records para DTOs (imutáveis)
- ✅ Lombok para reduzir boilerplate
- ✅ MapStruct para mapeamento automático
- ✅ Injeção de dependência via construtor
- ✅ Separação de responsabilidades clara

## 🧪 Testes

### Executar Testes
```bash
mvn test
```

### Testes Recomendados
- Testes unitários da ContaService com Mockito
- Testes de integração com @SpringBootTest
- Testes de autenticação JWT
- Testes de comunicação com User Service

## 🔌 Possíveis Integrações Futuras

### 1. Serviço de Transações
```
Wallet Service → Transaction Service
- Registrar histórico de depósitos/saques
- Implementar transferências entre contas
- Gerar extratos
```

### 2. Serviço de Notificações
```
Wallet Service → Notification Service
- Email/SMS após depósito
- Alertas de saldo baixo
- Confirmação de operações
```

### 3. Serviço de Relatórios
```
Wallet Service → Report Service
- Extrato de movimentações
- Análise de gastos
- Relatórios periódicos
```

### 4. Serviço de Investimentos
```
Wallet Service → Investment Service
- Aplicações em fundos
- Cálculo de rendimento
- Gestão de investimentos
```

## 💡 Melhorias Recomendadas

### Curto Prazo
1. **Validação de Saldo Insuficiente**
   - Impedir saques quando saldo < valor

2. **Auditoria**
   - Rastrear alterações de saldo
   - Histórico de transações

3. **Tratamento de Erros do User Service**
   - Circuit breaker com Resilience4j
   - Fallback em caso de indisponibilidade

### Médio Prazo
4. **Limites de Conta**
   - Limite de saque diário
   - Limite de transações

5. **Paginação**
   - Listar contas com paginação

6. **Cache**
   - Cache de usuários consultados
   - Redis para melhor performance

### Longo Prazo
7. **Transferências entre Contas**
   - Transações atômicas
   - Compensação distribuída

8. **Testes de Integração**
   - Testes com testcontainers
   - Testes end-to-end

9. **Monitoramento**
   - Actuator para health checks
   - Prometheus/Grafana

10. **API Gateway**
    - Centralizar autenticação
    - Rate limiting
    - Versionamento de API

## 📝 Variáveis de Ambiente

Configure as seguintes variáveis antes de executar:

```properties
# Database
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/wallet_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

# Security
JWT_SECRET=sua-chave-secreta-muito-segura-min-32-caracteres

# External Services
USER_SERVICE_URL=http://localhost:8081

# Server
SERVER_PORT=8080

# Swagger
SPRINGDOC_API_DOCS_PATH=/v3/api-docs
SPRINGDOC_SWAGGER_UI_PATH=/swagger-ui.html
```

## 🔗 Links Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Spring Security](https://spring.io/projects/spring-security)
- [JWT Auth0](https://github.com/auth0/java-jwt)
- [Jakarta Bean Validation](https://jakarta.ee/specifications/bean-validation/)
- [MapStruct](https://mapstruct.org/)
- [SpringDoc OpenAPI](https://springdoc.org/)

## 🤝 Contribuições

Este é um projeto de arquitetura de microsserviços. Sugestões e melhorias são bem-vindas!

## 📄 Licença

Este projeto está sob licença MIT.

---

**Desenvolvido por:** Misael Borges
