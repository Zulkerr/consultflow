CREATE TABLE consultants (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    primary_skill VARCHAR(50) NOT NULL,
    max_capacity_percent INT NOT NULL DEFAULT 100 CHECK (max_capacity_percent BETWEEN 1 AND 100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    client_name VARCHAR(100) NOT NULL,
    required_skill VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE allocations (
    id BIGSERIAL PRIMARY KEY,
    consultant_id BIGINT NOT NULL REFERENCES consultants(id),
    project_id BIGINT NOT NULL REFERENCES projects(id),
    allocation_percent INT NOT NULL CHECK (allocation_percent BETWEEN 1 AND 100),
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_allocations_consultant_id ON allocations(consultant_id);
CREATE INDEX idx_allocations_project_id ON allocations(project_id);
