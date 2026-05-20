# Vitrine Freelancers — Server — Estrutura & Estratégia

> Documento de referência para o backend `vitrine-freelancers-server`.
> Atualizar conforme o projeto evolui.

---

## 1. Visão Geral

API REST em **Spring Boot 3** que serve o frontend Next.js.  
Responsabilidades: autenticação JWT, CRUD de vagas, candidaturas, agendamento de encerramento automático de vagas e (futuro) contabilização de acessos.

- **Porta padrão:** `8080`
- **Banco de dados:** PostgreSQL
- **Migrations:** Liquibase
- **Autenticação:** JWT stateless (JJWT 0.11.5)
- **Java:** 21

---

## 2. Estrutura da Aplicação

```
src/
├── main/
│   ├── java/com/vitrine_freelancers_server/
│   │
│   ├── controllers/                         # Camada HTTP (endpoints REST)
│   │   ├── auth/
│   │   │   ├── AuthController.java          # POST /auth/login, POST /auth/register
│   │   │   ├── RegisterService.java         # Orquestra criação de User + Company
│   │   │   ├── requests/
│   │   │   │   ├── LoginRequest.java
│   │   │   │   └── RegisterUserCompanyDTO.java
│   │   │   ├── response/
│   │   │   │   ├── ResponseLogin.java
│   │   │   │   └── ResponseToken.java
│   │   │   └── dtos/
│   │   │       ├── CreateUserDTO.java
│   │   │       └── CreateCompanyDTO.java
│   │   │
│   │   ├── jobs/
│   │   │   ├── JobController.java           # CRUD /jobs
│   │   │   ├── requests/
│   │   │   │   └── JobCreateOrUpdateRequest.java
│   │   │   └── response/
│   │   │       └── JobResponse.java
│   │   │
│   │   ├── applications/
│   │   │   └── ApplicationController.java   # /applications
│   │   │
│   │   ├── users/
│   │   │   ├── UserController.java          # /users (ADMIN only)
│   │   │   ├── UserUpdateRequest.java
│   │   │   └── UserWithCompanyDTO.java
│   │   │
│   │   └── companies/
│   │       ├── CompanyController.java       # /companies
│   │       └── response/
│   │           └── CompanyResponse.java
│   │
│   ├── services/                            # Regras de negócio
│   │   ├── UserService.java                 # Login, autenticação
│   │   ├── CompanyService.java
│   │   ├── JobService.java                  # CRUD vagas + controle de autorização
│   │   ├── JobSchedule.java                 # @Scheduled — fecha vagas expiradas
│   │   └── ApplicationService.java          # Candidaturas
│   │
│   ├── domain/                              # Entidades JPA
│   │   ├── UserEntity.java
│   │   ├── CompanyEntity.java
│   │   ├── JobEntity.java
│   │   ├── ApplicationEntity.java
│   │   ├── Role.java
│   │   └── Permission.java
│   │
│   ├── repositories/                        # Spring Data JPA
│   │   ├── UserRepository.java
│   │   ├── CompanyRepository.java
│   │   ├── JobRepository.java
│   │   └── ApplicationRepository.java
│   │
│   ├── mappers/
│   │   ├── JobMapper.java
│   │   └── CompanyMapper.java
│   │
│   ├── dtos/
│   │   ├── User/
│   │   │   └── UserPrincipalDTO.java        # Principal do SecurityContext
│   │   └── job/
│   │       └── JobProjection.java           # Interface projection p/ scheduler
│   │
│   ├── enums/
│   │   ├── JobType.java                     # FIXO | FREELANCER
│   │   ├── UserRole.java
│   │   ├── UserStatus.java
│   │   └── ApplicationStatus.java           # PENDING | ACCEPTED | REJECTED
│   │
│   ├── exceptions/
│   │   ├── GlobalExceptionHandler.java      # @ControllerAdvice
│   │   ├── JobNotFoundException.java
│   │   ├── UserNotFoundException.java
│   │   ├── UserNotAuthorizationException.java
│   │   ├── UserEmailAlreadyExistsException.java
│   │   ├── InvalidLoginException.java
│   │   ├── CompanyNotFoundException.java
│   │   ├── CompanyAlreadyExistsException.java
│   │   └── response/
│   │       ├── ResponseSuccess.java
│   │       └── ResponseError.java
│   │
│   └── infra/
│       └── security/
│           ├── SecurityConfig.java          # FilterChain + CORS + roles
│           ├── SecurityFilter.java          # OncePerRequestFilter — valida JWT
│           ├── TokenService.java            # Gera e valida JWT (HS256, 24h)
│           └── CustomUserDetailsService.java
│
└── resources/
    ├── application.properties
    └── db/
        ├── changelog/
        │   └── changelog-master.xml         # Liquibase master
        └── scripts/
            ├── initial_create_tables.sql    # users, companies, jobs, roles...
            ├── 002_add_job_application_fields.sql
            └── 003_create_applications_table.sql
```

---

## 3. Endpoints da API

### Autenticação — `/auth`

| Método | Endpoint         | Auth   | Descrição                        |
|--------|------------------|--------|----------------------------------|
| POST   | `/auth/register` | Pública | Cria usuário + empresa           |
| POST   | `/auth/login`    | Pública | Retorna JWT (24h)                |

### Vagas — `/jobs`

| Método | Endpoint                  | Auth              | Descrição                        |
|--------|---------------------------|-------------------|----------------------------------|
| GET    | `/jobs`                   | Pública           | Lista vagas abertas (paginado)   |
| GET    | `/jobs/{id}`              | Pública           | Detalhe de uma vaga              |
| POST   | `/jobs`                   | ADMIN, COMPANY    | Cria nova vaga                   |
| PUT    | `/jobs/{id}`              | ADMIN, COMPANY    | Atualiza vaga (owner/admin)      |
| DELETE | `/jobs/{id}`              | ADMIN, COMPANY    | Encerra vaga (soft-close)        |
| GET    | `/jobs/company/{id}`      | ADMIN, COMPANY    | Vagas da empresa logada          |
| GET    | `/jobs/type/{id}`         | Autenticado       | Vagas por tipo                   |

### Candidaturas — `/applications`

| Método | Endpoint                  | Auth              | Descrição                        |
|--------|---------------------------|-------------------|----------------------------------|
| POST   | `/applications`           | Autenticado       | Candidatar-se a uma vaga         |
| GET    | `/applications/my`        | Autenticado       | Minhas candidaturas              |
| GET    | `/applications/job/{id}`  | ADMIN, COMPANY    | Candidatos de uma vaga           |
| PATCH  | `/applications/{id}/status` | ADMIN, COMPANY  | Atualizar status da candidatura  |

### Empresas — `/companies`

| Método | Endpoint           | Auth   | Descrição                  |
|--------|--------------------|--------|----------------------------|
| GET    | `/companies/{id}`  | Pública | Dados públicos da empresa  |
| *      | `/companies/**`    | ADMIN  | Gestão administrativa      |

### Usuários — `/users`

| Método | Endpoint    | Auth  | Descrição             |
|--------|-------------|-------|-----------------------|
| *      | `/users/**` | ADMIN | Gestão de usuários    |

---

## 4. Modelo de Dados

### Diagrama de relacionamentos

```
users ──────────── companies ──────────── jobs
  │                                         │
  │                                         │
  └──── user_roles ──── roles               └──── applications ──── users
                          │
                          └──── role_permissions ──── permissions
```

### Tabelas

| Tabela             | Colunas principais                                                              |
|--------------------|---------------------------------------------------------------------------------|
| `users`            | id, name, email (unique), password (bcrypt), status, created_at                 |
| `companies`        | id, name (unique), user_id (FK, unique), is_active, created_at                  |
| `jobs`             | id, type, position, description, requirements, date, start_time, end_time, daily_value, open, open_until, applications_count, has_new_applications, company_id (FK) |
| `applications`     | id, job_id (FK), user_id (FK), status (PENDING/ACCEPTED/REJECTED), created_at  |
| `roles`            | id, name, description                                                           |
| `permissions`      | id, name                                                                        |
| `user_roles`       | user_id, role_id                                                                |
| `role_permissions` | role_id, permission_id                                                          |

### Roles disponíveis

| Role           | Acesso                                                   |
|----------------|----------------------------------------------------------|
| `ROLE_ADMIN`   | Acesso total a todos os endpoints                        |
| `ROLE_COMPANY` | Gerencia próprias vagas e visualiza candidatos           |
| `ROLE_USER`    | Candidata-se a vagas, visualiza próprias candidaturas    |

---

## 5. Segurança

- **Stateless JWT** — sem sessão no servidor; token enviado via `Authorization: Bearer <token>`.
- **BCrypt** — senhas nunca armazenadas em texto puro.
- **SecurityFilter** — valida o JWT em cada request antes de chegar ao controller.
- **CORS** — atualmente permitido apenas para `http://localhost:3000`. **Atualizar para o domínio de produção antes do deploy.**
- **Autorização por role** — cada endpoint valida se o usuário autenticado possui a role necessária. `JobService.userCanExecute()` também verifica propriedade da vaga.

---

## 6. Agendador — Encerramento automático de vagas

`JobSchedule.java` roda via `@Scheduled` com expressão cron configurável em `application.properties` (`spring.task.scheduling.expression`).

**Comportamento:**
1. A cada execução, busca vagas onde `open = true` e `open_until <= now` (truncado para hora cheia).
2. Executa `UPDATE jobs SET open = false WHERE id IN (...)` em batch.
3. Loga resultado: quantidade e IDs das vagas fechadas.

**Observação:** o campo `open_until` é definido no momento da criação da vaga pelo frontend (home de criação da vaga). Atualmente é 12h após a publicação.

---

## 7. Estratégia de Contabilização de Acessos

> Integração com o frontend será feita em etapa futura. O backend será preparado agora para receber os eventos quando estiver pronto.

### 7.1 Objetivo

Registrar métricas de uso da plataforma para subsidiar decisões de produto e dimensionar o inventário de anúncios (AdSense no frontend). Dados coletados: quais páginas são visitadas, quando, com que frequência, e o volume de acessos por vaga.

### 7.2 Design da solução

#### Nova tabela — `page_views`

```sql
CREATE TABLE IF NOT EXISTS page_views
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    page       VARCHAR(100)  NOT NULL,    -- ex: 'vagas', 'job_detail', 'home'
    ref_id     BIGINT,                    -- id da vaga, quando aplicável
    ip_hash    VARCHAR(64),               -- SHA-256 do IP (privacidade LGPD)
    user_agent VARCHAR(512),
    session_id VARCHAR(64),               -- token anônimo gerado no browser
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_page_views PRIMARY KEY (id)
);

CREATE INDEX idx_page_views_page       ON page_views (page);
CREATE INDEX idx_page_views_created_at ON page_views (created_at);
CREATE INDEX idx_page_views_ref_id     ON page_views (ref_id);
```

#### Nova entidade — `PageViewEntity.java`

```java
@Entity(name = "page_views")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PageViewEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String page;        // "vagas" | "job_detail" | "home" | "sobre" | "contato"

    private Long refId;         // ID da vaga, quando page = "job_detail"

    @Column(length = 64)
    private String ipHash;      // SHA-256 do IP real

    @Column(length = 512)
    private String userAgent;

    @Column(length = 64)
    private String sessionId;   // gerado pelo browser (localStorage uuid)

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
```

#### Endpoint de recebimento — `POST /analytics/pageview`

```
Pública (sem autenticação)
Body:
{
  "page":      "job_detail",
  "refId":     42,
  "sessionId": "uuid-gerado-no-browser"
}
Resposta: 204 No Content
```

> O IP e o User-Agent são capturados pelo servidor (não enviados pelo cliente), evitando spoofing.

#### Endpoint de consulta — `GET /analytics/summary` (ADMIN only)

```
Resposta:
{
  "totalViews": 1520,
  "viewsToday": 87,
  "viewsThisWeek": 430,
  "topPages": [
    { "page": "vagas",      "count": 820 },
    { "page": "job_detail", "count": 540 },
    { "page": "home",       "count": 160 }
  ],
  "topJobs": [
    { "refId": 42, "position": "Atendente de Caixa", "views": 98 },
    { "refId": 17, "position": "Auxiliar de Limpeza", "views": 75 }
  ]
}
```

### 7.3 Arquivos a criar (quando implementar)

| Arquivo                                          | Responsabilidade                              |
|--------------------------------------------------|-----------------------------------------------|
| `domain/PageViewEntity.java`                     | Entidade JPA da tabela `page_views`           |
| `repositories/PageViewRepository.java`           | Spring Data JPA + queries de agregação        |
| `services/AnalyticsService.java`                 | Regras: gravar view, montar summary           |
| `controllers/analytics/AnalyticsController.java` | POST `/analytics/pageview`, GET `/analytics/summary` |
| `db/scripts/004_create_page_views_table.sql`     | Migration Liquibase                           |
| `db/changelog/changelog-master.xml`              | Adicionar novo changeSet `004`                |
| `SecurityConfig.java`                            | Liberar `POST /analytics/pageview` público    |

### 7.4 Configuração no `SecurityConfig.java`

```java
// Adicionar junto às outras regras de autorização:
.requestMatchers(HttpMethod.POST, "/analytics/pageview").permitAll()
.requestMatchers(HttpMethod.GET,  "/analytics/**").hasRole("ADMIN")
```

### 7.5 Integração futura com o frontend (Next.js)

Quando o frontend estiver pronto para integrar, basta adicionar em cada página pública:

```ts
// src/lib/analytics.ts
export async function trackPageView(page: string, refId?: number) {
  const sessionId = getOrCreateSessionId(); // localStorage
  await fetch(`${process.env.NEXT_PUBLIC_API_URL}/analytics/pageview`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ page, refId, sessionId }),
  });
}
```

---

## 8. Stack Tecnológica

| Camada           | Tecnologia                          | Versão    |
|------------------|-------------------------------------|-----------|
| Framework        | Spring Boot                         | 3.4.2     |
| Linguagem        | Java                                | 21        |
| Banco de dados   | PostgreSQL                          | —         |
| ORM              | Spring Data JPA / Hibernate         | —         |
| Migrations       | Liquibase                           | —         |
| Segurança        | Spring Security + JJWT              | 0.11.5    |
| Serialização     | Jackson                             | 2.17.1    |
| Boilerplate      | Lombok                              | —         |
| Testes           | JUnit 5 + Spring Security Test      | —         |
| Build            | Maven                               | —         |

---

## 9. Pendências & Próximos Passos

| # | Tarefa                                          | Prioridade | Status     |
|---|-------------------------------------------------|------------|------------|
| 1 | Implementar módulo de analytics (seção 7)       | Alta       | Pendente   |
| 2 | Atualizar CORS para domínio de produção         | Alta       | Pendente   |
| 3 | Externalizar segredo JWT para variável de ambiente segura | Alta | Pendente |
| 4 | Adicionar paginação na resposta de `/jobs`      | Média      | Parcial    |
| 5 | Retornar dados de endereço da empresa no `JobResponse` | Média | Pendente |
| 6 | Rate limiting no endpoint `POST /analytics/pageview` | Baixa | Pendente |
| 7 | Endpoint de estatísticas por período (diário/semanal) | Baixa | Pendente |
| 8 | Cobertura de testes nos controllers            | Baixa      | Parcial    |

---

*Documento gerado em 20/05/2026. Atualizar conforme o projeto evolui.*
