/**
 * Author:  jorge
 * Created: 22 may 2025
 */

/** TODO: Add audit data **/

CREATE TABLE scenario (id UUID);
INSERT INTO scenario VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a');

CREATE TABLE role (id UUID, scenario UUID, name TEXT);
INSERT INTO role VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','7376f89d-4ca7-423b-95f1-e29a8832ec4a','user');
INSERT INTO role VALUES ('655cfb3d-c740-48d2-ab4f-51e391c4deaf','7376f89d-4ca7-423b-95f1-e29a8832ec4a','misterious person in the room');

CREATE TABLE session (id UUID, scenario UUID);
INSERT INTO session VALUES ('7376f89d-4ca7-423b-95f1-e29a8832ec4a','7376f89d-4ca7-423b-95f1-e29a8832ec4a');

CREATE TABLE interaction (id UUID, role UUID, actor UUID, session UUID, timestamp BIGINT, text LONGTEXT, isuser bit, context UUID);

CREATE TABLE context (id UUID, scenario UUID, name TEXT, physical_desc LONGTEXT);
INSERT INTO context VALUES ('af521f08-65f4-4171-9152-8e8e5c229ebf','7376f89d-4ca7-423b-95f1-e29a8832ec4a','the white room','this is an empty white room with one door and one window, in the middle of a forest');

CREATE TABLE actor (id UUID, name TEXT, physical_desc LONGTEXT);
INSERT INTO actor VALUES ('caa30e65-1886-4366-bfb7-f415af9f4a40', 'Jack Isparragus', 'He is a very ugly and old pirate with a wooden leg and a hook instead of a hand.');
INSERT INTO actor VALUES ('857fa610-b987-454c-96c3-bbf5354f13a0', 'Mr. Developer', 'He is a plain human.');