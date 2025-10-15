-- 초기 섹터 데이터 삽입 (중복 방지)
-- 데이터가 있어도 에러 없이 무시됨

-- 섹터
INSERT IGNORE INTO sectors (sector_id, sector_name) VALUES (1, '화학');
INSERT IGNORE INTO sectors (sector_id, sector_name) VALUES (2, '제약');
INSERT IGNORE INTO sectors (sector_id, sector_name) VALUES (3, '전기·전자');
INSERT IGNORE INTO sectors (sector_id, sector_name) VALUES (4, '운송장비·부품');
INSERT IGNORE INTO sectors (sector_id, sector_name) VALUES (5, '기타금융');
INSERT IGNORE INTO sectors (sector_id, sector_name) VALUES (6, '기계·장비');
INSERT IGNORE INTO sectors (sector_id, sector_name) VALUES (7, '금속');
INSERT IGNORE INTO sectors (sector_id, sector_name) VALUES (8, '건설');
INSERT IGNORE INTO sectors (sector_id, sector_name) VALUES (9, 'IT 서비스');

-- 주식 (섹터별)
-- 화학 (1)
INSERT IGNORE INTO stocks (stock_id, sector_id, stock_name, stock_image) VALUES 
('017860', 1, 'DS단석', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-017860.png%3F20231215?width=96&height=96'),
('278470', 1, '에이피알', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-278470.png?width=96&height=96'),
('090430', 1, '아모레퍼시픽', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-090430.png?width=96&height=96'),
('010955', 1, 'S-OIL우', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-010950.png?width=96&height=96');

-- 제약 (2)
INSERT IGNORE INTO stocks (stock_id, sector_id, stock_name, stock_image) VALUES 
('002630', 2, '오리엔트바이오', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-002630.png?width=96&height=96'),
('019175', 2, '신풍제약우', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-019170.png?width=96&height=96'),
('207940', 2, '삼성바이오로직스', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-207940.png?width=96&height=96'),
('068270', 2, '셀트리온', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-068270.png?width=96&height=96');

-- 전기·전자 (3)
INSERT IGNORE INTO stocks (stock_id, sector_id, stock_name, stock_image) VALUES 
('007340', 3, 'DN오토모티브', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-007340.png%3F20240508?width=96&height=96'),
('005930', 3, '삼성전자', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-005930.png?width=96&height=96'),
('000660', 3, 'SK하이닉스', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-000660.png?width=96&height=96'),
('005935', 3, '삼성전자우', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-005930.png?width=96&height=96');

-- 운송장비·부품 (4)
INSERT IGNORE INTO stocks (stock_id, sector_id, stock_name, stock_image) VALUES 
('042660', 4, '한화오션', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-042660.png%3F20230817?width=96&height=96'),
('012450', 4, '한화에어로스페이스', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-012450.png?width=96&height=96'),
('329180', 4, 'HD현대중공업', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-329180.png%3F20230830?width=96&height=96'),
('005380', 4, '현대차', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-005380.png?width=96&height=96'),
('000270', 4, '기아', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-000270.png%3F20241203?width=96&height=96');

-- 기타금융 (5)
INSERT IGNORE INTO stocks (stock_id, sector_id, stock_name, stock_image) VALUES 
('000157', 5, '두산2우B', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-000150.png%3F20230614?width=96&height=96'),
('000155', 5, '두산우', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-000150.png%3F20230614?width=96&height=96'),
('105560', 5, 'KB금융', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-105560.png?width=96&height=96');

-- 기계·장비 (6)
INSERT IGNORE INTO stocks (stock_id, sector_id, stock_name, stock_image) VALUES 
('009310', 6, '참엔지니어링', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-009310.png?width=96&height=96'),
('145210', 6, '다이나믹디자인', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-145210.png?width=96&height=96'),
('071970', 6, 'HD현대마린엔진', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-267250.png%3F20230830?width=96&height=96'),
('034020', 6, '두산에너빌리티', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-034020.png%3F20230614?width=96&height=96');

-- 금속 (7)
INSERT IGNORE INTO stocks (stock_id, sector_id, stock_name, stock_image) VALUES 
('000670', 7, '영풍', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-000670.png?width=96&height=96'),
('008970', 7, 'KBI동양철관', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-008970.png?width=96&height=96'),
('005490', 7, 'POSCO 홀딩스', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-005490.png?width=96&height=96'),
('010130', 7, '고려아연', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-010130.png?width=96&height=96'),
('079550', 7, 'LIG 넥스원', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-079550.png%3F20230717?width=96&height=96'),
('004020', 7, '현대제철', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-004020.png?width=96&height=96');

-- 건설 (8)
INSERT IGNORE INTO stocks (stock_id, sector_id, stock_name, stock_image) VALUES 
('097230', 8, 'HJ중공업', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-097230.png?width=96&height=96'),
('000725', 8, '현대건설', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-000720.png?width=96&height=96'),
('051600', 8, '한전KPS', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-051600.png?width=96&height=96'),
('006360', 8, 'GS 건설', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-006360.png?width=96&height=96'),
('047040', 8, '대우 건설', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-047040.png?width=96&height=96');

-- IT 서비스 (9)
INSERT IGNORE INTO stocks (stock_id, sector_id, stock_name, stock_image) VALUES 
('035420', 9, 'NAVER', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-035420.png?width=96&height=96'),
('035720', 9, '카카오', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-035720.png?width=96&height=96'),
('259960', 9, '크래프톤', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-259960.png?width=96&height=96'),
('012510', 9, '더존비즈온', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-012510.png?width=96&height=96'),
('181710', 9, 'NHN', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-181710.png?width=96&height=96'),
('030190', 9, 'NICE 평가정보', 'https://images.tossinvest.com/https%3A%2F%2Fstatic.toss.im%2Fpng-icons%2Fsecurities%2Ficn-sec-fill-030190.png%3F20250819?width=96&height=96');
