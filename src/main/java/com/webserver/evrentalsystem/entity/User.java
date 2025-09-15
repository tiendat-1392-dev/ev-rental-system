package com.webserver.evrentalsystem.entity;

import com.webserver.evrentalsystem.jpaconverter.GenderJpaConverter;
import com.webserver.evrentalsystem.jpaconverter.MembershipClassJpaConverter;
import com.webserver.evrentalsystem.jpaconverter.RoleJpaConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName", nullable = false, unique = true)
    private String userName;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "luckyWheelSpin", columnDefinition = "integer default 0")
    private int luckyWheelSpin;

    @Column(name = "userPublicName", length = 20)
    private String userPublicName;

    @Column(name = "realName", length = 20)
    private String realName;

    @Column(name = "citizenIdentityCard")
    private String citizenIdentityCard;

    @Column(name = "avatar")
    private String avatar;

    @Convert(converter = RoleJpaConverter.class)
    @Column(name = "role", nullable = false)
    private Role role;

    @Convert(converter = MembershipClassJpaConverter.class)
    @Column(name = "membershipClass", nullable = false)
    private MembershipClass membershipClass;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "dateOfBirth")
    private Long dateOfBirth;

    @Convert(converter = GenderJpaConverter.class)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "createdAt")
    private Long createdAt;

    @Column(name = "createdBy")
    private String createdBy;

    @Column(name = "refreshToken")
    private String refreshToken;
}
