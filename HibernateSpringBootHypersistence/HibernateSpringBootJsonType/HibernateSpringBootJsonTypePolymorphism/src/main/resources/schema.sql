-- A GIN index makes jsonb containment (@>) and key/path lookups on the post
-- body index-friendly. Hibernate's schema generation cannot emit a "USING gin"
-- index, so it is created here and run after Hibernate (see
-- spring.jpa.defer-datasource-initialization=true in application.properties).
CREATE INDEX IF NOT EXISTS idx_polymorphic_post_body
    ON polymorphic_post USING gin (body);
