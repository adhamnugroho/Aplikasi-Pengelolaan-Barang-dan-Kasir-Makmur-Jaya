/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Menubar_login;

import com.raven.datechooser.EventDateChooser;
import com.raven.datechooser.SelectedAction;
import com.raven.datechooser.SelectedDate;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author adham
 */
public class Laporan_Admin extends javax.swing.JFrame {

    private Connection koneksi;
    private Statement pernyataan;
    private ResultSet hasil;
    private String sql;
    private String sql2;

    //ireport
    JasperReport JasRep;
    JasperPrint JasPri;
    Map param = new HashMap();
    JasperDesign JasDes;
    InputStream jasperStream;

    /**
     * Creates new form Laporan_Admin
     */
    public Laporan_Admin() {
        initComponents();

        Kosong();
        sistemTabelTransaksi();
        isi_tableTransaksiSemua();
    }

    public void Kosong() {
        txtTanggalAwal.setText("");
        txtTanggalAkhir.setText("");
    }

    private void sistemTabelTransaksi() {

        // untuk mendisable editing column pada tabel       
        tabelTransaksi.setDefaultEditor(Object.class, null);
    }

    private void isi_tableTransaksi() {

        String[] gabungTanggal = ambilJarakTanggal();
        String tanggal_awal = gabungTanggal[0];
        String tanggal_akhir = gabungTanggal[1];

        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate tanggalHariIni = LocalDate.now();
        LocalDate tanggalAwalUbahDate = LocalDate.parse(tanggal_awal, formatterInput);
        LocalDate tanggalAkhirUbahDate = LocalDate.parse(tanggal_akhir, formatterInput);

        // Jika tanggal kosong       
        if (tanggal_awal.equals(String.valueOf(tanggalHariIni))) {

            txtTanggalAwal.setText(String.valueOf(tanggalAwalUbahDate.format(formatterOutput)));
        }

        if (tanggal_akhir.equals(String.valueOf(tanggalHariIni))) {

            txtTanggalAkhir.setText(String.valueOf(tanggalAkhirUbahDate.format(formatterOutput)));
        }

        // Pengecekan Jarak hari 
        long jarakHari = ChronoUnit.DAYS.between(tanggalAwalUbahDate, tanggalAkhirUbahDate);

        if (jarakHari < 0) {

            JOptionPane.showMessageDialog(null,
                    "Tanggal yang dipilih tidak tepat!");
        } else {

            try {

                DefaultTableModel tb1 = new DefaultTableModel();
                tb1.addColumn("id_transaksi");
                tb1.addColumn("total_harga");
                tb1.addColumn("tanggal_transaksi");
                tb1.addColumn("status_pembayaran");
                tabelTransaksi.setModel(tb1);

                sql = "SELECT *"
                        + " FROM tb_transaksi"
                        + " WHERE tanggal_pembelian BETWEEN '" + tanggal_awal + "' AND '" + tanggal_akhir + "'";

                koneksi = jaringan.Jaringan.config();
                pernyataan = koneksi.createStatement();
                hasil = pernyataan.executeQuery(sql);

                while (hasil.next()) {

                    LocalDate tanggalTransaksiRaw = LocalDate.parse(hasil.getString("tanggal_pembelian"), formatterInput);
                    String tanggalTransaksiUbahFormat = tanggalTransaksiRaw.format(formatterOutput);

                    tb1.addRow(new Object[]{
                        hasil.getString("id_transaksi"),
                        hasil.getString("total_harga"),
                        tanggalTransaksiUbahFormat,
                        hasil.getString("status_pembayaran"),});
                }

                tabelTransaksi.setModel(tb1);

            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "Keterangan Error : " + e);
            }
        }
    }

    private void isi_tableTransaksiSemua() {

        DefaultTableModel tb1 = new DefaultTableModel();
        tb1.addColumn("id_transaksi");
        tb1.addColumn("total_harga");
        tb1.addColumn("tanggal_transaksi");
        tb1.addColumn("status_pembayaran");
        tabelTransaksi.setModel(tb1);

        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        try {

            sql = "SELECT *"
                    + " FROM tb_transaksi";

            koneksi = jaringan.Jaringan.config();
            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);

            while (hasil.next()) {

                LocalDate tanggalTransaksiRaw = LocalDate.parse(hasil.getString("tanggal_pembelian"), formatterInput);
                String tanggalTransaksiUbahFormat = tanggalTransaksiRaw.format(formatterOutput);

                tb1.addRow(new Object[]{
                    hasil.getString("id_transaksi"),
                    hasil.getString("total_harga"),
                    tanggalTransaksiUbahFormat,
                    hasil.getString("status_pembayaran"),});
            }

            tabelTransaksi.setModel(tb1);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
        }
    }

    public String[] ambilJarakTanggal() {

        SelectedDate tanggal_awal = dateTanggalAwal.getSelectedDate();
        SelectedDate tanggal_akhir = dateTanggalAkhir.getSelectedDate();

        String tanggal1 = String.valueOf(tanggal_awal.getDay());
        String bulan1 = String.valueOf(tanggal_awal.getMonth());
        String tahun1 = String.valueOf(tanggal_awal.getYear());

        String tanggal2 = String.valueOf(tanggal_akhir.getDay());
        String bulan2 = String.valueOf(tanggal_akhir.getMonth());
        String tahun2 = String.valueOf(tanggal_akhir.getYear());

        // pengecekan tanggal       
        if (hitungDigitAngka(Integer.parseInt(tanggal1)) == 1) {

            tanggal1 = "0" + tanggal1;
        }

        if (hitungDigitAngka(Integer.parseInt(tanggal2)) == 1) {

            tanggal2 = "0" + tanggal2;
        }

        // pengecekan bulan       
        if (hitungDigitAngka(Integer.parseInt(bulan1)) == 1) {

            bulan1 = "0" + bulan1;
        }

        if (hitungDigitAngka(Integer.parseInt(bulan2)) == 1) {

            bulan2 = "0" + bulan2;
        }

        String tanggal_awal_database = tahun1 + "-" + bulan1 + "-" + tanggal1;
        String tanggal_akhir_database = tahun2 + "-" + bulan2 + "-" + tanggal2;

        String[] gabungTanggal = new String[]{tanggal_awal_database, tanggal_akhir_database};

        return gabungTanggal;
    }

    private int hitungDigitAngka(int angka) {

        int count = 0;

        while (angka != 0) {

            angka /= 10;

            count++;
        }

        return count;
    }

    private void printData() {

        String[] gabungTanggal = ambilJarakTanggal();
        String tanggal_awal = gabungTanggal[0];
        String tanggal_akhir = gabungTanggal[1];

        DateTimeFormatter formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatterOutput = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate tanggalHariIni = LocalDate.now();
        LocalDate tanggalAwalUbahDate = LocalDate.parse(tanggal_awal, formatterInput);
        LocalDate tanggalAkhirUbahDate = LocalDate.parse(tanggal_akhir, formatterInput);

        // Jika tanggal kosong       
        if (tanggal_awal.equals(String.valueOf(tanggalHariIni))) {

            txtTanggalAwal.setText(String.valueOf(tanggalAwalUbahDate.format(formatterOutput)));
        }

        if (tanggal_akhir.equals(String.valueOf(tanggalHariIni))) {

            txtTanggalAkhir.setText(String.valueOf(tanggalAkhirUbahDate.format(formatterOutput)));
        }

        // Pengecekan Jarak hari 
        long jarakHari = ChronoUnit.DAYS.between(tanggalAwalUbahDate, tanggalAkhirUbahDate);

        if (jarakHari < 0) {

            JOptionPane.showMessageDialog(null,
                    "Tanggal yang dipilih tidak tepat!");
        } else {

            try {

                koneksi = jaringan.Jaringan.config();

                sql = "SELECT *"
                        + " FROM tb_transaksi"
                        + " WHERE tanggal_pembelian BETWEEN '" + tanggal_awal + "' AND '" + tanggal_akhir + "'";

                pernyataan = koneksi.createStatement();
                hasil = pernyataan.executeQuery(sql);

                boolean valueHasilQuery = hasil.next();

                if (valueHasilQuery != true) {

                    JOptionPane.showMessageDialog(null,
                            "Tidak ada data transaksi dari tanggal "
                            + tanggalAwalUbahDate.format(formatterOutput) + " sampai dengan tanggal "
                            + tanggalAkhirUbahDate.format(formatterOutput));
                } else {

                    File report = new File("src/report/report_laporan_admin_final.jasper");

                    JasRep = (JasperReport) JRLoader.loadObject(report);

                    param.clear();
                    param.put("tanggal_awal", tanggal_awal);
                    param.put("tanggal_akhir", tanggal_akhir);
                    param.put("tanggal_awal_tampil", tanggalAwalUbahDate.format(formatterOutput));
                    param.put("tanggal_akhir_tampil", tanggalAkhirUbahDate.format(formatterOutput));

                    JasPri = JasperFillManager.fillReport(JasRep, param, koneksi);
                    JasperViewer.viewReport(JasPri, false);
                    JasperViewer.setDefaultLookAndFeelDecorated(true);
                }
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
            }
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

        dateTanggalAwal = new com.raven.datechooser.DateChooser();
        dateTanggalAkhir = new com.raven.datechooser.DateChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTanggalAwal = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtTanggalAkhir = new javax.swing.JTextField();
        btnCari = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelTransaksi = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnKeluar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        dateTanggalAwal.setForeground(new java.awt.Color(216, 63, 166));
        dateTanggalAwal.setTextRefernce(txtTanggalAwal);

        dateTanggalAkhir.setForeground(new java.awt.Color(216, 63, 166));
        dateTanggalAkhir.setTextRefernce(txtTanggalAkhir);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(56, 197, 201));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 30)); // NOI18N
        jLabel1.setText("Laporan Transaksi");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(306, 306, 306)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                .addGap(28, 28, 28))
        );

        jPanel2.setBackground(new java.awt.Color(255, 242, 176));

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        jLabel2.setText("Range Tanggal");

        txtTanggalAwal.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTanggalAwal.setToolTipText("Kolom Tanggal Awal");
        txtTanggalAwal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTanggalAwalActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 20)); // NOI18N
        jLabel3.setText("-");

        txtTanggalAkhir.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtTanggalAkhir.setToolTipText("Kolom Tanggal Akhir");
        txtTanggalAkhir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTanggalAkhirActionPerformed(evt);
            }
        });

        btnCari.setText("Cari");
        btnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCariActionPerformed(evt);
            }
        });

        jButton2.setText("Print");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        tabelTransaksi.setAutoCreateRowSorter(true);
        tabelTransaksi.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelTransaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id_transaksi", "total_harga", "tanggal", "status_pembayaran"
            }
        ));
        tabelTransaksi.setToolTipText("Tabel data transaksi");
        tabelTransaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelTransaksiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelTransaksi);

        jPanel3.setBackground(new java.awt.Color(56, 197, 201));

        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnKeluar.setText("Keluar");
        btnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKeluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btnRefresh)
                .addGap(30, 30, 30)
                .addComponent(btnBack)
                .addGap(27, 27, 27)
                .addComponent(btnKeluar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(39, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKeluar, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
        );

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("Tabel Data Transaksi");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(37, 37, 37)
                                .addComponent(txtTanggalAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addGap(18, 18, 18)
                                .addComponent(txtTanggalAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel4))
                        .addGap(0, 127, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(btnCari, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTanggalAwal, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTanggalAkhir, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(90, 90, 90)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtTanggalAwalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTanggalAwalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTanggalAwalActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:

        printData();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCariActionPerformed
        // TODO add your handling code here:

        isi_tableTransaksi();
    }//GEN-LAST:event_btnCariActionPerformed

    private void txtTanggalAkhirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTanggalAkhirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTanggalAkhirActionPerformed

    private void tabelTransaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelTransaksiMouseClicked
        // TODO add your handling code here:

        sistemTabelTransaksi();
    }//GEN-LAST:event_tabelTransaksiMouseClicked

    private void btnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKeluarActionPerformed
        // TODO add your handling code here:

        dispose();
    }//GEN-LAST:event_btnKeluarActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:

        dispose();
        new Laporan_Admin().setVisible(true);
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:

        dispose();
        new AdminToko().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

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
            java.util.logging.Logger.getLogger(Laporan_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Laporan_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Laporan_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Laporan_Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Laporan_Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnCari;
    private javax.swing.JButton btnKeluar;
    private javax.swing.JButton btnRefresh;
    private com.raven.datechooser.DateChooser dateTanggalAkhir;
    private com.raven.datechooser.DateChooser dateTanggalAwal;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelTransaksi;
    private javax.swing.JTextField txtTanggalAkhir;
    private javax.swing.JTextField txtTanggalAwal;
    // End of variables declaration//GEN-END:variables
}
