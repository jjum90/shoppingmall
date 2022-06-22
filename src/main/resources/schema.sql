
    drop table if exists coupon CASCADE;


    drop table if exists member CASCADE;


    drop table if exists mileage CASCADE;


    drop table if exists order_product CASCADE;


    drop table if exists orders CASCADE;


    drop table if exists point CASCADE;


    drop table if exists point_history CASCADE;


    drop table if exists product CASCADE;


    drop sequence if exists hibernate_sequence;


    drop sequence if exists member_id_sequence;


    drop sequence if exists mileage_his_id_sequence;


    drop sequence if exists mileage_id_sequence;


    drop sequence if exists order_id_sequence;


    drop sequence if exists order_product_id_sequence;


    drop sequence if exists point_history_id_sequence;


    drop sequence if exists point_id_sequence;


    drop sequence if exists product_id_sequence;

 create sequence hibernate_sequence start with 1 increment by 1;
 create sequence member_id_sequence start with 2 increment by 50;
 create sequence mileage_id_sequence start with 2 increment by 50;
 create sequence order_id_sequence start with 1 increment by 50;
 create sequence order_product_id_sequence start with 1 increment by 50;
 create sequence point_history_id_sequence start with 1 increment by 50;
 create sequence point_id_sequence start with 4 increment by 50;
 create sequence product_id_sequence start with 8 increment by 50;

    create table coupon (
       coupon_id bigint not null,
        created_date timestamp,
        discount_rate double,
        reference_price numeric(19,2),
        use_status varchar(255),
        member_id bigint,
        primary key (coupon_id)
    );


    create table member (
       member_id bigint not null,
        created_date timestamp,
        name varchar(255),
        primary key (member_id)
    );


    create table mileage (
       mileage_id bigint not null,
        balance numeric(19,2),
        member_id bigint,
        primary key (mileage_id)
    );


    create table order_product (
       order_product_id bigint not null,
        created_date timestamp,
        count integer,
        order_price numeric(19,2),
        order_id bigint,
        product_id bigint,
        primary key (order_product_id)
    );


    create table orders (
       order_id bigint not null,
        created_date timestamp,
        mileage numeric(19,2),
        order_status varchar(255),
        pay_type varchar(255),
        payment numeric(19,2),
        coupon_id bigint,
        member_id bigint,
        primary key (order_id)
    );


    create table point (
       point_id bigint not null,
        created_date timestamp,
        balance numeric(19,2),
        expiry_date timestamp,
        member_id bigint,
        primary key (point_id)
    );


    create table point_history (
       point_his_id bigint not null,
        created_date timestamp,
        use_point numeric(19,2),
        order_id bigint,
        point_id bigint,
        primary key (point_his_id)
    );


    create table product (
       product_id bigint not null,
        created_date timestamp,
        name varchar(255),
        price numeric(19,2),
        primary key (product_id)
    );


    alter table coupon
       add constraint FK_COUPON_MEMBER
       foreign key (member_id)
       references member;


    alter table mileage
       add constraint FK_MILEAGE_MEMBER
       foreign key (member_id)
       references member;

    alter table order_product
       add constraint FK_ORDER_PRODUCT_ORDER
       foreign key (order_id)
       references orders;


    alter table order_product
       add constraint FK_ORDER_PRODUCT_PRODUCT
       foreign key (product_id)
       references product;


    alter table orders
       add constraint FK_ORDERS_COUPON
       foreign key (coupon_id)
       references coupon;


    alter table orders
       add constraint FK_ORDERS_MEMBER
       foreign key (member_id)
       references member;

    alter table point
       add constraint FK_POINT_MEMBER
       foreign key (member_id)
       references member;


    alter table point_history
       add constraint FK_POINT_HIS_ORDER
       foreign key (order_id)
       references orders;


    alter table point_history
       add constraint FK_POINT_HIST_POINT
       foreign key (point_id)
       references point;