CREATE OR REPLACE VIEW CUSTOMER_ORDER_SUMMARY
AS
SELECT o.customer_id as id, 
           COUNT(o.id) as total_orders, 
           SUM(o.amount) as total_spent 
    FROM orders o 
    GROUP BY o.customer_id;