**Nama: Ghina Nabila Gunawan**

**NPM: 2206825914**

**Kelas: AdvProg - A**


### **💲Progress Milestone Payment Service💲**


| Komponen                                                                                                     |
|--------------------------------------------------------------------------------------------------------------|
| **Software Design** (SOLID Principles, Maintainability, Design Patterns)                                     | 
| **Software Quality** (Clean Code, Secure Coding, Testing, Profiling)                                         |
| **Software Architecture** (Architecture, Concurrency, Asynchronous, High Level Networking)                   |
| **Software Deployment** (CI/CD, Deployment Strategies, Containerization, Monitoring, Infrastructure as Code) |

---

📌 **Implementasi Milestone 25%**:

> Pada milestone 25% ini, saya telah mengerjakan pondasi utama untuk fitur PaymentMethod, mencakup model, controller, service, DTO, repository, dan juga enum untuk tipe metode pembayaran. Selain itu, telah dilakukan integrasi dengan service Authentication untuk otorisasi berbasis JWT, serta penerapan role-based security pada endpoint menggunakan anotasi @PreAuthorize dan @PermitAll. 
> Dari sisi DevOps, pipeline CI/CD awal telah diterapkan menggunakan GitHub Actions untuk otomatisasi build dan deployment ke EC2. 

---

#### 1. **Software Design 🛠️**

**SOLID Principles ✅**
- **Single Responsibility Principle (SRP)**: Kode yang dibuat sudah mengikuti prinsip ini, seperti controller yang hanya menangani request dan response, service yang menangani business logic, dan model yang merepresentasikan data.
- **Open/Closed Principle (OCP)**: Sudah mengimplementasikan interface seperti `PaymentMethodService` yang memudahkan penambahan metode pembayaran baru tanpa mengubah kode yang ada.
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

**High-Level Networking (TBA)**
- Karena app ini akan menggunakan REST API, high-level networking seperti penggunaan API Gateway bisa dipertimbangkan.

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