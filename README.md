# Documentação do Sistema Lotofácil Analyzer (v3 - Evolução)

## Visão Geral

Este sistema permite analisar resultados de sorteios da Lotofácil, importar dados históricos, visualizar estatísticas detalhadas em um dashboard dinâmico, e gerar sugestões de jogos personalizadas com base em múltiplos filtros.

Esta versão inclui melhorias significativas em relação às anteriores, como entrada manual de resultados, atualização automática de estatísticas de atraso e marcação de combinações sorteadas, um dashboard mais completo, um motor de sugestões personalizadas e tema claro/escuro.

## Tecnologias Utilizadas

*   **Backend:** Java 17+, Spring Boot 3+, Spring Data JPA, Maven, MySQL
*   **Frontend:** React 18+, TypeScript, Material UI (MUI) v5+, Recharts, Axios
*   **Banco de Dados:** MySQL 8+
*   **Outros:** Lombok, Swagger (OpenAPI 3)

## Funcionalidades Principais

1.  **Importação de Resultados:** Importa resultados de sorteios anteriores a partir de planilhas Excel (`.xlsx`), evitando duplicidades.
2.  **Entrada Manual de Resultados:** Permite adicionar resultados de sorteios individualmente através de um formulário.
3.  **Atualização Automática de Dados:**
    *   **Tabela `atraso`:** Atualiza a contagem de atraso e a data do último sorteio para cada número (1-25) após cada novo resultado.
    *   **Tabela `todos` (Campo `sorteado`):** Marca a combinação exata sorteada como `sorteado = 1` na tabela `todos`.
    *   **Tabela `todos` (Campo `pontos` - Atualização Assíncrona):** Compara os 15 números do último resultado adicionado com todas as 3.2 milhões de combinações na tabela `todos` e atualiza a coluna `pontos` (contagem de acertos) de forma assíncrona em segundo plano. **Atenção:** Esta atualização não é imediata e pode levar um tempo considerável para ser concluída.
4.  **Dashboard Dinâmico:**
    *   Visualiza gráficos e estatísticas com base nos últimos N resultados (N selecionável pelo usuário: 10, 20, 50, 100, 200).
    *   Gráfico de linha da Soma das Dezenas.
    *   Gráfico de linha da Contagem de Números Ímpares.
    *   Gráfico de barras da Frequência de cada Número.
    *   Listas com contagem de Sequências (Seq 2, Seq 3, Seq 4).
    *   Listas com Top 10 ocorrências de Padrões de Linha e Coluna.
5.  **Sugestões Personalizadas:**
    *   Permite ao usuário definir múltiplos filtros (faixas de pontos, soma, ímpares, sequências; padrões de linha/coluna; excluir já sorteados) para buscar combinações na tabela `todos`.
    *   Retorna resultados paginados das combinações que atendem aos critérios.
6.  **Tema Claro/Escuro:** Permite alternar entre um tema claro e um tema escuro (inspirado na imagem fornecida) na interface.

## Estrutura do Projeto

```
LotofacilAnalyzer/
├── backend/
│   ├── pom.xml
│   └── src/
│       ├── main/
│       │   ├── java/com/lotofacil/
│       │   │   ├── config/         # Configurações (Swagger, CORS, Async)
│       │   │   ├── controller/     # Controladores REST
│       │   │   ├── dto/            # Data Transfer Objects (por funcionalidade)
│       │   │   ├── entity/         # Entidades JPA
│       │   │   ├── exception/      # Tratamento de exceções
│       │   │   ├── repository/     # Repositórios Spring Data JPA
│       │   │   ├── service/        # Lógica de negócio (Dashboard, Sugestão, Update, etc.)
│       │   │   │   └── importer/   # Serviço de importação
│       │   │   └── LotofacilAnalyzerApplication.java # Classe principal
│       │   └── resources/
│       │       └── application.properties # Configurações da aplicação (BD, etc.)
│       └── test/                 # Testes (não implementados nesta versão)
├── frontend/
│   └── lotofacil-frontend/
│       ├── public/
│       ├── src/
│       │   ├── components/     # Componentes React reutilizáveis (Layout, HealthCheck)
│       │   ├── contexts/       # Contextos React (ThemeContext)
│       │   ├── dto/            # DTOs (interfaces TypeScript)
│       │   ├── entity/         # Entidades (interfaces TypeScript)
│       │   ├── pages/          # Páginas da aplicação (Dashboard, Importacao, etc.)
│       │   ├── services/       # Serviços para chamadas API (axios)
│       │   ├── App.tsx         # Componente principal e rotas
│       │   ├── index.tsx       # Ponto de entrada do React
│       │   └── theme.ts        # Definição do tema MUI
│       ├── package.json
│       ├── tsconfig.json
│       └── ...
├── README.md                   # Este arquivo
└── start.sh                    # Script de inicialização (exemplo)
```

## Configuração e Execução

**Pré-requisitos:**

*   Java JDK 17 ou superior
*   Maven 3.6+
*   Node.js 16+ e npm 8+
*   MySQL Server 8+

**Passos:**

1.  **Banco de Dados:**
    *   Crie um banco de dados MySQL chamado `lotofacil` (ou o nome que preferir).
    *   **IMPORTANTE:** Certifique-se de que a tabela `todos` já exista e esteja populada com todas as 3.268.760 combinações possíveis. As colunas esperadas são: `id`, `coluna`, `linha`, `pares`, `pontos` (inicialmente pode ser NULL ou 0), `seq_dois`, `seq_tres`, `seq_quatro`, `seq_cinco`, `seq_seis`, `seq_sete`, `seq_oito`, `sequencia` (formato "01-02-..."), `soma`, `sorteado` (inicialmente 0).
    *   Crie as tabelas `sorteados` e `atraso` conforme o diagrama original ou deixe o Hibernate criá-las (verifique `spring.jpa.hibernate.ddl-auto` em `application.properties`).
    *   Configure as credenciais do banco de dados no arquivo `backend/src/main/resources/application.properties`.
    *   **CRIE OS ÍNDICES (ESSENCIAL PARA PERFORMANCE):** Execute os seguintes comandos SQL no seu banco de dados `lotofacil`:
        ```sql
        -- Índice principal para busca de combinações
        ALTER TABLE todos ADD UNIQUE INDEX idx_sequencia (sequencia);

        -- Índices recomendados para filtros de sugestão (adicione outros conforme necessidade)
        ALTER TABLE todos ADD INDEX idx_pontos (pontos);
        ALTER TABLE todos ADD INDEX idx_soma (soma);
        ALTER TABLE todos ADD INDEX idx_impares (impares);
        ALTER TABLE todos ADD INDEX idx_linha (linha);
        ALTER TABLE todos ADD INDEX idx_coluna (coluna);
        ALTER TABLE todos ADD INDEX idx_sorteado (sorteado);
        -- Considere índices para colunas de sequência (seq_dois, etc.) se forem muito usadas em filtros
        ```

2.  **Extraia o Projeto:** Extraia o conteúdo do arquivo `.zip` fornecido.

3.  **Backend:**
    *   Abra um terminal na pasta `backend`.
    *   Compile e empacote a aplicação: `mvn clean package -DskipTests`
    *   Execute a aplicação: `java -jar target/lotofacil-analyzer-0.0.1-SNAPSHOT.jar` (o nome do JAR pode variar).
    *   Alternativamente, importe o projeto Maven na sua IDE (IntelliJ, Eclipse) e execute a classe `LotofacilAnalyzerApplication`.
    *   A API estará disponível em `http://localhost:8080` e a documentação Swagger em `http://localhost:8080/swagger-ui.html`.

4.  **Frontend:**
    *   Abra um terminal na pasta `frontend/lotofacil-frontend`.
    *   Instale as dependências: `npm install`
    *   Inicie o servidor de desenvolvimento: `npm start`
    *   A interface estará disponível em `http://localhost:3000`.

5.  **Script `start.sh` (Exemplo):** O script `start.sh` fornecido é um exemplo básico para iniciar ambos os processos em segundo plano. Pode ser necessário adaptá-lo ao seu ambiente.

## Uso

*   **Dashboard:** Acesse a página principal para visualizar as estatísticas dinâmicas. Use o seletor para alterar o número de últimos resultados considerados.
*   **Importação:** Use a página "Importação" para carregar resultados de uma planilha Excel.
*   **Entrada Manual:** Use a página "Entrada Manual" para adicionar um resultado específico.
*   **Sugestões (Fechamentos):** A página original de sugestões baseadas em fechamentos foi mantida (pode ser removida/adaptada se necessário).
*   **Sugestões (Filtros):** Acesse a nova página "Sugestões (Filtros)" (precisa adicionar o link no Layout.tsx e a rota em App.tsx se ainda não foi feito) para definir critérios e buscar combinações na tabela `todos`.
*   **Alternar Tema:** Use o ícone de sol/lua no canto superior direito para alternar entre os temas claro e escuro.

## Considerações

*   **Performance da Atualização de Pontos:** A atualização assíncrona da coluna `pontos` na tabela `todos` é uma operação pesada. Monitore os logs do backend para verificar o tempo de execução e possíveis erros. Evite adicionar muitos resultados em sequência rápida para não sobrecarregar as tarefas assíncronas.
*   **Índices do Banco de Dados:** A performance das consultas do dashboard e das sugestões personalizadas depende crucialmente dos índices criados na tabela `todos`. Sem eles, as buscas podem ser muito lentas.
*   **Validação de Dados:** Validações básicas foram implementadas, mas podem ser aprimoradas (ex: garantir 15 números únicos na entrada manual).
*   **Testes:** Testes unitários e de integração não foram implementados nesta versão e são recomendados para garantir a robustez.

## Próximos Passos (Sugestões)

*   Implementar testes automatizados.
*   Adicionar mais opções de filtros para sugestões.
*   Melhorar a visualização dos resultados das sugestões.
*   Otimizar as queries do dashboard e sugestões (análise de planos de execução SQL).
*   Adicionar autenticação de usuário.
*   Implementar paginação ou carregamento sob demanda para gráficos com muitos pontos.

