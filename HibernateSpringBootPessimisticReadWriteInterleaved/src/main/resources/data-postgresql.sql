-- insert department rule ($500 max limit) and Alice's wallet ($100 balance)
INSERT INTO "department_budget" ("dept_code", "max_transfer_limit")
VALUES ('ENG', 500.00)
ON CONFLICT ("dept_code") DO UPDATE SET "max_transfer_limit" = 500.00;

INSERT INTO "employee_wallet" ("employee_id", "dept_code", "balance")
VALUES ('alice', 'ENG', 100.00)
ON CONFLICT ("employee_id") DO UPDATE SET "balance" = 100.00;
