ALTER TABLE tasks
    ADD COLUMN created_at TIMESTAMPTZ;

UPDATE tasks
SET created_at = CURRENT_TIMESTAMP
WHERE created_at IS NULL;

ALTER TABLE tasks
    ALTER COLUMN created_at SET NOT NULL;

CREATE INDEX IF NOT EXISTS idx_tasks_created_at
    ON tasks(created_at);