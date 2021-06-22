package com.utn.udee.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.AccessType;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "userType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Client.class, name = "CLIENT"),
        @JsonSubTypes.Type(value = Employee.class, name = "EMPLOYEE")
})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "users")
@DiscriminatorColumn(name = "user_type")
public abstract class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "The dni must be specified. It cannot be null or whitespace.")
    @Column
    private String dni;

    @NotBlank(message = "The firstname must be specified. It cannot be null or whitespace.")
    @Column
    private String firstname;

    @NotBlank(message = "The lastname must be specified. It cannot be null or whitespace.")
    @Column
    private String lastname;

    @NotBlank(message = "The username must be specified. It cannot be null or whitespace.")
    @Column
    private String username;

    @NotBlank(message = "The password must be specified. It cannot be null or whitespace.")
    @Column(name = "u_password")
    private String password;

    @AccessType(AccessType.Type.PROPERTY)
    public abstract UserType userType();

}
