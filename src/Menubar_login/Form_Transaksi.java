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
import java.awt.event.KeyEvent;
import jaringan.Jaringan;
import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import org.apache.commons.collections.*;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.view.JasperViewer;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.digester.Digester;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.poi.hssf.record.formula.functions.Today;

/**
 *
 * @author adham
 */
public class Form_Transaksi extends javax.swing.JFrame {

    //ireport
    JasperReport JasRep;
    JasperPrint JasPri;
    Map param = new HashMap();
    JasperDesign JasDes;
    InputStream jasperStream;

    private Connection koneksi;
    private Statement pernyataan;
    private ResultSet hasil;
    private String sql;
    private String sql2;
    private String hasilPecahanChar;
    private int cekTombol = 0;
    private boolean masukLoginTemp;

    /**
     * Creates new form NewJFrame
     */
    public Form_Transaksi() {
        initComponents();

        tidak_bisa_isi();
        Kosong();

        placeHolder();
        keyPressed();

        isi_tableBarang();
        isi_tableTransaksi();

        functionTotalTagihan();
    }

    private void jikaAktif() {

        if (jScrollPane4.isVisible()) {

            this.masukLoginTemp = true;
        }

        Menu_login user = new Menu_login();

        user.show();

        Menu_login.Instance.masukLogin = masukLoginTemp;

        user.jikaLoginUser();
    }

    private void Kosong() {

        txtSearchBarang.setText("");
        txtKodeBarang.setText("");
        txtNamaBarang.setText("");
        txtHarga.setText("");
        txtJumlah.setText("");
        txtSubtotal.setText("");
        txtSearchTransaksi.setText("");
        txtTotalTagihan.setText("");
        txtCash.setText("");
        txtKembalian.setText("");

    }

    private void bisa_isi() {

        txtSearchBarang.setEnabled(true);
        txtKodeTransaksi.setEnabled(true);
        txtKodeBarang.setEnabled(true);
        txtNamaBarang.setEnabled(true);
        txtHarga.setEnabled(true);
        txtJumlah.setEnabled(true);
        txtSubtotal.setEnabled(true);
        txtSearchTransaksi.setEnabled(true);
        txtTotalTagihan.setEnabled(true);
        txtCash.setEnabled(true);
        txtKembalian.setEnabled(true);
    }

    private void tidak_bisa_isi() {

        txtSearchBarang.setEnabled(true);
        txtKodeTransaksi.setEnabled(false);
        txtKodeBarang.setEnabled(false);
        txtNamaBarang.setEnabled(false);
        txtHarga.setEnabled(false);
        txtJumlah.setEnabled(true);
        txtSubtotal.setEnabled(false);
        txtSearchTransaksi.setEnabled(true);
        txtTotalTagihan.setEnabled(false);
        txtCash.setEnabled(true);
        txtKembalian.setEnabled(false);
    }

    private void tombol_mati() {

        btnBack.setEnabled(false);
        btnKeluar.setEnabled(false);
        btnReset.setEnabled(false);
        btnTambah.setEnabled(false);
        btnTambah.setText("Tambah");
        btnHapus.setEnabled(false);
        btnNewTransaksi.setEnabled(true);
        btnBayar.setEnabled(false);
        btnCetakBuktiPembayaran.setEnabled(false);
        btnRefresh.setEnabled(true);
    }

    private void tombol_hidup() {

        btnBack.setEnabled(true);
        btnKeluar.setEnabled(true);
        btnReset.setEnabled(true);
        btnTambah.setEnabled(true);
        btnTambah.setText("Tambah");
        btnHapus.setEnabled(true);
        btnNewTransaksi.setEnabled(true);
        btnBayar.setEnabled(true);
        btnCetakBuktiPembayaran.setEnabled(true);
        btnRefresh.setEnabled(true);
    }

    private void placeHolder() {

        txtKodeTransaksi.setToolTipText("TextField Kode Transaksi");
        txtKodeBarang.setToolTipText("TextField Kode Barang");
        txtNamaBarang.setToolTipText("TextField Nama Barang");
        txtHarga.setToolTipText("TextField Harga");
        txtJumlah.setToolTipText("TextField Jumlah");
        txtSubtotal.setToolTipText("TextField Subtotal");
        txtSearchBarang.setToolTipText("TextField Search Barang");
        txtSearchTransaksi.setToolTipText("TextField Search Transaksi");
        txtTotalTagihan.setToolTipText("TextField Total Tagihan");
        txtCash.setToolTipText("TextField Cash");
        txtKembalian.setToolTipText("TextField Kembalian");
    }

    private void keyPressed() {

        btnBack.setMnemonic(KeyEvent.VK_B);
        btnSearchInputBarang.setMnemonic(KeyEvent.VK_SLASH);
        btnSearchTransaksi.setMnemonic(KeyEvent.VK_BACK_SLASH);
        btnRefresh.setMnemonic(KeyEvent.VK_R);
        btnKeluar.setMnemonic(KeyEvent.VK_F4);
        btnReset.setMnemonic(KeyEvent.VK_BACK_SPACE);
        btnHapus.setMnemonic(KeyEvent.VK_D);
        btnEdit.setMnemonic(KeyEvent.VK_E);

        if (btnTambah.getText().equals("Tambah")) {

            btnTambah.setMnemonic(KeyEvent.VK_T);
        } else if (btnTambah.getText().equals("Update")) {

            System.out.println(btnTambah.getText());
            btnTambah.setMnemonic(KeyEvent.VK_U);
        }
    }

    private void ambilDataTabelBarang() {

        // untuk mendisable editing column pada tabel       
        tabelBarang.setDefaultEditor(Object.class, null);

        int tabelData = tabelBarang.getSelectedRow();

        txtKodeBarang.setText(tabelBarang.getValueAt(tabelData, 0).toString());
        txtNamaBarang.setText(tabelBarang.getValueAt(tabelData, 1).toString());
        txtHarga.setText(tabelBarang.getValueAt(tabelData, 2).toString());

        tombol_hidup();
        ambilKodeTransaksiTerakhir();
        subtotalHargaBarang();
        btnTambah.setEnabled(true);
    }

    private void sistemTabelTransaksi() {

        // untuk mendisable editing column pada tabel       
        tabelTransaksi.setDefaultEditor(Object.class, null);

        int tabelData = tabelTransaksi.getSelectedRow();

        txtKodeTransaksi.setText(tabelTransaksi.getValueAt(tabelData, 0).toString());
        txtNamaBarang.setText(tabelTransaksi.getValueAt(tabelData, 1).toString());
        txtHarga.setText(tabelTransaksi.getValueAt(tabelData, 2).toString());
        txtJumlah.setText(tabelTransaksi.getValueAt(tabelData, 3).toString());
        txtSubtotal.setText(tabelTransaksi.getValueAt(tabelData, 4).toString());

        tombol_hidup();
        ambilKodeBarangTerkait();
        btnTambah.setEnabled(true);
    }

    private void Simpan() throws SQLException {

        // Jika tidak ada data yang dimasukkan        
        if (txtKodeTransaksi.getText().equals("") || txtKodeBarang.getText().equals("") || txtNamaBarang.getText().equals("") || txtHarga.getText().equals("") || txtJumlah.getText().equals("") || txtSubtotal.getText().equals("")) {

            JOptionPane.showMessageDialog(null, "Tidak ada data yang dimasukkan / Data yang dimasukkkan tidak sesuai dengan format yang dibutuhkan! Silahkan memasukkan data lagi");

            if (txtKodeTransaksi.getText().equals("")) {

                try {
                    sql = "SELECT MAX(kode_tr_angka) AS kode_transaksi_angka, id_transaksi"
                            + " FROM tb_transaksi";

                    koneksi = jaringan.Jaringan.config();
                    pernyataan = koneksi.createStatement();
                    hasil = pernyataan.executeQuery(sql);

                    while (hasil.next()) {

                        txtKodeTransaksi.setText(hasil.getString("id_transaksi"));
                    }
                } catch (SQLException e) {

                    JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
                }
            }

        } else {

            try {

                //Mengatur agar tidak langsung di commit ketika proses transaksi terjadi                
                koneksi = jaringan.Jaringan.config();
                koneksi.setAutoCommit(false);

                String nama_barang = txtNamaBarang.getText();
                String transaksi_id = txtKodeTransaksi.getText();

                sql = "SELECT *"
                        + " FROM tb_detail_transaksi"
                        + " WHERE nama LIKE '%" + nama_barang + "%' AND transaksi_id LIKE '%" + transaksi_id + "%'";

                pernyataan = koneksi.createStatement();
                hasil = pernyataan.executeQuery(sql);

                // token               
                boolean valueAutoIncrement = hasil.next();

                // Pengecekan sudah ada tidaknya barang dalam suatu transaksi               
                if (valueAutoIncrement == true) {

                    subtotalHargaBarang();

                    mergePembelianBarang();
                } else {

                    sql = "INSERT INTO tb_detail_transaksi(transaksi_id, barang_kode, nama, harga, jumlah, subtotal) VALUES"
                            + "('" + txtKodeTransaksi.getText() + "', '" + txtKodeBarang.getText() + "' ,'" + txtNamaBarang.getText() + "', " + txtHarga.getText() + ", " + txtJumlah.getText() + ", " + txtSubtotal.getText() + ")";

                    pernyataan = koneksi.createStatement();
                    pernyataan.execute(sql);
                }

                // commit jika sistem simpan barang berhasil dijalankan
                koneksi.commit();
            } catch (SQLException e) {

                JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);

                // rollback autoincrement kode transaksi gagal dijalankan
                koneksi.rollback();
            }

            tidak_bisa_isi();

            isi_tableTransaksi();
            isi_tableBarang();
        }
    }

    private void updateJumlah() {

        // Jika tidak ada data yang dimasukkan        
        if (txtKodeTransaksi.getText().equals("") || txtNamaBarang.getText().equals("") || txtHarga.getText().equals("") || txtJumlah.getText().equals("") || txtSubtotal.getText().equals("")) {

            JOptionPane.showMessageDialog(null, "Tidak ada data yang dimasukkan / Data yang dimasukkkan tidak sesuai dengan format yang dibutuhkan! Silahkan memasukkan data lagi");

        } else {

            try {

                sql = "UPDATE tb_detail_transaksi"
                        + " SET jumlah = '" + txtJumlah.getText() + "', subtotal = '" + txtSubtotal.getText() + "'"
                        + " WHERE transaksi_id LIKE '%" + txtKodeTransaksi.getText() + "%' AND nama LIKE '%" + txtNamaBarang.getText() + "%'";

                koneksi = jaringan.Jaringan.config();
                pernyataan = koneksi.createStatement();
                pernyataan.executeUpdate(sql);

                JOptionPane.showMessageDialog(null, "Update Jumlah Barang Berhasil");
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
            }

            isi_tableTransaksi();
            isi_tableBarang();

            btnEdit.setEnabled(true);
        }
    }

    private void hapus() {

        // Jika tidak ada data yang dimasukkan        
        if (txtKodeTransaksi.getText().equals("") || txtNamaBarang.getText().equals("") || txtHarga.getText().equals("") || txtJumlah.getText().equals("") || txtSubtotal.getText().equals("")) {

            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih! Silahkan memilih data lagi");

        } else {

            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "Data Akan di Hapus", "Hapus Data", dialogButton);

            if (dialogResult == 0) {

                try {

                    sql = "DELETE"
                            + " FROM tb_detail_transaksi"
                            + " WHERE barang_kode LIKE '%" + txtKodeBarang.getText() + "%' AND transaksi_id LIKE '%" + txtKodeTransaksi.getText() + "%'";

                    koneksi = jaringan.Jaringan.config();
                    pernyataan = koneksi.createStatement();
                    pernyataan.execute(sql);

                    JOptionPane.showMessageDialog(null, "Berhasil di Hapus");

                    // kosong();
                    isi_tableBarang();
                    isi_tableTransaksi();
                } catch (Exception e) {

                    JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
                }
            } else {

                JOptionPane.showMessageDialog(null, "Batal di Hapus");
            }
        }
    }

    private void isi_tableTransaksi() {

        DefaultTableModel tb1 = new DefaultTableModel();
        tb1.addColumn("id_transaksi");
        tb1.addColumn("nama");
        tb1.addColumn("harga");
        tb1.addColumn("jumlah");
        tb1.addColumn("subtotal");
        tabelTransaksi.setModel(tb1);

        try {

            sql = "SELECT *"
                    + " FROM tb_detail_transaksi"
                    + " WHERE transaksi_id = (SELECT MAX(id_transaksi) AS id_transaksi"
                    + " FROM tb_transaksi)";

            koneksi = jaringan.Jaringan.config();
            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);

            while (hasil.next()) {

                tb1.addRow(new Object[]{
                    hasil.getString("transaksi_id"),
                    hasil.getString("nama"),
                    hasil.getString("harga"),
                    hasil.getString("jumlah"),
                    hasil.getString("subtotal"),});
            }

            tabelTransaksi.setModel(tb1);

        } catch (Exception e) {
        }

    }

    private void isi_tableBarang() {

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

    private void AutoIncrement() throws SQLException {

        boolean hitungTemp;
        int hasilHitung;
        String jumlah, kodeTransaksi, kode_tr_angkaTemp, id_transaksiTemp, tambahKarakter1, valueAutoIncrement;
        int tambahId, tambahKarakter;
        int id_transaksi;

        try {

            //Mengatur agar tidak langsung di commit ketika proses transaksi terjadi                
            koneksi = jaringan.Jaringan.config();
            koneksi.setAutoCommit(false);

            sql = "SELECT MAX(kode_tr_angka) AS kode_transaksi_angka, MAX(id_transaksi) AS id_transaksi"
                    + " FROM tb_transaksi";

            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);

            valueAutoIncrement = "auto increment: " + hasil.next();

            if (hasil.getString("kode_transaksi_angka") == null && hasil.getString("id_transaksi") == null) {

                id_transaksi = 1;

                jumlah = "P" + 0 + id_transaksi;

                txtKodeTransaksi.setText(jumlah);

                sql = "INSERT INTO tb_transaksi(kode_tr_angka, id_transaksi, status_pembayaran, total_harga, nominal_pembayaran, kembalian, tanggal_pembelian) VALUES"
                        + "(" + id_transaksi + ", '" + jumlah + "', 'Belum_Selesai', 0, 0, 0, '" + LocalDate.now() + "')";

                pernyataan = koneksi.createStatement();
                pernyataan.execute(sql);
            } else {

                // hasil dari query sql               
                hasilHitung = hasil.getInt("kode_transaksi_angka");
                kodeTransaksi = hitungKarakter(hasil.getString("id_transaksi"));

                tambahId = hasilHitung + 1;
                tambahKarakter = Integer.parseInt(kodeTransaksi) + 1;

                // pengkondisian berdasarkan digit angka               
                if (hitungDigitAngka(tambahKarakter) == 1) {

                    tambahKarakter1 = "0" + Integer.toString(tambahKarakter);

                    jumlah = "P" + tambahKarakter1;

                    txtKodeTransaksi.setText(jumlah);

                    sql = "INSERT INTO tb_transaksi(kode_tr_angka, id_transaksi, status_pembayaran, total_harga, nominal_pembayaran, kembalian, tanggal_pembelian) VALUES"
                            + "(" + tambahId + ", '" + jumlah + "', 'Belum_Selesai', 0, 0, 0, '" + LocalDate.now() + "')";

                    pernyataan = koneksi.createStatement();
                    pernyataan.execute(sql);
                } else {

                    jumlah = "P" + String.valueOf(tambahKarakter);

                    txtKodeTransaksi.setText(jumlah);

                    sql = "INSERT INTO tb_transaksi(kode_tr_angka, id_transaksi, status_pembayaran, total_harga, nominal_pembayaran, kembalian, tanggal_pembelian) VALUES"
                            + "(" + tambahId + ", '" + jumlah + "', 'Belum_Selesai', 0, 0, 0, '" + LocalDate.now() + "')";

                    pernyataan = koneksi.createStatement();
                    pernyataan.execute(sql);
                }
            }

            // commit jika sistem autoincrement kode transaksi berhasil dijalankan
            koneksi.commit();
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);

            // rollback autoincrement kode transaksi gagal dijalankan
            koneksi.rollback();
        }

    }

    private int hitungDigitAngka(int angka) {

        int count = 0;

        while (angka != 0) {

            angka /= 10;

            count++;
        }

        return count;
    }

    // Digunakan untuk memisahkan angka dengan huruf   
    private String hitungKarakter(String karakterTemp) {

        String karakter;
        String karakter2;
        String karakter3;
        String karakter4;
        String karakter5;
        String karakter6;
        String karakter7;
        String karakter8;
        String karakter9;
        String karakter10;
        String karakter11;
        String karakter12;

        String str = new String(karakterTemp);

        if (str.length() == 3) {

            // Digit angka puluhan               
            karakter2 = String.valueOf(str.charAt(str.length() - 2));

            // Digit angka satuan               
            karakter = String.valueOf(str.charAt(str.length() - 1));

            // Penggabungan karakter 1 dan 2       
            this.hasilPecahanChar = karakter2 + karakter;

        } else if (str.length() == 4) {

            // Digit angka ratusan               
            karakter3 = String.valueOf(str.charAt(str.length() - 3));

            // Digit angka puluhan               
            karakter2 = String.valueOf(str.charAt(str.length() - 2));

            // Digit angka satuan               
            karakter = String.valueOf(str.charAt(str.length() - 1));

            // Penggabungan karakter 1, 2, 3      
            this.hasilPecahanChar = karakter3 + karakter2 + karakter;
        } else if (str.length() == 5) {

            // Digit angka ribuan               
            karakter4 = String.valueOf(str.charAt(str.length() - 4));

            // Digit angka ratusan               
            karakter3 = String.valueOf(str.charAt(str.length() - 3));

            // Digit angka puluhan               
            karakter2 = String.valueOf(str.charAt(str.length() - 2));

            // Digit angka satuan               
            karakter = String.valueOf(str.charAt(str.length() - 1));

            // Penggabungan karakter 1, 2, 3, 4
            this.hasilPecahanChar = karakter4 + karakter3 + karakter2 + karakter;
        } else if (str.length() == 6) {

            // Digit angka puluhan ribu               
            karakter5 = String.valueOf(str.charAt(str.length() - 5));

            // Digit angka ribuan
            karakter4 = String.valueOf(str.charAt(str.length() - 4));

            // Digit angka ratusan
            karakter3 = String.valueOf(str.charAt(str.length() - 3));

            // Digit angka puluhan
            karakter2 = String.valueOf(str.charAt(str.length() - 2));

            // Digit angka satuan               
            karakter = String.valueOf(str.charAt(str.length() - 1));

            // Penggabungan karakter 1, 2, 3, 4, 5    
            this.hasilPecahanChar = karakter5 + karakter4 + karakter3 + karakter2 + karakter;
        }

        return hasilPecahanChar;
    }

    private void SearchBarang() {

        // Jika TextField search kosong       
        if (txtSearchBarang.getText().equals("")) {

            txtSearchBarang.requestFocus();

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
                        + " WHERE kode = '" + txtSearchBarang.getText() + "' || nama LIKE '%" + txtSearchBarang.getText() + "%' || kategori LIKE '%" + txtSearchBarang.getText() + "%'";

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

//                txtKode.setEnabled(true);
            } catch (Exception e) {
            }
        }
    }

    private void SearchTransaksi() {

        // Jika TextField search kosong       
        if (txtSearchTransaksi.getText().equals("")) {

            txtSearchTransaksi.requestFocus();

        } else {

            DefaultTableModel tb1 = new DefaultTableModel();
            tb1.addColumn("id_transaksi");
            tb1.addColumn("nama");
            tb1.addColumn("harga");
            tb1.addColumn("jumlah");
            tb1.addColumn("subtotal");
            tabelTransaksi.setModel(tb1);

            ambilKodeTransaksiTerakhir();
            try {

                sql = "SELECT *"
                        + " FROM tb_detail_transaksi"
                        + " WHERE subtotal = '" + txtSearchTransaksi.getText() + "' || nama LIKE '%" + txtSearchTransaksi.getText() + "%' AND transaksi_id LIKE '%" + txtKodeTransaksi.getText() + "%'";

                koneksi = jaringan.Jaringan.config();
                pernyataan = koneksi.createStatement();
                hasil = pernyataan.executeQuery(sql);

                while (hasil.next()) {

                    tb1.addRow(new Object[]{
                        hasil.getString("transaksi_id"),
                        hasil.getString("nama"),
                        hasil.getString("harga"),
                        hasil.getString("jumlah"),
                        hasil.getString("subtotal"),});
                }

                tabelTransaksi.setModel(tb1);
            } catch (Exception e) {
            }
        }
    }

    public void functionEdit() {

        if (txtKodeTransaksi.getText().equals("") || txtNamaBarang.getText().equals("") || txtHarga.getText().equals("") || txtJumlah.getText().equals("") || txtSubtotal.getText().equals("")) {

            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih! Silahkan memilih data lagi");
        } else {

            txtJumlah.setEnabled(true);

            tombol_mati();

            txtJumlah.requestFocus();

            btnTambah.setEnabled(true);
            btnTambah.setText("Update");

            btnEdit.setEnabled(false);

            txtKodeTransaksi.setEnabled(false);
            txtKodeBarang.setEnabled(false);
        }
    }

    private void functionTotalTagihan() {

        try {

            sql = "SELECT SUM(subtotal) as jumlah"
                    + " FROM tb_detail_transaksi"
                    + " WHERE transaksi_id = (SELECT MAX(id_transaksi) AS id_transaksi"
                    + " FROM tb_transaksi)";

            koneksi = jaringan.Jaringan.config();
            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);

            while (hasil.next()) {

                txtTotalTagihan.setText(hasil.getString("jumlah"));
            }
        } catch (Exception e) {
        }
    }

    private void bayar() throws SQLException {

        String uang = txtCash.getText();
        String totalTagihan = txtTotalTagihan.getText();
        String ConvHasil, namaBarang, transaksi_id;

        int ConvUang = Integer.parseInt(uang);
        int ConvTotalTagihan = Integer.parseInt(totalTagihan);
        int hasil;

        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this, "Anda Ingin Membayar Transaksi Ini?", "Konfirmasi Pembayaran Transaksi", dialogButton);

        if (dialogResult == 0) {

            // Uang yang dibayarkan harus lebih besar / sama dengan nominal total tagihan        
            if (ConvUang < ConvTotalTagihan) {

                JOptionPane.showMessageDialog(null, "Uang yang harus dibayarkan harus melebihi atau sama dengan total tagihan!");
            } else {

                if (txtCash.getText().equals("-") || txtCash.getText().equals(".") || txtCash.getText().equals(",")
                        || txtCash.getText().equals("_") || txtCash.getText().equals("k")
                        || txtCash.getText().equals("K")) {

                    JOptionPane.showMessageDialog(null, "Format Angka yang Anda Masukkan Kurang Tepat!");
                } else {

                    hasil = ConvUang - ConvTotalTagihan;
                    ConvHasil = Integer.toString(hasil);

                    txtKembalian.setText(ConvHasil);

                    ambilKodeTransaksiTerakhir();
                    // Mengganti status pembayaran pada transaksi yang terkait           
                    try {

                        //Mengatur agar tidak langsung di commit ketika proses transaksi terjadi                
                        koneksi = jaringan.Jaringan.config();
                        koneksi.setAutoCommit(false);

                        sql = "UPDATE tb_transaksi"
                                + " SET status_pembayaran = 'Selesai', total_harga =" + txtTotalTagihan.getText() + ", nominal_pembayaran =" + txtCash.getText() + ", kembalian = " + txtKembalian.getText()
                                + " WHERE id_transaksi LIKE '%" + txtKodeTransaksi.getText() + "%'";

                        pernyataan = koneksi.createStatement();
                        pernyataan.execute(sql);

                        // Mengurangi stok yang sudah ada dengan jumlah barang yang dibeli
                        sql = "SELECT *"
                                + " FROM tb_detail_transaksi"
                                + " WHERE transaksi_id LIKE '%" + txtKodeTransaksi.getText() + "%'";

                        pernyataan = koneksi.createStatement();
                        this.hasil = pernyataan.executeQuery(sql);

                        while (this.hasil.next()) {

                            namaBarang = this.hasil.getString("nama");
                            transaksi_id = this.hasil.getString("transaksi_id");

                            sql2 = "UPDATE tb_barang"
                                    + " SET stok = (SELECT (tb_barang.stok - tb_detail_transaksi.jumlah) AS stok_update"
                                    + " FROM tb_barang"
                                    + " INNER JOIN tb_detail_transaksi ON (tb_detail_transaksi.nama = tb_barang.nama)"
                                    + " WHERE tb_detail_transaksi.nama LIKE '%" + namaBarang + "%' AND tb_detail_transaksi.transaksi_id LIKE '%" + transaksi_id + "%')"
                                    + " WHERE nama LIKE '%" + namaBarang + "%'";

                            pernyataan = koneksi.createStatement();
                            pernyataan.execute(sql2);
                        }

                        // commit jika sistem bayar berhasil dijalankan
                        koneksi.commit();

                        JOptionPane.showMessageDialog(null, "Pembayaran Berhasil");
                    } catch (Exception e) {

                        JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);

                        // commit jika sistem bayar berhasil dijalankan
                        koneksi.rollback();
                    }
                }
            }
        } else {

            JOptionPane.showMessageDialog(null, "Pembayaran Pada Transaksi Ini Dibatalkan");

        }
    }

    private void subtotalHargaBarang() {

        String jumlah = txtJumlah.getText();
        String harga = txtHarga.getText();

        if (!jumlah.isEmpty() && !harga.isEmpty()) {

            int ConvJumlah = Integer.parseInt(jumlah);
            int ConvHarga = Integer.parseInt(harga);
            int hasil;

            if (jumlah.equals("-") || jumlah.equals(".") || jumlah.equals(",")
                    || jumlah.equals("_") || jumlah.equals("k") || jumlah.equals("K")) {

                JOptionPane.showMessageDialog(null, "Format Angka yang Anda Masukkan Kurang Tepat!");
            } else {

                hasil = ConvJumlah * ConvHarga;

                String ConvHasil = Integer.toString(hasil);

                txtSubtotal.setText(ConvHasil);
            }
        }
    }

    private void TransaksiBaru() throws SQLException {

        int dialogButton = JOptionPane.YES_NO_OPTION;
        int dialogResult = JOptionPane.showConfirmDialog(this, "Anda Ingin Membuat Transaksi Baru?", "Konfirmasi Pembuatan Transaksi Baru", dialogButton);

        if (dialogResult == 0) {

            AutoIncrement();

            tidak_bisa_isi();

            isi_tableTransaksi();
            isi_tableBarang();
        } else {

            JOptionPane.showMessageDialog(null, "Pembuatan Transaksi Gagal");
        }
    }

    private void printData() {

        if (txtCash.getText().equals("") && txtKembalian.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Tidak Ada Nilai Pada Kolom Pembayaran!");
        } else if (txtKodeTransaksi.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Kolom Kode Transaksi Kosong!");
        } else if (txtTotalTagihan.getText().equals("")) {
            JOptionPane.showMessageDialog(null, "Kolom Total Tagihan Kosong!");
        } else {
            try {

                File report = new File("src/report/report_detail_transaksi_final.jasper");

                JasRep = (JasperReport) JRLoader.loadObject(report);

                param.clear();

                param.put("id_tr", txtKodeTransaksi.getText());
                param.put("total_tagihan", txtTotalTagihan.getText());
                param.put("nominal_pembayaran", txtCash.getText());
                param.put("nominal_kembalian", txtKembalian.getText());

                koneksi = jaringan.Jaringan.config();

                JasPri = JasperFillManager.fillReport(JasRep, param, koneksi);
                JasperViewer.viewReport(JasPri, false);
                JasperViewer.setDefaultLookAndFeelDecorated(true);

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, e);
            }
        }
    }

    private void ambilKodeTransaksiTerakhir() {

        // Digunakan untuk megambil kode transaksi yang paling terakhir       
        try {
            sql = "SELECT *"
                    + " FROM tb_transaksi"
                    + " ORDER BY kode_tr_angka DESC"
                    + " LIMIT 0, 1";

            koneksi = jaringan.Jaringan.config();
            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);

            while (hasil.next()) {
                txtKodeTransaksi.setText(hasil.getString("id_transaksi"));
            }

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
        }
    }

    private void ambilKodeBarangTerkait() {

        try {

            sql = "SELECT * "
                    + " FROM tb_detail_transaksi"
                    + " WHERE nama LIKE '%" + txtNamaBarang.getText() + "%'";

            koneksi = jaringan.Jaringan.config();
            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);

            while (hasil.next()) {
                txtKodeBarang.setText(hasil.getString("barang_kode"));
            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
        }
    }

    private void mergePembelianBarang() throws SQLException {

        String nama_barang = txtNamaBarang.getText();
        String transaksi_id = txtKodeTransaksi.getText();
        int jumlah = Integer.parseInt(txtJumlah.getText());
        int subtotal = Integer.parseInt(txtSubtotal.getText());

        // Pengecekan kesamaan barang dengan data di database       
        try {

            //Mengatur agar tidak langsung di commit ketika proses transaksi terjadi                
            koneksi = jaringan.Jaringan.config();
            koneksi.setAutoCommit(false);

            sql = "SELECT *"
                    + " FROM tb_detail_transaksi"
                    + " WHERE nama LIKE '%" + nama_barang + "%' AND transaksi_id LIKE '%" + transaksi_id + "%'";

            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);

            while (hasil.next()) {

                int jumlah1 = Integer.parseInt(hasil.getString("jumlah"));
                int subtotal1 = Integer.parseInt(hasil.getString("subtotal"));

                int jumlah_hasil = jumlah1 + jumlah;
                int subtotal_hasil = subtotal1 + subtotal;

                // Mengupdate data detail_transaksi               
                sql2 = "UPDATE tb_detail_transaksi"
                        + " SET subtotal = " + subtotal_hasil + ", jumlah = " + jumlah_hasil
                        + " WHERE nama LIKE '%" + nama_barang + "%' AND transaksi_id LIKE '%" + transaksi_id + "%'";

                pernyataan = koneksi.createStatement();
                pernyataan.executeUpdate(sql2);

                // commit jika sistem bayar berhasil dijalankan
                koneksi.commit();
            }
        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);

            // commit jika sistem bayar berhasil dijalankan
            koneksi.rollback();
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

        jScrollPane3 = new javax.swing.JScrollPane();
        panelAtas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtKembalian = new javax.swing.JTextField();
        txtCash = new javax.swing.JTextField();
        btnBayar = new javax.swing.JButton();
        btnCetakBuktiPembayaran = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtSearchBarang = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelBarang = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtKodeTransaksi = new javax.swing.JTextField();
        txtKodeBarang = new javax.swing.JTextField();
        txtNamaBarang = new javax.swing.JTextField();
        txtHarga = new javax.swing.JTextField();
        txtJumlah = new javax.swing.JTextField();
        txtSubtotal = new javax.swing.JTextField();
        txtSearchTransaksi = new javax.swing.JTextField();
        btnReset = new javax.swing.JButton();
        btnTambah = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabelTransaksi = new javax.swing.JTable();
        jLabel11 = new javax.swing.JLabel();
        txtTotalTagihan = new javax.swing.JTextField();
        btnHapus = new javax.swing.JButton();
        btnNewTransaksi = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        btnSearchInputBarang = new javax.swing.JButton();
        btnSearchTransaksi = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1220, 1030));

        panelAtas.setBackground(new java.awt.Color(255, 255, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel1.setText("Transaksi");

        btnBack.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnKeluar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        btnRefresh.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelAtasLayout = new javax.swing.GroupLayout(panelAtas);
        panelAtas.setLayout(panelAtasLayout);
        panelAtasLayout.setHorizontalGroup(
            panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelAtasLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(195, 195, 195)
                .addComponent(btnRefresh)
                .addGap(18, 18, 18)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnKeluar)
                .addContainerGap())
        );
        panelAtasLayout.setVerticalGroup(
            panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelAtasLayout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addGroup(panelAtasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBack)
                    .addComponent(btnKeluar)
                    .addComponent(btnRefresh)
                    .addComponent(jLabel1))
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 0));

        jLabel13.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel13.setText("Cash");

        jLabel14.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel14.setText("Kembalian");

        btnBayar.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnBayar.setText("Bayar");
        btnBayar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBayarActionPerformed(evt);
            }
        });

        btnCetakBuktiPembayaran.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnCetakBuktiPembayaran.setText("Cetak Bukti Pembayaran");
        btnCetakBuktiPembayaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakBuktiPembayaranActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(txtCash, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(btnBayar)
                        .addGap(50, 50, 50)
                        .addComponent(btnCetakBuktiPembayaran))
                    .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txtCash, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBayar, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCetakBuktiPembayaran, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(0, 153, 0));

        jLabel2.setBackground(new java.awt.Color(204, 204, 204));
        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 0));
        jLabel2.setText("Cari Barang");

        tabelBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Kode Barang", "Nama Barang", "Harga", "Stok", "Kategori"
            }
        ));
        tabelBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelBarangMouseClicked(evt);
            }
        });
        tabelBarang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tabelBarangKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tabelBarang);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 0));
        jLabel3.setText("Kode Transaksi");

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 0));
        jLabel4.setText("Kode Barang");

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 0));
        jLabel5.setText("Nama Barang");

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 0));
        jLabel6.setText("Harga");

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 0));
        jLabel7.setText("Jumlah");

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 0));
        jLabel8.setText("Subtotal");

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 0));
        jLabel9.setText("Daftar Pembelian");

        txtJumlah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtJumlahActionPerformed(evt);
            }
        });
        txtJumlah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtJumlahKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtJumlahKeyTyped(evt);
            }
        });

        btnReset.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnReset.setText("Reset");
        btnReset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnResetActionPerformed(evt);
            }
        });

        btnTambah.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnTambah.setText("Tambah");
        btnTambah.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahActionPerformed(evt);
            }
        });

        tabelTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Id", "Nama", "Harga", "Jumlah", "Subtotal"
            }
        ));
        tabelTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelTransaksiMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tabelTransaksi);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 0));
        jLabel11.setText("Total Tagihan");

        btnHapus.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnHapus.setText("Hapus");
        btnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHapusActionPerformed(evt);
            }
        });

        btnNewTransaksi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnNewTransaksi.setText("New transaksi");
        btnNewTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewTransaksiActionPerformed(evt);
            }
        });

        jLabel12.setBackground(new java.awt.Color(204, 204, 204));
        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 0));
        jLabel12.setText("Cari Transaksi");

        btnSearchInputBarang.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSearchInputBarang.setText("Search");
        btnSearchInputBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchInputBarangActionPerformed(evt);
            }
        });

        btnSearchTransaksi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnSearchTransaksi.setText("Search");
        btnSearchTransaksi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchTransaksiActionPerformed(evt);
            }
        });

        btnEdit.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        btnEdit.setText("Edit");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1083, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel2)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(txtSearchBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 213, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(27, 27, 27)
                                            .addComponent(btnSearchInputBarang))
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(66, 66, 66)
                                    .addComponent(jLabel3)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtKodeTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(38, 38, 38))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(518, 518, 518)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addComponent(jLabel8)
                                            .addGap(73, 73, 73)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(btnReset, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(btnTambah)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(btnEdit, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanel2Layout.createSequentialGroup()
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel4)
                                                .addComponent(jLabel5)
                                                .addComponent(jLabel6)
                                                .addComponent(jLabel7))
                                            .addGap(32, 32, 32)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(txtKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(78, 78, 78)))))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addGap(18, 18, 18)
                                .addComponent(txtTotalTagihan, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(131, 131, 131)
                                .addComponent(btnHapus, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnNewTransaksi)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtSearchTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnSearchTransaksi)
                        .addGap(130, 130, 130))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearchBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtKodeTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(btnSearchInputBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKodeBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNamaBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(31, 31, 31)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtHarga, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtJumlah, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8)
                    .addComponent(txtSubtotal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTambah)
                    .addComponent(btnReset)
                    .addComponent(btnEdit))
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtSearchTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(btnSearchTransaksi))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtTotalTagihan, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnHapus)
                    .addComponent(btnNewTransaksi))
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jScrollPane4.setViewportView(jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelAtas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 1220, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(panelAtas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 689, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        getAccessibleContext().setAccessibleParent(jScrollPane1);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHapusActionPerformed
        // TODO add your handling code here:

        hapus();

        tombol_hidup();
        Kosong();
        functionTotalTagihan();
    }//GEN-LAST:event_btnHapusActionPerformed

    private void btnBayarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBayarActionPerformed

        try {
            // TODO add your handling code here:

            bayar();
            isi_tableBarang();
        } catch (SQLException ex) {
            Logger.getLogger(Form_Transaksi.class.getName()).log(Level.SEVERE, null, ex);
        }

        functionTotalTagihan();
    }//GEN-LAST:event_btnBayarActionPerformed

    private void btnCetakBuktiPembayaranActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakBuktiPembayaranActionPerformed
        // TODO add your handling code here:

        ambilKodeTransaksiTerakhir();
        printData();
        functionTotalTagihan();
    }//GEN-LAST:event_btnCetakBuktiPembayaranActionPerformed

    private void btnSearchInputBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchInputBarangActionPerformed
        // TODO add your handling code here:

        SearchBarang();
        functionTotalTagihan();
    }//GEN-LAST:event_btnSearchInputBarangActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:

        jikaAktif();

        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:

        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void tabelBarangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tabelBarangKeyPressed
        // TODO add your handling code here:


    }//GEN-LAST:event_tabelBarangKeyPressed

    private void tabelBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelBarangMouseClicked
        // TODO add your handling code here:

        ambilDataTabelBarang();
        functionTotalTagihan();
    }//GEN-LAST:event_tabelBarangMouseClicked

    private void tabelTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelTransaksiMouseClicked
        // TODO add your handling code here:

        sistemTabelTransaksi();
        functionTotalTagihan();
    }//GEN-LAST:event_tabelTransaksiMouseClicked

    private void btnTambahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahActionPerformed
        // TODO add your handling code here:

        if (btnTambah.getText() == "Tambah") {

            try {
                Simpan();
            } catch (SQLException ex) {
                Logger.getLogger(Form_Transaksi.class.getName()).log(Level.SEVERE, null, ex);
            }

            btnTambah.setEnabled(false);
        } else {

            updateJumlah();

            tombol_hidup();
            Kosong();
        }

        functionTotalTagihan();
    }//GEN-LAST:event_btnTambahActionPerformed

    private void btnResetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnResetActionPerformed
        // TODO add your handling code here:

        Kosong();

        tombol_hidup();
        functionTotalTagihan();
    }//GEN-LAST:event_btnResetActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here

        functionEdit();
        functionTotalTagihan();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnSearchTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchTransaksiActionPerformed
        // TODO add your handling code here:

        SearchTransaksi();
        functionTotalTagihan();
    }//GEN-LAST:event_btnSearchTransaksiActionPerformed

    private void btnNewTransaksiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewTransaksiActionPerformed
        // TODO add your handling code here:

        Kosong();

        try {
            TransaksiBaru();
        } catch (SQLException ex) {
            Logger.getLogger(Form_Transaksi.class.getName()).log(Level.SEVERE, null, ex);
        }

        functionTotalTagihan();

        tombol_hidup();

    }//GEN-LAST:event_btnNewTransaksiActionPerformed

    private void txtJumlahActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtJumlahActionPerformed
        // TODO add your handling code here:


    }//GEN-LAST:event_txtJumlahActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:

        dispose();
        new Form_Transaksi().setVisible(true);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void txtJumlahKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyTyped
        // TODO add your handling code here:

//        subtotalHargaBarang();
    }//GEN-LAST:event_txtJumlahKeyTyped

    private void txtJumlahKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtJumlahKeyReleased
        // TODO add your handling code here:

        subtotalHargaBarang();
        functionTotalTagihan();
    }//GEN-LAST:event_txtJumlahKeyReleased

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
            java.util.logging.Logger.getLogger(Form_Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form_Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form_Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form_Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Form_Transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBayar;
    private javax.swing.JButton btnCetakBuktiPembayaran;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnHapus;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnNewTransaksi;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSearchInputBarang;
    private javax.swing.JButton btnSearchTransaksi;
    private javax.swing.JButton btnTambah;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JPanel panelAtas;
    private javax.swing.JTable tabelBarang;
    private javax.swing.JTable tabelTransaksi;
    private javax.swing.JTextField txtCash;
    private javax.swing.JTextField txtHarga;
    private javax.swing.JTextField txtJumlah;
    private javax.swing.JTextField txtKembalian;
    private javax.swing.JTextField txtKodeBarang;
    private javax.swing.JTextField txtKodeTransaksi;
    private javax.swing.JTextField txtNamaBarang;
    private javax.swing.JTextField txtSearchBarang;
    private javax.swing.JTextField txtSearchTransaksi;
    private javax.swing.JTextField txtSubtotal;
    private javax.swing.JTextField txtTotalTagihan;
    // End of variables declaration//GEN-END:variables
}
