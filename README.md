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

## Documentação da API - Swagger

Esta API está documentada utilizando o Swagger, uma ferramenta interativa que facilita a visualização, teste e compreensão dos endpoints disponíveis.

🔗 Acesso à documentação
Após iniciar o projeto, acesse a documentação Swagger através do navegador:

http://localhost:8080/swagger-ui/index.html
