CREATE OR REPLACE VIEW "customer_order_summary" AS
SELECT o."customer_id" AS "id",
       COUNT(o."id") AS "total_orders",
       SUM(o."amount") AS "total_spent"
FROM "orders" o
GROUP BY o."customer_id";
