// Menambahkan atribut baru untuk menunjukkan status apakah film sedang tayang sekarang
public class FilmTayang extends Film {
    private boolean tayangSekarang;

    // Konstruktor untuk menginisialisasi objek FilmTayang, termasuk atribut baru
    public FilmTayang(int id, String judul, String jadwalTayang, double hargaTiket, int idRuangan, boolean tayangSekarang) {
        super(id, judul, jadwalTayang, hargaTiket, idRuangan);
        this.tayangSekarang = tayangSekarang;
    }

    // Getter untuk mendapatkan status apakah film sedang tayang sekarang
    public boolean isTayangSekarang() {
        return tayangSekarang;
    }

    // Setter untuk mengubah status apakah film sedang tayang sekarang
    public void setTayangSekarang(boolean tayangSekarang) {
        this.tayangSekarang = tayangSekarang;
    }

    // Override metode toString untuk menambahkan informasi status tayang sekarang
    @Override
    public String toString() {
        return super.toString() + " | Tayang Sekarang: " + tayangSekarang;
    }
}
