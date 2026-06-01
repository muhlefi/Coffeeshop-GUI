# Rencana Eksekusi Tugas Besar PBO 2025/2026 (Target Nilai Maksimal)

## 1) Ringkasan Ketentuan Wajib dari Dosen
- Tools: NetBeans `12.6+`
- DBMS: `MySQL`
- Minimal `6 tabel` dengan `>=10 data sampel` per tabel
- Minimal `2 pengelolaan data master` (`CRUD + search`)
- Minimal `2 jenis transaksi`
- Minimal `1 report`
- Wajib menerapkan konsep PBO:
  - Encapsulation
  - Inheritance
  - Polymorphism
  - Abstraction
  - Class & Object
  - Constructor & Destructor (di Java: constructor + mekanisme cleanup/resource close)
  - Access modifier (`public/private/protected`)
  - Interface dan/atau Abstract Class
- Wajib ada relasi entity + relasi kelas yang benar
- Error handling:
  - `User defined exception` untuk input kosong
  - `Default exception` (contoh input harus number)
- IMK: GUI rapi, komponen bervariasi, styling baik, mudah dipakai
- Output dokumen: ERD, Class Diagram (UML), penjelasan penerapan PBO

## 2) Strategi Nilai Maksimal
- Penuhi semua minimal requirement, lalu tambah nilai pada:
  - Konsistensi arsitektur (`MVC + DAO + Service`)
  - Kualitas UX (validasi realtime, feedback jelas, shortcut keyboard, tabel bisa sort/filter)
  - Kualitas kode (package rapi, naming konsisten, reusable component)
  - Kualitas data (constraint DB lengkap, foreign key, index, data seed realistis)
  - Kualitas report (lebih dari 1 laporan + grafik sederhana)
  - Kualitas presentasi (demo alur bisnis lengkap, bukan cuma pindah form)

## 3) Topik yang Direkomendasikan
- Topik: **Sistem Manajemen Coffee Shop**
- Catatan: pastikan topik didaftarkan dan disetujui di `Ms Teams` agar tidak duplikat antar kelompok.

## 4) Scope Fitur (Di atas Batas Minimal)
## 4.1 Master Data (>=2, direkomendasikan 5)
- `Produk` (CRUD + search + filter kategori)
- `Kategori Produk` (CRUD + search)
- `Pelanggan` (CRUD + search)
- `Pegawai/User` (CRUD + search + role)
- `Supplier` (CRUD + search)

## 4.2 Transaksi (>=2, direkomendasikan 3)
- `Transaksi Penjualan`:
  - tambah item ke keranjang
  - hitung subtotal, diskon, pajak, total
  - simpan header + detail
  - cetak struk sederhana
- `Transaksi Pembelian/Restock`:
  - input pembelian dari supplier
  - update stok otomatis
- `Retur/Adjustment Stok` (nilai tambah):
  - koreksi stok rusak/hilang

## 4.3 Report (>=1, direkomendasikan 3)
- Laporan penjualan bulanan per periode
- Laporan produk terlaris
- Laporan pergerakan stok (masuk/keluar/sisa)

## 5) Rancangan Database (Rekomendasi 8 Tabel)
- `users` (id_user, nama, username, password_hash, role, status)
- `customers` (id_customer, nama, no_hp, email, alamat)
- `suppliers` (id_supplier, nama_supplier, no_hp, alamat)
- `categories` (id_category, nama_category, deskripsi)
- `products` (id_product, id_category, nama_produk, harga, stok, status)
- `sales` (id_sale, tanggal, id_user, id_customer, subtotal, diskon, pajak, total)
- `sales_detail` (id_sale_detail, id_sale, id_product, qty, harga, subtotal_item)
- `purchases` (id_purchase, tanggal, id_user, id_supplier, total)
- `purchase_detail` (id_purchase_detail, id_purchase, id_product, qty, harga_beli, subtotal_item)

Catatan:
- Jika ingin tetap 8 tabel, gabungkan `purchase` dan `purchase_detail` menjadi satu desain sederhana.  
- Tetap disarankan pisah header-detail agar desain relasional lebih benar.

## 6) Mapping Konsep PBO ke Implementasi Kode
- `Encapsulation`:
  - field private pada semua model/entity
  - akses via getter/setter tervalidasi
- `Inheritance`:
  - `BaseEntity` diturunkan oleh `Product`, `Customer`, `Supplier`, `User`
- `Polymorphism`:
  - interface `ReportGenerator` diimplementasi `MonthlySalesReport`, `TopProductReport`
- `Abstraction`:
  - abstract class `AbstractTransactionService`
- `Class & Object`:
  - semua entitas direpresentasi class, dipakai sebagai object di service/controller
- `Constructor`:
  - constructor default dan parameterized pada model
- `Destructor ekuivalen Java`:
  - cleanup resource pada DAO (`try-with-resources`) dan close koneksi
- `Access Modifier`:
  - `private` untuk field, `public` untuk API class, `protected` untuk class turunan
- `Interface/Abstract`:
  - DAO interface + implementasi JDBC

## 7) Arsitektur Project Java (NetBeans)
Struktur package:
- `config` (DBConnection, AppConfig)
- `model` (entity/class domain)
- `repository` (DAO interface)
- `repository.impl` (JDBC implementation)
- `service` (business rules, validasi)
- `exception` (custom exception)
- `ui` (JFrame/JDialog/Form)
- `util` (helper, formatter, validator)
- `report` (generator laporan)

Pola alur:
- UI -> Service -> Repository/DAO -> MySQL

## 8) Strategi Exception (Wajib + Nilai Tambah)
- Custom:
  - `InputKosongException` (semua field wajib tidak boleh kosong)
  - `DataTidakValidException` (format salah, nilai negatif, dll)
- Default exception:
  - `NumberFormatException` untuk parse angka
  - `SQLException` untuk error DB
- Standar handling:
  - tangkap per layer
  - tampilkan pesan user-friendly di GUI
  - log error teknis untuk debug

## 9) Standar GUI dan IMK (Nilai Tinggi)
- Konsistensi tema warna, font, ukuran tombol, margin
- Komponen bervariasi: `JTable`, `JTabbedPane`, `JComboBox`, `JDateChooser`/date field, `JDialog`
- Validasi input realtime (bukan hanya saat tombol simpan)
- Navigasi cepat:
  - Enter untuk pindah field
  - Esc untuk batal
  - Ctrl+S untuk simpan (opsional)
- Feedback jelas:
  - sukses/gagal dengan dialog informatif
  - konfirmasi sebelum hapus
- Tabel:
  - sorting, search, filter
  - auto refresh setelah transaksi

## 10) Checklist Implementasi Teknis
- Setup project `X_Y_Topik` di NetBeans
- Setup koneksi MySQL + schema `X_Y_Topik`
- Buat seluruh tabel + FK + index
- Insert data dummy `>=10` record per tabel
- Implement master data (minimal 2, target 5)
- Implement transaksi (minimal 2, target 3)
- Implement report (minimal 1, target 3)
- Implement exception handling sesuai syarat
- Uji alur end-to-end
- Hardening UI dan validasi

## 11) Struktur Dokumen Laporan `.docx`
- Halaman judul (kelas, kelompok, topik, anggota)
- Bab 1 Pendahuluan:
  - latar belakang
  - tujuan aplikasi
  - batasan masalah
- Bab 2 Analisis dan Perancangan:
  - kebutuhan fungsional
  - use case ringkas
  - ERD
  - class diagram UML
- Bab 3 Implementasi:
  - struktur package
  - penjelasan modul fitur
  - cuplikan kode penting penerapan PBO
- Bab 4 Pengujian:
  - skenario uji master data
  - skenario uji transaksi
  - skenario uji report
  - skenario uji exception
- Bab 5 Kesimpulan
- Lampiran:
  - screenshot GUI
  - SQL schema + sample data

## 12) Pembagian Kerja Tim (4 Orang)
- Anggota 1: DB design + SQL + seed data
- Anggota 2: Master data UI + CRUD module
- Anggota 3: Transaksi + business logic
- Anggota 4: Report + dokumentasi + integrasi final

## 13) Timeline Eksekusi (Disarankan)
- Minggu 1:
  - finalisasi topik, requirement, ERD draft
- Minggu 2:
  - implement schema DB + model + DAO dasar
- Minggu 3:
  - implement master data + validasi + exception
- Minggu 4:
  - implement transaksi + update stok + testing
- Minggu 5:
  - implement report + polishing GUI/IMK
- Minggu 6:
  - final testing, dokumentasi, simulasi presentasi, packing ZIP

## 13A) Fase Kerja Detail (Agar Maksimal dan Tetap Ringan)
## Fase 0 - Kickoff & Guardrail
- Output:
  - scope final (fitur wajib + fitur nilai tambah)
  - standar coding (naming, package, error handling)
- Quality gate:
  - semua anggota paham requirement wajib dari dosen
- Batas sesi:
  - maksimal 1 sesi hanya untuk desain, tidak coding dulu

## Fase 1 - Fondasi Teknis
- Output:
  - project Java (`Maven/Gradle`) siap run
  - koneksi MySQL stabil
  - struktur package final (`model/service/repository/ui/exception`)
- Quality gate:
  - aplikasi bisa start tanpa error
  - koneksi DB lulus test CRUD sederhana

## Fase 2 - Database & Data Dummy
- Output:
  - schema final (>=6 tabel, relasi FK benar)
  - seed data >=10 data/tabel
- Quality gate:
  - semua FK valid
  - query join utama berjalan

## Fase 3 - Modul Master Data
- Output:
  - minimal 2 master CRUD+search selesai (target 5)
  - validasi input dan custom exception aktif
- Quality gate:
  - test create/update/delete/search tiap master lulus
  - UI tabel auto refresh

## Fase 4 - Modul Transaksi
- Output:
  - minimal 2 transaksi selesai (target 3)
  - logika hitung total + update stok berjalan
- Quality gate:
  - data header-detail konsisten
  - rollback/handling error SQL aman

## Fase 5 - Report & IMK Polishing
- Output:
  - minimal 1 report selesai (target 3)
  - styling GUI, alur UX, navigasi keyboard rapi
- Quality gate:
  - report sesuai periode/filter
  - tampilan konsisten antar form

## Fase 6 - Dokumentasi & Presentasi
- Output:
  - `.docx` lengkap (ERD, UML, penerapan PBO)
  - skrip demo presentasi 8-12 menit
- Quality gate:
  - semua konsep PBO punya contoh class nyata
  - demo jalan dari login sampai report

## Fase 7 - Final QA & Packaging
- Output:
  - final bugfix
  - file pengumpulan sesuai format nama
- Quality gate:
  - checklist pengumpulan 100% terpenuhi
  - zip final lolos uji ekstrak dan run

## 13B) Protokol Anti-Overheat (Eksekusi Bertahap)
- Kerja per batch kecil:
  - 1 batch = 1 modul kecil + 1 validasi (contoh: `Customer CRUD` saja)
- Batas konteks:
  - jangan kerjakan >2 modul besar dalam 1 sesi
- Freeze rule:
  - setelah tiap fase, freeze dulu dan lakukan smoke test
- Definisi selesai per fase:
  - kode jalan, diuji, dan dicatat di checklist; bukan sekadar “sudah dibuat”
- Dokumentasi paralel:
  - update laporan tiap fase agar tidak menumpuk di akhir
- Recovery cepat:
  - simpan snapshot Git tiap fase (`phase-1`, `phase-2`, dst)

## 14) Checklist Final Pengumpulan (Wajib Sesuai Format)
- Project NetBeans: `X_Y_Topik`
- Database export `.sql`: `X_Y_Topik`
- Dokumen `.docx`: `X_Y_Topik.docx`
- Identitas anggota `.txt`: `X_Y_Topik.txt`
- Library tambahan (jika ada)
- ZIP akhir: `X_Y_Topik_TB.zip`

## 15) Standar Presentasi Pertemuan 14
- Demo login -> master data -> transaksi -> report (alur utuh)
- Tunjukkan relasi DB dan class diagram singkat
- Jelaskan setiap konsep PBO dengan contoh class nyata
- Tunjukkan penanganan error:
  - input kosong (custom exception)
  - input angka invalid (default exception)
- Siapkan data demo realistis agar tidak blank saat presentasi

---

Dokumen ini adalah blueprint kerja implementasi + pelaporan.  
Jika topik diganti selain Coffee Shop, struktur yang sama tetap dapat dipakai dengan penyesuaian nama entitas dan transaksi.

## Status Eksekusi (2026-06-01)
- Fase 1:
  - selesai: inisialisasi project Java Swing + Maven, struktur package, koneksi MySQL
  - selesai: kerangka OOP (`inheritance`, `interface`, `abstract class`, custom exception)
- Fase 2 (awal):
  - selesai: schema SQL relasional (9 tabel) + seed data 10 row per tabel
- Fase 3 (parsial):
  - selesai: 2 master data (`Produk`, `Customer`) dengan `CRUD + search`
- Fase 4:
  - selesai: 2 jenis transaksi (`Penjualan`, `Pembelian`) dengan simpan header-detail
  - selesai: update stok otomatis (penjualan mengurangi, pembelian menambah)
  - selesai: validasi transaksi + rollback saat gagal
- Fase 5:
  - selesai: report `penjualan bulanan`, `produk terlaris`, `pergerakan stok`
  - selesai: peningkatan IMK transaksi (dropdown lookup user/customer/supplier/produk, format rupiah)
- Fase 6:
  - selesai: draft laporan `.docx` terstruktur lengkap (Bab 1-5 + lampiran)
  - catatan QA dokumen: render otomatis PNG belum bisa dijalankan karena `soffice/LibreOffice` tidak tersedia di environment saat ini.
- Catatan:
  - compile berhasil setelah `JAVA_HOME` diarahkan ke `D:\LefiArchive\Java`.
  - perlu set permanen `JAVA_HOME` di environment Windows agar perintah Maven tidak perlu diset ulang tiap sesi.
