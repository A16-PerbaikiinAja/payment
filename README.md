**Nama: Ghina Nabila Gunawan**

**NPM: 2206825914**

**Kelas: AdvProg - A**

### 💲Software Architecture Payment Service💲

### Component Diagram Payment Management Service

![Component Diagram Payment Management Service](./img/Component Diagram untuk Payment Management Service.png)

**Aktor (Ungu)**

- **Admin User**: Pengguna dengan hak akses admin yang dapat melakukan operasi CRUD pada payment method
- **Regular User**: Pengguna biasa yang hanya dapat melihat payment method aktif
- **Order Service**: Layanan eksternal yang menggunakan payment method untuk pemrosesan pesanan

**Controller (Hijau)**
- **Payment Method Controller**: Menangani semua HTTP request terkait payment method, dengan endpoint terpisah untuk admin dan pengguna biasa


**Interface (Abu-abu)**
- **PaymentMethodService**: Interface yang mendefinisikan kontrak untuk layanan payment method
- **PaymentMethodRepository**: Interface untuk akses data payment method


**Service (Hijau)**

- **Payment Method Service Impl**: Implementasi dari interface PaymentMethodService yang berisi logika bisnis untuk pengelolaan payment method


**Model (Hijau)**

- **Payment Method**: Model dasar untuk semua jenis payment method
- **Bank Transfer**: Model untuk payment method transfer bank
- **COD**: Model untuk payment method tunai (Cash On Delivery)
- **E-Wallet**: Model untuk payment method e-wallet


**DTO (Kuning)**

- **Payment Method Register DTO**: Data Transfer Object untuk registrasi payment method baru
- **Payment Method DTO**: DTO dasar untuk respons payment method
- **Bank Transfer DTO**, **COD DTO**, **E-Wallet DTO**: DTO spesifik untuk setiap jenis payment method


**Database (Biru)**

- **Payment Method Database**: Menyimpan data payment method dan konfigurasinya


---

### Code Diagram

![Code Diagram](./img/Code Diagram.png)

**Code Diagram 1: Inheritance Hierarchy Payment Method**

Diagram ini menunjukkan hierarki inheritance untuk model payment method. `PaymentMethod` adalah class dasar yang memiliki properti umum seperti id, nama, dan deskripsi. Class turunan seperti `BankTransfer`, `COD`, dan `EWallet` memperluas class dasar dengan properti khusus untuk setiap jenis payment method.

![Inheritance Hierarchy](./img/Diagram Kode 1 Inheritance Hierarchy Payment Method.png)

**Code Diagram 2: DTO Pattern untuk Payment Method**

Diagram ini menggambarkan pola Data Transfer Object (DTO) yang digunakan untuk pertukaran data antara lapisan dalam sistem. `PaymentMethodDTO` adalah class dasar untuk DTO, dengan class turunan seperti `BankTransferDTO`, `CodDTO`, dan `EWalletDTO` untuk jenis payment method spesifik. `PaymentMethodRegisterDTO` digunakan untuk menerima data dari klien saat membuat atau memperbarui payment method.

![DTO Pattern](./img/Diagram Kode 2 DTO Pattern untuk Payment Method.png)

**Code Diagram 3: Controller dan Service Interface**

Diagram ini menunjukkan hubungan antara controller, service interface, service implementation, dan repository. `PaymentMethodController` menangani HTTP request dan memanggil `PaymentMethodService`. `PaymentMethodServiceImpl` mengimplementasikan interface `PaymentMethodService` dan menggunakan `PaymentMethodRepository` untuk akses data.

![Controller and Service](./img/Diagram Kode 3 Controller dan Service Interface.png)

**Code Diagram 4: Status Metode Pembayaran**

Diagram status menggambarkan siklus hidup payment method, termasuk status ACTIVE dan INACTIVE (soft delete). Dalam status ACTIVE, payment method dapat memiliki berbagai tipe (COD, BANK_TRANSFER, E_WALLET) dan dapat diperbarui oleh admin.

![Payment Method Status](./img/Diagram Kode 4 Status Metode Pembayaran.png)


---

### 💲Progress Milestone Payment Service💲


| Komponen                                                                                                     |
|--------------------------------------------------------------------------------------------------------------|
| **Software Design** (SOLID Principles, Maintainability, Design Patterns)                                     | 
| **Software Quality** (Clean Code, Secure Coding, Testing, Profiling)                                         |
| **Software Architecture** (Architecture, Concurrency, Asynchronous, High Level Networking)                   |
| **Software Deployment** (CI/CD, Deployment Strategies, Containerization, Monitoring, Infrastructure as Code) |

---

📌 **Progress Milestone 50%**

> Pada milestone 50% ini, saya telah mengerjakan dan menguji pada Postman seluruh fitur utama dari `PaymentMethod`, termasuk seluruh endpoint untuk operasi CRUD dengan dukungan role-based access. Salah satu yang terpenting yaitu penerapan **High-Level Networking** menggunakan protokol **HTTP-based RESTful API** yang memfasilitasi komunikasi antara frontend dan backend. Implementasi ini mendukung komunikasi client-server yang stabil dan aman. 

---

📌 **Progress Milestone 25%**:

> Pada milestone 25% ini, saya telah mengerjakan pondasi utama untuk fitur `PaymentMethod`, mencakup model, controller, service, DTO, repository, dan juga enum untuk tipe payment method. Selain itu, telah dilakukan integrasi dengan service Authentication untuk otorisasi berbasis JWT, serta penerapan role-based security pada endpoint menggunakan anotasi @PreAuthorize dan @PermitAll. 
> Dari sisi DevOps, pipeline CI/CD awal telah diterapkan menggunakan GitHub Actions untuk otomatisasi build dan deployment ke EC2. 

---

#### 1. **Software Design 🛠️**

**SOLID Principles ✅**
- **Single Responsibility Principle (SRP)**: Kode yang dibuat sudah mengikuti prinsip ini, seperti controller yang hanya menangani request dan response, service yang menangani business logic, dan model yang merepresentasikan data.
- **Open/Closed Principle (OCP)**: Sudah mengimplementasikan interface seperti `PaymentMethodService` yang memudahkan penambahan payment method baru tanpa mengubah kode yang ada.
- **Liskov Substitution Principle (LSP)**: Penggunaan inheritance dengan `PaymentMethod` sebagai superclass dan `COD`, `BankTransfer`, dan `EWallet` sebagai subclass sudah sesuai dengan prinsip ini.
- **Interface Segregation Principle (ISP)**: Interface yang digunakan sudah tersegmentasi, seperti `PaymentMethodService` yang hanya punya method yang relevan untuk class yang mengimplementasikannya.
- **Dependency Inversion Principle (DIP)**: Menggunakan dependency injection melalui anotasi `@Autowired` yang memisahkan antara komponen tinggi (controller/service) dengan komponen rendah (repository).

**Maintainability 🧰**
- **Separation of Concerns**: Controller hanya menangani request/response, service menangani logika bisnis, dan repository yang berhubungan dengan database. 
- **Modularisasi**: Sudah cukup modular dengan penggunaan DTO untuk memisahkan data yang dikirim ke dan dari client, serta menggunakan Factory Pattern untuk pembuatan objek yang berbasis tipe pembayaran.
- **Readability**: Sudah cukup readable, tapi mungkin kedepannya akan diberikan komentar atau dokumentasi supaya lebih clear.
- **Testability**: Sudah terdapat unit tests yang menguji controller dan service, memastikan bahwa perubahan atau penambahan fitur tidak merusak fungsionalitas yang ada.

**Design Patterns 🎨**
- **DTO (Data Transfer Object)**: Diimplementasikan dalam pembuatan objek `PaymentMethod` berdasarkan jenis pembayaran, seperti `COD`, `BankTransfer`, dan `EWallet`.
    > Alasannya, karena menurut saya, menggunakan DTO itu akan memudahkan dalam mengelola data yang dikirim antara app layer (seperti dari controller ke service atau sebaliknya). Dengan DTO, saya bisa memisahkan bussiness logic dari data representation yang digunakan oleh client (misalnya, frontend). Jadi kalau ada yang ingin mengambil API bisa langsung transfer object nya.

- **Factory Pattern**: Diterapkan pada jenis-jenis pembayaran, memungkinkan penambahan metode pembayaran baru dengan mudah tanpa merubah kode yang ada.
    > Saya memilih menggunakan Factory Pattern untuk menyederhanakan pembuatan objek-objek, terutama pada bagian PaymentMethod yang punya berbagai tipe. Factory Pattern memungkinkan aplikasi untuk menangani variasi tipe objek (misalnya BankTransfer, EWallet, dan COD) tanpa harus membuat objek secara langsung di banyak tempat.

---

#### **Software Quality 🏆**

**Clean Code 💡**
- **Separation of Concerns**: Kode sudah memisahkan antara logic request/response di controller, bisnis di service, dan data di model.
- **Naming Conventions**: Nama variabel dan method yang digunakan sudah cukup jelas dan menggambarkan fungsinya. Contoh: `createPaymentMethod`, `getPaymentMethodById`, dll.
- **Readability**: Kode sudah cukup bersih dan mudah dibaca. 

**Secure Coding 🔒**
- **Input Validation**: Saat ini, input dari pengguna seperti di `PaymentMethodRegisterDTO` perlu divalidasi untuk memastikan data yang masuk tidak menyebabkan kerentanannya, misalnya menggunakan anotasi validasi seperti `@NotNull`, `@Size`, atau `@Email`.
- **Access Control**: Sudah diterapkan menggunakan anotasi `@PreAuthorize("hasRole('ADMIN')")` dan `@PermitAll`, membatasi akses berdasarkan peran pengguna.
- **JWT Authentication**: Sudah digunakan untuk autentikasi dan kontrol akses. Saya akan memastikan lagi kalau token selalu diverifikasi dengan benar untuk mencegah masalah terkait keamanan.

**Testing 🧪**
- **Unit Testing**: Sudah dilakukan untuk memastikan bahwa controller dan service berfungsi sebagaimana mestinya. Tapi masih bisa ditambah lebih banyak edge case dan skenario error yang perlu diuji.
- **Mocking**: Sudah menggunakan mocking (`Mockito`) untuk memastikan bahwa unit tests tidak bergantung pada implementasi yang lebih besar, seperti database.
- **Test Coverage**: Masih bisa ditambahkan lebih banyak jalur yang mungkin (misalnya validasi input yang salah, kesalahan autentikasi) dan memastikan kode bisa diubah tanpa merusak fungsionalitas yang ada.

**Profiling (TBA)**:
- Belum ada, saya akan menggunakan tools seperti **JProfiler** untuk memantau performa.

---

#### 3. **Software Architecture 🏗️**

**Architecture**
- **Modular**: Sudah mengadopsi arsitektur yang terpisah dengan controller, service, dan repository. Ini membuat aplikasi lebih mudah dikelola dan dikembangkan.

**Concurrency (TBA)**
- Belum ada implementasi untuk mengelola concurrency. 

**Asynchronous (TBA)**
- Belum ada implementasi untuk mengelola asynchronous.

**High-Level Networking 🌐**

* `PaymentMethodController` ini mengimplementasikan pola komunikasi **unary** melalui HTTP REST API, di mana setiap endpoint menerima satu request dan mereturn satu respons. Hal ini cocok untuk kebutuhan operasi CRUD (Create, Read, Update, Delete) karena bersifat stateless, efisien, dan mudah diintegrasikan dengan frontend berbasis HTTP seperti Next.js. 
* Komunikasi bersifat **stateless**, di mana setiap request membawa konteks autentikasi melalui token JWT. Tidak ada penyimpanan sesi server-side, yang menjadikan API lebih scalable.
* **CORS** telah diaktifkan supaya komunikasi antara frontend (`localhost:3000`) dan backend bisa berjalan tanpa konflik keamanan.
* Mekanisme autentikasi menggunakan **JWT (JSON Web Token)** memastikan bahwa hanya user yang telah terverifikasi dan authorized yang bisa mengakses endpoint spesifik.
* Endpoint juga mendukung pengamanan tambahan melalui anotasi `@PreAuthorize` untuk endpoint sensitif dan `@PermitAll` untuk endpoint publik.

---

#### 4. **Software Deployment 🚀**

**CI/CD (Continuous Integration/Continuous Deployment) 💻**
- Sudah diterapkan menggunakan **GitHub Actions**. Setiap perubahan pada branch `main` memicu proses build, analisis kode statis, dan deploy ke EC2.
- Deployment dilakukan **setelah** job testing dan quality check (seperti PMD analysis dan Scorecard security analysis) berhasil. Hal ini memastikan kalau hanya kode yang telah diuji dengan baik yang akan dideploy ke production environment.

**Deployment Strategies 🔧**
- **Automated Deployment**: Setiap kali ada perubahan pada `main`, aplikasi di-build dan dideploy otomatis ke EC2.
- **Rollback Strategy (TBA)**: Belum ada rollback strategy jika proses deployment gagal. Bisa ditambahkan, misalnya dengan menggunakan tag pada Docker image yang bisa digunakan untuk rollback ke versi sebelumnya kalau terjadi masalah.

**Containerization 🐳**
- Menggunakan **Docker** untuk membangun image dan meng-deploy aplikasi ke EC2. Docker memastikan aplikasi berjalan konsisten di berbagai environment (dev, staging, production).

**Monitoring (TBA)**
- Belum ada implementasi untuk mengelola monitoring.

**Infrastructure as Code (IaC) (TBA)**
- Belum ada implementasi untuk mengelola IaC.

---