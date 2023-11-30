CREATE TABLE symbols (
    ID SERIAL PRIMARY KEY,
    VALUE VARCHAR
);

CREATE TABLE quotes (
    id SERIAL PRIMARY KEY,
    bid NUMERIC,
    ask NUMERIC,
    last_price NUMERIC,
    volume NUMERIC,
    symbol SERIAL,
    FOREIGN KEY (symbol) REFERENCES symbols(id),
    CONSTRAINT last_price_is_positive CHECK (last_price > 0),
    CONSTRAINT volume_is_positive_or_zero CHECK (volume >= 0)
);