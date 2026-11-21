-- insert authors
INSERT INTO "author" (
  "id", "name", "email", "primary_genre", "country_code", "birth_date", "biography",
  "social_metadata", "sell_rank", "total_books_sold", "last_active_at") VALUES (
    1,
    'Olivia Goy',
    'olivia.goy@example.com',
    'Horror',
    'US',
    '1984-05-14',
    'Olivia Goy is a bestselling horror novelist known for psychological thrillers and supernatural suspense.',
    '{"website": "https://example.com", "twitter": "@oliviagoy_books", "newsletterSubscribers": 48200}'::jsonb,
    12,
    345000,
    '2026-11-20T18:30:00Z'
  ),
  (
    2,
    'Joana Nimar',
    'joana.nimar@example.com',
    'History',
    'GB',
    '1976-09-22',
    'Joana Nimar is a historian and author specializing in ancient civilizations and European medieval chronicles.',
    '{"website": "https://example.com", "mastodon": "@jnimar@history.social", "awards": ["Historical Non-Fiction Prize"]}'::jsonb,
    45,
    128500,
    '2026-11-18T09:15:00Z'
  )
ON CONFLICT ("id") DO NOTHING;