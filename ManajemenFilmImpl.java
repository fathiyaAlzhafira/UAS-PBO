import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class ManajemenFilmImpl implements ManajemenFilm {
    private static Scanner scanner = new Scanner(System.in);

    // Memeriksa apakah format tanggal valid
    public static boolean isValidDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            sdf.setLenient(false); // Matikan toleransi kesalahan pada format tanggal
            sdf.parse(date); // Mencoba untuk memparsing tanggal
            return true;
        } catch (ParseException e) {
            return false; // Jika parsing gagal, format tanggal tidak valid
        }
    }

    // Memeriksa apakah ID ruangan valid
    public static boolean cekIdRuangan(int idRuangan) {
        String sql = "SELECT * FROM ruangan WHERE id = ?"; // Query untuk memeriksa keberadaan ID ruangan
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idRuangan);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // Jika ID ruangan ditemukan, resultSet.next() akan bernilai true
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Jika terjadi error atau ID tidak ditemukan
    }

      // Implementasi metode tambahFilm
    @Override
    public void tambahFilm() {
        // Menambahkan pemanggilan nextLine() untuk membersihkan buffer
        scanner.nextLine(); // Membersihkan buffer input sebelumnya
        
        System.out.print("Masukkan Judul Film: ");
        String judul = scanner.nextLine();

        String jadwalTayang = "";
        boolean validJadwal = false;
        // Perulangan untuk memastikan input jadwal memiliki format yang valid
        while (!validJadwal) {
            System.out.print("Masukkan Jadwal Tayang (YYYY-MM-DD HH:MM): ");
            jadwalTayang = scanner.nextLine();
        
            // Validasi format tanggal
            if (isValidDate(jadwalTayang)) {
                validJadwal = true;
            } else {
                System.out.println("Format tanggal tidak valid! Pastikan formatnya adalah YYYY-MM-DD HH:MM.");
            }
        }

        System.out.print("Masukkan Harga Tiket: ");
        double hargaTiket = scanner.nextDouble();
        
        // Menambahkan pemanggilan nextLine() untuk membersihkan buffer
        scanner.nextLine(); // Membersihkan buffer input sebelumnya
        
        System.out.print("Masukkan ID Ruangan Baru: ");
        int idRuangan = scanner.nextInt();

        // Memeriksa apakah ID ruangan ada di tabel ruangan
        if (!cekIdRuangan(idRuangan)) {
            System.out.println("ID Ruangan tidak ditemukan! Silakan coba ID ruangan yang valid.");
            return;
        }

          // Menyiapkan query untuk memasukkan data film baru
        String sql = "INSERT INTO film (judul, jadwal_tayang, harga_tiket, id_ruangan) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, judul);
            statement.setString(2, jadwalTayang);
            statement.setDouble(3, hargaTiket);
            statement.setInt(4, idRuangan);
            statement.executeUpdate();
            System.out.println("Film berhasil ditambahkan!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Implementasi metode lihatDaftarFilm
    @Override
    public void lihatDaftarFilm() {
        String sql = "SELECT * FROM film";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("========================================");
            System.out.println("              DAFTAR FILM              ");
            System.out.println("========================================");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String judul = resultSet.getString("judul");
                String jadwalTayang = resultSet.getString("jadwal_tayang");
                double hargaTiket = resultSet.getDouble("harga_tiket");
                int idRuangan = resultSet.getInt("id_ruangan");
                System.out.printf("%d. %s | Rp %.2f | Jadwal: %s | Ruangan ID: %d%n",
                        id, judul, hargaTiket, jadwalTayang, idRuangan);
            }
            System.out.println("========================================");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Implementasi metode updateFilm
    @Override
    public void updateFilm() {
        System.out.print("Masukkan ID Film yang ingin diupdate: ");
        int idFilm = scanner.nextInt();
        scanner.nextLine(); // Clear buffer
        System.out.print("Masukkan Judul Baru: ");
        String judulBaru = scanner.nextLine();
        System.out.print("Masukkan Jadwal Tayang Baru: ");
        String jadwalTayangBaru = scanner.nextLine();
        System.out.print("Masukkan Harga Tiket Baru: ");
        double hargaTiketBaru = scanner.nextDouble();
        System.out.print("Masukkan ID Ruangan Baru: ");
        int idRuanganBaru = scanner.nextInt();

        String sql = "UPDATE film SET judul = ?, jadwal_tayang = ?, harga_tiket = ?, id_ruangan = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, judulBaru);
            statement.setString(2, jadwalTayangBaru);
            statement.setDouble(3, hargaTiketBaru);
            statement.setInt(4, idRuanganBaru);
            statement.setInt(5, idFilm);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Film berhasil diupdate!");
            } else {
                System.out.println("Film tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Implementasi metode deleteFilm
    @Override
    public void deleteFilm() {
        System.out.print("Masukkan ID Film yang ingin dihapus: ");
        int idFilm = scanner.nextInt();

        String sql = "DELETE FROM film WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idFilm);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Film berhasil dihapus!");
            } else {
                System.out.println("Film tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
