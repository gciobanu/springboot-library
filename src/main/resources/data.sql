INSERT INTO category(id, name) VALUES (1, 'Literature');
INSERT INTO category(id, name) VALUES (2, 'Fiction');
INSERT INTO category(id, name) VALUES (3, 'Science');
INSERT INTO category(id, name) VALUES (4, 'Children books');

INSERT INTO book(id, title, author)
VALUES (1, 'Book 1', 'Authors of book 1');
INSERT INTO book(id, title, author)
VALUES (2, 'Book 2', 'Authors of book 2');
INSERT INTO book(id, title, author)
VALUES (3, 'Book 3', 'Authors of book 3');
INSERT INTO book(id, title, author)
VALUES (4, 'Book 4', 'Authors of book 4');

INSERT INTO book_category(book_id, category_id)
VALUES (1, 1);
INSERT INTO book_category(book_id, category_id)
VALUES (2, 1);
INSERT INTO book_category(book_id, category_id)
VALUES (3, 2);

INSERT INTO book_category(book_id, category_id)
VALUES (4, 1);
INSERT INTO book_category(book_id, category_id)
VALUES (4, 4);


INSERT INTO usermember(id, firstname, surname)
VALUES (1, 'John', 'Doe');
INSERT INTO usermember(id, firstname, surname)
VALUES (2, 'Jane', 'Doe');
INSERT INTO usermember(id, firstname, surname)
VALUES (3, 'Johnny', 'Kid');


