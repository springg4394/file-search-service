package com.file.search.service.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class MemberEntity {

    @Id
    @Column(name = "member_id", columnDefinition = "varchar(100)", nullable = false)
    private String memberId;

    @Column(name = "member_name", columnDefinition = "varchar(300)", nullable = false)
    private String memberName;

    @Column(name = "member_password", columnDefinition = "varchar(200)", nullable = false)
    private String memberPassword;

    @Column(name = "admin_yn", nullable = false)
    private String adminYn = "N";

    @Column(name = "register_id", columnDefinition = "varchar(100)", nullable = false, updatable = false)
    private String registerId;

    @CreatedDate
    @Column(name = "register_datetime", nullable = false, updatable = false)
    private LocalDateTime registerDatetime;

    @Column(name = "modify_id", columnDefinition = "varchar(100)")
    private String modifyId;

    @LastModifiedDate
    @Column(name = "modify_datetime")
    private LocalDateTime modifyDatetime;

    @Builder
    public MemberEntity(String memberId, String memberName, String memberPassword, String adminYn, String registerId, String modifyId) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.memberPassword = memberPassword;
        this.adminYn = adminYn != null ? adminYn : "N";  
        this.registerId = registerId;
        this.modifyId = modifyId;
    }

}
