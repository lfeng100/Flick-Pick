USE flick_pick;

-- Delete existing data to avoid duplication
DELETE FROM Preferences;
DELETE FROM Reviews;
DELETE FROM GroupUsers;
DELETE FROM `Groups`;
DELETE FROM Users;

-- Insert 20 mock users
INSERT INTO Users (userID, email, username, firstName, lastName) VALUES
('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'alice.smith@example.com', 'alice_smith', 'Alice', 'Smith'),
('7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', 'bob.johnson@example.com', 'bobby_j', 'Bob', 'Johnson'),
('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'charlie.davis@example.com', 'charlie_d', 'Charlie', 'Davis'),
('a153e660-28c8-405f-9c4d-c67c6d0968bf', 'diana.miller@example.com', 'diana_m', 'Diana', 'Miller'),
('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'ethan.wilson@example.com', 'ethan_w', 'Ethan', 'Wilson'),
('b114b2c7-abe0-4c4f-8d97-8571343b163e', 'fiona.taylor@example.com', 'fiona_t', 'Fiona', 'Taylor'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'george.moore@example.com', 'george_m', 'George', 'Moore'),
('efbcee67-f88c-44e5-941c-d23704dec266', 'hannah.white@example.com', 'hannah_w', 'Hannah', 'White'),
('9247d422-e204-4570-9b44-2b80c83a6df2', 'ian.thomas@example.com', 'ian_t', 'Ian', 'Thomas'),
('feadfa0c-d287-413d-b7cb-0451261ed614', 'julia.anderson@example.com', 'julia_a', 'Julia', 'Anderson'),
('6466b516-ce64-4cd2-b960-3732a7d3bb44', 'kevin.martin@example.com', 'kevin_m', 'Kevin', 'Martin'),
('a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'laura.thompson@example.com', 'laura_t', 'Laura', 'Thompson'),
('05b58228-0a3f-403d-91fe-cab0868ebd68', 'michael.garcia@example.com', 'michael_g', 'Michael', 'Garcia'),
('9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'natalie.lee@example.com', 'natalie_l', 'Natalie', 'Lee'),
('37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', 'oliver.harris@example.com', 'oliver_h', 'Oliver', 'Harris'),
('53fe5e8a-e298-4adf-84f3-e5b96f1449bd', 'patricia.clark@example.com', 'patricia_c', 'Patricia', 'Clark'),
('1244cc48-24e7-4d90-b88f-24bb9cb2896b', 'quentin.rodriguez@example.com', 'quentin_r', 'Quentin', 'Rodriguez'),
('5808e35f-2b90-40bc-b565-3e4a593ac99c', 'rachel.lewis@example.com', 'rachel_l', 'Rachel', 'Lewis'),
('57120095-057d-4820-a643-63c4df39fad2', 'samuel.walker@example.com', 'samuel_w', 'Samuel', 'Walker'),
('7b53d249-75be-40b2-ac16-2f51d849c7cd', 'tina.hall@example.com', 'tina_h', 'Tina', 'Hall');

-- Insert Preferences
INSERT INTO Preferences (userID, tagID) VALUES
('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'science_fiction'), ('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'war'), ('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'history'),
('7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', 'thriller'), ('7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', 'science_fiction'), ('7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', 'adventure'),
('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'science_fiction'), ('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'history'), ('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'war'),
('a153e660-28c8-405f-9c4d-c67c6d0968bf', 'science_fiction'), ('a153e660-28c8-405f-9c4d-c67c6d0968bf', 'drama'), ('a153e660-28c8-405f-9c4d-c67c6d0968bf', 'history'),
('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'action'), ('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'science_fiction'), ('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'drama'),
('b114b2c7-abe0-4c4f-8d97-8571343b163e', 'drama'), ('b114b2c7-abe0-4c4f-8d97-8571343b163e', 'science_fiction'), ('b114b2c7-abe0-4c4f-8d97-8571343b163e', 'war'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'documentary'), ('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'drama'), ('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'history'),
('efbcee67-f88c-44e5-941c-d23704dec266', 'crime'), ('efbcee67-f88c-44e5-941c-d23704dec266', 'thriller'), ('efbcee67-f88c-44e5-941c-d23704dec266', 'drama'),
('9247d422-e204-4570-9b44-2b80c83a6df2', 'western'), ('9247d422-e204-4570-9b44-2b80c83a6df2', 'war'), ('9247d422-e204-4570-9b44-2b80c83a6df2', 'romance'),
('feadfa0c-d287-413d-b7cb-0451261ed614', 'action'), ('feadfa0c-d287-413d-b7cb-0451261ed614', 'thriller'), ('feadfa0c-d287-413d-b7cb-0451261ed614', 'drama'),
('6466b516-ce64-4cd2-b960-3732a7d3bb44', 'science_fiction'), ('6466b516-ce64-4cd2-b960-3732a7d3bb44', 'action'), ('6466b516-ce64-4cd2-b960-3732a7d3bb44', 'adventure'),
('a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'action'), ('a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'crime'), ('a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'animation'),
('05b58228-0a3f-403d-91fe-cab0868ebd68', 'action'), ('05b58228-0a3f-403d-91fe-cab0868ebd68', 'romance'), ('05b58228-0a3f-403d-91fe-cab0868ebd68', 'fantasy'),
('9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'action'), ('9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'war'), ('9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'thriller'),
('37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', 'thriller'), ('37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', 'horror'), ('37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', 'action'),
('53fe5e8a-e298-4adf-84f3-e5b96f1449bd', 'horror'), ('53fe5e8a-e298-4adf-84f3-e5b96f1449bd', 'crime'), ('53fe5e8a-e298-4adf-84f3-e5b96f1449bd', 'thriller'),
('1244cc48-24e7-4d90-b88f-24bb9cb2896b', 'horror'), ('1244cc48-24e7-4d90-b88f-24bb9cb2896b', 'mystery'), ('1244cc48-24e7-4d90-b88f-24bb9cb2896b', 'thriller'),
('5808e35f-2b90-40bc-b565-3e4a593ac99c', 'science_fiction'), ('5808e35f-2b90-40bc-b565-3e4a593ac99c', 'action'), ('5808e35f-2b90-40bc-b565-3e4a593ac99c', 'horror'),
('57120095-057d-4820-a643-63c4df39fad2', 'crime'), ('57120095-057d-4820-a643-63c4df39fad2', 'drama'), ('57120095-057d-4820-a643-63c4df39fad2', 'mystery'),
('7b53d249-75be-40b2-ac16-2f51d849c7cd', 'war'), ('7b53d249-75be-40b2-ac16-2f51d849c7cd', 'history'), ('7b53d249-75be-40b2-ac16-2f51d849c7cd', 'drama');


-- Insert Reviews
INSERT INTO Reviews (rating, message, timestamp, userID, movieID) VALUES
(4.1, 'The acting and cinematography were fantastic!', '2024-07-05 11:21:16', '4f24ab93-ac23-4693-b182-fe48c1d234b4', 'a-night-to-remember'),
(2.2, 'Beautiful movie with a deep story. Still, a bit slow at times.', '2025-09-19 19:31:13', '4f24ab93-ac23-4693-b182-fe48c1d234b4', 'memoirs-of-a-geisha'),
(2.0, 'A visually stunning film, but the plot did not leave much of an impression.', '2024-06-04 22:27:08', '4f24ab93-ac23-4693-b182-fe48c1d234b4', 'a-trip-to-the-moon'),
(2.0, 'An intriguing watch, though a bit too bizarre for my taste.', '2024-02-29 07:26:08', '4f24ab93-ac23-4693-b182-fe48c1d234b4', 'a-clockwork-orange'),
(2.1, 'Not the best Star Trek movie, but it still had some memorable moments.', '2024-09-22 01:39:45', '7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', 'star-trek-nemesis'),
(4.1, 'A solid Star Trek film with some great space exploration scenes.', '2024-08-06 17:55:34', '7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', 'star-trek-the-motion-picture'),
(2.9, 'A raw, emotional film with a powerful message about life and loss.', '2024-02-25 22:52:17', '7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', '25th-hour'),
(2.7, 'An intense war movie, but a bit too gritty for my liking.', '2024-03-01 01:06:23', '8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'black-hawk-down'),
(4.8, 'A compelling, emotionally charged story, but not as strong as I hoped.', '2025-02-14 17:32:49', '8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'flags-of-our-fathers'),
(3.1, 'A decent martial arts film with some great choreography.', '2024-10-03 03:55:07', '8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'ip-man-3'),
(2.0, 'Some good scenes, but overall lacking the magic of the earlier Star Trek films.', '2024-01-29 18:43:26', '8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'star-trek-iv-the-voyage-home'),
(4.1, 'A nice continuation of the Star Trek saga, though not as memorable.', '2024-01-26 15:12:21', '8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'star-trek-insurrection'),
(3.4, 'Its not the best Star Trek movie, but it had its moments.', '2025-04-20 23:21:34', 'a153e660-28c8-405f-9c4d-c67c6d0968bf', 'star-trek-v-the-final-frontier'),
(5.0, 'A heartwarming story with brilliant animation. Truly a masterpiece.', '2024-06-27 04:16:21', 'a153e660-28c8-405f-9c4d-c67c6d0968bf', 'summer-wars'),
(2.8, 'A war film with some powerful sequences, but not as impactful as it could be.', '2024-06-07 12:40:26', 'a153e660-28c8-405f-9c4d-c67c6d0968bf', '1917'),
(2.4, 'An alright movie, but nothing compared to the original Star Wars trilogy.', '2025-08-29 16:23:46', 'a153e660-28c8-405f-9c4d-c67c6d0968bf', 'star-wars-episode-iii-revenge-of-the-sith'),
(2.1, 'Not as exciting as other Star Wars films, but it had a few cool moments.', '2024-03-16 09:32:54', 'e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'star-wars-the-clone-wars'),
(4.5, 'One of the best Star Wars movies! It was a thrilling ride from start to finish.', '2024-01-08 01:57:46', 'e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'star-wars-the-last-jedi'),
(2.4, 'A fun monster movie, but felt a bit over the top and lacking substance.', '2024-08-05 23:00:30', 'e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'godzilla-final-wars'),
(4.9, 'An incredible sequel with stunning visuals and intense action. A must-see!', '2024-12-10 02:39:26', 'e56969ca-64ac-4ff1-8a1e-a8016eff79fb', '300-rise-of-an-empire'),
(2.1, 'Not my kind of film. did not enjoy the story or characters.', '2025-10-11 17:17:44', 'e56969ca-64ac-4ff1-8a1e-a8016eff79fb', '28-days'),
(4.0, 'A heartfelt exploration of love and heartbreak, though it lacked a bit of emotional depth.', '2024-10-30 14:08:49', 'b114b2c7-abe0-4c4f-8d97-8571343b163e', '500-days-of-summer'),
(4.1, 'A solid film with some beautiful moments, but it did not quite reach its full potential.', '2025-12-05 15:55:57', 'b114b2c7-abe0-4c4f-8d97-8571343b163e', 'a-short-film-about-love'),
(2.1, 'The film had a lot of potential but felt underwhelming in its execution.', '2024-09-01 23:09:11', 'b114b2c7-abe0-4c4f-8d97-8571343b163e', 'a-special-day'),
(2.5, 'Stunning visuals and strong performances, but the story felt a bit weak in places.', '2024-06-18 16:08:26', 'f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'everest-2015'),
(4.9, 'An exquisite film that captures the complexities of love and longing. Highly recommend.', '2024-07-20 11:50:28', 'f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'flowers-of-shanghai'),
(2.8, 'A slow burn with great performances, but the pacing could have been better.', '2024-09-15 18:47:44', 'f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'burning-2018'),
(4.2, 'A beautifully crafted film that captures the opulence and tragedy of Marie AntoinetteÆs life.', '2025-01-23 02:51:18', 'f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'marie-antoinette-2006'),
(2.7, 'A gripping war film, but it left me wanting more from the characters.', '2024-10-03 13:41:55', 'f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'flags-of-our-fathers'),
(4.4, 'A fun and energetic film that will make you want to get up and dance.', '2024-11-30 22:32:59', 'efbcee67-f88c-44e5-941c-d23704dec266', 'footloose'),
(3.9, 'A brilliant exploration of class and society, with a few flaws in pacing.', '2025-09-17 23:49:02', 'efbcee67-f88c-44e5-941c-d23704dec266', 'parasite-2019'),
(2.4, 'A classic mystery film with a lot of depth, but it felt a bit slow at times.', '2024-08-08 11:43:55', 'efbcee67-f88c-44e5-941c-d23704dec266', 'the-name-of-the-rose'),
(3.7, 'An action-packed ride, though the filmÆs length might be off-putting to some.', '2025-07-31 13:15:11', 'efbcee67-f88c-44e5-941c-d23704dec266', 'rrr'),
(4.3, 'A hilarious and heartwarming holiday film with great performances all around.', '2024-05-13 19:41:02', '9247d422-e204-4570-9b44-2b80c83a6df2', 'a-bad-moms-christmas'),
(3.8, 'A solid sports drama with some inspiring moments, but overall forgettable.', '2025-03-05 13:52:10', '9247d422-e204-4570-9b44-2b80c83a6df2', '42'),
(4.6, 'A thought-provoking and engaging film with brilliant cinematography and performances.', '2025-03-24 22:01:42', '9247d422-e204-4570-9b44-2b80c83a6df2', '71-fragments-of-a-chronology-of-chance'),
(3.6, 'A decent thriller with some good moments, but the story lacked emotional depth.', '2024-12-31 22:39:45', '9247d422-e204-4570-9b44-2b80c83a6df2', '8-46-2020'),
(3.9, 'A gripping historical drama, though it felt a bit disjointed at times.', '2025-09-18 10:03:20', 'feadfa0c-d287-413d-b7cb-0451261ed614', 'the-great-train-robbery'),
(3.1, 'An action-packed but forgettable film that did not quite live up to expectations.', '2025-09-06 23:31:51', 'feadfa0c-d287-413d-b7cb-0451261ed614', 'terminator-salvation'),
(4.3, 'A fresh take on the Terminator franchise, but it did not resonate as strongly as earlier films.', '2024-04-02 13:29:27', 'feadfa0c-d287-413d-b7cb-0451261ed614', 'terminator-genisys'),
(2.6, 'An action-packed film, but the story felt thin and did not quite capture my attention.', '2024-08-21 12:35:13', 'feadfa0c-d287-413d-b7cb-0451261ed614', 'barely-lethal'),
(4.0, 'A classic Batman film with a unique tone, but did not quite captivate me as much as I had hoped.', '2025-04-06 04:15:14', '6466b516-ce64-4cd2-b960-3732a7d3bb44', 'batman-1989'),
(3.7, 'A fun, if flawed, Batman film with some enjoyable moments.', '2024-04-24 23:35:22', '6466b516-ce64-4cd2-b960-3732a7d3bb44', 'batman-forever'),
(3.0, 'An action-packed film, but the story lacked emotional depth.', '2024-03-16 20:41:25', '6466b516-ce64-4cd2-b960-3732a7d3bb44', 'desperado'),
(3.3, 'Interesting concept, but the execution did not quite deliver on its potential.', '2025-10-30 10:49:33', '6466b516-ce64-4cd2-b960-3732a7d3bb44', 'minority-report'),
(4.0, 'A thrilling and well-crafted movie with an intriguing storyline.', '2025-11-21 05:49:58', 'a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'minority-report'),
(2.5, 'Visually stunning, but the story felt long and overblown.', '2024-12-03 03:35:49', 'a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'pirates-of-the-caribbean-at-worlds-end'),
(3.0, 'A decent film, but not as memorable as previous installments.', '2025-02-06 22:29:20', 'a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'pirates-of-the-caribbean-dead-men-tell-no-tales'),
(4.4, 'A great action movie with thrilling moments, but it lacked character depth.', '2024-06-21 11:51:59', 'a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'nobody-2021'),
(4.8, 'A Bond classic with great action sequences, but not my absolute favorite.', '2024-03-28 09:12:58', '05b58228-0a3f-403d-91fe-cab0868ebd68', 'goldfinger'),
(2.1, 'Had potential, but the plot was weak and the pacing off.', '2024-02-23 00:11:42', '05b58228-0a3f-403d-91fe-cab0868ebd68', '47-ronin-2013'),
(4.8, 'A solid action flick with a lot of intense moments.', '2025-02-12 07:11:45', '05b58228-0a3f-403d-91fe-cab0868ebd68', 'jack-reacher'),
(2.8, 'Entertaining at times, but did not quite live up to expectations.', '2025-03-07 03:31:37', '9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'iron-man-3'),
(2.3, 'A nostalgic take on Batman, but it did not capture my interest as much as I expected.', '2024-08-05 12:46:16', '9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'batman-1989'),
(2.7, 'Not the best of the Terminator franchise, but still somewhat enjoyable.', '2025-12-09 11:05:43', '9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'terminator-genisys'),
(2.9, 'An entertaining ride, but ultimately forgettable.', '2025-11-18 01:48:46', '9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'pirates-of-the-caribbean-at-worlds-end'),
(4.4, 'A gripping and suspenseful movie with excellent performances, though it lacked a bit in story depth.', '2024-11-08 13:17:55', '37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', '10-cloverfield-lane'),
(4.1, 'A thrilling and dark film that will keep you on the edge of your seat. Highly recommend.', '2024-10-12 03:25:44', '37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', '12-hour-shift'),
(2.3, 'Had some good moments, but did not quite hold my attention overall.', '2024-05-10 15:41:49', '37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', '28-days-later'),
(4.0, 'An incredibly tense and well-executed thriller. Highly recommend.', '2024-04-10 09:09:29', '53fe5e8a-e298-4adf-84f3-e5b96f1449bd', '12-hour-shift'),
(3.6, 'A solid horror movie with some chilling moments, but not as memorable as I had hoped.', '2025-06-14 08:37:05', '53fe5e8a-e298-4adf-84f3-e5b96f1449bd', '3-from-hell'),
(3.2, 'A decent film with some good scares, but it did not live up to its potential.', '2024-01-29 11:01:30', '53fe5e8a-e298-4adf-84f3-e5b96f1449bd', 'a-classic-horror-story'),
(4.9, 'A hilarious horror comedy with great performances. A fun watch!', '2025-10-31 14:59:43', '53fe5e8a-e298-4adf-84f3-e5b96f1449bd', 'a-haunted-house'),
(2.3, 'A slasher film with some good scares, but felt dated and did not offer much new.', '2025-07-16 20:07:08', '1244cc48-24e7-4d90-b88f-24bb9cb2896b', 'a-nightmare-on-elm-street'),
(2.5, 'Entertaining at first, but ultimately forgettable.', '2024-07-18 00:38:34', '1244cc48-24e7-4d90-b88f-24bb9cb2896b', 'the-wretched'),
(3.7, 'A quirky and unique film, though not everyone will appreciate its style.', '2025-02-01 03:09:41', '1244cc48-24e7-4d90-b88f-24bb9cb2896b', 'basket-case'),
(2.4, 'A weird, low-budget film with some funny moments but not much else.', '2025-05-12 10:17:58', '5808e35f-2b90-40bc-b565-3e4a593ac99c', 'bad-taste'),
(2.2, 'A cult classic, but it did not work for me as much as it might for others.', '2025-05-27 07:19:36', '5808e35f-2b90-40bc-b565-3e4a593ac99c', 'spookies'),
(2.6, 'An average film that had some potential but did not fully capture my attention.', '2024-05-31 20:53:47', '5808e35f-2b90-40bc-b565-3e4a593ac99c', 'stay-alive'),
(4.8, 'A chilling slasher film with strong performances and a suspenseful atmosphere.', '2025-06-03 12:01:13', '5808e35f-2b90-40bc-b565-3e4a593ac99c', 'theres-someone-inside-your-house'),
(4.2, 'A solid thriller, but it did not quite engage me as much as I expected.', '2025-02-16 12:51:44', '57120095-057d-4820-a643-63c4df39fad2', '13-sins'),
(4.2, 'A cinematic masterpiece thatÆs visually stunning and thought-provoking. Highly recommend.', '2025-08-16 22:10:26', '57120095-057d-4820-a643-63c4df39fad2', '2001-a-space-odyssey'),
(4.6, 'A beautifully crafted film about love and loss, a must-watch for cinephiles.', '2024-12-03 16:24:48', '57120095-057d-4820-a643-63c4df39fad2', '45-years'),
(4.2, 'A decent thriller, though it did not have the emotional depth I was hoping for.', '2024-08-10 03:20:26', '57120095-057d-4820-a643-63c4df39fad2', '7500'),
(3.6, 'A solid war film with good performances, but it did not stand out as much as I hoped.', '2024-05-31 09:03:28', '7b53d249-75be-40b2-ac16-2f51d849c7cd', 'a-bridge-too-far'),
(2.5, 'An interesting take on Marie Antoinette, but ultimately forgettable.', '2024-03-28 22:54:42', '7b53d249-75be-40b2-ac16-2f51d849c7cd', 'marie-antoinette-2006'),
(2.7, 'An epic story with grand scale, but I wasnÆt emotionally invested in the characters.', '2024-03-08 08:09:54', '7b53d249-75be-40b2-ac16-2f51d849c7cd', 'troy'),
(4.7, 'A decent historical drama, though it lacked the intensity I was expecting.', '2024-05-21 05:35:44', '7b53d249-75be-40b2-ac16-2f51d849c7cd', 'young-mr-lincoln');

-- Insert Groups
INSERT INTO `Groups` (groupID, groupName, adminUserID) VALUES
('dedd1f42-c728-4a9d-9798-3662314cafb3', 'Sci-Fi Enthusiasts', '4f24ab93-ac23-4693-b182-fe48c1d234b4'),
('ed29d0f8-caf7-4c6b-b848-44b679464539', 'Drama Lovers', 'e56969ca-64ac-4ff1-8a1e-a8016eff79fb'),
('b8de5de8-7258-4c00-a5fb-e7de3342c07f', 'Action Junkies', 'feadfa0c-d287-413d-b7cb-0451261ed614'),
('d1301bda-e906-4554-834b-4a881030a8b3', 'Horror Club', '37d0c8a5-dd0a-46d8-a2fa-5a94854533c8'),
('a9f5606b-d0b2-43b0-b891-ddfd5586eb23', 'Classic Movie Fans', '7b53d249-75be-40b2-ac16-2f51d849c7cd');

-- Insert GroupUsers
INSERT INTO GroupUsers (groupID, userID, joinedAt) VALUES
('dedd1f42-c728-4a9d-9798-3662314cafb3', '4f24ab93-ac23-4693-b182-fe48c1d234b4', '2025-04-03 12:01:13'), ('dedd1f42-c728-4a9d-9798-3662314cafb3', '7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', '2025-01-03 12:01:13'), ('dedd1f42-c728-4a9d-9798-3662314cafb3', '8d27d2f3-79f5-4b8b-801e-315411ff4e2f', '2025-03-03 12:01:13'), ('dedd1f42-c728-4a9d-9798-3662314cafb3', 'a153e660-28c8-405f-9c4d-c67c6d0968bf', '2025-02-03 12:01:13'), ('dedd1f42-c728-4a9d-9798-3662314cafb3', 'e56969ca-64ac-4ff1-8a1e-a8016eff79fb', '2025-04-13 12:01:13'),
('ed29d0f8-caf7-4c6b-b848-44b679464539', 'e56969ca-64ac-4ff1-8a1e-a8016eff79fb', '2025-01-03 12:01:13'), ('ed29d0f8-caf7-4c6b-b848-44b679464539', 'b114b2c7-abe0-4c4f-8d97-8571343b163e', '2025-02-03 12:01:13'), ('ed29d0f8-caf7-4c6b-b848-44b679464539', 'f4997c8c-2bf3-4e24-8c81-b505c6ffa883', '2025-03-03 12:01:13'), ('ed29d0f8-caf7-4c6b-b848-44b679464539', 'efbcee67-f88c-44e5-941c-d23704dec266', '2025-04-03 12:01:13'),
('b8de5de8-7258-4c00-a5fb-e7de3342c07f', 'feadfa0c-d287-413d-b7cb-0451261ed614', '2025-01-03 12:01:13'), ('b8de5de8-7258-4c00-a5fb-e7de3342c07f', '6466b516-ce64-4cd2-b960-3732a7d3bb44', '2025-02-03 12:01:13'), ('b8de5de8-7258-4c00-a5fb-e7de3342c07f', 'a8ed10ba-2705-4753-af64-3f2b190ccd7e', '2025-03-03 12:01:13'), ('b8de5de8-7258-4c00-a5fb-e7de3342c07f', '05b58228-0a3f-403d-91fe-cab0868ebd68', '2025-04-03 12:01:13'), ('b8de5de8-7258-4c00-a5fb-e7de3342c07f', '9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', '2025-05-03 12:01:13'),
('d1301bda-e906-4554-834b-4a881030a8b3', '37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', '2025-01-03 12:01:13'), ('d1301bda-e906-4554-834b-4a881030a8b3', '53fe5e8a-e298-4adf-84f3-e5b96f1449bd', '2025-02-03 12:01:13'), ('d1301bda-e906-4554-834b-4a881030a8b3', '1244cc48-24e7-4d90-b88f-24bb9cb2896b', '2025-03-03 12:01:13'), ('d1301bda-e906-4554-834b-4a881030a8b3', '5808e35f-2b90-40bc-b565-3e4a593ac99c', '2025-04-03 12:01:13'),
('a9f5606b-d0b2-43b0-b891-ddfd5586eb23', '7b53d249-75be-40b2-ac16-2f51d849c7cd', '2025-01-03 12:01:13'), ('a9f5606b-d0b2-43b0-b891-ddfd5586eb23', '4f24ab93-ac23-4693-b182-fe48c1d234b4', '2025-02-03 12:01:13'), ('a9f5606b-d0b2-43b0-b891-ddfd5586eb23', '8d27d2f3-79f5-4b8b-801e-315411ff4e2f', '2025-03-03 12:01:13'), ('a9f5606b-d0b2-43b0-b891-ddfd5586eb23', 'f4997c8c-2bf3-4e24-8c81-b505c6ffa883', '2025-04-03 12:01:13');

-- Insert UserWatched
INSERT INTO UserWatched (userID, movieID, timestamp) VALUES
('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'black-hawk-down', '2025-02-17 02:34:11'),
('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'flags-of-our-fathers', '2024-04-19 02:13:13'),
('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'a-clockwork-orange', '2025-04-06 22:32:12'),
('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'the-day-the-earth-stood-still-2008', '2024-05-10 17:31:37'),
('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'a-night-to-remember', '2025-01-17 02:34:11'),
('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'memoirs-of-a-geisha', '2024-05-19 02:13:13'),
('4f24ab93-ac23-4693-b182-fe48c1d234b4', 'a-trip-to-the-moon', '2025-02-06 22:32:12'),
('7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', 'godzilla-king-of-the-monsters', '2024-11-02 03:29:13'),
('7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', 'star-wars-the-last-jedi', '2024-12-22 09:31:15'),
('7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', 'star-trek-the-motion-picture', '2025-08-10 20:15:51'),
('7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', 'star-trek-nemesis', '2024-06-28 08:00:42'),
('7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c', '25th-hour', '2024-04-28 08:00:42'),
('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'ivan-the-terrible-part-i', '2025-09-30 09:00:42'),
('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'memoirs-of-a-geisha', '2024-04-18 10:22:33'),
('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'star-trek-the-motion-picture', '2024-05-01 21:33:23'),
('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'star-trek-iv-the-voyage-home', '2024-09-25 03:11:01'),
('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'black-hawk-down', '2025-02-27 05:00:42'),
('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'flags-of-our-fathers', '2024-10-18 10:22:33'),
('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'ip-man-3', '2024-07-01 21:33:23'),
('8d27d2f3-79f5-4b8b-801e-315411ff4e2f', 'star-trek-insurrection', '2024-02-25 03:11:01'),
('a153e660-28c8-405f-9c4d-c67c6d0968bf', 'star-wars-episode-iii-revenge-of-the-sith', '2025-01-19 00:20:10'),
('a153e660-28c8-405f-9c4d-c67c6d0968bf', 'godzilla-final-wars', '2025-04-21 13:19:04'),
('a153e660-28c8-405f-9c4d-c67c6d0968bf', 'godzilla-king-of-the-monsters', '2025-02-03 08:42:14'),
('a153e660-28c8-405f-9c4d-c67c6d0968bf', 'star-trek-v-the-final-frontier', '2025-03-19 00:20:10'),
('a153e660-28c8-405f-9c4d-c67c6d0968bf', 'summer-wars', '2025-05-21 13:19:04'),
('a153e660-28c8-405f-9c4d-c67c6d0968bf', '1917', '2025-03-03 08:42:14'),
('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', '2001-a-space-odyssey', '2025-04-02 04:23:28'),
('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'star-trek-v-the-final-frontier', '2024-06-24 19:37:15'),
('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', '300-rise-of-an-empire', '2024-08-27 00:49:24'),
('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', '28-days', '2025-02-04 02:59:27'),
('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'star-wars-the-clone-wars', '2025-03-02 04:23:28'),
('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'star-wars-the-last-jedi', '2024-07-24 19:37:15'),
('e56969ca-64ac-4ff1-8a1e-a8016eff79fb', 'godzilla-final-wars', '2025-01-04 02:59:27'),
('b114b2c7-abe0-4c4f-8d97-8571343b163e', 'a-special-day', '2024-08-20 04:02:23'),
('b114b2c7-abe0-4c4f-8d97-8571343b163e', 'burning-2018', '2025-05-04 07:09:02'),
('b114b2c7-abe0-4c4f-8d97-8571343b163e', 'footloose', '2024-05-29 12:34:55'),
('b114b2c7-abe0-4c4f-8d97-8571343b163e', '500-days-of-summer', '2024-02-20 04:02:23'),
('b114b2c7-abe0-4c4f-8d97-8571343b163e', 'a-short-film-about-love', '2025-03-04 07:09:02'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'snowden', '2025-02-22 00:26:22'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'spartacus', '2025-07-31 05:02:26'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'young-mr-lincoln', '2024-10-27 11:10:52'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'taipei-story', '2025-08-18 13:47:23'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'rrr', '2025-07-07 15:09:29'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'everest-2015', '2025-01-22 00:26:22'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'flowers-of-shanghai', '2025-04-22 05:02:26'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'burning-2018', '2024-11-27 11:10:52'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'marie-antoinette-2006', '2025-02-18 13:47:23'),
('f4997c8c-2bf3-4e24-8c81-b505c6ffa883', 'flags-of-our-fathers', '2025-03-07 15:09:29'),
('efbcee67-f88c-44e5-941c-d23704dec266', 'parasite-2019', '2025-12-08 21:03:30'),
('efbcee67-f88c-44e5-941c-d23704dec266', 'high-fidelity', '2024-12-06 13:13:16'),
('efbcee67-f88c-44e5-941c-d23704dec266', 'a-short-film-about-love', '2024-11-17 15:39:48'),
('efbcee67-f88c-44e5-941c-d23704dec266', 'footloose', '2025-01-08 21:03:30'),
('efbcee67-f88c-44e5-941c-d23704dec266', 'the-name-of-the-rose', '2024-10-06 13:13:16'),
('efbcee67-f88c-44e5-941c-d23704dec266', 'rrr', '2024-07-17 15:39:48'),
('9247d422-e204-4570-9b44-2b80c83a6df2', '71', '2025-11-04 08:27:06'),
('9247d422-e204-4570-9b44-2b80c83a6df2', '2001-a-space-odyssey', '2025-11-29 09:30:42'),
('9247d422-e204-4570-9b44-2b80c83a6df2', '99-homes', '2024-04-09 21:28:32'),
('9247d422-e204-4570-9b44-2b80c83a6df2', 'a-bad-moms-christmas', '2025-02-04 08:27:06'),
('9247d422-e204-4570-9b44-2b80c83a6df2', '42', '2025-12-29 09:30:42'),
('9247d422-e204-4570-9b44-2b80c83a6df2', '71-fragments-of-a-chronology-of-chance', '2024-03-09 21:28:32'),
('9247d422-e204-4570-9b44-2b80c83a6df2', '8-46-2020', '2024-05-09 21:28:32'),
('feadfa0c-d287-413d-b7cb-0451261ed614', 'the-great-train-robbery', '2024-04-15 17:39:47'),
('feadfa0c-d287-413d-b7cb-0451261ed614', 'terminator-salvation', '2025-06-20 08:24:07'),
('feadfa0c-d287-413d-b7cb-0451261ed614', 'terminator-genisys', '2024-12-14 19:40:47'),
('feadfa0c-d287-413d-b7cb-0451261ed614', 'barely-lethal', '2024-05-15 17:39:47');


-- Insert UserWatchList
INSERT INTO UserWatchlist (userID, movieID, timestamp) VALUES
('6466b516-ce64-4cd2-b960-3732a7d3bb44', '47-ronin-2013', '2025-01-24 23:42:46'),
('6466b516-ce64-4cd2-b960-3732a7d3bb44', 'barely-lethal', '2024-07-24 21:34:30'),
('6466b516-ce64-4cd2-b960-3732a7d3bb44', 'nobody-2021', '2025-12-05 17:09:27'),
('6466b516-ce64-4cd2-b960-3732a7d3bb44', 'jack-reacher', '2025-07-19 17:14:28'),
('a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'desperado', '2024-05-15 15:47:17'),
('a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'iron-man-3', '2025-05-03 17:31:01'),
('a8ed10ba-2705-4753-af64-3f2b190ccd7e', 'goldfinger', '2024-10-20 21:43:11'),
('05b58228-0a3f-403d-91fe-cab0868ebd68', 'pirates-of-the-caribbean-at-worlds-end', '2025-01-14 04:07:03'),
('05b58228-0a3f-403d-91fe-cab0868ebd68', 'pirates-of-the-caribbean-dead-men-tell-no-tales', '2024-04-24 22:43:21'),
('05b58228-0a3f-403d-91fe-cab0868ebd68', 'terminator-salvation', '2025-01-27 17:11:23'),
('9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'the-great-train-robbery', '2025-04-24 17:31:35'),
('9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'jack-reacher', '2024-11-09 17:29:19'),
('9fb62e08-f7c9-4db7-8f27-cb71c7a14f25', 'desperado', '2024-07-21 14:38:05'),
('37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', '3-from-hell', '2025-06-14 16:08:31'),
('37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', 'a-classic-horror-story', '2025-11-16 22:08:01'),
('37d0c8a5-dd0a-46d8-a2fa-5a94854533c8', 'a-haunted-house', '2024-01-14 19:21:40'),
('53fe5e8a-e298-4adf-84f3-e5b96f1449bd', 'bad-taste', '2025-09-07 19:22:44'),
('53fe5e8a-e298-4adf-84f3-e5b96f1449bd', 'the-wretched', '2025-03-24 04:01:52'),
('53fe5e8a-e298-4adf-84f3-e5b96f1449bd', 'a-nightmare-on-elm-street', '2024-09-23 07:32:35'),
('1244cc48-24e7-4d90-b88f-24bb9cb2896b', 'theres-someone-inside-your-house', '2024-10-08 18:26:52'),
('1244cc48-24e7-4d90-b88f-24bb9cb2896b', 'bad-taste', '2024-03-22 05:39:09'),
('1244cc48-24e7-4d90-b88f-24bb9cb2896b', 'spookies', '2024-08-19 14:45:54'),
('5808e35f-2b90-40bc-b565-3e4a593ac99c', '3-from-hell', '2025-04-17 10:02:04'),
('5808e35f-2b90-40bc-b565-3e4a593ac99c', '10-cloverfield-lane', '2025-11-21 08:17:30'),
('5808e35f-2b90-40bc-b565-3e4a593ac99c', 'the-wretched', '2024-03-24 07:41:52'),
('57120095-057d-4820-a643-63c4df39fad2', '42', '2024-02-09 19:45:10'),
('57120095-057d-4820-a643-63c4df39fad2', '16-blocks', '2025-04-01 06:09:51'),
('57120095-057d-4820-a643-63c4df39fad2', '13-ghosts', '2024-11-26 01:24:38'),
('7b53d249-75be-40b2-ac16-2f51d849c7cd', 'spartacus', '2025-07-01 15:35:59'),
('7b53d249-75be-40b2-ac16-2f51d849c7cd', 'a-night-to-remember', '2024-06-30 16:48:18'),
('7b53d249-75be-40b2-ac16-2f51d849c7cd', 'apollo-13', '2024-05-14 08:18:17');

COMMIT;

-- Classic (History):
-- 7b53d249-75be-40b2-ac16-2f51d849c7cd
-- 4f24ab93-ac23-4693-b182-fe48c1d234b4
-- 8d27d2f3-79f5-4b8b-801e-315411ff4e2f
-- f4997c8c-2bf3-4e24-8c81-b505c6ffa883

-- a-bridge-too-far
-- a-night-to-remember
-- apollo-13
-- black-hawk-down
-- flags-of-our-fathers
-- everest-2015
-- ip-man-3
-- ivan-the-terrible-part-i
-- memoirs-of-a-geisha
-- marie-antoinette-2006
-- shakespeare-in-love
-- snowden
-- spartacus
-- troy
-- young-mr-lincoln
-- zulu

-- Action:
-- feadfa0c-d287-413d-b7cb-0451261ed614
-- 6466b516-ce64-4cd2-b960-3732a7d3bb44
-- a8ed10ba-2705-4753-af64-3f2b190ccd7e
-- 05b58228-0a3f-403d-91fe-cab0868ebd68
-- 9fb62e08-f7c9-4db7-8f27-cb71c7a14f25

-- the-great-train-robbery
-- terminator-salvation
-- terminator-genisys
-- barely-lethal
-- batman-1989
-- batman-forever
-- desperado
-- minority-report
-- pirates-of-the-caribbean-at-worlds-end
-- pirates-of-the-caribbean-dead-men-tell-no-tales
-- nobody-2021
-- goldfinger
-- jack-reacher
-- iron-man-3

-- Horror:
-- 37d0c8a5-dd0a-46d8-a2fa-5a94854533c8
-- 53fe5e8a-e298-4adf-84f3-e5b96f1449bd
-- 1244cc48-24e7-4d90-b88f-24bb9cb2896b
-- 5808e35f-2b90-40bc-b565-3e4a593ac99c

-- 10-cloverfield-lane
-- 12-hour-shift
-- 28-days-later
-- 3-from-hell
-- a-classic-horror-story
-- a-haunted-house
-- a-nightmare-on-elm-street
-- basket-case
-- bad-taste
-- spookies
-- stay-alive
-- theres-someone-inside-your-house
-- the-wretched

-- Sci-Fi:
-- 4f24ab93-ac23-4693-b182-fe48c1d234b4
-- 7f8efdc4-b61c-4a14-a4ab-71d2f32ab81c
-- 8d27d2f3-79f5-4b8b-801e-315411ff4e2f
-- a153e660-28c8-405f-9c4d-c67c6d0968bf
-- e56969ca-64ac-4ff1-8a1e-a8016eff79fb

-- a-clockwork-orange
-- a-trip-to-the-moon
-- the-day-the-earth-stood-still-2008
-- star-trek-nemesis
-- star-trek-the-motion-picture
-- star-trek-insurrection
-- star-trek-iv-the-voyage-home
-- star-trek-v-the-final-frontier
-- summer-wars
-- star-wars-episode-iii-revenge-of-the-sith
-- star-wars-the-clone-wars
-- star-wars-the-last-jedi
-- godzilla-final-wars
-- godzilla-king-of-the-monsters

-- Drama:
-- e56969ca-64ac-4ff1-8a1e-a8016eff79fb
-- b114b2c7-abe0-4c4f-8d97-8571343b163e
-- f4997c8c-2bf3-4e24-8c81-b505c6ffa883
-- efbcee67-f88c-44e5-941c-d23704dec266

-- 28-days
-- 300-rise-of-an-empire
-- 500-days-of-summer
-- a-short-film-about-love
-- a-special-day
-- burning-2018
-- flowers-of-shanghai
-- footloose
-- parasite-2019
-- the-name-of-the-rose
-- taipei-story
-- rrr
-- high-fidelity
