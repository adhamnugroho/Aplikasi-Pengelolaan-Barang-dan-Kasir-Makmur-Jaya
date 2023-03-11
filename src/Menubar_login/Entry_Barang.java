/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Menubar_login;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.KeyEvent;
import jaringan.Jaringan;

/**
 *
 * @author adham PSN
 */
public class Entry_Barang extends javax.swing.JFrame {

    private Connection koneksi;
    private Statement pernyataan;
    private ResultSet hasil;
    private String sql;
    private String sql2;

    /**
     * Creates new form Entry_Barang
     */
    public Entry_Barang() {
        initComponents();

        tidak_bisa_isi();
        btnSave.setEnabled(false);

        Kosong();
        placeHolder();
        keyPressed();

        isiItemKategori();
        isi_table();
    }

    private void Kosong() {

        txtSearch.setText("");
        txtKode.setText("");
        txtNama.setText("");
        txtHarga.setText("");
        txtStock.setText("");
        cmbKategori.setSelectedItem(null);
    }

    private void bisa_isi() {

        txtSearch.setEnabled(true);
        txtKode.setEnabled(false);
        txtNama.setEnabled(true);
        txtHarga.setEnabled(true);
        txtStock.setEnabled(true);
        cmbKategori.setEnabled(true);
    }

    private void tidak_bisa_isi() {
        txtKode.setEnabled(false);
        txtNama.setEnabled(false);
        txtHarga.setEnabled(false);
        txtStock.setEnabled(false);
        cmbKategori.setEnabled(false);
    }

    private void tombol_mati() {

        btnSave.setText("Simpan");
        btnSave.setEnabled(false);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        btnSearch.setEnabled(true);
    }

    private void tombol_hidup() {

        btnSave.setText("Simpan");
        btnBaru.setEnabled(true);
        btnSave.setEnabled(false);
        btnEdit.setEnabled(true);
        btnHapus.setEnabled(true);
        btnSearch.setEnabled(true);
        btnReset.setEnabled(true);
        btnBack.setEnabled(true);
        btnTambahStock.setEnabled(true);
    }

    private void placeHolder() {

        txtKode.setToolTipText("TextField Kode");
        txtNama.setToolTipText("TextField Nama");
        txtHarga.setToolTipText("TextField Harga");
        txtStock.setToolTipText("TextField Stock");
        cmbKategori.setToolTipText("ComboBox Kategori");
        txtSearch.setToolTipText("TextField Search");
    }

    private void keyPressed() {

        btnBaru.setMnemonic(KeyEvent.VK_B);
        btnSearch.setMnemonic(KeyEvent.VK_SLASH);
        btnRefresh.setMnemonic(KeyEvent.VK_R);
        btnBack.setMnemonic(KeyEvent.VK_F4);
        btnReset.setMnemonic(KeyEvent.VK_BACK_SPACE);
        btnHapus.setMnemonic(KeyEvent.VK_D);
        btnEdit.setMnemonic(KeyEvent.VK_E);
        btnTambahStock.setMnemonic(KeyEvent.VK_T);

        if (btnSave.getText().equals("Simpan")) {

            btnSave.setMnemonic(KeyEvent.VK_S);
        } else {

            btnSave.setMnemonic(KeyEvent.VK_U);
        }

    }

    private void Simpan() {

        // Jika tidak ada data yang dimasukkan        
        if (txtKode.getText().equals("") || txtNama.getText().equals("")
                || txtHarga.getText().equals("") || txtStock.getText().equals("")
                || cmbKategori.getSelectedItem().equals("")) {

            JOptionPane.showMessageDialog(null, "Tidak ada data yang dimasukkan / Data yang dimasukkkan tidak sesuai dengan format yang dibutuhkan! Silahkan memasukkan data lagi");

            Kosong();
            tidak_bisa_isi();
            tombol_hidup();
        } else {

            if ((txtHarga.getText().equals("-") || txtHarga.getText().equals(".")
                    || txtHarga.getText().equals(",") || txtHarga.getText().equals("_")
                    || txtHarga.getText().equals("k") || txtHarga.getText().equals("K"))
                    && (txtStock.getText().equals("-") || txtStock.getText().equals(".")
                    || txtStock.getText().equals(",") || txtStock.getText().equals("_")
                    || txtStock.getText().equals("k") || txtStock.getText().equals("K"))) {

                JOptionPane.showMessageDialog(null, "Format Angka yang Anda Masukkan Kurang Tepat!");
            } else {

                try {

                    sql = "insert into tb_barang(kode, nama, harga, stok, kategori) values"
                            + "(" + txtKode.getText() + ", '" + txtNama.getText() + "', " + txtHarga.getText() + ", " + txtStock.getText() + ", '" + cmbKategori.getSelectedItem() + "')";

                    koneksi = jaringan.Jaringan.config();
                    pernyataan = koneksi.createStatement();
                    pernyataan.execute(sql);

                    tidak_bisa_isi();
                    isi_table();

                    btnSave.setEnabled(false);
                } catch (Exception e) {

                    JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
                }

                Kosong();
                tombol_hidup();
            }

        }
    }

    private void update() {

        // Jika tidak ada data yang dimasukkan        
        if (txtKode.getText().equals("")
                || txtNama.getText().equals("") || txtHarga.getText().equals("")
                || txtStock.getText().equals("") || cmbKategori.getSelectedItem().equals("")) {

            JOptionPane.showMessageDialog(null, "Tidak ada data yang dimasukkan / Data yang dimasukkkan tidak sesuai dengan format yang dibutuhkan! Silahkan memasukkan data lagi");

        } else {

            if ((txtHarga.getText().equals("-") || txtHarga.getText().equals(".")
                    || txtHarga.getText().equals(",") || txtHarga.getText().equals("_")
                    || txtHarga.getText().equals("k") || txtHarga.getText().equals("K"))
                    && (txtStock.getText().equals("-") || txtStock.getText().equals(".")
                    || txtStock.getText().equals(",") || txtStock.getText().equals("_")
                    || txtStock.getText().equals("k") || txtStock.getText().equals("K"))) {

                JOptionPane.showMessageDialog(null, "Format Angka yang Anda Masukkan Kurang Tepat!");
            } else {

                try {

                    sql = "UPDATE tb_barang"
                            + " SET nama = '" + txtNama.getText() + "', harga = " + txtHarga.getText() + ", stok = " + txtStock.getText() + ", kategori = '" + cmbKategori.getSelectedItem() + "'"
                            + " WHERE kode = " + txtKode.getText();

                    koneksi = jaringan.Jaringan.config();
                    pernyataan = koneksi.createStatement();
                    pernyataan.executeUpdate(sql);

                    JOptionPane.showMessageDialog(null, "Update Berhasil");

                    Kosong();
                    isi_table();
                    tidak_bisa_isi();
                    tombol_hidup();

                    btnSave.setEnabled(false);
                } catch (Exception e) {

                    JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
                }
            }
        }
    }

    private void hapus() {

        // Jika tidak ada data yang dimasukkan        
        if (txtKode.getText().equals("") || txtNama.getText().equals("")
                || txtHarga.getText().equals("") || txtStock.getText().equals("") || 
                cmbKategori.getSelectedItem().equals("")) {

            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih! Silahkan memilih data lagi");

        } else {

            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "Data Akan di Hapus", "Hapus Data", dialogButton);

            if (dialogResult != 0) {

                JOptionPane.showMessageDialog(null, "Batal di Hapus");
            } else {

                try {

                    sql = "DELETE"
                            + " FROM tb_barang"
                            + " WHERE kode = " + txtKode.getText();

                    koneksi = jaringan.Jaringan.config();
                    pernyataan = koneksi.createStatement();
                    pernyataan.execute(sql);

                    JOptionPane.showMessageDialog(null, "Berhasil di Hapus");

                    Kosong();
                    isi_table();
                } catch (Exception e) {

                    JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
                }
            }
        }
    }

    private void isi_table() {

        DefaultTableModel tb1 = new DefaultTableModel();
        tb1.addColumn("kode");
        tb1.addColumn("nama");
        tb1.addColumn("harga");
        tb1.addColumn("stok");
        tb1.addColumn("kategori");
        tabelBarang.setModel(tb1);

        try {

            sql = "SELECT *"
                    + " FROM tb_barang";

            koneksi = jaringan.Jaringan.config();
            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);

            while (hasil.next()) {

                tb1.addRow(new Object[]{
                    hasil.getString("kode"),
                    hasil.getString("nama"),
                    hasil.getString("harga"),
                    hasil.getString("stok"),
                    hasil.getString("kategori"),});
            }

            tabelBarang.setModel(tb1);

        } catch (Exception e) {
        }

    }

    private void isiItemKategori() {

        DefaultComboBoxModel cmb1 = new DefaultComboBoxModel();

        try {

            sql = "SELECT *"
                    + " FROM tb_kategori";

            koneksi = jaringan.Jaringan.config();
            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);

            while (hasil.next()) {

                cmb1.addElement(hasil.getString("nama_kategori"));

                cmbKategori.setModel(cmb1);
            }
        } catch (Exception e) {
        }

        Kosong();
    }

    private void tambahStock() {

        // Jika tidak ada data yang dipilih        
        if (txtKode.getText().equals("") || txtNama.getText().equals("") || 
                txtHarga.getText().equals("") || txtStock.getText().equals("") || 
                cmbKategori.getSelectedItem().equals("")) {

            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih! Silahkan memilih data lagi");

        } else {

            int ConvJumlah;
            int jumlah;
            int ConvStok;
            String ConvHasil;

            String stok = JOptionPane.showInputDialog(
                    "Update Stock: "
            );

            ConvStok = Integer.parseInt(stok);

            if (!stok.equals("")) {

                if (stok.equals("-") || stok.equals(".") || stok.equals(",") || 
                        stok.equals("_") || stok.equals("k") || stok.equals("K")) {

                    JOptionPane.showMessageDialog(null, "Format Angka yang Anda Masukkan Kurang Tepat!");
                } else {

                    try {

                        sql = "SELECT *"
                                + " FROM tb_barang"
                                + " WHERE kode = " + txtKode.getText();

                        koneksi = jaringan.Jaringan.config();
                        pernyataan = koneksi.createStatement();
                        hasil = pernyataan.executeQuery(sql);

                        while (hasil.next()) {

                            ConvJumlah = Integer.parseInt(hasil.getString("stok"));
                            jumlah = ConvStok + ConvJumlah;

                            ConvHasil = String.valueOf(jumlah);

                            sql2 = "UPDATE tb_barang"
                                    + " SET stok = " + ConvHasil
                                    + " WHERE kode = " + txtKode.getText();

                            koneksi = jaringan.Jaringan.config();
                            pernyataan = koneksi.createStatement();
                            pernyataan.executeUpdate(sql2);

                        }

                        JOptionPane.showMessageDialog(null, "Tambah Stock Berhasil");
                    } catch (Exception e) {

                        JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
                    }

                    isi_table();
                    Kosong();
                }
            }
        }
    }

    private void AutoIncrement() {

        int ConvHasil;
        int jumlah;
        String ConvJumlah;

        try {

            sql = "SELECT MAX(kode) AS kode, nama, harga, stok, kategori"
                    + " FROM tb_barang";

            koneksi = jaringan.Jaringan.config();
            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);

            while (hasil.next()) {

                ConvHasil = Integer.parseInt(hasil.getString("kode"));

                jumlah = ConvHasil + 1;

                ConvJumlah = String.valueOf(jumlah);

                txtKode.setText(ConvJumlah);
            }

        } catch (Exception e) {
        }
    }

    private void Search() {

        // Jika TextField search kosong       
        if (txtSearch.getText().equals("")) {

            txtSearch.requestFocus();

        } else {

            DefaultTableModel tb1 = new DefaultTableModel();
            tb1.addColumn("kode");
            tb1.addColumn("nama");
            tb1.addColumn("harga");
            tb1.addColumn("stok");
            tb1.addColumn("kategori");
            tabelBarang.setModel(tb1);

            try {

                sql = "SELECT *"
                        + " FROM tb_barang"
                        + " WHERE kode = '" + txtSearch.getText() + "' || nama LIKE '%" + txtSearch.getText() + "%' || kategori LIKE '%" + txtSearch.getText() + "%'";

                koneksi = jaringan.Jaringan.config();
                pernyataan = koneksi.createStatement();
                hasil = pernyataan.executeQuery(sql);

                while (hasil.next()) {

                    tb1.addRow(new Object[]{
                        hasil.getString("kode"),
                        hasil.getString("nama"),
                        hasil.getString("harga"),
                        hasil.getString("stok"),
                        hasil.getString("kategori"),});
                }

                tabelBarang.setModel(tb1);

                txtKode.setEnabled(true);
            } catch (Exception e) {
            }
        }
    }

    private void functionEdit() {

        if (txtKode.getText().equals("") || txtNama.getText().equals("") || 
                txtHarga.getText().equals("") || txtStock.getText().equals("") || 
                cmbKategori.getSelectedItem().equals("")) {

            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih! Silahkan memilih data lagi");
        } else {

            bisa_isi();
            tombol_mati();

            txtNama.requestFocus();
            btnSave.setText("Update");
            btnSave.setEnabled(true);
            txtKode.setEnabled(false);

            btnBaru.setEnabled(false);
            btnEdit.setEnabled(false);
            btnHapus.setEnabled(false);
            btnBack.setEnabled(false);
            btnTambahStock.setEnabled(false);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtKode = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtHarga = new javax.swing.JTextField();
        txtStock = new javax.swing.JTextField();
        cmbKategori = new javax.swing.JComboBox<>();
        jPanel3 = new javax.swing.JPanel();
        btnBaru = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnHapus = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelBarang = new javax.swing.JTable();
        btnRefresh = new javax.swing.JButton();
        btnTambahStock = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));

        jPanel1.setBackground(new java.awt.Color(204, 0, 204));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setText("FORM INPUT BARANG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(411, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(383, 383, 383))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel2.setBackground(new java.awt.Color(255, 153, 255));

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("KODE");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("NAMA");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("HARGA");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("STOCK");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("KATEGORI");

        txtKode.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtKode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodeActionPerformed(evt);
            }
        });

        txtNama.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N

        txtHarga.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtHarga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHargaActionPerformed(evt);
            }
        });

        txtStock.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtStockActionPerformed(evt);
            }
        });

        cmbKategori.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        cmbKategori.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbKategoriActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel6)
                        .addComponent(jLabel2)
                        .addComponent(jLabel3)
                        .addComponent(jLabel5))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel4)))
                .addGap(56, 56, 56)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 558, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtStock, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(71, Short.MAX_VALUE))
        );

        jPanel3.setBackground(new java.awt.Color(153, 153, 153));

        btnBaru.setText("BARU");
        btnBaru.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnEdit.setText("EDIT");
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnHapus.setText("HAPUS");
        btnHapus.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnReset.setText("RESET");
        btnReset.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(183, 183, 183)
                .addComponent(btnBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnHapus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnReset, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tabelBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "KODE", "NAMA", "HARGA", "STOCK", "KATEGORI"
            }
        ));
        tabelBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelBarangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelBarang);

        btnRefresh.setText("REFRESH");
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnTambahStock.setText("TAMBAH STOCK");
        btnTambahStock.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnTambahStock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahStockActionPerformed(evt);
            }
        });

        btnBack.setText("BACK");
        btnBack.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("SEARCH");

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchActionPerformed(evt);
            }
        });

        btnSearch.setText("SEARCH");
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(btnTambahStock)
                        .addGap(38, 38, 38)
                        .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSearch)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTambahStock, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        setSize(new java.awt.Dimension(1219, 992));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtStockActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStockActionPerformed

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruActionPerformed
        // TODO add your handling code here:

        Kosong();
        bisa_isi();

        btnSave.setText("Simpan");
        btnSave.setEnabled(true);
        btnBaru.setEnabled(false);
        btnEdit.setEnabled(false);
        btnHapus.setEnabled(false);
        btnBack.setEnabled(false);
        btnTambahStock.setEnabled(false);

        isiItemKategori();
        AutoIncrement();

        txtNama.requestFocus();

    }//GEN-LAST:event_btnBaruActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:

        if (btnSave.getText() == "Simpan") {

            Simpan();
        } else {

            update();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:

        functionEdit();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:

        hapus();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:

        JOptionPane.showMessageDialog(null, "Reset");

        Kosong();
        tombol_hidup();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnTambahStockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahStockActionPerformed
        // TODO add your handling code here:

        tidak_bisa_isi();

        tambahStock();
    }//GEN-LAST:event_btnTambahStockActionPerformed

    private void txtHargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHargaActionPerformed

    private void tabelBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelBarangMouseClicked
        // TODO add your handling code here:

        // untuk mendisable editing column pada tabel       
        tabelBarang.setDefaultEditor(Object.class, null);

        int tabelData = tabelBarang.getSelectedRow();

        txtKode.setText(tabelBarang.getValueAt(tabelData, 0).toString());
        txtNama.setText(tabelBarang.getValueAt(tabelData, 1).toString());
        txtHarga.setText(tabelBarang.getValueAt(tabelData, 2).toString());
        txtStock.setText(tabelBarang.getValueAt(tabelData, 3).toString());
        cmbKategori.setSelectedItem(tabelBarang.getValueAt(tabelData, 4).toString());

        tabelBarang.isCellEditable(tabelData, 0);
        tabelBarang.isCellEditable(tabelData, 1);
        tabelBarang.isCellEditable(tabelData, 2);
        tabelBarang.isCellEditable(tabelData, 3);
        tabelBarang.isCellEditable(tabelData, 4);
    }//GEN-LAST:event_tabelBarangMouseClicked

    private void txtKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtKodeActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:

        Search();
        bisa_isi();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void cmbKategoriActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbKategoriActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbKategoriActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:

        dispose();

        new Entry_Barang().setVisible(true);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:

        dispose();

        new AdminToko().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSearchActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Entry_Barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Entry_Barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Entry_Barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Entry_Barang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Entry_Barang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBaru;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnTambahStock;
    private javax.swing.JComboBox<String> cmbKategori;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelBarang;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtSearch;
    private javax.swing.JTextField txtStock;
    // End of variables declaration//GEN-END:variables
}
