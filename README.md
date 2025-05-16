Wymagania: Java 17+, Maven, Git

Budowanie:
mvn clean install

Uruchomienie:
mvn spring-boot:run

Aplikacja będzie dostępna pod adresem:
http://localhost:8080

Przykładowe zapytania cURL
Utworzenie zbiórki:
curl -X POST http://localhost:8080/events -H "Content-Type: application/json" -d '{"name": "Charity One", "currency": "EUR"}'

