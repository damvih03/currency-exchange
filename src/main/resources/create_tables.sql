CREATE TABLE currencies
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    code VARCHER NOT NULL UNIQUE,
    name VARCHAR NOT NULL,
    sign VARCHAR NOT NULL
);

CREATE TABLE exchange_rates
(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id INTEGER,
    target_currency_id INTEGER,
    rate DECIMAL(6),
    UNIQUE(base_currency_id, target_currency_id),
    FOREIGN KEY (base_currency_id) REFERENCES currencies(id),
    FOREIGN KEY (target_currency_id) REFERENCES currencies(id)
)