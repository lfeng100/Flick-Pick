USE flick_pick;

-- Delete existing data to avoid duplication
DELETE FROM Preferences;
DELETE FROM Reviews;
DELETE FROM GroupUsers;
DELETE FROM `Groups`;
DELETE FROM Users;

-- Insert 20 mock users
INSERT INTO Users (userID, email, username) VALUES
('1', 'user1@example.com', 'User1'),
('2', 'user2@example.com', 'User2'),
('3', 'user3@example.com', 'User3'),
('4', 'user4@example.com', 'User4'),
('5', 'user5@example.com', 'User5'),
('6', 'user6@example.com', 'User6'),
('7', 'user7@example.com', 'User7'),
('8', 'user8@example.com', 'User8'),
('9', 'user9@example.com', 'User9'),
('10', 'user10@example.com', 'User10'),
('11', 'user11@example.com', 'User11'),
('12', 'user12@example.com', 'User12'),
('13', 'user13@example.com', 'User13'),
('14', 'user14@example.com', 'User14'),
('15', 'user15@example.com', 'User15'),
('16', 'user16@example.com', 'User16'),
('17', 'user17@example.com', 'User17'),
('18', 'user18@example.com', 'User18'),
('19', 'user19@example.com', 'User19'),
('20', 'user20@example.com', 'User20');

-- Insert Preferences
INSERT INTO Preferences (userID, tagID) VALUES
('1', 'action'), ('1', 'drama'), ('1', 'comedy'),
('2', 'thriller'), ('2', 'horror'), ('2', 'adventure'),
('3', 'science_fiction'), ('3', 'fantasy'), ('3', 'mystery'),
('4', 'romance'), ('4', 'drama'), ('4', 'history'),
('5', 'action'), ('5', 'comedy'), ('5', 'animation'),
('6', 'adventure'), ('6', 'science_fiction'), ('6', 'war'),
('7', 'documentary'), ('7', 'war'), ('7', 'history'),
('8', 'crime'), ('8', 'thriller'), ('8', 'mystery'),
('9', 'western'), ('9', 'war'), ('9', 'romance'),
('10', 'action'), ('10', 'thriller'), ('10', 'drama'),
('11', 'science_fiction'), ('11', 'horror'), ('11', 'adventure'),
('12', 'comedy'), ('12', 'family'), ('12', 'animation'),
('13', 'mystery'), ('13', 'romance'), ('13', 'fantasy'),
('14', 'drama'), ('14', 'war'), ('14', 'thriller'),
('15', 'adventure'), ('15', 'western'), ('15', 'history'),
('16', 'animation'), ('16', 'family'), ('16', 'comedy'),
('17', 'horror'), ('17', 'mystery'), ('17', 'thriller'),
('18', 'science_fiction'), ('18', 'action'), ('18', 'war'),
('19', 'crime'), ('19', 'drama'), ('19', 'mystery'),
('20', 'fantasy'), ('20', 'history'), ('20', 'romance');

-- Insert Reviews
INSERT INTO Reviews (reviewID, rating, message, timestamp, userID, movieID) VALUES
('101', 4.5, 'Great movie!', NOW(), '1', '300'),
('102', 3.0, 'It was okay.', NOW(), '1', 'inception'),
('103', 5.0, 'Absolutely loved it!', NOW(), '2', '1917'),
('104', 4.0, 'Solid watch.', NOW(), '2', '10-cloverfield-lane'),
('105', 2.5, 'Not my type.', NOW(), '3', '50-first-dates'),
('106', 4.0, 'Really good!', NOW(), '3', '12-angry-men'),
('107', 3.5, 'Enjoyable.', NOW(), '4', '500-days-of-summer'),
('108', 5.0, 'Masterpiece!', NOW(), '4', '2001-a-space-odyssey'),
('109', 2.0, 'Boring.', NOW(), '5', '8-mile'),
('110', 3.5, 'Not bad.', NOW(), '5', 'interstellar'),
('111', 4.5, 'Amazing effects!', NOW(), '6', 'avatar'),
('112', 3.0, 'Decent watch.', NOW(), '6', '2046'),
('113', 4.0, 'Very interesting.', NOW(), '7', '1408'),
('114', 5.0, 'A must-watch!', NOW(), '7', '13-going-on-30'),
('115', 3.5, 'Had some great moments.', NOW(), '8', '2-fast-2-furious'),
('116', 4.0, 'Well done.', NOW(), '9', '7-days-in-hell'),
('117', 3.0, 'Mediocre.', NOW(), '10', 'blade-runner-2049');

-- Insert Groups
INSERT INTO `Groups` (groupID, groupName, adminUserID) VALUES
('g1', 'Sci-Fi Enthusiasts', '1'),
('g2', 'Drama Lovers', '5'),
('g3', 'Action Junkies', '10'),
('g4', 'Horror Club', '15'),
('g5', 'Classic Movie Fans', '20');

-- Insert GroupUsers
INSERT INTO GroupUsers (groupID, userID, joinedAt) VALUES
('g1', '1', NOW()), ('g1', '2', NOW()), ('g1', '3', NOW()), ('g1', '4', NOW()), ('g1', '5', NOW()),
('g2', '5', NOW()), ('g2', '6', NOW()), ('g2', '7', NOW()), ('g2', '8', NOW()),
('g3', '10', NOW()), ('g3', '11', NOW()), ('g3', '12', NOW()), ('g3', '13', NOW()), ('g3', '14', NOW()),
('g4', '15', NOW()), ('g4', '16', NOW()), ('g4', '17', NOW()), ('g4', '18', NOW()),
('g5', '20', NOW()), ('g5', '1', NOW()), ('g5', '3', NOW()), ('g5', '7', NOW());

COMMIT;
