/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Menubar_login;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.lang.String;
import jaringan.Jaringan;
import java.awt.event.KeyEvent;


/**
 *
 * @author adham PSN
 */
public class Form_Kategori extends javax.swing.JFrame {
    
    private Connection koneksi;
    private Statement pernyataan;
    private ResultSet hasil;
    private String sql;
    private String hasilPecahanChar;
    private int cekTombol = 0;
    

    /**
     * Creates new form Form_Kategori
     */
    public Form_Kategori() {
        initComponents();
        
        Kosong();
        tidak_bisa_isi();
        keyPressed();
        placeHolder();
        
        isi_table();
    }
    
    
    private void Kosong() {
    
        txtKode.setText("");
        txtNama.setText("");
        
    }
    
    
    private void bisa_isi() {
    
        txtKode.setEnabled(false);
        txtNama.setEnabled(true);
    }
    
    
    private void tidak_bisa_isi() {
    
        txtKode.setEnabled(false);
        txtNama.setEnabled(false);
    }
    
    
    private void tombol_mati() {
    
        btnSave.setText("Simpan");
        btnSave.setEnabled(false);
        btnDelete.setEnabled(false);
        btnEdit.setEnabled(false);
        btnBaru.setEnabled(false);
    }
    
    
    private void tombol_hidup() {
    
        btnSave.setText("Simpan");
        btnSave.setEnabled(true);
        btnEdit.setEnabled(true);
        btnDelete.setEnabled(true);
        btnBaru.setEnabled(true);
    }
    
    
    private void placeHolder() {
        
        txtKode.setToolTipText("TextField Kode");
        txtNama.setToolTipText("TextField Nama");
        txtSearch.setToolTipText("TextField Search");
    }
    
    
    private void keyPressed() {
        
        btnBaru.setMnemonic(KeyEvent.VK_B);
        btnSearch.setMnemonic(KeyEvent.VK_SLASH);
        btnKembali.setMnemonic(KeyEvent.VK_F4);
        btnCancel.setMnemonic(KeyEvent.VK_BACK_SPACE);
        btnRefresh.setMnemonic(KeyEvent.VK_R);
        btnDelete.setMnemonic(KeyEvent.VK_D);
        btnEdit.setMnemonic(KeyEvent.VK_E);
        
        if(btnSave.getText().equals("Simpan")) {
            
            btnSave.setMnemonic(KeyEvent.VK_ENTER);
        } else {
        
            btnSave.setMnemonic(KeyEvent.VK_U);
        }
    }
    
    
    private void Simpan() {
        
        // Jika tidak ada data yang dipilih        
        if(txtKode.getText().equals("") || txtNama.getText().equals("")) {
            
            JOptionPane.showMessageDialog(null, "Tidak ada data yang dimasukkan / Data yang dimasukkan kurang lengkap! Silahkan memasukkan data lagi");
            
        } else {
            
            try {
        
                sql = "insert into tb_kategori values"
                        + "('" + txtKode.getText() + "', '" + txtNama.getText() + "')";

                koneksi = jaringan.Jaringan.config();
                pernyataan = koneksi.createStatement();
                pernyataan.execute(sql);


            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
            }

            isi_table();
            tidak_bisa_isi();
            
            btnSave.setEnabled(false);
            btnEdit.setEnabled(true);
            btnDelete.setEnabled(true);
            btnKembali.setEnabled(true);
            btnBaru.setEnabled(true);
        }  
    }
    
    
    private void update() {
        
        // Jik tidak ada data yang dipilih       
        if(txtKode.getText().equals("") || txtNama.getText().equals("")) {
            
            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih! Silahkan memasukkan data lagi");
            
        } else {
            
            try {
        
                sql = "update tb_kategori"
                    + " set nama_kategori = '" + txtNama.getText() +"'"
                    + " where id_kategori = '" + txtKode.getText() + "'";

                koneksi = jaringan.Jaringan.config();
                pernyataan = koneksi.createStatement();
                pernyataan.executeUpdate(sql);

                JOptionPane.showMessageDialog(null, "Update Berhasil");
                
                Kosong();
            } catch (Exception e) {

                JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
            }

            isi_table();
        }
    }
    
    
    private void hapus() {
        
        // Jika tidak ada data yang dipilih        
        if(txtKode.getText().equals("") || txtNama.getText().equals("")) {
            
            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih! Silahkan memilih data lagi");
            
        } else {
            
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog(this, "Data Akan di Hapus", "Hapus Data", dialogButton);
            
            if(dialogResult != 0) {
            
                JOptionPane.showMessageDialog(null, "Batal di Hapus");
            } else {
                
                try {
        
                    sql = "DELETE" 
                            + " FROM tb_kategori"
                            + " WHERE id_kategori LIKE '%" + txtKode.getText() + "%' AND nama_kategori LIKE '%" + txtNama.getText() + "%'";

                    koneksi = jaringan.Jaringan.config();
                    pernyataan = koneksi.createStatement();
                    pernyataan.execute(sql);

                    JOptionPane.showMessageDialog(null, "Berhasil di Hapus");

                    isi_table();   
                    Kosong();
                } catch(Exception e) {

                    JOptionPane.showMessageDialog(null, "Keterangan Error :" + e);
                }
            }
        }
    }
    
    
    private void isi_table () {

        DefaultTableModel tb1 = new DefaultTableModel();
        tb1.addColumn("id_kategori");
        tb1.addColumn("nama_kategori");
        tabelKategori.setModel(tb1);
        
        try {
        
            sql = "Select * from tb_kategori";
            
            koneksi = jaringan.Jaringan.config();
            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);
            
            while(hasil.next()) {
            
                tb1.addRow(new Object[] {
                    
                    hasil.getString("id_kategori"),
                    hasil.getString("nama_kategori"),
                });
                
                tabelKategori.setModel(tb1);
            } 
        } catch (Exception e) {}
    }
    
    
    private void AutoIncrement() {
        
        String hasilHitung;
        String jumlah;
        int tambahKarakter;
        
        try {
            
            sql = "SELECT MAX(id_kategori) AS id_kategori, nama_kategori"
                    + " FROM tb_kategori";

            koneksi = jaringan.Jaringan.config();
            pernyataan = koneksi.createStatement();
            hasil = pernyataan.executeQuery(sql);
            
            while(hasil.next()) {
            
                // hasil dari query sql               
                hasilHitung = hitungKarakter(hasil.getString("id_kategori"));
               
                tambahKarakter = Integer.parseInt(hasilHitung) + 1;

                
                // pengkondisian berdasarkan digit angka               
                if(hitungDigitAngka(tambahKarakter) == 1) {
                
                    jumlah = "B" + 0 + String.valueOf(tambahKarakter);
                    
                    txtKode.setText(jumlah);
                } else if(hitungDigitAngka(tambahKarakter) == 0) {
                    
                    jumlah = "B"  + 0 + 1;
                    
                    txtKode.setText(jumlah);
                } else {
                
                    jumlah = "B"  + String.valueOf(tambahKarakter);
                    
                    txtKode.setText(jumlah);
                }
            }
        }  catch(Exception e) {} 
        
    }
    
    
    private int hitungDigitAngka(int angka) {
    
        int count = 0;
        
        while(angka != 0) {
        
            angka /= 10;
            
            count++;
        }
        
        return count;
    }
    
    
    // Digunakan untuk memisahkan angka dengan huruf   
    private String hitungKarakter(String karakterTemp ){
        
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
        
        
        if(str.length() == 3) {
                
            // Digit angka puluhan               
            karakter2 = String.valueOf(str.charAt(str.length() - 2));

            // Digit angka satuan               
            karakter = String.valueOf(str.charAt(str.length() - 1));

            // Penggabungan karakter 1 dan 2       
            this.hasilPecahanChar = karakter2 + karakter;
        } else if(str.length() == 4) {
        
            // Digit angka ratusan               
            karakter3 = String.valueOf(str.charAt(str.length() - 3));

            // Digit angka puluhan               
            karakter2 = String.valueOf(str.charAt(str.length() - 2));
            
            // Digit angka satuan               
            karakter = String.valueOf(str.charAt(str.length() - 1));

            // Penggabungan karakter 1, 2, 3      
            this.hasilPecahanChar = karakter3 + karakter2 + karakter;
        } else if(str.length() == 5) {
        
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
        } else if(str.length() == 6) {
        
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
    
    
    private void Search() {
        
         // Jika TextField search kosong       
        if(txtSearch.getText().equals("")) {
            
            txtSearch.requestFocus();
            
        } else {
            
            DefaultTableModel tb1 = new DefaultTableModel();
            tb1.addColumn("id_kategori");
            tb1.addColumn("nama_kategori");
            tabelKategori.setModel(tb1);
            
            try {

                sql = "select *"
                        + " from tb_kategori"
                        + " where nama_kategori like '%" + txtSearch.getText() + "%' || id_kategori = '" + txtSearch.getText() + "'";

                koneksi = jaringan.Jaringan.config();
                pernyataan = koneksi.createStatement();
                hasil = pernyataan.executeQuery(sql);

                while (hasil.next()) {

                    tb1.addRow(new Object[] {

                        hasil.getString("id_kategori"),
                        hasil.getString("nama_kategori"),
                    });

                    tabelKategori.setModel(tb1);
                }

                txtKode.setEnabled(false);

            } catch(Exception e) {}
        }
    }
    
    
    private void functionEdit() {
        
        if(txtKode.getText().equals("") || txtNama.getText().equals("")) {
            
            JOptionPane.showMessageDialog(null, "Tidak ada data yang dipilih! Silahkan memilih data lagi");
        } else {
            
            bisa_isi();
            tombol_mati();
        
            txtNama.requestFocus();
            btnSave.setText("Update");
            btnSave.setEnabled(true);
            txtKode.setEnabled(false);
        }
        
    }
    
    
    public void refreshHalaman() {
    
        dispose();
        
        new Form_Kategori().setVisible(true);
    } 
            
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtKode = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabelKategori = new javax.swing.JTable();
        btnKembali = new javax.swing.JButton();
        btnBaru = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnRefresh = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("FORM KATEGORI");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("KODE");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("NAMA");

        txtKode.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtKode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtKodeActionPerformed(evt);
            }
        });

        txtNama.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtNama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNamaActionPerformed(evt);
            }
        });

        btnSearch.setText("Search");
        btnSearch.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        btnSave.setText("Save");
        btnSave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnEdit.setText("Edit");
        btnEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnCancel.setText("Cancel");
        btnCancel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        tabelKategori.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        tabelKategori.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "ID KATEGORI", "NAMA KATEGORI"
            }
        ));
        tabelKategori.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tabelKategori.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tabelKategoriMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tabelKategori);

        btnKembali.setText("Kembali");
        btnKembali.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnKembali.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKembaliActionPerformed(evt);
            }
        });

        btnBaru.setText("Baru");
        btnBaru.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnBaru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBaruActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setText("SEARCH");

        txtSearch.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        txtSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        btnRefresh.setText("Refresh");
        btnRefresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(423, 423, 423))
            .addGroup(layout.createSequentialGroup()
                .addGap(117, 117, 117)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 818, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel2))
                            .addComponent(jLabel4))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 597, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(39, 39, 39)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46)
                        .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(145, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(77, 77, 77)
                .addComponent(jLabel1)
                .addGap(59, 59, 59)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtKode, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(47, 47, 47)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBaru, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(82, 82, 82)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnKembali, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRefresh, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(1102, 867));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        
        Search();
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:       
        
        if(btnSave.getText() == "Simpan") {
            
            Simpan();
        } else {
        
            update();
            tombol_hidup();
            tidak_bisa_isi();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        
        functionEdit();
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
        
        hapus();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void txtNamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNamaActionPerformed

    private void tabelKategoriMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tabelKategoriMouseClicked
        // TODO add your handling code here:
        
        // untuk mendisable editing column pada tabel       
        tabelKategori.setDefaultEditor(Object.class, null);
        
        int tabelData = tabelKategori.getSelectedRow();
        
        txtKode.setText(tabelKategori.getValueAt(tabelData, 0).toString());
        txtNama.setText(tabelKategori.getValueAt(tabelData, 1).toString());
        
        tombol_hidup();
    }//GEN-LAST:event_tabelKategoriMouseClicked

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        // TODO add your handling code here:
        
        JOptionPane.showMessageDialog(null, "Batal");
        
        Kosong();
        tidak_bisa_isi();
        tombol_hidup();
        
        btnKembali.setEnabled(true);
        
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtKodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtKodeActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtKodeActionPerformed

    private void btnKembaliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKembaliActionPerformed
        // TODO add your handling code here:

        dispose();
        
        new AdminToko().setVisible(true);
    }//GEN-LAST:event_btnKembaliActionPerformed

    private void btnBaruActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBaruActionPerformed
        // TODO add your handling code here:
        
        bisa_isi();
        Kosong();
        
        txtKode.setEnabled(false);
        txtNama.requestFocus();
        
        btnEdit.setEnabled(false);
        btnDelete.setEnabled(false);
        btnKembali.setEnabled(false);
        btnBaru.setEnabled(false);
        
        btnSave.setText("Simpan");
        btnSave.setEnabled(true);
        
        AutoIncrement();
        keyPressed();
        
    }//GEN-LAST:event_btnBaruActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        // TODO add your handling code here:
        
        refreshHalaman();
    }//GEN-LAST:event_btnRefreshActionPerformed

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
            java.util.logging.Logger.getLogger(Form_Kategori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Form_Kategori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Form_Kategori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Form_Kategori.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Form_Kategori().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBaru;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnKembali;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tabelKategori;
    private javax.swing.JTextField txtKode;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}
