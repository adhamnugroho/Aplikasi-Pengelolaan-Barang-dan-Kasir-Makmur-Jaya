-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 07 Apr 2023 pada 10.08
-- Versi server: 10.4.27-MariaDB
-- Versi PHP: 8.2.0

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `makmurjaya`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_barang`
--

CREATE TABLE `tb_barang` (
  `kode` int(11) NOT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `harga` int(11) DEFAULT NULL,
  `stok` int(11) DEFAULT NULL,
  `kategori` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_barang`
--

INSERT INTO `tb_barang` (`kode`, `nama`, `harga`, `stok`, `kategori`) VALUES
(2, 'pocari', 1000, 9990, 'minuman'),
(3, 'gurami bakar', 10000, 616, 'Makanan'),
(4, 'Milo', 100000, 9964, 'minuman'),
(5, 'baks', 100000, 9938, 'Makanan'),
(6, 'cilor', 10000, 9990, 'minuman'),
(9, 'martabak', 1000, 975, 'Makanan'),
(10, 'terang bulan', 100000, 9984, 'Makanan'),
(11, 'kopi tubruk', 10000, 9053, 'minuman'),
(12, 'yakult', 1200, 118872, 'Pork'),
(13, 'baru', 120000, 8890, 'pork'),
(14, 'jamu', 10000, 8782, 'minuman');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_detail_transaksi`
--

CREATE TABLE `tb_detail_transaksi` (
  `id_transaksi_detail` int(11) NOT NULL,
  `transaksi_id` varchar(5) DEFAULT NULL,
  `barang_kode` int(11) DEFAULT NULL,
  `nama` varchar(50) DEFAULT NULL,
  `jumlah` int(11) DEFAULT NULL,
  `harga` int(11) DEFAULT NULL,
  `subtotal` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_detail_transaksi`
--

INSERT INTO `tb_detail_transaksi` (`id_transaksi_detail`, `transaksi_id`, `barang_kode`, `nama`, `jumlah`, `harga`, `subtotal`) VALUES
(72, 'P01', 4, 'Milo', 2, 100000, 200000),
(73, 'P01', 12, 'yakult', 2, 1200, 2400),
(74, 'P02', 4, 'Milo', 2, 100000, 200000),
(75, 'P02', 10, 'terang bulan', 2, 100000, 200000),
(76, 'P03', 10, 'terang bulan', 2, 100000, 200000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_kategori`
--

CREATE TABLE `tb_kategori` (
  `id_kategori` varchar(10) NOT NULL,
  `nama_kategori` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_kategori`
--

INSERT INTO `tb_kategori` (`id_kategori`, `nama_kategori`) VALUES
('B13', 'Boba'),
('B11', 'kopi'),
('B01', 'Makanan'),
('B02', 'minuman'),
('B03', 'Pork'),
('B09', 'sapi'),
('B07', 'Sosis'),
('B12', 'Steak'),
('B10', 'Wedang');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_login`
--

CREATE TABLE `tb_login` (
  `id` int(100) NOT NULL,
  `username` varchar(200) DEFAULT NULL,
  `password` varchar(25) DEFAULT NULL,
  `akses` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_login`
--

INSERT INTO `tb_login` (`id`, `username`, `password`, `akses`) VALUES
(1, 'kasir', '123', 'kasir'),
(2, 'admin', '456', 'admin');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_transaksi`
--

CREATE TABLE `tb_transaksi` (
  `kode_tr_angka` int(11) NOT NULL,
  `id_transaksi` varchar(5) NOT NULL,
  `status_pembayaran` enum('Belum_Selesai','Selesai') DEFAULT NULL,
  `total_harga` bigint(20) NOT NULL,
  `nominal_pembayaran` bigint(20) NOT NULL,
  `kembalian` bigint(20) NOT NULL,
  `tanggal_pembelian` date NOT NULL,
  `created_at` timestamp NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_transaksi`
--

INSERT INTO `tb_transaksi` (`kode_tr_angka`, `id_transaksi`, `status_pembayaran`, `total_harga`, `nominal_pembayaran`, `kembalian`, `tanggal_pembelian`, `created_at`) VALUES
(1, 'P01', 'Selesai', 202400, 300000, 97600, '2023-03-03', '2023-03-03 05:55:20'),
(2, 'P02', 'Selesai', 400000, 400000, 0, '2023-03-04', '2023-03-03 17:32:43'),
(3, 'P03', 'Selesai', 200000, 300000, 100000, '2023-03-18', '2023-03-18 04:17:35');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `tb_barang`
--
ALTER TABLE `tb_barang`
  ADD PRIMARY KEY (`kode`),
  ADD KEY `kategori` (`kategori`);

--
-- Indeks untuk tabel `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  ADD PRIMARY KEY (`id_transaksi_detail`),
  ADD KEY `tb_transaksi_detail_dan_tb_transaksi` (`transaksi_id`),
  ADD KEY `tb_detail_transaksi_dan_tb_barang` (`barang_kode`);

--
-- Indeks untuk tabel `tb_kategori`
--
ALTER TABLE `tb_kategori`
  ADD PRIMARY KEY (`nama_kategori`);

--
-- Indeks untuk tabel `tb_login`
--
ALTER TABLE `tb_login`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `tb_transaksi`
--
ALTER TABLE `tb_transaksi`
  ADD PRIMARY KEY (`id_transaksi`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  MODIFY `id_transaksi_detail` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=77;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `tb_barang`
--
ALTER TABLE `tb_barang`
  ADD CONSTRAINT `tb_barang_dan_tb_kategori` FOREIGN KEY (`kategori`) REFERENCES `tb_kategori` (`nama_kategori`);

--
-- Ketidakleluasaan untuk tabel `tb_detail_transaksi`
--
ALTER TABLE `tb_detail_transaksi`
  ADD CONSTRAINT `tb_detail_transaksi_dan_tb_barang` FOREIGN KEY (`barang_kode`) REFERENCES `tb_barang` (`kode`),
  ADD CONSTRAINT `tb_transaksi_detail_dan_tb_transaksi` FOREIGN KEY (`transaksi_id`) REFERENCES `tb_transaksi` (`id_transaksi`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
