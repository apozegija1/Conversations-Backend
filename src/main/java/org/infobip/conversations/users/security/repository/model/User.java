package org.infobip.conversations.users.security.repository.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.infobip.conversations.company.repository.model.Company;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
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

   @JsonIgnore
   @Column(name = "password", length = 100)
   @NotNull
   @Size(min = 4, max = 100)
   private String password;

   @Column(name = "first_name", length = 50)
   @NotNull
   @Size(min = 4, max = 50)
   private String firstname;

   @Column(name = "last_name", length = 50)
   @NotNull
   @Size(min = 4, max = 50)
   private String lastname;

   @Column(name = "email", length = 50)
   @NotNull
   @Size(min = 4, max = 50)
   private String email;

   @Column(name = "phone", length = 50)
   @Size(min = 4, max = 50)
   @Null
   private String phone;

   @Column(name = "gender", length = 50)
   @Null
   @Size(min = 4, max = 50)
   private String gender;

   @OneToOne(cascade = CascadeType.ALL)
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

   public String getPassword() {
      return password;
   }

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
}
