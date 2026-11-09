# account-transfer-api

A small Spring Boot REST API for creating bank accounts and transferring money between them, built to be safe under concurrent transfers.

## The interesting part: concurrent transfers without deadlocks

Two transfers running at the same time in opposite directions (A → B and B → A) can deadlock if each one locks its sender first: one holds A and waits for B, the other holds B and waits for A.

`AccountServiceImpl.transferMoney` avoids this by:

1. **Pessimistic row locks.** Each account is loaded with `SELECT … FOR UPDATE` (`@Lock(PESSIMISTIC_WRITE)` on the repository method), so two transfers can't read the same balance and both spend it.
2. **A fixed lock order.** Both accounts are always locked in ascending account-number order, regardless of transfer direction. Every transaction waits for locks in the same order, so a circular wait can't happen.
3. **One transaction.** The withdraw and deposit run inside a single `@Transactional` method, so a failure in either step rolls back both.

Balance rules (positive amount, no overdraft) live on the `Account` entity (`withdraw` / `deposit`), and money is stored as `BigDecimal`.

A manual load check with 3 × 300 concurrent opposite-direction transfers between the same two accounts finished with every request succeeding, no deadlocks and correct final balances.

## Endpoints

Base path: `/api/v1/account`

| Method | Path | Body / params | Result |
|---|---|---|---|
| `POST` | `/create` | `accountNumber`, `username`, `password`, `email`, `balance` | `201 Created` |
| `GET` | `/balance` | `?accountNumber=…` | account number and balance |
| `POST` | `/transfer` | `fromAccountNo`, `toAccountNo`, `amount` | `200 OK` |

Errors go through a global `@RestControllerAdvice` and return `{ "success": false, "message": … }`:

| Situation | Status |
|---|---|
| Account not found | `404` |
| Insufficient balance | `422` |
| Same sender and receiver, validation error | `400` |
| Duplicate account number or email | `409` |

## Stack

Java 17 · Spring Boot 4 · Spring Data JPA · H2 (in-memory) · MapStruct · Lombok · Bean Validation · JUnit 5 + Mockito + Instancio

## Run

```bash
./mvnw spring-boot:run
```

```bash
./mvnw test
```

## Known limitations

This was a timed exercise, so a few things are deliberately left out:

- No authentication. The `password` field is stored as plain text and isn't used for anything yet; a real version would hash it (e.g. BCrypt) or move users to a separate auth service.
- Tests are unit tests with a mocked repository. The concurrency behaviour was checked with the manual load test above, not with an automated integration test.
- Some validation messages are in Turkish.
