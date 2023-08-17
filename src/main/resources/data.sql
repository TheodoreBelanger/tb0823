CREATE TABLE tools (
  tool_code VARCHAR(255) NOT NULL,
  tool_type VARCHAR(255) NOT NULL,
  brand VARCHAR(255) NOT NULL,
  PRIMARY KEY (tool_code)
);

INSERT INTO tools (tool_code, tool_type, brand)
VALUES
  ('CHNS', 'Chainsaw', 'Stihl'),
  ('LADW', 'Ladder', 'Werner'),
  ('JAKD', 'Jackhammer', 'DeWalt'),
  ('JAKR', 'Jackhammer', 'Ridgid');

CREATE TABLE toolpurchasedata (
  tool_type VARCHAR(255) NOT NULL,
  daily_charge DECIMAL(10,2) NOT NULL,
  weekday_charge BOOLEAN NOT NULL,
  weekend_charge BOOLEAN NOT NULL,
  holiday_charge BOOLEAN NOT NULL,
  PRIMARY KEY (tool_type)
);

INSERT INTO toolpurchasedata (tool_type, daily_charge, weekday_charge, weekend_charge, holiday_charge)
VALUES
  ('Chainsaw', 1.49, true, false, true),
  ('Ladder', 1.99, true, true, false),
  ('Jackhammer', 2.99, true, false, false);