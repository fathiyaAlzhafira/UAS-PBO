public class Film {
    private int id;
    private String judul;
    private String jadwalTayang;
    private double hargaTiket;
    private int idRuangan;

    // Konstruktor untuk menginisialisasi objek Film dengan atribut yang diberikan
    public Film(int id, String judul, String jadwalTayang, double hargaTiket, int idRuangan) {
        this.id = id;
        this.judul = judul;
        this.jadwalTayang = jadwalTayang;
        this.hargaTiket = hargaTiket;
        this.idRuangan = idRuangan;
    }

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public String getJudul() {
        return judul;
    }

    public String getJadwalTayang() {
        return jadwalTayang;
    }

    public double getHargaTiket() {
        return hargaTiket;
    }

    public int getIdRuangan() {
        return idRuangan;
    }
    // Override metode toString untuk memberikan representasi string dari objek Film
    @Override
    public String toString() {
        return id + ". " + judul + " | Jadwal: " + jadwalTayang + " | Harga: Rp " + hargaTiket;
    }
}
