package org.infobip.conversations.users.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.Nullable;
import org.hibernate.annotations.BatchSize;
import org.infobip.conversations.common.Constant;
import org.infobip.conversations.common.validators.ValidPassword;
import org.infobip.conversations.companies.repository.model.Company;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Cacheable(false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "users")
public class User {

   @Id
   @Column(name = "id")
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "username", length = 50, unique = true)
   @NotNull
   @Size(min = 4, max = 50)
   private String username;

   @ValidPassword
   @Column(name = "password", length = 100)
   @NotNull
   @Size(min = 8, max = 100)
   private String password;

   @Column(name = "first_name", length = 20)
   @NotNull
   @Size(min = 3, max = 20)
   private String firstname;

   @Column(name = "last_name", length = 20)
   @NotNull
   @Size(min = 3, max = 20)
   private String lastname;

   @Email
   @Column(name = "email", length = 50)
   @NotNull
   @Size(min = 3, max = 50)
   private String email;

   @Column(name = "phone", length = 50)
   @Nullable
   @Size(min = 3, max = 50)
   private String phone;

   @Column(name = "gender", length = 50)
   @Nullable
   @Size(min = 3, max = 50)
   private String gender;

   // Was getting some error with returning company in json serializer
   // https://stackoverflow.com/questions/24994440/no-serializer-found-for-class-org-hibernate-proxy-pojo-javassist-javassist/52148725
   @OneToOne
   @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
   @JoinColumn(name = "company_id", referencedColumnName = "id")
   private Company company;

   @JsonIgnore
   @Column(name = "activated")
   @NotNull
   private boolean activated;

   @ManyToMany
   @JoinTable(
      name = "userroles",
      joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
   @BatchSize(size = 20)
   private Set<Role> roles = new HashSet<>();

   @CreatedDate
   @JsonFormat(shape=JsonFormat.Shape.STRING, pattern= Constant.TIMESTAMP_FORMAT, timezone="Europe/Berlin")
   @Column(name = "created_at", nullable = false, updatable = false)
   private Timestamp createdAt;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }
   // Ignore password when we return user as we don't want to expose password hash to requests by user
   @JsonIgnore
   public String getPassword() {
      return password;
   }
   // Add ability for user to send password
   @JsonProperty
   public void setPassword(String password) {
      this.password = password;
   }

   public String getFirstname() {
      return firstname;
   }

   public void setFirstname(String firstname) {
      this.firstname = firstname;
   }

   public String getLastname() {
      return lastname;
   }

   public void setLastname(String lastname) {
      this.lastname = lastname;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public boolean isActivated() {
      return activated;
   }

   public void setActivated(boolean activated) {
      this.activated = activated;
   }

   public Set<Role> getRoles() {
      return roles;
   }

   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return id.equals(user.id);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   @Override
   public String toString() {
      return "User{" +
         "username='" + username + '\'' +
         ", password='" + password + '\'' +
         ", firstname='" + firstname + '\'' +
         ", lastname='" + lastname + '\'' +
         ", email='" + email + '\'' +
         ", activated=" + activated +
         '}';
   }

   public String getPhone() {
      return phone;
   }

   public void setPhone(String phone) {
      this.phone = phone;
   }

   public String getGender() {
      return gender;
   }

   public void setGender(String gender) {
      this.gender = gender;
   }

   public Company getCompany() {
      return company;
   }

   public void setCompany(Company company) {
      this.company = company;
   }

   public String getFullName() {
      return this.firstname + " " + this.lastname;
   }
}
