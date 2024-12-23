import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SistemManajemenBioskop {
    // Deklarasi scanner untuk input dari user
    private Scanner scanner = new Scanner(System.in);
    // Membuat objek untuk implementasi interface ManajemenFilm
    private ManajemenFilmImpl manajemenFilm = new ManajemenFilmImpl();


    // Menu utama aplikasi
    public void menuUtama() {
        while (true) { // Perulangan utama untuk menu
            System.out.println("========================================");
            System.out.println("       SISTEM MANAJEMEN BIOSKOP        ");
            System.out.println("========================================");
            System.out.println("1. Kelola Film");
            System.out.println("2. Lihat Daftar Film akan Tayang");
            System.out.println("3. Hitung Pendapatan Potensial");
            System.out.println("4. Statistik Film ");
            System.out.println("5. Keluar");
            System.out.println("========================================");
            System.out.print("Masukkan pilihan Anda: ");
            int pilihan = scanner.nextInt(); // Membaca input user
            scanner.nextLine(); // Clear buffer


            switch (pilihan) { // Percabangan berdasarkan pilihan user
                case 1:
                    kelolaFilm();
                    break;
                case 2:
                    lihatDaftarFilmSedangTayang();
                    break;
                case 3:
                    Potensial();
                    break;
                case 4:
                    statistikfilm();
                    break;
                case 5:
                    System.out.println("Terima kasih telah menggunakan Sistem Manajemen Bioskop!");
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }


    // Menu untuk kelola film (CRUD: Create, Read, Update, Delete)
    private void kelolaFilm() {
        while (true) {  // Perulangan untuk menu kelola film
            System.out.println("========================================");
            System.out.println("              KELOLA FILM              ");
            System.out.println("========================================");
            System.out.println("1. Tambah Film");
            System.out.println("2. Lihat Daftar Film");
            System.out.println("3. Update Film");
            System.out.println("4. Hapus Film");
            System.out.println("5. Kembali ke Menu Utama");
            System.out.println("========================================");
            System.out.print("Masukkan pilihan Anda: ");
            int pilihan = scanner.nextInt();
            scanner.nextLine(); // Clear buffer


            switch (pilihan) {
                case 1:
                    manajemenFilm.tambahFilm();
                    break;
                case 2:
                    manajemenFilm.lihatDaftarFilm();
                    break;
                case 3:
                    manajemenFilm.updateFilm();
                    break;
                case 4:
                    manajemenFilm.deleteFilm();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }


     // Menampilkan daftar film yang akan tayang (Read dari database)
    private void lihatDaftarFilmSedangTayang() {
    List<Film> daftarFilmTayang = new ArrayList<>(); // Menggunakan Collection Framework (ArrayList)
    String sql = "SELECT * FROM film WHERE jadwal_tayang > NOW()"; // Menyaring film yang sedang tayang


    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {


        // Menambahkan film yang sedang tayang ke ArrayList
        while (resultSet.next()) { // Perulangan untuk membaca hasil query
            int id = resultSet.getInt("id");
            String judul = resultSet.getString("judul");
            String jadwalTayang = resultSet.getString("jadwal_tayang");
            double hargaTiket = resultSet.getDouble("harga_tiket");
            int idRuangan = resultSet.getInt("id_ruangan");


            Film film = new Film(id, judul, jadwalTayang, hargaTiket, idRuangan);
            daftarFilmTayang.add(film); // Menambahkan film ke dalam list
        }


        // Menampilkan film yang sedang tayang
        if (daftarFilmTayang.isEmpty()) {
            System.out.println("Tidak ada film yang sedang tayang saat ini.");
        } else {
            System.out.println("========================================");
            System.out.println("         DAFTAR FILM Akan TAYANG      ");
            System.out.println("========================================");
            for (Film film : daftarFilmTayang) {
                System.out.printf("%d. %s | Jadwal Tayang: %s | Harga Tiket: Rp %.2f%n",
                        film.getId(), film.getJudul(), film.getJadwalTayang(), film.getHargaTiket());
            }
            System.out.println("========================================");
        }
    } catch (SQLException e) {  // Exception handling untuk SQLException
        e.printStackTrace();
    }
}


     // Menghitung pendapatan potensial (Perhitungan matematika)
    private void Potensial() {
    System.out.println("========================================");
    System.out.println("        Hitung Pendapatan Potensial           ");
    System.out.println("========================================");
    lihatDaftarFilmSedangTayang(); // Menampilkan daftar film


    System.out.print("Masukkan ID Film: ");
    int idFilm = scanner.nextInt();
    System.out.print("Masukkan Jumlah Kursi Ruangan: ");
    int jumlahKursi = scanner.nextInt();


    String sqlFilm = "SELECT * FROM film WHERE id = ?";
    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statementFilm = connection.prepareStatement(sqlFilm)) {
        statementFilm.setInt(1, idFilm);
        ResultSet resultSet = statementFilm.executeQuery();


        if (resultSet.next()) {
            String judul = resultSet.getString("judul");
            double hargaTiket = resultSet.getDouble("harga");
            double totalHarga = hargaTiket * jumlahKursi; // Perhitungan matematika


            System.out.println("Detail Potensial:");
            System.out.printf("Film                 : %s%n", judul);
            System.out.printf("Harga                : Rp %.2f%n", hargaTiket);
            System.out.printf("Total                : Rp %.2f%n", totalHarga);
            System.out.print("Konfirmasi? (Y/N): ");
            char konfirmasi = scanner.next().charAt(0);


            if (konfirmasi == 'Y' || konfirmasi == 'y') { // Percabangan untuk konfirmasi
                String idTiket = "TIX" + System.currentTimeMillis(); // Manipulasi String dan Date
                String sqlTiket = "INSERT INTO tiket (id, id_film, jumlah) VALUES (?, ?, ?)";
                try (PreparedStatement statementTiket = connection.prepareStatement(sqlTiket)) {
                    statementTiket.setString(1, idTiket);
                    statementTiket.setInt(2, idFilm);
                    statementTiket.setInt(3, jumlahKursi);
                    statementTiket.executeUpdate();
                    System.out.println("Pendapatan Potensial Berhasil di simpan.");
                }
            } else {
                System.out.println("Pendapatan Potensial gagal di simpan.");
            }
        } else {
            System.out.println("Film tidak ditemukan!");
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    //menampilkan statistik film
    private void statistikfilm() {
    System.out.println("========================================");
    System.out.println("         STATISTIK Film            ");
    System.out.println("========================================");
    String sql = "SELECT f.judul, SUM(t.jumlah) AS tiket_habis, " +
                 "SUM(t.jumlah * f.harga_tiket) AS pendapatan " +
                 "FROM tiket t " +
                 "JOIN film f ON t.id_film = f.id " +
                 "GROUP BY f.id";


    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql);
         ResultSet resultSet = statement.executeQuery()) {


        System.out.println("Film             | Jumlah Kursi | Pendapatan Potensial");
        System.out.println("----------------------------------------");
        double totalPendapatan = 0.0;


        while (resultSet.next()) {
            String judulFilm = resultSet.getString("judul");
            int tikethabis = resultSet.getInt("tiket_habis");
            double pendapatan = resultSet.getDouble("pendapatan");
            totalPendapatan += pendapatan;


            System.out.printf("%-16s | %-13d | Rp %.2f%n", judulFilm, tikethabis, pendapatan);
        }


        System.out.println("----------------------------------------");
        System.out.printf("Pendapatan Potensial       : Rp %.2f%n", totalPendapatan);
        System.out.println("========================================");
    } catch (SQLException e) {
        e.printStackTrace();
    }
}


    public static void main(String[] args) {
        SistemManajemenBioskop bioskop = new SistemManajemenBioskop();
        bioskop.menuUtama();
    }
}
