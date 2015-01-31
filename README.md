# Homework for HH school on ORM

Simple database with two tables: Employers and vacancies, offered by employers.
Employers implemented with Hibernate, vacancies - with JDBC.

> один обращается к другому в транзакции

При удалении работодателя из таблицы, все связанные с ним вакансии удаляются из таблицы вакансий - происходит вызов одного из сервисов из другого. При падении любой из транзакций, происходит её rollback.

# Configure
- Change configuration (user, password) in hibernate.properties and Config.java
- run /src/main/resources/prepare.sh to create db and tables

# Task:
- 2 простые таблицы
- 2 DAO
  - один на hibernate
  - второй на чем-нибудь другом
  - в каждом insert, select, update, delete
- 2 сервиса
  - один обращается к другому в транзакции
  - транзакция должна быть обоснована
- тесты (опционально)
  - можно запустить сразу после git clone через mvn test
- желательно на maven
