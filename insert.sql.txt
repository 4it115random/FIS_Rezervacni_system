INSERT INTO cenova_zona(price) VALUES (599);
INSERT INTO cenova_zona(price) VALUES (999);
INSERT INTO cenova_zona(price) VALUES (1499);
INSERT INTO cenova_zona(price) VALUES (1999);

INSERT INTO omezeni(type,name,description) VALUES (1,'vek 15','vekove omezeni vstupu 15 let');

INSERT INTO zarizeni(name,location) VALUES ('Kino Lucerna','Vaclavske Namesi, 5');
INSERT INTO zarizeni(name,location) VALUES ('Kino Smichov','Smichov, 44');
INSERT INTO zarizeni(name,location) VALUES ('Kino Flora','V Prameni, 12');
INSERT INTO zarizeni(name,location) VALUES ('Kino Zlicin','U Pecky, 33');
INSERT INTO zarizeni(name,location) VALUES ('Stadion Strahov','Na Strahove, 15');
INSERT INTO zarizeni(name,location) VALUES ('Stadion Letna','Na Letne, 3');

INSERT INTO benefit(name,description,sleva) VALUES ('sleva 5%','Sleva 5% na udalosti', 5);
INSERT INTO benefit(name,description,sleva) VALUES ('sleva 10%','Sleva 10% na udalosti', 10);
INSERT INTO benefit(name,description,sleva) VALUES ('sleva 20%','Sleva 20% na udalosti', 20);
INSERT INTO benefit(name,description,sleva) VALUES ('sleva 50%','Sleva 50% na udalosti', 50);
INSERT INTO benefit(name,description,sleva) VALUES ('vstup zdarma','vstup zdarma', 100);

INSERT INTO udalost(name,datum,available_seats,ticket_price,zarizeni_id) VALUES ('Koncert Lustela','2018-1-24',20000,599,5);
INSERT INTO udalost(name,datum,available_seats,ticket_price,zarizeni_id) VALUES ('Snimek Padesatka','2018-2-05',300,199,2);
INSERT INTO udalost(name,datum,available_seats,ticket_price,zarizeni_id) VALUES ('Snimek Padesatka','2018-2-06',350,1991,3);
INSERT INTO udalost(name,datum,available_seats,ticket_price,zarizeni_id) VALUES ('Snimek Padesatka','2018-2-07',300,199,4);
INSERT INTO udalost(name,datum,available_seats,ticket_price,zarizeni_id) VALUES ('Snimek Vcelka Maja','2018-1-17',300,149,2);
INSERT INTO udalost(name,datum,available_seats,ticket_price,zarizeni_id) VALUES ('Snimek Vcelka Maja','2018-1-17',300,149,1);
INSERT INTO udalost(name,datum,available_seats,ticket_price,zarizeni_id) VALUES ('Koncert LinkinPark','2018-2-15',20000,999,6);

INSERT INTO obsahuje(omezeni_id,udalost_id) VALUES (1,2);
INSERT INTO obsahuje(omezeni_id,udalost_id) VALUES (1,3);
INSERT INTO obsahuje(omezeni_id,udalost_id) VALUES (1,4);

INSERT INTO ma(cenova_zona_id,udalost_id) VALUES (2,1);
INSERT INTO ma(cenova_zona_id,udalost_id) VALUES (3,6);
INSERT INTO ma(cenova_zona_id,udalost_id) VALUES (4,6);