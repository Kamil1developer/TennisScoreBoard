-- SQL-скрипт для наполнения базы данных тестовыми данными.
-- Hibernate автоматически выполнит этот скрипт при старте, если `hibernate.hbm2ddl.auto` установлено в `create` или `create-drop`.

--
-- Вставка 20 уникальных игроков
--
INSERT INTO players (name) VALUES ('Roger Federer');    -- ID: 1
INSERT INTO players (name) VALUES ('Rafael Nadal');     -- ID: 2
INSERT INTO players (name) VALUES ('Novak Djokovic');   -- ID: 3
INSERT INTO players (name) VALUES ('Andy Murray');      -- ID: 4
INSERT INTO players (name) VALUES ('Serena Williams');  -- ID: 5
INSERT INTO players (name) VALUES ('Venus Williams');   -- ID: 6
INSERT INTO players (name) VALUES ('Maria Sharapova');  -- ID: 7
INSERT INTO players (name) VALUES ('Andre Agassi');     -- ID: 8
INSERT INTO players (name) VALUES ('Pete Sampras');     -- ID: 9
INSERT INTO players (name) VALUES ('Carlos Alcaraz');   -- ID: 10
INSERT INTO players (name) VALUES ('Jannik Sinner');    -- ID: 11
INSERT INTO players (name) VALUES ('Daniil Medvedev');  -- ID: 12
INSERT INTO players (name) VALUES ('Alexander Zverev'); -- ID: 13
INSERT INTO players (name) VALUES ('Stefanos Tsitsipas');-- ID: 14
INSERT INTO players (name) VALUES ('Iga Swiatek');      -- ID: 15
INSERT INTO players (name) VALUES ('Aryna Sabalenka');  -- ID: 16
INSERT INTO players (name) VALUES ('Coco Gauff');       -- ID: 17
INSERT INTO players (name) VALUES ('Naomi Osaka');      -- ID: 18
INSERT INTO players (name) VALUES ('John McEnroe');     -- ID: 19
INSERT INTO players (name) VALUES ('Bjorn Borg');       -- ID: 20

--
-- Вставка 30 завершенных матчей
-- first_player_id, second_player_id, winner_id
--
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (1, 2, 2);   -- Federer vs Nadal, winner_id: Nadal
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (3, 4, 3);   -- Djokovic vs Murray, winner_id: Djokovic
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (5, 6, 5);   -- S. Williams vs V. Williams, winner_id: S. Williams
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (10, 11, 10); -- Alcaraz vs Sinner, winner_id: Alcaraz
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (12, 13, 13); -- Medvedev vs Zverev, winner_id: Zverev
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (1, 3, 1);   -- Federer vs Djokovic, winner_id: Federer
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (2, 4, 2);   -- Nadal vs Murray, winner_id: Nadal
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (8, 9, 8);   -- Agassi vs Sampras, winner_id: Agassi
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (15, 16, 15); -- Swiatek vs Sabalenka, winner_id: Swiatek
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (17, 18, 17); -- Gauff vs Osaka, winner_id: Gauff
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (19, 20, 19); -- McEnroe vs Borg, winner_id: McEnroe
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (1, 10, 1);  -- Federer vs Alcaraz, winner_id: Federer
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (2, 11, 2);  -- Nadal vs Sinner, winner_id: Nadal
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (3, 12, 3);  -- Djokovic vs Medvedev, winner_id: Djokovic
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (5, 7, 5);   -- S. Williams vs Sharapova, winner_id: S. Williams
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (14, 4, 14);  -- Tsitsipas vs Murray, winner_id: Tsitsipas
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (1, 4, 1);   -- Federer vs Murray, winner_id: Federer
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (2, 3, 3);   -- Nadal vs Djokovic, winner_id: Djokovic
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (10, 12, 12); -- Alcaraz vs Medvedev, winner_id: Medvedev
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (11, 13, 11); -- Sinner vs Zverev, winner_id: Sinner
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (6, 7, 7);   -- V. Williams vs Sharapova, winner_id: Sharapova
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (15, 17, 15); -- Swiatek vs Gauff, winner_id: Swiatek
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (16, 18, 18); -- Sabalenka vs Osaka, winner_id: Osaka
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (8, 20, 20);  -- Agassi vs Borg, winner_id: Borg
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (9, 19, 19);  -- Sampras vs McEnroe, winner_id: McEnroe
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (1, 9, 1);   -- Federer vs Sampras, winner_id: Federer
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (2, 8, 2);   -- Nadal vs Agassi, winner_id: Nadal
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (3, 10, 3);  -- Djokovic vs Alcaraz, winner_id: Djokovic
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (13, 14, 13); -- Zverev vs Tsitsipas, winner_id: Zverev
INSERT INTO matches (first_player_id, second_player_id, winner_id) VALUES (5, 15, 15);  -- S. Williams vs Swiatek, winner_id: Swiatek
