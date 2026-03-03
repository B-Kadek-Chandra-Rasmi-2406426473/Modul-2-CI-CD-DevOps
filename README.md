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



