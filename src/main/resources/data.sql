INSERT INTO SITE_USER (
    id, email, password, name, nickname, phone_number, address, manner_score, profile_img, status, my_location, auth_type, created_at, updated_at
) VALUES (
             1,
             'mockEmail@gmail.com',
             'password',
             'name',
             'nickname',
             '010-1234-5678',
             'address',
             0,
             'profileImg',
             true,
             ST_GeomFromText('POINT(80.97796919 90.56667062)', 4326),
             'GENERAL',
             '2023-07-03 10:00:00',
             '2023-07-03 10:00:00'
         );
INSERT INTO SITE_USER (
    id, email, password, name, nickname, phone_number, address, manner_score, profile_img, status, my_location, auth_type, created_at, updated_at
) VALUES (
             2,
             'tlsehdgk1234@gmail.com',
             'password',
             '신동하',
             '자바조아',
             '010-1234-5678',
             'address',
             0,
             'profileImg',
             true,
             ST_GeomFromText('POINT(80.97796919 90.56667062)', 4326),
             'GENERAL',
             '2023-07-04 10:00:00',
             '2023-07-04 10:00:00'
         );

INSERT INTO SITE_USER (
    id, email, password, name, nickname, phone_number, address, manner_score, profile_img, status, my_location, auth_type, created_at, updated_at
) VALUES (
             3,
             'tlsehdgk1234@gmail.com',
             'password',
             '신동순',
             '자바조아',
             '010-1234-5678',
             'address',
             0,
             'profileImg',
             true,
             ST_GeomFromText('POINT(80.97796919 90.56667062)', 4326),
             'GENERAL',
             '2023-07-04 10:00:00',
             '2023-07-04 10:00:00'
         );

INSERT INTO SITE_USER (
    id, email, password, name, nickname, phone_number, address, manner_score, profile_img, status, my_location, auth_type, created_at, updated_at
) VALUES (
             4,
             'tlsehdgk1234@gmail.com',
             'password',
             '신동근',
             '자바조아',
             '010-1234-5678',
             'address',
             0,
             'profileImg',
             true,
             ST_GeomFromText('POINT(80.97796919 90.56667062)', 4326),
             'GENERAL',
             '2023-07-04 10:00:00',
             '2023-07-04 10:00:00'
         );


INSERT INTO MARKET (
    id, id_num, market_name, market_type, road_address, street_address, location
) VALUES (
             1,
             1010110,
             '방이시장',
             1,
             'road_address',
             'street_address',
             ST_GeomFromText('POINT(80.97796919 90.56667062)', 4326)
         );

INSERT INTO MARKET_PURCHASE_REQUEST(
    id,user_id, market_id, title, content,post_img, fee, status, meetup_time, MEETUP_DATE, MEETUP_ADDRESS, MEETUP_LOCATION, created_at, updated_at
) VALUES (
             1,
             '1',
             '1',
             'title',
             'content',
             'post-img',
             3000,
             'RECRUITING',
             '2023-07-10 09:00:00',
             '2023-07-10 09:00:00',
             '서울시 송파구 송파동',
             ST_GeomFromText('POINT(80.97796919 90.56667062)', 4326),
             '2023-07-04 10:00:00',
             '2023-07-04 10:00:00'
         );

INSERT INTO MARKET_PURCHASE_REQUEST(
    id,user_id, market_id, title, content,post_img, fee, status, meetup_time, MEETUP_DATE, MEETUP_ADDRESS, MEETUP_LOCATION, created_at, updated_at
) VALUES (
             2,
             '2',
             '1',
             'title',
             'content',
             'post-img',
             3000,
             'RECRUITING',
             '2023-07-10 09:00:00',
             '2023-07-10 09:00:00',
             '서울시 송파구 송파동',
             ST_GeomFromText('POINT(80.97796919 90.56667062)', 4326),
             '2023-07-04 10:00:00',
             '2023-07-04 10:00:00'
         );

INSERT INTO  CHATROOM(id, request_id, requester_id, agent_id, created_at)
    VALUES (
            '1',
            '1',
            '1',
            '2',
            '2023-07-04 10:00:00'

           )