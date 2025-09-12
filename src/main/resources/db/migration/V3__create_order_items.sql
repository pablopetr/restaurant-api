CREATE TABLE IF NOT EXISTS orders (
  id UUID PRIMARY KEY,
  status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
  total NUMERIC(19,2) NOT NULL DEFAULT 0,
  version BIGINT NOT NULL DEFAULT 0,
  created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT now(),

  CONSTRAINT chk_orders_status
    CHECK (status IN ('PENDING','QUEUED','IN_PREPARATION','READY','DELIVERED','CANCELLED')),

  CONSTRAINT chk_orders_total_nonneg CHECK (total >= 0)
);

CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_orders_created_at ON orders(created_at);

CREATE TABLE IF NOT EXISTS order_items (
  id UUID PRIMARY KEY,

  order_id UUID NOT NULL,
  item_id  UUID NOT NULL,

  item_name_snapshot     VARCHAR(255) NOT NULL,
  unit_price_snapshot    NUMERIC(19,2) NOT NULL,
  quantity               INTEGER NOT NULL,
  line_total             NUMERIC(19,2) NOT NULL,

  CONSTRAINT fk_order_items_order
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,

  CONSTRAINT fk_order_items_item
    FOREIGN KEY (item_id) REFERENCES items(id),

  CONSTRAINT uq_order_items_unique_per_order UNIQUE (order_id, item_id),

  CONSTRAINT chk_order_items_qty_pos     CHECK (quantity > 0),
  CONSTRAINT chk_order_items_unit_nonneg CHECK (unit_price_snapshot >= 0),
  CONSTRAINT chk_order_items_total_nonneg CHECK (line_total >= 0)
);

CREATE INDEX IF NOT EXISTS idx_order_items_order_id ON order_items(order_id);
CREATE INDEX IF NOT EXISTS idx_order_items_item_id  ON order_items(item_id);