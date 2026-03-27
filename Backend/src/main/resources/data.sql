-- ─── Staff Users (password = Admin@123) ──────────────────────
MERGE INTO USERS (ID,USERNAME,PASSWORD,ROLE,FULL_NAME,EMAIL,ACTIVE) KEY(USERNAME) VALUES
(1,'admin',   '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh/a','ADMIN',   'Admin User',    'admin@resto.com',   true),
(2,'manager', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh/a','MANAGER', 'Ravi Kumar',    'ravi@resto.com',    true),
(3,'waiter1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh/a','WAITER',  'Arjun Sharma',  'arjun@resto.com',   true),
(4,'waiter2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh/a','WAITER',  'Priya Nair',    'priya@resto.com',   true),
(5,'cashier', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lh/a','CASHIER', 'Deepa Menon',   'deepa@resto.com',   true);

-- ─── Menu Items ───────────────────────────────────────────────
MERGE INTO MENU (ID,NAME,DESCRIPTION,PRICE,CATEGORY,AVAILABLE,PREPARATION_TIME,VEG) KEY(ID) VALUES
(1,'Veg Spring Rolls',    'Crispy rolls with mixed vegetables',        120.00,'STARTER', true,10,true),
(2,'Chicken Wings',       'Spicy grilled wings with dip',              180.00,'STARTER', true,15,false),
(3,'Paneer Tikka',        'Grilled cottage cheese with spices',        160.00,'STARTER', true,20,true),
(4,'Fish Finger',         'Battered fried fish with tartar sauce',     200.00,'STARTER', true,12,false),
(5,'Veg Biryani',         'Fragrant basmati with vegetables',          220.00,'MAIN',    true,25,true),
(6,'Chicken Biryani',     'Dum-cooked chicken biryani',                280.00,'MAIN',    true,30,false),
(7,'Butter Chicken',      'Creamy tomato-based chicken curry',         300.00,'MAIN',    true,20,false),
(8,'Dal Makhani',         'Slow-cooked black lentils',                 180.00,'MAIN',    true,20,true),
(9,'Paneer Butter Masala','Rich paneer in tomato gravy',               250.00,'MAIN',    true,20,true),
(10,'Fish Curry',         'Kerala-style fish in coconut gravy',        320.00,'MAIN',    true,25,false),
(11,'Gulab Jamun',        'Soft milk solids in sugar syrup',           80.00, 'DESSERT', true,5,true),
(12,'Ice Cream',          'Vanilla / Chocolate / Strawberry',          90.00, 'DESSERT', true,2,true),
(13,'Rasgulla',           'Soft cottage cheese balls in syrup',        70.00, 'DESSERT', true,5,true),
(14,'Chocolate Brownie',  'Warm brownie with ice cream',               120.00,'DESSERT', true,8,true),
(15,'Masala Chai',        'Indian spiced tea',                         40.00, 'BEVERAGE',true,5,true),
(16,'Fresh Lime Soda',    'Sweet or salted lime soda',                 60.00, 'BEVERAGE',true,3,true),
(17,'Mango Lassi',        'Chilled mango yogurt drink',                80.00, 'BEVERAGE',true,3,true),
(18,'Cold Coffee',        'Blended coffee with milk and ice',          90.00, 'BEVERAGE',true,5,true);

-- ─── Customers ────────────────────────────────────────────────
MERGE INTO CUSTOMERS (ID,NAME,PHONE,EMAIL,VISIT_COUNT,LOYALTY_TIER,TOTAL_SPENT) KEY(ID) VALUES
(1,'Aditya Rao',   '9876500001','aditya@email.com',  12,'GOLD',   12500.0),
(2,'Sneha Patel',  '9876500002','sneha@email.com',    5,'SILVER',  4200.0),
(3,'Kiran Menon',  '9876500003','kiran@email.com',    2,'BRONZE',  1800.0),
(4,'Pooja Singh',  '9876500004','pooja@email.com',    8,'SILVER',  6500.0),
(5,'Rohit Verma',  '9876500005','rohit@email.com',   15,'GOLD',   18000.0);
