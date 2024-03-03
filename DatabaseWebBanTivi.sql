CREATE DATABASE webbantivi

go

USE webbantivi
go

CREATE TABLE discount
  (
     id			 INT IDENTITY(1, 1) PRIMARY KEY,
     code		 VARCHAR(30) not null unique,
     [value]	 VARCHAR(10) not null,
	 image		 varchar(200),
	 date_start  DATE not null,
     date_end	 DATE not null,
     active		 BIT
  )

create table group_product(
  id int identity(1,1) primary key,
  name_group nvarchar(100)
  
)


Create table field(
	id		int identity(1,1) primary key,
	code	varchar(10),
	name	nvarchar(100) not null,
	variant bit,
	active  bit
)
Create table Brand
(
id	   int identity(1,1) primary key,
name   nvarchar(100),
active bit
)

CREATE TABLE product
  (
     id             INT IDENTITY(1, 1) PRIMARY KEY,
     sku            VARCHAR(40),
	 group_product	int references group_product(id),
	 name_product   nvarchar(200),
	 avg_point		float,
	 date_create    datetime,
	 id_brand		int references brand(id),
     active         BIT
)



CREATE TABLE product_detail
  (
     id             INT IDENTITY(1, 1) PRIMARY KEY,
	 product_id		int references product(id),
     sku            VARCHAR(40),
     price_import   money,
     price_export   money,
     quantity       INT,
	 date_create    datetime,
	 id_discount	int references discount(id),
     active         BIT
)


create table product_field_value (
	id					int	identity(1,1) primary key,
	value				nvarchar(1000),
	field_id			int references field(id),
	product_id			int references product(id),
)

create table product_detail_field(
	id				int identity(1,1) primary key,
	field_id		int references field(id),
	product_detail	int references product_detail(id),
	value			nvarchar(1000)
)

create table image (
	id					int identity(1,1) primary key,
	id_product			int references product_detail(id),
	link				varchar(1000),
	location			int
)

-- tài khoản
CREATE TABLE users
  (
     id                INT IDENTITY(1, 1) PRIMARY KEY,
	 username          NVARCHAR(50), 
     [name]            NVARCHAR(50),
     [date]            DATE,
     [address]         NVARCHAR(max),
     phone_number      VARCHAR(10) not null,
     email             VARCHAR(50) UNIQUE not null,
	 password          VARCHAR(max),
     gender            BIT,
     avatar            VARCHAR(50),
	 roles              varchar(20),
     status            bit not null,
  )
  

CREATE TABLE voucher
  (
     id               INT IDENTITY(1, 1) PRIMARY KEY,
     code             VARCHAR(30) not null unique,
     name_voucher     NVARCHAR(100) not null,
     [value]          INT not null,
     minimum_value    MONEY not null,-- giá trị đơn hàng tối thiểu cần
     quantity         INT not null, -- số lượng voucher
	 start_day		  DATE not null,-- thời gian bắt đầu có hiệu lực
     expiration_date  DATE not null,-- thời gian mã giảm giá hết hiệu lực
	 active			  BIT,
	 image			  varchar(100)
  )

CREATE TABLE voucher_user
  (	 id			 Int identity(1,1) primary key,
     id_user	 INT references users(id),
     id_voucher  INT references voucher(id),
	 active      BIT not null
  )

-- đánh giá
CREATE TABLE evaluate
  (
	 id          INT IDENTITY(1, 1) PRIMARY KEY,
     id_product  INT REFERENCES product(id) not null,
     id_user	 INT REFERENCES users(id) not null,
     date_create DATETIME not null,
     point       float not null,
     comment     NVARCHAR(max),
	 active		bit
  )

CREATE TABLE image_evaluate
  (
     id          INT IDENTITY(1, 1) PRIMARY KEY,
     id_evaluate INT REFERENCES evaluate(id) not null,
	 name_image  varchar(200) not null
  )

-- trạng thái hóa đơn
CREATE TABLE bill_status
  (
     id          INT IDENTITY(1, 1) PRIMARY KEY,
	 code		 varchar(10) not null unique,
     status      NVARCHAR(100) not null,
     description NVARCHAR(max) not null
  )

-- phương thức thanh toán
CREATE TABLE payment_method
  (
     id             INT IDENTITY(1, 1) PRIMARY KEY,
	 code			varchar(10) not null unique,
     payment_method NVARCHAR(255) not null,
     active         BIT not null,
     description    NVARCHAR(max)
  )

--hóa đơn
CREATE TABLE bill
  (
     id               INT IDENTITY(1, 1) PRIMARY KEY,
     id_user		  INT REFERENCES users(id) not null,
     code             VARCHAR(20) not null unique,
     create_date      DATE NOT NULL,
	 total_price	  Money,
     payment_date     DATE not null,-- ngày thanh toán
     id_status        INT REFERENCES bill_status(id) not null,
     id_paymentmethod INT REFERENCES payment_method(id) not null,
	 id_voucher		  int references voucher(id),
	 voucher_value	  money,
	 payment_status	  INT default 1,
     note             NVARCHAR(max),

  )

-- hoa don chi tiet
CREATE TABLE bill_product
  (
	 id				int identity(1,1) primary key,
     id_bill		INT REFERENCES bill(id),
     id_product		INT REFERENCES product_detail(id),
     quantity		INT not null,
     price			money not null,
	 reduced_money	money,
	 status			int,
	 note			NVARCHAR(200),
   --  PRIMARY KEY(id_bill, id_product)
  )
  CREATE TABLE history_billProduct
  (
     id			          INT IDENTITY(1, 1) PRIMARY KEY,
	 id_bill              int REFERENCES bill(id) not null,
     id_bill_product	  INT REFERENCES bill_product(id) not null,
     quantity_request_return	INT,
	 quantity_accept_return		INT,
	 date                DATETIME ,
     reason 		     nvarchar(max),
     status		         int ,
	 return_times        int ,
	 note               nvarchar(max),
	 status_bill_product int,
	 return_method      int,
	 account_number_bank nvarchar(100),
	 bank  nvarchar(max),
	 owner nvarchar(max)

  )
 

CREATE TABLE image_returned
  (
     id					 INT IDENTITY(1, 1) PRIMARY KEY,
     id_history_billProduct	 INT REFERENCES history_billProduct(id) not null,
	 name_image			 varchar(200) not null,
  )


CREATE TABLE delivery_notes
  (
     id					INT IDENTITY(1,1) PRIMARY KEY,
     received			NVARCHAR(50) not null,
     received_phone		VARCHAR(20) not null,
	 received_email		VARCHAR(100) not null,
	 receiving_address  NVARCHAR(200) NOT NULL,
     deliver			NVARCHAR(50),
     delivery_phone		VARCHAR(20) not null,
     delivery_date		DATE,
     received_date		DATE,
     delivery_fee		MONEY,
     note				NVARCHAR(max),
     status				INT,
	 id_bill			INT REFERENCES bill(id) not null,
  )

--giỏ hàng
CREATE TABLE cart
  (
     id          INT IDENTITY(1, 1) PRIMARY KEY,
     id_users	INT REFERENCES users(id) not null,
     code        NVARCHAR(30),
  )

-- giỏ hàng chi tiết
CREATE TABLE cart_product
  (
     cart_id     INT REFERENCES cart(id),
     product_id  INT REFERENCES product_detail(id),
     quantity    INT not null,
     note        NVARCHAR(max),
     PRIMARY KEY(cart_id, product_id)
  )
GO


insert into users(username, password,gender,roles,status,phone_number,email)
values('PDO','$2a$10$qHWKLl/DjkAuZaVcqOML4OsFYzLMPcD70E1xh9DA30K7takJbMRXO',0,'ADMIN',1,'0978973','opham084@gmail.com')

insert into users(username, password,gender,roles,status,phone_number,email)
values('PDO123','$2a$10$qHWKLl/DjkAuZaVcqOML4OsFYzLMPcD70E1xh9DA30K7takJbMRXO',0,'USER',1,'0978973','phamducoanh03@gmail.com')


 INSERT INTO [dbo].[bill_status]
           ([code]
           ,[status]
           ,[description])
     VALUES
           ('WP'
           ,N'Chờ xử lý'
           ,N'Chờ xác nhận mua hàng bên phía khách hàng')
GO
INSERT INTO [dbo].[bill_status]
           ([code]
           ,[status]
           ,[description])
     VALUES
           ('PG'
           ,N'Đang chuẩn bị hàng'
           ,N'Cửa hàng chuẩn bị hàng, đóng gói hàng cho khách')
GO
INSERT INTO [dbo].[bill_status]
           ([code]
           ,[status]
           ,[description])
     VALUES
           ('DE'
           ,N'Đang giao hàng'
           ,N'Sản phẩm đang được giao đến cho khách hàng')
GO

INSERT INTO [dbo].[bill_status]
           ([code]
           ,[status]
           ,[description])
     VALUES
           ('CO'
           ,N'Đã hoàn thành'
           ,N'Đơn hàng giao thành công đến cho khách hàng')
		   GO
INSERT INTO [dbo].[bill_status]
           ([code]
           ,[status]
           ,[description])
     VALUES
           ('CA'
           ,N'Hủy hàng'
           ,N'Đơn hàng bị hủy')

GO
INSERT INTO [dbo].[bill_status]
           ([code]
           ,[status]
           ,[description])
     VALUES
           ('RR'
           ,N'Yêu cầu trả hàng'
           ,N'Khách hàng yêu cầu trả hàng')

GO
INSERT INTO [dbo].[bill_status]
           ([code]
           ,[status]
           ,[description])
     VALUES
           ('WR'
           ,N'Chờ trả hàng'
           ,N'Chờ sản phẩm gửi về khi yêu cầu trả hàng được xác nhận')
GO

INSERT INTO [dbo].[bill_status]
           ([code]
           ,[status]
           ,[description])
     VALUES
           ('RE'
           ,N'Đã trả hàng'
           ,N'Đã nhận lại được sản phẩm bị yêu cầu trả hàng')
GO


INSERT INTO [dbo].[bill_status]
           ([code]
           ,[status]
           ,[description])
     VALUES
           ('RN'
           ,N'Hoàn trả hàng'
           ,N'Hoàn trả hàng khi khách không nhận hàng')
GO
INSERT INTO [dbo].[payment_method]
           ([code]
           ,[payment_method]
           ,[active]
           ,[description])
     VALUES
           ('CA'
           ,N'Tiền mặt'
           ,1
           ,N'Thanh toán trực tiếp bằng tiền mặt khi nhận hàng')
GO

INSERT INTO [dbo].[payment_method]
           ([code]
           ,[payment_method]
           ,[active]
           ,[description])
     VALUES
           ('VNP'
           ,'Thanh toán điện tử Vn-pay'
           ,1
           ,N'Thanh toán trực tuyến qua Vn-pay')
GO

insert into group_product values('Tivi')
insert into group_product values(N'Phụ kiện')

insert into Brand values(N'Samsung',1)
insert into Brand values(N'Sony',1)
insert into Brand values(N'LG',1)
insert into field values('123','Loai Tivi', 1,1)
