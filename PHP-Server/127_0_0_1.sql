-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Φιλοξενητής: 127.0.0.1
-- Χρόνος δημιουργίας: 24 Μάη 2017 στις 14:49:25
-- Έκδοση διακομιστή: 5.7.14
-- Έκδοση PHP: 5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Βάση δεδομένων: `sensostalker`
--
CREATE DATABASE IF NOT EXISTS `sensostalker` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `sensostalker`;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `hum`
--

CREATE TABLE `hum` (
  `humidid` int(11) NOT NULL,
  `humidit` int(11) NOT NULL,
  `hhour` time NOT NULL,
  `hdate` date NOT NULL,
  `divergence` int(11) NOT NULL,
  `idsens` enum('1','2') COLLATE utf8_unicode_ci NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `humh`
--

CREATE TABLE `humh` (
  `humidity` int(11) DEFAULT NULL,
  `humid` int(11) NOT NULL,
  `tumitimeh` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idsens` enum('1','2') COLLATE utf8_unicode_ci NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `humm`
--

CREATE TABLE `humm` (
  `humidity` int(11) DEFAULT NULL,
  `humid` int(11) NOT NULL,
  `tumitimeh` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idsens` enum('1','2') COLLATE utf8_unicode_ci NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `light`
--

CREATE TABLE `light` (
  `lightid` int(11) NOT NULL,
  `lightaver` int(11) NOT NULL,
  `lighttime` time NOT NULL,
  `lightday` date NOT NULL,
  `diverg` int(11) NOT NULL,
  `idsens` enum('1','2') COLLATE utf8_unicode_ci NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `lighth`
--

CREATE TABLE `lighth` (
  `idlighth` int(11) NOT NULL,
  `light` int(11) NOT NULL,
  `lighttimesh` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idsens` enum('1','2') COLLATE utf8_unicode_ci NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `temp`
--

CREATE TABLE `temp` (
  `idtem` int(11) NOT NULL,
  `avertemp` float NOT NULL,
  `hour` time NOT NULL,
  `day` date NOT NULL,
  `divergence` int(11) NOT NULL,
  `idsens` enum('1','2') COLLATE utf8_unicode_ci NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `temph`
--

CREATE TABLE `temph` (
  `idtemph` int(11) NOT NULL,
  `temperature` int(11) DEFAULT NULL,
  `timestemh` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idsens` enum('1','2') COLLATE utf8_unicode_ci NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `tempm`
--

CREATE TABLE `tempm` (
  `idtemph` int(11) NOT NULL,
  `temperature` int(11) DEFAULT NULL,
  `timestemh` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `idsens` enum('1','2') COLLATE utf8_unicode_ci NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Δομή πίνακα για τον πίνακα `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `unique_id` varchar(23) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `email` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `encrypted_password` varchar(80) COLLATE utf8_unicode_ci NOT NULL,
  `salt` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `admin` tinyint(1) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Ευρετήρια για άχρηστους πίνακες
--

--
-- Ευρετήρια για πίνακα `hum`
--
ALTER TABLE `hum`
  ADD PRIMARY KEY (`humidid`);

--
-- Ευρετήρια για πίνακα `humh`
--
ALTER TABLE `humh`
  ADD PRIMARY KEY (`humid`);

--
-- Ευρετήρια για πίνακα `humm`
--
ALTER TABLE `humm`
  ADD PRIMARY KEY (`humid`);

--
-- Ευρετήρια για πίνακα `light`
--
ALTER TABLE `light`
  ADD PRIMARY KEY (`lightid`);

--
-- Ευρετήρια για πίνακα `lighth`
--
ALTER TABLE `lighth`
  ADD PRIMARY KEY (`idlighth`),
  ADD UNIQUE KEY `idlighth` (`idlighth`);

--
-- Ευρετήρια για πίνακα `temp`
--
ALTER TABLE `temp`
  ADD PRIMARY KEY (`idtem`);

--
-- Ευρετήρια για πίνακα `temph`
--
ALTER TABLE `temph`
  ADD PRIMARY KEY (`idtemph`),
  ADD UNIQUE KEY `idtemph` (`idtemph`);

--
-- Ευρετήρια για πίνακα `tempm`
--
ALTER TABLE `tempm`
  ADD PRIMARY KEY (`idtemph`),
  ADD UNIQUE KEY `idtemph` (`idtemph`);

--
-- Ευρετήρια για πίνακα `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `unique_id` (`unique_id`,`email`);

--
-- AUTO_INCREMENT για άχρηστους πίνακες
--

--
-- AUTO_INCREMENT για πίνακα `hum`
--
ALTER TABLE `hum`
  MODIFY `humidid` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT για πίνακα `humh`
--
ALTER TABLE `humh`
  MODIFY `humid` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT για πίνακα `humm`
--
ALTER TABLE `humm`
  MODIFY `humid` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT για πίνακα `light`
--
ALTER TABLE `light`
  MODIFY `lightid` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT για πίνακα `lighth`
--
ALTER TABLE `lighth`
  MODIFY `idlighth` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT για πίνακα `temp`
--
ALTER TABLE `temp`
  MODIFY `idtem` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT για πίνακα `temph`
--
ALTER TABLE `temph`
  MODIFY `idtemph` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT για πίνακα `tempm`
--
ALTER TABLE `tempm`
  MODIFY `idtemph` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT για πίνακα `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
