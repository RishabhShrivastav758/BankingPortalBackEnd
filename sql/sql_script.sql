use bankingwebapplication;

CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT,
  `accountNumber` varchar(100) NOT NULL unique,
  `balance` double NOT NULL,
  `pin` varchar(20) NOT NULL,
  `accountStatus` varchar(50) NOT NULL,
  `account_type` varchar(20) NOT NULL,
  `branch` varchar(50) NOT NULL,
  `IFSC_code` varchar(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`id`),
  CONSTRAINT `user_id` FOREIGN KEY (`id`) REFERENCES `user` (`id`) ON DELETE CASCADE on update cascade
);

INSERT INTO `account` (`accountNumber`,`balance`, `pin`, `accountStatus`,`account_type`,branch,IFSC_code)
 VALUES ('159123456789125', 45000.00, '9867', 'Active', 'Saving', 'Bangalore City', 'SBI1234');

 INSERT INTO `account` (`accountNumber`,`balance`, `pin`, `accountStatus`,`account_type`,branch,IFSC_code)
VALUES ('159986545123565', 55000.00, '9867', 'Active', 'Saving', 'Bangalore City', 'SBI1234');

 INSERT INTO `account` (`accountNumber`,`balance`, `pin`, `accountStatus`,`account_type`,branch,IFSC_code)
VALUES ('159847525489532', 80000.00, '9867', 'Active', 'Saving', 'Bangalore City', 'SBI1234');

 INSERT INTO `account` (`accountNumber`,`balance`, `pin`, `accountStatus`,`account_type`,branch,IFSC_code)
 VALUES ('159642517326984', 74000.00, '9867', 'Active', 'Saving', 'Bangalore City', 'SBI1234');

CREATE TABLE `otpInfo` (
  `id` int NOT NULL auto_increment,
   `accountNumber` varchar(100) NOT NULL,
  `otp` varchar(100),
  `generatedAt` timestamp DEFAULT NULL,
  PRIMARY KEY (`id`)
);


CREATE TABLE `transaction` (
  `id` int NOT NULL auto_increment,
  `amount` double NOT NULL,
  `transactionType` varchar(100) Not Null,
  `transaction_date` date NOT NULL,
  source_account_id int NOT NULL,
  target_account_id int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `source_account_idx` (`source_account_id`),
  KEY `target_account_idx` (`target_account_id`),
  CONSTRAINT `source_account` FOREIGN KEY (`source_account_id`) REFERENCES `Account` (`id`),
  CONSTRAINT `target_account` FOREIGN KEY (`target_account_id`) REFERENCES `Account` (`id`)
);

INSERT INTO `transaction` (`amount`, `transactionType`, `transaction_date`, source_account_id, target_account_id)
VALUES (3000, 'CASH_DEPOSIT', CURDATE(), 1, 2);

INSERT INTO `transaction` (`amount`, `transactionType`, `transaction_date`, source_account_id, target_account_id)
VALUES (4000, 'CASH_WITHDRAWAL', CURDATE(), 2, 3);

INSERT INTO `transaction` (`amount`, `transactionType`, `transaction_date`, source_account_id, target_account_id)
VALUES (6000, 'CASH_TRANSFER', CURDATE(), 3, 4);

INSERT INTO `transaction` (`amount`, `transactionType`, `transaction_date`, source_account_id, target_account_id)
VALUES (13000, 'CASH_DEPOSIT', CURDATE(), 3, 1);


CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `password` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL unique,
  `address` varchar(200) NOT NULL,
  `phone_number` varchar(15) NOT NULL,
  `otpRetryCount` int,
  `lastOtpRequestTime` timestamp DEFAULT NULL,
  PRIMARY KEY (`id`)
);

INSERT INTO `user` ( `name`, `password`, `email`, `address`, `phone_number`, `lastOtpRequestTime`)
 VALUES ( 'John', '12345', 'john@gmail.com', 'BANGALORE', '1234567891', current_timestamp());

 INSERT INTO `user` ( `name`, `password`, `email`, `address`, `phone_number`, `lastOtpRequestTime`)
 VALUES ( 'Danny', '12345', 'danny@gmail.com', 'BANGALORE', '6548791245', current_timestamp());

 INSERT INTO `user` ( `name`, `password`, `email`, `address`, `phone_number`, `lastOtpRequestTime`)
 VALUES ( 'Joe', '12345', 'joe@gmail.com', 'BANGALORE', '6598784523', current_timestamp());

 INSERT INTO `user` ( `name`, `password`, `email`, `address`, `phone_number`, `lastOtpRequestTime`)
 VALUES ( 'Jimmy', '12345', 'jimmy@gmail.com', 'BANGALORE', '2645781232', current_timestamp());










































INSERT INTO `loans` ( `customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`, `create_dt`)
 VALUES ( 1, '2020-06-06', 'Vehicle', 40000, 10000, 30000, '2020-06-06');

INSERT INTO `loans` ( `customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`, `create_dt`)
 VALUES ( 1, '2018-02-14', 'Home', 50000, 10000, 40000, '2018-02-14');

INSERT INTO `loans` ( `customer_id`, `start_dt`, `loan_type`, `total_loan`, `amount_paid`, `outstanding_amount`, `create_dt`)
 VALUES ( 1, '2018-02-14', 'Personal', 10000, 3500, 6500, '2018-02-14');

CREATE TABLE `cards` (
  `card_id` int NOT NULL AUTO_INCREMENT,
  `card_number` varchar(100) NOT NULL,
  `customer_id` int NOT NULL,
  `card_type` varchar(100) NOT NULL,
  `total_limit` int NOT NULL,
  `amount_used` int NOT NULL,
  `available_amount` int NOT NULL,
  `create_dt` date DEFAULT NULL,
  PRIMARY KEY (`card_id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `card_customer_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE
);

INSERT INTO `cards` (`card_number`, `customer_id`, `card_type`, `total_limit`, `amount_used`, `available_amount`, `create_dt`)
 VALUES ('4565XXXX4656', 1, 'Credit', 10000, 500, 9500, CURDATE());

INSERT INTO `cards` (`card_number`, `customer_id`, `card_type`, `total_limit`, `amount_used`, `available_amount`, `create_dt`)
 VALUES ('3455XXXX8673', 1, 'Credit', 7500, 600, 6900, CURDATE());

INSERT INTO `cards` (`card_number`, `customer_id`, `card_type`, `total_limit`, `amount_used`, `available_amount`, `create_dt`)
 VALUES ('2359XXXX9346', 1, 'Credit', 20000, 4000, 16000, CURDATE());

CREATE TABLE `notice_details` (
  `notice_id` int NOT NULL AUTO_INCREMENT,
  `notice_summary` varchar(200) NOT NULL,
  `notice_details` varchar(500) NOT NULL,
  `notic_beg_dt` date NOT NULL,
  `notic_end_dt` date DEFAULT NULL,
  `create_dt` date DEFAULT NULL,
  `update_dt` date DEFAULT NULL,
  PRIMARY KEY (`notice_id`)
);

INSERT INTO `notice_details` ( `notice_summary`, `notice_details`, `notic_beg_dt`, `notic_end_dt`, `create_dt`, `update_dt`)
VALUES ('Home Loan Interest rates reduced', 'Home loan interest rates are reduced as per the goverment guidelines. The updated rates will be effective immediately',
CURDATE() - INTERVAL 30 DAY, CURDATE() + INTERVAL 30 DAY, CURDATE(), null);

INSERT INTO `notice_details` ( `notice_summary`, `notice_details`, `notic_beg_dt`, `notic_end_dt`, `create_dt`, `update_dt`)
VALUES ('Net Banking Offers', 'Customers who will opt for Internet banking while opening a saving account will get a $50 amazon voucher',
CURDATE() - INTERVAL 30 DAY, CURDATE() + INTERVAL 30 DAY, CURDATE(), null);

INSERT INTO `notice_details` ( `notice_summary`, `notice_details`, `notic_beg_dt`, `notic_end_dt`, `create_dt`, `update_dt`)
VALUES ('Mobile App Downtime', 'The mobile application of the EazyBank will be down from 2AM-5AM on 12/05/2020 due to maintenance activities',
CURDATE() - INTERVAL 30 DAY, CURDATE() + INTERVAL 30 DAY, CURDATE(), null);

INSERT INTO `notice_details` ( `notice_summary`, `notice_details`, `notic_beg_dt`, `notic_end_dt`, `create_dt`, `update_dt`)
VALUES ('E Auction notice', 'There will be a e-auction on 12/08/2020 on the Bank website for all the stubborn arrears.Interested parties can participate in the e-auction',
CURDATE() - INTERVAL 30 DAY, CURDATE() + INTERVAL 30 DAY, CURDATE(), null);

INSERT INTO `notice_details` ( `notice_summary`, `notice_details`, `notic_beg_dt`, `notic_end_dt`, `create_dt`, `update_dt`)
VALUES ('Launch of Millennia Cards', 'Millennia Credit Cards are launched for the premium customers of EazyBank. With these cards, you will get 5% cashback for each purchase',
CURDATE() - INTERVAL 30 DAY, CURDATE() + INTERVAL 30 DAY, CURDATE(), null);

INSERT INTO `notice_details` ( `notice_summary`, `notice_details`, `notic_beg_dt`, `notic_end_dt`, `create_dt`, `update_dt`)
VALUES ('COVID-19 Insurance', 'EazyBank launched an insurance policy which will cover COVID-19 expenses. Please reach out to the branch for more details',
CURDATE() - INTERVAL 30 DAY, CURDATE() + INTERVAL 30 DAY, CURDATE(), null);

CREATE TABLE `contact_messages` (
  `contact_id` varchar(50) NOT NULL,
  `contact_name` varchar(50) NOT NULL,
  `contact_email` varchar(100) NOT NULL,
  `subject` varchar(500) NOT NULL,
  `message` varchar(2000) NOT NULL,
  `create_dt` date DEFAULT NULL,
  PRIMARY KEY (`contact_id`)
);

CREATE TABLE `authorities` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `authorities_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`)
);

INSERT INTO `authorities` (`customer_id`, `name`)
 VALUES (1, 'VIEWACCOUNT');

INSERT INTO `authorities` (`customer_id`, `name`)
 VALUES (1, 'VIEWCARDS');

 INSERT INTO `authorities` (`customer_id`, `name`)
  VALUES (1, 'VIEWLOANS');

 INSERT INTO `authorities` (`customer_id`, `name`)
   VALUES (1, 'VIEWBALANCE');

 DELETE FROM `authorities`;

 INSERT INTO `authorities` (`customer_id`, `name`)
  VALUES (1, 'ROLE_USER');

 INSERT INTO `authorities` (`customer_id`, `name`)
  VALUES (1, 'ROLE_ADMIN');
