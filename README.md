Nama : Kadek Chandra Rasmi 
NPM : 2406426473

<details>
  <summary>Module 1</summary>
<details>
  <summary>Reflection 1</summary>
  
### Implementasi Clean Code Principles
- Meaningful Names: menggunakan nama variabel dan fungsi yang deskriptif sesuai tujuannya, seperti class name `ProductController`, `ProductRepository`, lalu penamaan vaiable seperti `productId`, `productName`, dan lainnya. Hal ini membuat kode mudah dibaca tanpa perlu banyak komentar tambahan.
  - Single Responsibility Principle (SRP): memisahkan tanggung jawab dengan jelas, seperti `ProductController` hanya menangani request HTTP, `ProductService` menangani logika bisnis, dan `ProductRepository` menangani manipulasi data.
  - Lombok: Penggunaan Lombok pada model Product membantu mengurangi bagian seperti getter/setter yang panjang, sehingga kelas model terlihat lebih bersih dan fokus pada data.
  - Pada fitur Edit dan Delete, saya mengubah logika dari mengembalikan null menjadi melempar `NoSuchElementException`. Ini memastikan error terdeteksi lebih cepat dan tidak menyebabkan bug tersembunyi.

### Secure Coding
Penggunaan UUID.randomUUID() untuk menghasilkan ID produk. Ini lebih aman dibandingkan integer increment (1, 2, 3...) karena ID menjadi unik secara global dan sulit ditebak oleh pihak luar (mencegah Insecure Direct Object References / ID Enumeration)

### Improvement yang bisa dilakukan
Input Validation yang lebih ketat untuk menangani berbagai jenis kemungkinan input yang tidak valid. Error handling yang lebih menyeluruh juga diperlukan untuk program ini. 

</details>

<details>
  <summary>Reflection 2</summary>

1. Saya merasa sedikit lebih yakin dengan kode saya setelah melakukan unit test karena fungsionalitasnya berjalan lancar. Menyusun unit test menurut saya cukup menantang karena harus memikirkan segala kemungkinan kondisi (terutama edge cases) yang berpotensi memunculkan bug. 

Menurut saya tidak ada ukuran pasti berapa banyak unit test yang dibutuhkan untuk suatu class. Jumlahnya menyesuaikan dengan kebutuhan untuk memverifikasi semua kemungkinan alur logika. Unit test dapat diukur dengan tingkat coveragenya. Code coverage membantu kita melihat baris kode mana yang sudah dieksekusi oleh tes dan mana yang belum (misalnya, cabang if/else yang terlewat). Namun, 100% Code Coverage tidak menjamin kode bebas bug. Coverage hanya menghitung apakah baris kode dijalankan, bukan apakah logika baris tersebut benar.

2. Jika saya membuat functional test baru dengan menyalin prosedur setup dan variabel instance yang sama persis dari `CreateProductFunctionalTest.java`, hal itu akan menurunkan kualitas kode. Masalah utamanya adalah Code Duplication yang akan menyusahkan untuk dimaintain nantinya. Untuk membuat kode lebih clean, sebaiknya membuat satu `base` class yang berisi semua konfigurasi umum dan nantinya class test lain cukup mengextend base class tersebut sehingga mengurangi duplikasi dan lebih mudah dimaintain jika terjadi perubahan di kemudian hari. 
</details>

</details>


<details>
  <summary>Module 2</summary>

1.List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them. 

Selama mengerjakan latihan dan modul ini, saya menemukan dan memperbaiki beberapa isu terkait fungsionalitas dan kualitas kode (code maintainability), antara lain:
- Maintainability Code Smell, yaitu Penggunaan Field Injection (Dideteksi oleh SonarCloud). SonarCloud mendeteksi penggunaan anotasi @Autowired yang diletakkan langsung pada field variabel (misalnya pada dependensi ProductService) sebagai masalah maintainability. Pendekatan Field Injection ini tidak disarankan karena membuat kelas tersebut menyembunyikan dependensinya dan menjadi sulit untuk diinisialisasi secara independen saat pembuatan unit test.
Strategi Penyelesaian: Saya me-refactor kode tersebut dengan mengubah pendekatannya menjadi Constructor Injection. Saya mengubah field dependensi menjadi private final, lalu membuat constructor untuk kelas tersebut dan menyematkan anotasi @Autowired pada constructor. Hal ini membuat dependensi menjadi wajib diisi saat objek dibuat dan jauh lebih aman serta mudah untuk proses testing.

- Bug Fungsionalitas, berupa inkonsistensi case sensitivity pada template. Aplikasi mengalami Internal Server Error (500) saat di-deploy ke lingkungan berbasis Linux (Render) karena adanya perbedaan huruf kapital antara return string pada Controller (example: "createProduct") dan nama file HTML aslinya ("CreateProduct.html").
Strategi Penyelesaian: Saya menganalisis log error dari server Render, kemudian menyamakan return value pada method di ProductController agar sama persis secara case-sensitive dengan nama file di folder templates. Selain itu, saya juga memperbarui assertion pada ProductControllerTest agar proses otomatisasi build dan tes di pipeline CI/CD dapat kembali berjalan sukses.
 
2. Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons!
Ya, menurut saya implementasi pipeline saat ini sudah memenuhi definisi dari Continuous Integration (CI) dan Continuous Deployment (CD). Pada aspek Continuous Integration, otomatisasi telah berjalan secara konsisten melalui GitHub Actions setiap kali ada push kode baru ke repositori. Workflow CI ini bertugas ganda, yaitu mengeksekusi unit test menggunakan Gradle untuk memverifikasi fungsionalitas, serta melakukan analisis kualitas dan keamanan kode menggunakan SonarCloud secara terpusat. Lalu, untuk aspek Continuous Deployment, repositori GitHub telah dihubungkan langsung dengan platform PaaS (Render). Dengan mengaktifkan konfigurasi Auto-Deploy On Commit, setiap perubahan kode yang terintegrasi ke branch utama (main) akan langsung memicu proses build dan deploy ke server secara otomatis. Rangkaian automasi ini berhasil menghilangkan pekerjaan deploy manual dari developer, sehingga mempercepat siklus rilis dan meminimalisir risiko human error.
</details>
