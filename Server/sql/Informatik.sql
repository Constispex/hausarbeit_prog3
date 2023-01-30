create table IF NOT EXISTS Buecher
(
    Title     varchar(150) not null,
    Author    varchar(600) null,
    Publisher varchar(200) null,
    Rating    int(1)       null,
    Subareas  varchar(200) null,
    constraint Informatik_pk
        primary key (Title)
);

INSERT INTO buecher(Title, Author, Publisher, Rating, Subareas)
VALUES ('Einführung in die Informatik', 'Bastian Küppers', 'Springer Vieweg', '3',
        'Theoretische und praktische Grundlagen');

INSERT INTO buecher(Title, Author, Publisher, Rating, Subareas)
VALUES ('Grundkurs Mobile Kommunikationssysteme', 'Martin Sauter', 'Springer Vieweg', '5',
        '5G New Radio und Kernnetz, LTE-Advanced Pro, GSM, Wireless LAN und Bluetooth');

INSERT INTO buecher(Title, Author, Publisher, Rating, Subareas)
VALUES ('Betriebssysteme kompakt', 'Christian Baun', 'Springer Vieweg', '2',
        'Grundlagen, Hardware, Speicher, Daten und Dateien, Prozesse und Kommunikation, Virtualisierung');

INSERT INTO buecher(Title, Author, Publisher, Rating, Subareas)
VALUES ('Quantencomputing für Dummies', 'Hans-Jürgen Steffens, Christian Zöllner, Kathrin Schäfer', 'Wiley-VCH', '5',
        'Elektrotechnik u. Elektronik, Informatik, Quantencomputer, Quantencomputing');

INSERT INTO buecher(Title, Author, Publisher, Rating, Subareas)
VALUES ('Fit fürs Studium – Informatik', 'Arne Boockmeyer, Philipp Fischbeck, Stefan Neubert', 'Rheinwerk', '1',
        'Grundkonzepte der Informatik verstehen und Wissenslücken schließen');