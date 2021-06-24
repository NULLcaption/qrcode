package com.cxg.qrcode.server.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "user_token")
@Data
@AllArgsConstructor  // 有参构造
@NoArgsConstructor  // 无参构造
public class UserTokenDo implements Serializable {

    /** 主键-id uuid */
    @Id  //此备注代表该字段为该类的主键
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid",strategy = "uuid")
    @Column(name="uuid",length=32) //name - 指定对应列的名称 ,length - 最大长度
    private String uuid;

    @Column(name = "user_id", nullable=false, unique=true)
    private String userId;

    @Column(name = "login_time")
    private String loginTime;

    @Column(name = "create_time")
    private Date createTime;

    @Column
    private String state;

}
