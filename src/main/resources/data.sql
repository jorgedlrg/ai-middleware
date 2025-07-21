/**
 * Author:  jorge
 * Created: 22 may 2025
 */

INSERT IGNORE INTO scenario VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a', 'development scenario', 'development scenario');

INSERT IGNORE INTO role VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','7376f89d-4ca7-423b-95f1-e29a8832ec4a','user','user');
INSERT IGNORE INTO role VALUES ('655cfb3d-c740-48d2-ab4f-51e391c4deaf','7376f89d-4ca7-423b-95f1-e29a8832ec4a','misterious person in the room','misterious person in the room');
INSERT IGNORE INTO role VALUES ('cf9d843f-bfaa-4aa5-a22b-0754afc57502','7376f89d-4ca7-423b-95f1-e29a8832ec4a','third person in the scenario','third person in the scenario');

INSERT IGNORE INTO session VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','7376f89d-4ca7-423b-95f1-e29a8832ec4a','af521f08-65f4-4171-9152-8e8e5c229ebf','es_ES',null);

INSERT IGNORE INTO performance VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','7376f89d-4ca7-423b-95f1-e29a8832ec4a','857fa610-b987-454c-96c3-bbf5354f13a0');
INSERT IGNORE INTO performance VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','655cfb3d-c740-48d2-ab4f-51e391c4deaf','caa30e65-1886-4366-bfb7-f415af9f4a40');
INSERT IGNORE INTO performance VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','cf9d843f-bfaa-4aa5-a22b-0754afc57502','47c4b97f-c273-4a07-8dab-ab6336c15ae6');

INSERT IGNORE INTO context VALUES ('af521f08-65f4-4171-9152-8e8e5c229ebf','7376f89d-4ca7-423b-95f1-e29a8832ec4a','the white room','this is an empty white room with one door and one window, in the middle of a forest');

INSERT IGNORE INTO actor VALUES ('caa30e65-1886-4366-bfb7-f415af9f4a40', 'Jack Isparragus', 'He is a very ugly and old pirate with a wooden leg and a hook instead of a hand.', null);
INSERT IGNORE INTO actor VALUES ('857fa610-b987-454c-96c3-bbf5354f13a0', 'Mr. Developer', 'He is a plain human.','79dd7db9-2d83-4b0f-8321-579c413ec9fc');
INSERT IGNORE INTO actor VALUES ('47c4b97f-c273-4a07-8dab-ab6336c15ae6', 'Alfonsito', 'A very tall human. He is bald and has big glasses. He speaks funny.', null);

INSERT IGNORE INTO mind VALUES ('caa30e65-1886-4366-bfb7-f415af9f4a40','Talks as an old, funny pirate. He likes telling jokes and old sea stories.');

INSERT IGNORE INTO outfit VALUES ('79dd7db9-2d83-4b0f-8321-579c413ec9fc','boat shaped paper hat','a boat-shaped paper hat');

INSERT IGNORE INTO usertable VALUES ('857fa610-b987-454c-96c3-bbf5354f13a0','developer@developer.com');
INSERT IGNORE INTO settings VALUES ('857fa610-b987-454c-96c3-bbf5354f13a0','ollama',null,'google/gemma-3-12b-it','http://localhost:11434','gemma3:12b',true,true,true);
