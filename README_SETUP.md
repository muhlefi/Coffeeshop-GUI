# Setup dan Menjalankan Coffeeshop GUI (Tanpa NetBeans)

## 1) Prasyarat
- JDK 17 terpasang penuh (`JAVA_HOME` harus mengarah ke folder JDK, bukan shortcut `javapath`)
- Maven 3.9+
- MySQL 8+

## 2) Setup Database
Jalankan berurutan:
1. `sql/01_schema.sql`
2. `sql/02_seed.sql`

Contoh MySQL CLI:
```bash
mysql -u root -p < sql/01_schema.sql
mysql -u root -p < sql/02_seed.sql
```

## 3) Konfigurasi Koneksi DB
Edit file:
- `src/main/resources/db.properties`

Default:
```properties
db.url=jdbc:mysql://localhost:3306/coffeeshop_gui
db.username=root
db.password=
```

## 4) Run Aplikasi
```bash
mvn clean compile
mvn exec:java
```

Main class:
- `com.coffeeshop.app.MainApp`

## 5) Modul yang Sudah Tersedia
- Dashboard:
  - test koneksi database
- Master Produk:
  - create, read, update, delete, search
- Master Customer:
  - create, read, update, delete, search
- Transaksi Penjualan:
  - tambah item, hitung subtotal/diskon/pajak/total
  - simpan header + detail
  - update stok berkurang otomatis
  - riwayat 20 transaksi terbaru
- Transaksi Pembelian:
  - tambah item pembelian dan hitung total
  - simpan header + detail
  - update stok bertambah otomatis
  - riwayat 20 transaksi terbaru
- Report:
  - penjualan bulanan per tahun
  - top produk terlaris per periode (custom date range)
  - pergerakan stok (masuk, keluar, stok saat ini)

## 6) Exception yang Sudah Diterapkan
- User-defined:
  - `InputKosongException`
  - `DataTidakValidException`
- Default:
  - `NumberFormatException`
  - `SQLException`
