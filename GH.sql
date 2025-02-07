drop table member cascade constraint;
drop table donation cascade constraint;
drop table payment cascade constraint;
drop table news cascade constraint;
drop table image cascade constraint;

CREATE TABLE member (
    mem_No NUMBER primary key NOT NULL,
    mem_Name VARCHAR2(30) NOT NULL,
    mem_Id VARCHAR2(30) NOT NULL unique,
    mem_Pwd VARCHAR2(2000) NOT NULL,
    mem_Address VARCHAR2(300) NOT NULL,
    mem_Type VARCHAR2(20) NOT NULL,
    mem_Grade VARCHAR2(20) DEFAULT 'UNRANK' NOT NULL,
    mem_Status VARCHAR2(3) DEFAULT 'Y' NOT NULL check(mem_status in('Y', 'N')),
    mem_confirm varchar2(3) NOT NULL,
    mem_email unique not null
);
	
CREATE TABLE donation (
    do_No NUMBER PRIMARY KEY NOT NULL,
    do_Title VARCHAR2(50) NOT NULL,
    do_Content CLOB NOT NULL,
    do_Goal NUMBER NOT NULL,
    do_Category VARCHAR2(30) NOT NULL,
    do_Startdate DATE NOT NULL,
    do_enddate DATE NOT NULL,
    do_Views NUMBER DEFAULT 0 NOT NULL,
    do_Createdate DATE DEFAULT SYSDATE NOT NULL,
    do_Status VARCHAR2(3) DEFAULT 'Y' NOT NULL,
    mem_No NUMBER NOT NULL,
    CONSTRAINT chk_do_status CHECK (do_Status IN ('Y', 'N')),
    CONSTRAINT FK_DO_mem FOREIGN KEY (mem_No) REFERENCES MEMBER(mem_No) ON DELETE CASCADE
);
	
CREATE TABLE payment (
    pay_No NUMBER PRIMARY KEY NOT NULL, 
    pay_Amount NUMBER DEFAULT 0 NOT NULL, 
    pay_Createdate DATE DEFAULT SYSDATE NOT NULL, 
    pay_Status VARCHAR2(50) NOT NULL, 
    mem_No NUMBER NOT NULL, 
    do_No NUMBER NOT NULL, 
    CONSTRAINT chk_pay_status CHECK (pay_Status IN ('pending', 'success','fail')), 
    CONSTRAINT FK_pay_mem FOREIGN KEY (mem_No) REFERENCES member(mem_No) ON DELETE CASCADE, 
    CONSTRAINT FK_pay_do FOREIGN KEY (do_No) REFERENCES donation(do_No) ON DELETE CASCADE
);
	
CREATE TABLE news (
    news_No NUMBER PRIMARY KEY NOT NULL, 
    news_Title VARCHAR2(50) NOT NULL, 
    news_Content CLOB NOT NULL, 
    news_Createdate DATE DEFAULT SYSDATE NOT NULL, 
    news_Status VARCHAR2(3) DEFAULT 'Y' NOT NULL, 
    mem_No NUMBER NOT NULL, 
    do_No NUMBER NOT NULL, 
    CONSTRAINT chk_news_status CHECK (news_Status IN ('Y', 'N')), 
    CONSTRAINT fk_news_mem FOREIGN KEY (mem_No) REFERENCES member(mem_No) ON DELETE CASCADE, 
    CONSTRAINT fk_news_do FOREIGN KEY (do_No) REFERENCES donation(do_No) ON DELETE CASCADE
);


CREATE TABLE image (	
	img_no NUMBER primary key NOT NULL,
	img_path varchar2(2000) NOT NULL,
    img_name varchar2(1000) not null,
    img_rename varchar2(1000) not null,
    img_type varchar2(3) not null,
    ref_no number not null
);

	
create SEQUENCE seq_mem_no;	
create SEQUENCE seq_do_no;	
create SEQUENCE seq_pay_no;	
create SEQUENCE seq_news_no;	
create SEQUENCE seq_img_no;
commit;
