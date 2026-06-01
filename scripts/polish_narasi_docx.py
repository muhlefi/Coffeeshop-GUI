from docx import Document

DOC_PATH = "LAPORAN_TB_PBO_COFFEESHOP_2025_2026.docx"
OUTPUT_PATH = "LAPORAN_TB_PBO_COFFEESHOP_2025_2026_REVISI_NARASI.docx"


REPLACEMENTS = {
    "Coffee shop modern membutuhkan sistem yang mampu mengelola data master, transaksi, dan pelaporan secara cepat serta konsisten. Proses manual berisiko menimbulkan kesalahan input, keterlambatan rekap, dan inkonsistensi stok.":
        "Industri coffee shop modern menuntut pengelolaan operasional yang cepat, akurat, dan terintegrasi. Pengolahan data secara manual berpotensi menimbulkan kesalahan pencatatan, keterlambatan rekapitulasi, serta ketidaksesuaian data stok yang berdampak pada kualitas pengambilan keputusan manajerial.",
    "Aplikasi GUI Java ini dirancang untuk membantu pengelolaan operasional harian melalui fitur master data, transaksi penjualan, transaksi pembelian/restock, dan laporan periodik.":
        "Berdasarkan kebutuhan tersebut, penelitian terapan ini mengembangkan aplikasi desktop berbasis Java GUI dengan dukungan basis data MySQL. Sistem mencakup pengelolaan data master, transaksi penjualan, transaksi pembelian/restock, serta penyusunan laporan periodik sebagai dasar evaluasi kinerja operasional.",
    "Bagaimana membangun aplikasi desktop Java GUI untuk operasional coffee shop berbasis MySQL?":
        "Bagaimana merancang dan mengimplementasikan aplikasi desktop berbasis Java GUI dan MySQL untuk mendukung proses operasional coffee shop secara terstruktur?",
    "Bagaimana menerapkan prinsip PBO secara utuh pada implementasi kode?":
        "Bagaimana prinsip-prinsip Pemrograman Berorientasi Objek diterapkan secara konsisten pada arsitektur dan implementasi kode program?",
    "Bagaimana menyediakan data report yang akurat untuk monitoring bisnis?":
        "Bagaimana sistem menghasilkan laporan yang akurat dan relevan untuk kebutuhan pemantauan serta evaluasi bisnis?",
    "Membangun aplikasi GUI Java yang user-friendly dan stabil.":
        "Membangun aplikasi desktop berbasis Java GUI yang stabil, mudah digunakan, dan sesuai kebutuhan operasional coffee shop.",
    "Menerapkan konsep PBO: encapsulation, inheritance, polymorphism, abstraction, interface/abstract class.":
        "Menerapkan konsep PBO meliputi encapsulation, inheritance, polymorphism, abstraction, serta penggunaan interface/abstract class pada implementasi sistem.",
    "Menyediakan fitur CRUD master data, transaksi, dan report sesuai ketentuan tugas.":
        "Menyediakan fitur pengelolaan data master, transaksi, dan pelaporan sesuai ketentuan Tugas Besar PBO Semester Genap 2025/2026.",
    "Aplikasi berbasis desktop Java Swing dengan database MySQL.":
        "Ruang lingkup pengembangan dibatasi pada aplikasi desktop Java Swing dengan penyimpanan data menggunakan MySQL.",
    "Fokus pada modul master data, transaksi penjualan, transaksi pembelian, dan report.":
        "Fokus fungsional sistem meliputi modul master data, transaksi penjualan, transaksi pembelian, dan laporan operasional.",
    "Belum mencakup integrasi pembayaran online dan multi-cabang.":
        "Pengembangan saat ini belum mencakup integrasi pembayaran daring, sinkronisasi multi-cabang, dan integrasi dengan perangkat eksternal.",
    "Pengelolaan master produk (create, read, update, delete, search).":
        "Sistem menyediakan pengelolaan master produk mencakup create, read, update, delete, dan search.",
    "Pengelolaan master customer (create, read, update, delete, search).":
        "Sistem menyediakan pengelolaan master customer mencakup create, read, update, delete, dan search.",
    "Transaksi penjualan (header-detail, hitung diskon/pajak/total, update stok keluar).":
        "Sistem mendukung transaksi penjualan model header-detail, termasuk perhitungan diskon, pajak, total transaksi, serta pembaruan stok keluar secara otomatis.",
    "Transaksi pembelian (header-detail, update stok masuk).":
        "Sistem mendukung transaksi pembelian model header-detail dengan pembaruan stok masuk secara otomatis.",
    "Laporan penjualan bulanan, produk terlaris, dan pergerakan stok.":
        "Sistem menyediakan laporan penjualan bulanan, laporan produk terlaris, dan laporan pergerakan stok sebagai bahan evaluasi.",
    "Tempelkan gambar ERD final pada bagian ini. ERD wajib menunjukkan relasi tabel: users, customers, suppliers, categories, products, sales, sales_detail, purchases, purchase_detail.":
        "Bagian ini memuat ERD final sebagai representasi rancangan basis data. Diagram harus menunjukkan entitas utama beserta relasi antar tabel, meliputi users, customers, suppliers, categories, products, sales, sales_detail, purchases, dan purchase_detail.",
    "Tempelkan gambar class diagram final pada bagian ini. Pastikan terlihat relasi inheritance, interface implementation, dan layer UI-Service-Repository.":
        "Bagian ini memuat class diagram final yang merepresentasikan struktur objek pada sistem. Diagram perlu menampilkan relasi inheritance, implementasi interface, serta keterkaitan antarlapis UI, service, dan repository.",
    "InputKosongException: dipakai untuk validasi field wajib.":
        "InputKosongException digunakan untuk memvalidasi field wajib agar tidak bernilai kosong pada proses input data.",
    "DataTidakValidException: dipakai untuk validasi nilai data/ID/logic bisnis.":
        "DataTidakValidException digunakan untuk memvalidasi kesesuaian nilai data, identitas entitas, dan aturan logika bisnis.",
    "NumberFormatException: menangani input angka yang tidak valid.":
        "NumberFormatException dimanfaatkan untuk menangani kesalahan konversi data numerik dari input pengguna.",
    "SQLException: menangani error query/koneksi database.":
        "SQLException ditangani untuk mengelola kegagalan proses query maupun koneksi basis data secara terkontrol.",
    "Pengujian dilakukan dengan metode uji fungsional berbasis skenario.":
        "Pengujian sistem dilakukan menggunakan pendekatan uji fungsional berbasis skenario untuk memastikan seluruh fitur berjalan sesuai kebutuhan pengguna.",
    "Catatan: isi status akhir berdasarkan hasil pengujian aktual saat demo final.":
        "Status akhir pada setiap skenario pengujian diisi berdasarkan hasil uji aktual pada tahap demonstrasi dan validasi akhir sistem.",
    "Aplikasi berhasil memenuhi syarat tugas besar PBO dengan penerapan konsep OOP yang relevan.":
        "Aplikasi yang dikembangkan telah memenuhi ketentuan Tugas Besar PBO dan menunjukkan penerapan konsep OOP secara relevan pada struktur sistem.",
    "Fitur master data, transaksi, dan report telah terintegrasi dengan database MySQL.":
        "Integrasi modul master data, transaksi, dan pelaporan dengan basis data MySQL telah berjalan baik sehingga mendukung konsistensi data operasional.",
    "Exception handling dan validasi input meningkatkan stabilitas aplikasi.":
        "Penerapan exception handling dan validasi input memberikan kontribusi nyata terhadap peningkatan keandalan serta stabilitas aplikasi.",
    "Menambahkan autentikasi login berbasis role yang lebih ketat.":
        "Pengembangan lanjutan dapat mencakup autentikasi berbasis role yang lebih ketat dan aman.",
    "Menambahkan ekspor report ke PDF/Excel.":
        "Sistem dapat ditingkatkan melalui fitur ekspor laporan ke format PDF maupun Excel.",
    "Menambahkan dashboard statistik grafis untuk analisis penjualan.":
        "Penambahan dashboard visual berbasis grafik direkomendasikan untuk memperkuat analisis tren penjualan."
}


def main() -> None:
    doc = Document(DOC_PATH)
    replaced = 0

    for p in doc.paragraphs:
        old_text = p.text.strip()
        if old_text in REPLACEMENTS:
            p.text = REPLACEMENTS[old_text]
            replaced += 1

    doc.save(OUTPUT_PATH)
    print(f"Updated: {OUTPUT_PATH}")
    print(f"Paragraphs replaced: {replaced}")


if __name__ == "__main__":
    main()
