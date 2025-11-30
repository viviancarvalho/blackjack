Manual pro Guilherme (POA/AspectJ para microserviços de Blackjack):

Em resumo, esse módulo AOP_MARIA implementa Programação Orientada a Aspectos com AspectJ para o sistema de Blackjack.
Ou seja, ele fornece:

LoggingAspect para log automático de controllers e services

AuditAspect com anotação @Auditable para auditar ações importantes

CorrelationIdFilter para rastrear requisições entre microserviços

SecurityUtils para pegar o usuário autenticado.

Basta adicionar a dependência no pom.xml de cada microserviço e usar @Auditable nos métodos críticos
(ex.: iniciar rodada, hit, stand, jogadas da casa).

De forma mais específica, Como usar:

1 Se tiver Maven instalado, rodar na pasta AOP_MARIA:
   mvn clean install

2 Em cada microserviço, adicionar no pom.xml:
   <dependency>
     <groupId>com.unifor.blackjack</groupId>
     <artifactId>AOP_MARIA</artifactId>
     <version>1.0.0</version>
   </dependency>

3 Usar a anotação @Auditable nos métodos que devem ser auditados:
   import aop.Auditable;

   @Auditable(action = "start-round", resource = "game")
   public void startRound(...) { ... }

4 LoggingAspect registra entrada, saída e erros em controller e service.

5 CorrelationIdFilter garante correlationId por requisição via header "correlationId".