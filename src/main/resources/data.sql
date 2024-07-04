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
