-- Creating the database `form_submission_db`
CREATE DATABASE IF NOT EXISTS `form_submission_db` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `form_submission_db`;

-- Creating `user` table
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `dob` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `reg_year` int NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `User_ID` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Inserting data into `user` table
INSERT INTO `user` VALUES
(1, '1996-10-16', 'kavindunuwandika@gmail.com', 'Kavindu', 'Wijekoon', '123456', '0714672020', 'Admin', 'USER_1', 2023, 'male'),
(2, '1985-02-15', 'john.doe@example.com', 'Johnyyy', 'Doeet', '123456', '0712345678', 'Student', 'USER_2', 2024, 'male'),
(3, '1992-06-30', 'jane.smith@example.com', 'Jane', 'Smith', 'password5678', '0755678123', 'Admin', 'USER_3', 2023, 'male'),
(4, '1990-09-20', 'emily.johnson@example.com', 'Emily', 'Johnson', 'emily1234', '0788765234', 'Student', 'USER_4', 2023, 'male'),
(5, '1987-01-10', 'michael.brown@example.com', 'Michael', 'Brown', 'michaelpass', '0701122334', 'Student', 'USER_5', 2023, 'male'),
(6, '1995-04-05', 'sarah.williams@example.com', 'Sarah', 'Williams', 'sarahpassword', '0783344556', 'Student', 'USER_6', 2023, 'male'),
(7, '1983-11-25', 'robert.davis@example.com', 'Robert', 'Davis', 'robertpassword123', '0775566778', 'Student', 'USER_7', 2023, 'male'),
(8, '2000-03-15', 'laura.martinez@example.com', 'Laura', 'Martinez', 'laura1234', '0727788990', 'Student', 'USER_8', 2023, 'male'),
(9, '1998-12-22', 'james.miller@example.com', 'James', 'Miller', 'jamespass123', '0799001122', 'Student', 'USER_9', 2023, 'male'),
(10, '1984-08-30', 'olivia.wilson@example.com', 'Olivia', 'Wilson', 'olivia1984', '0722233445', 'Student', 'USER_10', 2023, 'male'),
(11, '1996-07-01', 'daniel.moore@example.com', 'Daniel', 'Moore', 'danielmoorepass', '0784455667', 'Student', 'USER_11', 2023, 'male'),
(12, '1994-05-18', 'sophia.taylor@example.com', 'Sophia', 'Taylor', 'sophia1234', '0726677889', 'Student', 'USER_12', 2023, 'male'),
(13, '1989-10-12', 'david.anderson@example.com', 'David', 'Anderson', 'davidpass1989', '0778899001', 'Student', 'USER_13', 2023, 'male'),
(14, '1997-03-25', 'lily.thomas@example.com', 'Lily', 'Thomas', 'lilypassword', '0779901223', 'Student', 'USER_14', 2023, 'male'),
(15, '1982-06-17', 'william.jackson@example.com', 'William', 'Jackson', 'williampass1982', '0712345567', 'Teacher', 'USER_15', 2024, 'female'),
(16, '1993-04-20', 'isabella.white@example.com', 'Isabella', 'White', 'isabella2023', '0733345778', 'Student', 'USER_16', 2024, 'female'),
(17, '2001-01-13', 'elijah.harris@example.com', 'Elijah', 'Harris', 'elijah12345', '0755567889', 'Student', 'USER_17', 2024, 'female'),
(18, '1990-11-28', 'chloe.clark@example.com', 'Chloe', 'Clark', 'chloe@2023', '0786678990', 'Student', 'USER_18', 2024, 'female'),
(19, '1999-05-23', 'ethan.lewis@example.com', 'Ethan', 'Lewis', 'ethan1234', '0777788991', 'Student', 'USER_19', 2024, 'female'),
(20, '1992-02-09', 'ava.walker@example.com', 'Ava', 'Walker', 'avapassword', '0788899002', 'Student', 'USER_20', 2024, 'female'),
(27, '1996-04-25', 'isuridh@gmail.com', 'Isuri', 'Herath', '123456', '0769208398', 'Teacher', 'USER_27', 2024, 'female'),
(28, '1212-12-12', 'teststudent1@example.com', 'New', 'Test', 'test123', '0714672022', 'Student', 'USER_28', 2024, 'male');

-- Creating `classes` table
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes` (
  `course_id` bigint NOT NULL AUTO_INCREMENT,
  `course_code` varchar(255) NOT NULL,
  `academic_year` int NOT NULL,
  `course_name` varchar(255) NOT NULL,
  `credits` int NOT NULL,
  `main_subject` varchar(255) NOT NULL,
  `teacher_id` bigint DEFAULT NULL,
  PRIMARY KEY (`course_id`),
  UNIQUE KEY `UC_course_code_academic_year` (`course_code`, `academic_year`),
  KEY `FK_teacher` (`teacher_id`),
  CONSTRAINT `FK_teacher` FOREIGN KEY (`teacher_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Inserting data into `classes` table
INSERT INTO `classes` VALUES
(1, 'PH100', 2024, 'Basic Physics', 2, 'Physics', 27),
(8, 'CH100', 2024, 'Basic Chemistry', 2, 'Chemistry', 15),
(9, 'MT100', 2024, 'Basic Maths', 2, 'Maths', 27);

-- Creating `enrollments` table
DROP TABLE IF EXISTS `enrollments`;
CREATE TABLE `enrollments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL,
  `student_id` bigint NOT NULL,
  `marks` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `course_id` (`course_id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `enrollments_ibfk_1` FOREIGN KEY (`course_id`) REFERENCES `classes` (`course_id`) ON DELETE CASCADE,
  CONSTRAINT `enrollments_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Inserting data into `enrollments` table
INSERT INTO `enrollments` VALUES
(1, 1, 16, 60), (2, 1, 17, 60), (3, 1, 18, 60), (4, 1, 19, 70), (5, 1, 20, 80),
(6, 1, 28, NULL), (7, 1, 2, 83), (8, 8, 2, NULL), (9, 8, 4, NULL), (10, 8, 5, NULL),
(11, 8, 6, NULL), (12, 8, 7, NULL), (13, 8, 8, NULL), (14, 8, 9, NULL), (15, 8, 10, NULL),
(16, 8, 11, NULL), (17, 8, 12, NULL), (18, 8, 13, NULL), (19, 8, 14, NULL), (20, 8, 16, NULL),
(21, 8, 17, NULL), (22, 8, 18, NULL), (23, 8, 19, NULL), (24, 8, 20, NULL), (26, 9, 2, 80),
(27, 9, 28, 70);
