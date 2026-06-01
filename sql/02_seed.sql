USE coffeeshop_gui;

INSERT INTO users (nama, username, password_hash, role, status) VALUES
('Admin Utama', 'admin', 'hash_admin', 'ADMIN', TRUE),
('Kasir 1', 'kasir1', 'hash_kasir1', 'KASIR', TRUE),
('Kasir 2', 'kasir2', 'hash_kasir2', 'KASIR', TRUE),
('Gudang 1', 'gudang1', 'hash_gudang1', 'GUDANG', TRUE),
('Manager 1', 'manager1', 'hash_manager1', 'MANAGER', TRUE),
('Kasir 3', 'kasir3', 'hash_kasir3', 'KASIR', TRUE),
('Gudang 2', 'gudang2', 'hash_gudang2', 'GUDANG', TRUE),
('Manager 2', 'manager2', 'hash_manager2', 'MANAGER', TRUE),
('Kasir 4', 'kasir4', 'hash_kasir4', 'KASIR', TRUE),
('Admin 2', 'admin2', 'hash_admin2', 'ADMIN', TRUE);

INSERT INTO customers (nama, no_hp, email, alamat) VALUES
('Andi Pratama', '081200000001', 'andi@mail.com', 'Jl. Merdeka 1'),
('Budi Santoso', '081200000002', 'budi@mail.com', 'Jl. Merdeka 2'),
('Citra Lestari', '081200000003', 'citra@mail.com', 'Jl. Merdeka 3'),
('Dewi Maharani', '081200000004', 'dewi@mail.com', 'Jl. Merdeka 4'),
('Eko Nugroho', '081200000005', 'eko@mail.com', 'Jl. Merdeka 5'),
('Fajar Akbar', '081200000006', 'fajar@mail.com', 'Jl. Merdeka 6'),
('Gita Putri', '081200000007', 'gita@mail.com', 'Jl. Merdeka 7'),
('Hendra Saputra', '081200000008', 'hendra@mail.com', 'Jl. Merdeka 8'),
('Intan Permata', '081200000009', 'intan@mail.com', 'Jl. Merdeka 9'),
('Joko Susilo', '081200000010', 'joko@mail.com', 'Jl. Merdeka 10');

INSERT INTO suppliers (nama_supplier, no_hp, alamat) VALUES
('PT Biji Nusantara', '082100000001', 'Bandung'),
('PT Susu Murni', '082100000002', 'Bogor'),
('PT Gula Sejahtera', '082100000003', 'Cirebon'),
('PT Roti Prima', '082100000004', 'Depok'),
('PT Snack Indo', '082100000005', 'Bekasi'),
('PT Kemasan Jaya', '082100000006', 'Jakarta'),
('PT Air Segar', '082100000007', 'Tangerang'),
('PT Teh Harum', '082100000008', 'Sukabumi'),
('PT Coklat Makmur', '082100000009', 'Semarang'),
('PT Peralatan Cafe', '082100000010', 'Surabaya');

INSERT INTO categories (nama_category, deskripsi) VALUES
('Coffee', 'Minuman kopi panas dan dingin'),
('Non Coffee', 'Minuman non kopi'),
('Tea Series', 'Minuman berbasis teh'),
('Signature', 'Menu andalan'),
('Pastry', 'Croissant, muffin, dan sejenisnya'),
('Snack', 'Cemilan ringan'),
('Heavy Meal', 'Makanan berat'),
('Bottle Drink', 'Minuman kemasan botol'),
('Add On', 'Topping dan tambahan'),
('Merchandise', 'Produk non makanan');

INSERT INTO products (id_category, nama_produk, harga, stok, status) VALUES
(1, 'Espresso', 18000, 120, TRUE),
(1, 'Americano', 22000, 100, TRUE),
(1, 'Cappuccino', 28000, 90, TRUE),
(1, 'Cafe Latte', 30000, 80, TRUE),
(2, 'Chocolate Milk', 26000, 85, TRUE),
(3, 'Lemon Tea', 20000, 95, TRUE),
(4, 'Caramel Macchiato', 34000, 75, TRUE),
(5, 'Butter Croissant', 24000, 60, TRUE),
(6, 'French Fries', 25000, 70, TRUE),
(7, 'Chicken Rice Bowl', 38000, 55, TRUE);

INSERT INTO sales (tanggal, id_user, id_customer, subtotal, diskon, pajak, total) VALUES
('2026-05-01 09:10:00', 2, 1, 18000, 0, 1980, 19980),
('2026-05-02 10:15:00', 2, 2, 22000, 0, 2420, 24420),
('2026-05-03 11:20:00', 3, 3, 28000, 2000, 2860, 28860),
('2026-05-04 12:25:00', 3, 4, 30000, 0, 3300, 33300),
('2026-05-05 13:30:00', 6, 5, 26000, 1000, 2750, 27750),
('2026-05-06 14:35:00', 6, 6, 20000, 0, 2200, 22200),
('2026-05-07 15:40:00', 2, 7, 34000, 2000, 3520, 35520),
('2026-05-08 16:45:00', 3, 8, 24000, 0, 2640, 26640),
('2026-05-09 17:50:00', 2, 9, 25000, 0, 2750, 27750),
('2026-05-10 18:55:00', 6, 10, 38000, 3000, 3850, 38850);

INSERT INTO sales_detail (id_sale, id_product, qty, harga, subtotal_item) VALUES
(1, 1, 1, 18000, 18000),
(2, 2, 1, 22000, 22000),
(3, 3, 1, 28000, 28000),
(4, 4, 1, 30000, 30000),
(5, 5, 1, 26000, 26000),
(6, 6, 1, 20000, 20000),
(7, 7, 1, 34000, 34000),
(8, 8, 1, 24000, 24000),
(9, 9, 1, 25000, 25000),
(10, 10, 1, 38000, 38000);

INSERT INTO purchases (tanggal, id_user, id_supplier, total) VALUES
('2026-04-20 08:00:00', 4, 1, 500000),
('2026-04-21 08:30:00', 4, 2, 420000),
('2026-04-22 09:00:00', 7, 3, 360000),
('2026-04-23 09:30:00', 7, 4, 310000),
('2026-04-24 10:00:00', 4, 5, 280000),
('2026-04-25 10:30:00', 7, 6, 260000),
('2026-04-26 11:00:00', 4, 7, 240000),
('2026-04-27 11:30:00', 7, 8, 230000),
('2026-04-28 12:00:00', 4, 9, 220000),
('2026-04-29 12:30:00', 7, 10, 210000);

INSERT INTO purchase_detail (id_purchase, id_product, qty, harga_beli, subtotal_item) VALUES
(1, 1, 20, 15000, 300000),
(2, 2, 20, 17000, 340000),
(3, 3, 15, 21000, 315000),
(4, 4, 12, 23000, 276000),
(5, 5, 10, 20000, 200000),
(6, 6, 10, 16000, 160000),
(7, 7, 8, 27000, 216000),
(8, 8, 10, 18000, 180000),
(9, 9, 10, 19000, 190000),
(10, 10, 8, 30000, 240000);
