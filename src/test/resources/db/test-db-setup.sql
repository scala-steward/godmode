
CREATE TABLE games (
  id UUID PRIMARY KEY,
  name TEXT NOT NULL
);

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO games(id, name) VALUES
  (uuid_generate_v4(), 'the legend of zelda: breath of the wild'),
  (uuid_generate_v4(), 'super mario odyssey');
