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
('200', 4.1, 'Loved the acting and cinematography!', NOW(), '1', '101-dalmatians'),
('201', 2.2, 'Amazing movie! Highly recommend.', NOW(), '1', '8-mile'),
('202', 2.0, 'Enjoyable but forgettable.', NOW(), '1', '3-2003-1'),
('203', 2.1, 'Decent watch.', NOW(), '2', '30-minutes-or-less'),
('204', 4.1, 'Decent watch.', NOW(), '2', '13-hours-the-secret-soldiers-of-benghazi'),
('205', 2.9, 'A must-watch for movie lovers!', NOW(), '2', '25th-hour'),
('206', 2.7, 'Decent watch.', NOW(), '3', '2-or-3-things-i-know-about-her'),
('207', 4.8, 'It was okay, not the best.', NOW(), '3', '12-strong'),
('208', 3.1, 'Decent watch.', NOW(), '3', '47-meters-down'),
('209', 4.0, 'Had some great moments, but lacked depth.', NOW(), '3', '28-days-later'),
('210', 2.1, 'Enjoyable but forgettable.', NOW(), '3', '25th-hour'),
('211', 3.4, 'It was okay, not the best.', NOW(), '4', '24-frames'),
('212', 5.0, 'Decent watch.', NOW(), '4', '25th-hour'),
('213', 2.8, 'Had some great moments, but lacked depth.', NOW(), '4', '1917'),
('214', 2.4, 'Decent watch.', NOW(), '4', '6-underground'),
('215', 2.1, 'It was okay, not the best.', NOW(), '5', '21-jump-street'),
('216', 4.5, 'Amazing movie! Highly recommend.', NOW(), '5', '3-2003-1'),
('217', 2.4, 'Enjoyable but forgettable.', NOW(), '5', '13-sins'),
('218', 4.9, 'A true masterpiece!', NOW(), '5', '20-feet-from-stardom'),
('219', 2.1, 'Not my cup of tea.', NOW(), '5', '31-2016'),
('220', 4.0, 'Had some great moments, but lacked depth.', NOW(), '6', '8-bit-christmas'),
('221', 4.1, 'It was okay, not the best.', NOW(), '6', '31-2016'),
('222', 2.1, 'Had some great moments, but lacked depth.', NOW(), '6', '2-fast-2-furious'),
('223', 2.5, 'Loved the acting and cinematography!', NOW(), '7', '21-grams'),
('224', 4.9, 'A must-watch for movie lovers!', NOW(), '7', '3-idiots'),
('225', 2.8, 'Could have been better, but still enjoyable.', NOW(), '7', '7-days-in-hell'),
('226', 4.2, 'A true masterpiece!', NOW(), '7', '300-rise-of-an-empire'),
('227', 2.7, 'A must-watch for movie lovers!', NOW(), '7', '31-2016'),
('228', 4.4, 'A must-watch for movie lovers!', NOW(), '8', '6-underground'),
('229', 3.9, 'A must-watch for movie lovers!', NOW(), '8', '30-minutes-or-less'),
('230', 2.4, 'Amazing movie! Highly recommend.', NOW(), '8', '28-days'),
('231', 3.7, 'Had some great moments, but lacked depth.', NOW(), '8', '8-46-2020'),
('232', 4.3, 'Loved the acting and cinematography!', NOW(), '9', 'a-bad-moms-christmas'),
('233', 3.8, 'Enjoyable but forgettable.', NOW(), '9', '42'),
('234', 4.6, 'Could have been better, but still enjoyable.', NOW(), '9', '71-fragments-of-a-chronology-of-chance'),
('235', 3.6, 'Had some great moments, but lacked depth.', NOW(), '9', '8-46-2020'),
('236', 3.9, 'Had some great moments, but lacked depth.', NOW(), '10', '47-meters-down-uncaged'),
('237', 3.1, 'Enjoyable but forgettable.', NOW(), '10', '30-days-of-night'),
('238', 4.3, 'Not my cup of tea.', NOW(), '10', '2-fast-2-furious'),
('239', 2.6, 'A must-watch for movie lovers!', NOW(), '10', '99-homes'),
('240', 4.0, 'Not my cup of tea.', NOW(), '11', '8mm'),
('241', 3.7, 'Could have been better, but still enjoyable.', NOW(), '11', '13-going-on-30'),
('242', 3.0, 'Had some great moments, but lacked depth.', NOW(), '11', '16-wishes'),
('243', 3.3, 'Had some great moments, but lacked depth.', NOW(), '11', '1941'),
('244', 4.0, 'Decent watch.', NOW(), '12', '101-dalmatians'),
('245', 2.5, 'A must-watch for movie lovers!', NOW(), '12', '71'),
('246', 3.0, 'Decent watch.', NOW(), '12', '24-frames'),
('247', 4.4, 'Had some great moments, but lacked depth.', NOW(), '12', '31-2016'),
('248', 4.8, 'It was okay, not the best.', NOW(), '13', '12-angry-men'),
('249', 2.1, 'Decent watch.', NOW(), '13', '47-ronin-2013'),
('250', 4.8, 'Decent watch.', NOW(), '13', '8mm'),
('251', 2.8, 'A must-watch for movie lovers!', NOW(), '14', '71'),
('252', 2.3, 'A must-watch for movie lovers!', NOW(), '14', '1941'),
('253', 2.7, 'Could have been better, but still enjoyable.', NOW(), '14', '365-days-2020'),
('254', 2.9, 'Enjoyable but forgettable.', NOW(), '14', '24-frames'),
('255', 4.4, 'Had some great moments, but lacked depth.', NOW(), '15', '21'),
('256', 4.1, 'A must-watch for movie lovers!', NOW(), '15', '12-years-a-slave'),
('257', 2.3, 'A must-watch for movie lovers!', NOW(), '15', '1br'),
('258', 4.0, 'Amazing movie! Highly recommend.', NOW(), '16', '12-hour-shift'),
('259', 3.6, 'A true masterpiece!', NOW(), '16', '8mm'),
('260', 3.2, 'Could have been better, but still enjoyable.', NOW(), '16', '3-women'),
('261', 4.9, 'Decent watch.', NOW(), '16', '102-dalmatians'),
('262', 2.3, 'Decent watch.', NOW(), '17', '4-44-last-day-on-earth'),
('263', 2.5, 'Enjoyable but forgettable.', NOW(), '17', '10-cloverfield-lane'),
('264', 3.7, 'A true masterpiece!', NOW(), '17', '13-sins'),
('265', 2.4, 'Had some great moments, but lacked depth.', NOW(), '18', '2036-nexus-dawn'),
('266', 2.2, 'Had some great moments, but lacked depth.', NOW(), '18', '2-fast-2-furious'),
('267', 2.6, 'Decent watch.', NOW(), '18', '6-underground'),
('268', 4.8, 'Loved the acting and cinematography!', NOW(), '18', '13-sins'),
('269', 4.2, 'Not my cup of tea.', NOW(), '19', '13-sins'),
('270', 4.2, 'Amazing movie! Highly recommend.', NOW(), '19', '2001-a-space-odyssey'),
('271', 4.6, 'A must-watch for movie lovers!', NOW(), '19', '45-years'),
('272', 4.2, 'Decent watch.', NOW(), '19', '7500'),
('273', 3.6, 'Enjoyable but forgettable.', NOW(), '20', '21-bridges'),
('274', 2.5, 'Enjoyable but forgettable.', NOW(), '20', '1408'),
('275', 2.7, 'A true masterpiece!', NOW(), '20', '24-frames'),
('276', 4.7, 'Decent watch.', NOW(), '20', '24-hour-party-people');


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
