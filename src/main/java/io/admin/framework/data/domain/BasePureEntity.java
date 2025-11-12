package io.admin.framework.data.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.admin.framework.data.DBConstants;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"}, ignoreUnknown = true)
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public abstract class BasePureEntity  {




    @Id
    @Column(length = DBConstants.LEN_ID)
    private String id;


    @CreatedDate
    @Column(updatable = false)
    private Date createTime;




    @LastModifiedDate
    private Date updateTime;



    /// ===== 乐观锁字段 =====
    @org.springframework.data.annotation.Version
    @Column(columnDefinition = "bigint default 0") // 建议：仅用于提示数据库建表时设置默认值
    private Integer lockVersion;

}
