# tech-challenge-10adjt

O Problema: 

Na nossa região, um grupo de restaurantes decidiu contratar estudantes 
para construir um sistema de gestão para seus estabelecimentos. Essa decisão 
foi motivada pelo alto custo de sistemas individuais, o que levou os 
restaurantes a se unirem para desenvolver um sistema único e compartilhado. 
Esse sistema permitirá que os clientes escolham restaurantes com base na 
comida oferecida, em vez de se basearem na qualidade do sistema de gestão. 
O objetivo é criar um sistema robusto que permita a todos os 
restaurantes gerenciar eficientemente suas operações, enquanto os clientes 
poderão consultar informações, deixar avaliações e fazer pedidos online. 
Devido à limitação de recursos financeiros, foi acordado que a entrega do 
sistema será realizada em fases, garantindo que cada etapa seja desenvolvida 
de forma cuidadosa e eficaz. 

A divisão em fases possibilitará uma implementação gradual e 
controlada, permitindo ajustes e melhorias contínuas conforme o sistema for 
sendo utilizado e avaliado tanto pelos restaurantes quanto pelos clientes. 

## ADJT - Coleção de Requisições API (ADJT.postman_collection.json)

Esta coleção Postman contém endpoints organizados para testes e integração com a API ADJT. Inclui operações de autenticação, gerenciamento de usuários e tipos de usuários, como criação, atualização, busca, paginação e ativação/inativação. Ideal para desenvolvedores que desejam validar funcionalidades e automatizar testes em ambiente de desenvolvimento.

🔐 Autenticação via token Bearer  
📦 Estrutura organizada por módulos (login, usuário, tipo-usuario)  
🧪 Exemplos de requisições válidas e inválidas para facilitar testes

Ao fazer a busca paginada os filtros disponíveis são:

Filtros 
* EQUALS = "eq" 
* NOT_EQUALS = "ne" 
* LIKE = "like" 
* GREATER_THAN = "gt" 
* LESS_THAN = "lt" 
* GREATER_EQUAL = "ge" 
* LESS_EQUAL = "le" 
* BETWEEN = "bt" 

e os tipos de dados suportados são:
* string: filtro do tipo string, caractere
* number: filtro do tipo numérico, inteiro 
* boolean: filtro do tipo booleano, verdadeiro, falso 
* date: filtro do tipo data, no formato yyyy-MM-dd 

Exemplo de como fazer a busca paginado

{
    "pagina": 0,
    "tamanho": 10,
    "filtros": [
        {
            "campo": "nome",
            "operador": "like",
            "valor": "João",
            "tipo": "string"
        }
    ]
}

## docker-compose (adjt/local/docker-compose.yml)

Este projeto utiliza o PostgreSQL como banco de dados, configurado via docker-compose.yml para facilitar o setup em ambiente local.

✅ Requisitos

Antes de iniciar, certifique-se de ter instalado:

Docker (https://www.docker.com/)
Docker Compose (https://docs.docker.com/compose/)
Java 25 (https://www.oracle.com/br/java/technologies/downloads/)
Postman (https://www.postman.com/)

⚙️ Configuração

O serviço PostgreSQL será iniciado com as seguintes configurações padrão:

Variável            Valor padrão
POSTGRES_USER	    admin
POSTGRES_PASSWORD	secret
POSTGRES_DB	        myrestaurante
Porta exposta	    5432
Volume persistente	pg_data

🚀 Como usar (cmd, prompt, bash)
Para subir o banco de dados: docker-compose up -d
Exemplo adjt/local/> docker-compose up -d 

Para parar os serviços: docker-compose down

🔍 Verificação de saúde
O container possui um healthcheck que verifica se o banco está pronto para conexões usando pg_isready. Isso garante que o serviço só seja considerado "saudável" quando estiver realmente disponível.

📦 Persistência de dados
Os dados do banco são armazenados no volume nomeado pg_data, garantindo que não sejam perdidos ao reiniciar o container.

## Documentação da API - Swagger

Esta API está documentada utilizando o Swagger, uma ferramenta interativa que facilita a visualização, teste e compreensão dos endpoints disponíveis.

🔗 Acesso à documentação
Após iniciar o projeto, acesse a documentação Swagger através do navegador:

http://localhost:8080/swagger-ui/index.html

📌 Funcionalidades disponíveis
A documentação inclui:

📄 Descrição dos endpoints

🔐 Autenticação via token Bearer

📥 Exemplos de requisições e respostas

🧪 Testes interativos diretamente pelo navegador