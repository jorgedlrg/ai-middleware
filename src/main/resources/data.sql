/**
 * Author:  jorge
 * Created: 22 may 2025
 */

/** TODO: Add audit data **/

CREATE TABLE scenario (id UUID NOT NULL PRIMARY KEY);
INSERT INTO scenario VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a');

CREATE TABLE role (id UUID NOT NULL PRIMARY KEY, scenario UUID NOT NULL, name TEXT NOT NULL);
INSERT INTO role VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','7376f89d-4ca7-423b-95f1-e29a8832ec4a','user');
INSERT INTO role VALUES ('655cfb3d-c740-48d2-ab4f-51e391c4deaf','7376f89d-4ca7-423b-95f1-e29a8832ec4a','misterious person in the room');
INSERT INTO role VALUES ('cf9d843f-bfaa-4aa5-a22b-0754afc57502','7376f89d-4ca7-423b-95f1-e29a8832ec4a','third person in the scenario');

CREATE TABLE session (id UUID NOT NULL PRIMARY KEY, scenario UUID NOT NULL, current_context UUID);
INSERT INTO session VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','7376f89d-4ca7-423b-95f1-e29a8832ec4a','af521f08-65f4-4171-9152-8e8e5c229ebf');

CREATE TABLE performance (session UUID NOT NULL, role UUID NOT NULL, actor UUID NOT NULL, PRIMARY KEY(session, role));
INSERT INTO performance VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','7376f89d-4ca7-423b-95f1-e29a8832ec4a','857fa610-b987-454c-96c3-bbf5354f13a0');
INSERT INTO performance VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','655cfb3d-c740-48d2-ab4f-51e391c4deaf','caa30e65-1886-4366-bfb7-f415af9f4a40');
INSERT INTO performance VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','cf9d843f-bfaa-4aa5-a22b-0754afc57502','47c4b97f-c273-4a07-8dab-ab6336c15ae6');

CREATE TABLE interaction (id UUID NOT NULL PRIMARY KEY, role UUID NOT NULL, actor UUID NOT NULL, session UUID NOT NULL, timestamp BIGINT NOT NULL, text LONGTEXT NOT NULL, context UUID NOT NULL);

CREATE TABLE context (id UUID NOT NULL PRIMARY KEY, scenario UUID NOT NULL, name TEXT NOT NULL, physical_desc LONGTEXT NOT NULL);
INSERT INTO context VALUES ('af521f08-65f4-4171-9152-8e8e5c229ebf','7376f89d-4ca7-423b-95f1-e29a8832ec4a','the white room','this is an empty white room with one door and one window, in the middle of a forest');

CREATE TABLE actor (id UUID NOT NULL PRIMARY KEY, name TEXT NOT NULL, physical_desc LONGTEXT NOT NULL, current_outfit UUID);
INSERT INTO actor VALUES ('caa30e65-1886-4366-bfb7-f415af9f4a40', 'Jack Isparragus', 'He is a very ugly and old pirate with a wooden leg and a hook instead of a hand.', null);
INSERT INTO actor VALUES ('857fa610-b987-454c-96c3-bbf5354f13a0', 'Mr. Developer', 'He is a plain human.','79dd7db9-2d83-4b0f-8321-579c413ec9fc');
INSERT INTO actor VALUES ('47c4b97f-c273-4a07-8dab-ab6336c15ae6', 'Alfonsito', 'A very tall human. He is bald and has big glasses. He speaks funny.', null);

CREATE TABLE mind (actor UUID NOT NULL PRIMARY KEY, personality LONGTEXT NOT NULL);
INSERT INTO mind VALUES ('caa30e65-1886-4366-bfb7-f415af9f4a40','Talks as an old, funny pirate. He likes telling jokes and old sea stories.');

CREATE TABLE outfit (id UUID NOT NULL PRIMARY KEY, actor UUID NOT NULL, description LONGTEXT NOT NULL);
INSERT INTO outfit VALUES ('79dd7db9-2d83-4b0f-8321-579c413ec9fc','857fa610-b987-454c-96c3-bbf5354f13a0','a boat-shaped paper hat');