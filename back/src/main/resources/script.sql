-- CREATE DATABASE ocp6;

-- CREATE USER ocp6 WITH PASSWORD 'ocp6';

-- Table: users
CREATE TABLE users
(
    id         VARCHAR(255) PRIMARY KEY,
    username   VARCHAR(255) UNIQUE NOT NULL,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255)        NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

-- Table: topics
CREATE TABLE topics
(
    id          VARCHAR(255) PRIMARY KEY,
    title       VARCHAR(255) UNIQUE NOT NULL,
    description TEXT                NOT NULL
);

-- Table: posts
CREATE TABLE posts
(
    id         VARCHAR(255) PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    author_id  VARCHAR(255) NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    topic_id   VARCHAR(255) NOT NULL REFERENCES topics (id) ON DELETE CASCADE
);

-- Table: comments
CREATE TABLE comments
(
    id         VARCHAR(255) PRIMARY KEY,
    content    TEXT         NOT NULL,
    created_at TIMESTAMP,
    author_id  VARCHAR(255) NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    post_id    VARCHAR(255) NOT NULL REFERENCES posts (id) ON DELETE CASCADE
);

-- Table: refresh_tokens
CREATE TABLE refresh_tokens
(
    id          VARCHAR(255) PRIMARY KEY,
    token       TEXT      NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    user_id     VARCHAR(255) UNIQUE REFERENCES users (id) ON DELETE CASCADE
);

-- Join table: users_topics
CREATE TABLE users_topics
(
    user_id  VARCHAR(255) NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    topic_id VARCHAR(255) NOT NULL REFERENCES topics (id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, topic_id)
);

GRANT ALL ON TABLE public.users TO ocp6;
GRANT ALL ON TABLE public.topics TO ocp6;
GRANT ALL ON TABLE public.posts TO ocp6;
GRANT ALL ON TABLE public.comments TO ocp6;
GRANT ALL ON TABLE public.refresh_tokens TO ocp6;
GRANT ALL ON TABLE public.users_topics TO ocp6;

