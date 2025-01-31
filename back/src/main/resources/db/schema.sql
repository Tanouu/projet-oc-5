CREATE TABLE IF NOT EXISTS TEACHERS  (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          last_name VARCHAR(40),
                          first_name VARCHAR(40),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS SESSIONS  (
                          id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(50),
                          description VARCHAR(2000),
                          date TIMESTAMP,
                          teacher_id INT,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS USERS (
                       id INT PRIMARY KEY AUTO_INCREMENT,
                       last_name VARCHAR(40),
                       first_name VARCHAR(40),
                       admin BOOLEAN NOT NULL DEFAULT false,
                       email VARCHAR(255),
                       password VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS PARTICIPATE (
                             user_id INT,
                             session_id INT
);

ALTER TABLE SESSIONS ADD FOREIGN KEY (teacher_id) REFERENCES TEACHERS (id);
ALTER TABLE PARTICIPATE ADD FOREIGN KEY (user_id) REFERENCES USERS (id);
ALTER TABLE PARTICIPATE ADD FOREIGN KEY (session_id) REFERENCES SESSIONS (id);
