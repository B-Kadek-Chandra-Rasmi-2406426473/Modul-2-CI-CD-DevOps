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


<details>
  <summary>Module 3</summary>
1) Explain what principles you apply to your project!

  Saya telah menerapkan kelima prinsip SOLID, yaitu:
  1.** Single Responsibility Principle (SRP)**: Saya memisahkan `CarController` dan `ProductController` menjadi dua class yang berbeda pada dua file yang berbeda pula. Sebelumnya, CarController berada di dalam file yang sama dan menangani tugas yang tumpang tindih. Kini, masing-masing controller hanya memiliki satu tanggung jawab (single responsibility). Untuk kelas-kelas lainnya juga sudah mengimplementasikan SRP dimana tiap tanggung jawab terpisah dengan baik, seperti kelas pada Controller hanya menangani interaksi HTTP, Service mengelola logika bisnis, dan Repository mengelola akses data.
  
  2.** Open/Closed Principle (OCP)**: Memodifikasi method update pada `CarRepository` dan `ProductRepository`. Daripada melakukan setter pada setiap atribut secara manual (seperti car.setCarName(...)), saya mengubah logikanya untuk langsung mengganti keseluruhan objek lama dengan objek baru. Tanpa perubahan tersebut, tiap kali entitas Produk atau Car bertambah atributnya, kita terpaksa harus memodifikasi isi dari `ProductRepository.java` dan hal ini melanggar OCP karena Tidak Tertutup terhadap Modifikasi.  
  
  3. **Liskov Substitution Principle (LSP)**: Saya menghapus relasi inheritance (`extends ProductController`) pada class `CarController`. Dalam konteks arsitektur MVC Spring Boot, sebuah `CarController` tidak memiliki perilaku yang sama dengan `ProductController`. Jika extends dipertahankan, `CarController` akan mewarisi method dan anotasi routing (seperti @GetMapping) dari induknya, yang dapat menyebabkan Ambiguous Mapping Error atau perilaku routing halaman yang tidak terduga. Dengan menghapus extends ini, kedua controller dapat beroperasi sesuai fungsinya masing-masing tanpa saling merusak fungsionalitas.
  
  5.**Interface Segregation Principle (ISP)**: memiliki interface CarService dan ProductService. Dengan demikian, setiap controller hanya bergantung pada antarmuka dan method yang benar-benar mereka butuhkan saja, tidak dipaksa mengimplementasikan method dari entitas lain.
  
  6. **Dependency Inversion Principle (DIP)**: Mengubah cara injeksi dependensi dari Field Injection (@Autowired langsung pada variabel) menjadi Constructor Injection pada semua Controller dan Service. Kelas level atas sekarang bergantung pada abstraksi (interface `CarService` atau `ProductService`), bukan implementasi konkretnya (`CarServiceImpl` ataupun `ProductServiceImpl`).
  
2) Explain the advantages of applying SOLID principles to your project with examples.

Menerapkan prinsip SOLID membuat kode menjadi lebih modular, mudah dipelihara/maintainable, dan mudah dikembangkan/scalable. Berdasarkan perubahan yang saya terapkan:
  1. Method update di repository sekarang langsung menimpa objek lama dengan objek baru, maka misalnya jika terdapat penambahan attribute baru pada model Car (misalnya engineType atau price), tidak perlu lagi memodifikasi baris kode di dalam CarRepository. Kode menjadi tertutup untuk modifikasi namun terbuka untuk ekstensi.
  2. Mempermudah Pengujian / Unit Testing. Dengan beralih menggunakan Constructor Injection dan bergantung pada abstraksi (Interface), dapat dengan mudah menginject mock object saat melakukan unit testing.
  3. Mengurangi Risiko Bug Berantai, seperti pada pemisahan CarController dan ProductController ke filenya masing-masing dan memutuskan hubungan extends di antara keduanya. Perubahan apa pun pada fitur produk tidak akan secara tidak sengaja merusak fungsionalitas fitur car.
  
4) Explain the disadvantages of not applying SOLID principles to your project with examples.

Jika tidak menerpakan prinsip SOLID, code yang ditulis akan sangat kotor dan sulit untuk dimaintain. Contohnya:
   1. Jika CarController masih melakukan extends ProductController, kedua kelas ini akan `tightly coupled`. Jika ada developer lain yang mengubah routing atau logika dependensi di ProductController, hal tersebut bisa menyebabkan halaman mobil (Car) menjadi error tanpa peringatan yang jelas.
   2. Sulit melakukan perubahan. Jika mempertahankan logika update yang lama (menge-set data satu per satu seperti product.setProductName(...) dan product.setProductQuantity(...)), setiap kali ada penambahan atribut baru, developer harus selalu ingat untuk mengedit method update di Repository. Jika terlupa satu atribut saja, akan terjadi bug di mana data tidak tersimpan dengan baik. Ini akan sangat menyulitkan dan memakan waktu lama.
   3. Menyulitkan Inisialisasi dan Testing. Penggunaan Field Injection (@Autowired pada variabel) menyembunyikan dependensi asli dari suatu kelas. Hal ini membuat pembuatan objek untuk keperluan testing menjadi sangat merepotkan, serta menghasilkan code smell yang terus dideteksi oleh tools analisis, seperti SonarCloud.

</details>

<details>
  <summary>Module 4</summary>
1. Selama mengerjakan latihan ini, saya mencoba menerapkan alur Test-Driven Development (TDD) melalui siklus Red-Green-Refactor. Secara keseluruhan, pendekatan TDD  ini cukup berguna karena menuntut untuk mendefinisikan batasan dan ekspektasi dari sebuah fitur sebelum mulai menulis kode implementasinya. Hal ini membantu menstrukturkan kode dengan lebih baik dan mendeteksi kesalahan logika sejak awal. Untuk ke depannya, saat membuat tes baru, saya perlu merancang skenario pengujian dan batas-batas mocking dengan lebih teliti sejak fase awal. Hal ini penting agar tidak menghabiskan banyak waktu hanya untuk merevisi tes yang sudah dibuat agar sesuai dengan perubahan pada kode implementasi.


2. Test yang dibuat sudah mengikuti prinsip F.I.R.S.T. Memenuhi aspek Fast karena dapat dieksekusi dengan sangat cepat akibat cakupan pengujiannya yang kecil dan spesifik. Pada prinsip Isolated, pengujian telah berdiri sendiri tanpa bergantung pada urutan eksekusi karena inisialisasi ulang data sebelum tes dimulai, serta penggunaan teknik mocking yang dapat mengisolasi testing. Prinsip Repeatable juga tercapai karena pengujian sama sekali tidak bergantung pada infrastruktur eksternal, sehingga hasilnya akan selalu konsisten di lingkungan mana pun. Lalu, Self-validating terimplementasi melalui fungsi Assertions JUnit yang otomatis memberikan hasil pengecekan apakah pass/failed. Prinsip Timely juga diterapkan melalui siklus TDD, di mana kode pengujian dirancang dan ditulis lebih awal sebagai panduan sebelum logika implementasi fitur benar-benar dikerjakan.
</details>


