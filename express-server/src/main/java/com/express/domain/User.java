package com.express.domain;

import java.util.*;

import javax.persistence.*;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.hibernate.annotations.OptimisticLock;

import com.express.security.Role;

/**
 * The core actor in the Express system.
 * @author Adam Boas
 *
 */
@Entity
@Table(name = "express_user")
@NamedQueries({
   @NamedQuery(name = "User.findByUsername", query = "SELECT U FROM User U WHERE LOWER(U.email) = :email")
})
public class User implements Persistable, UserDetails, Comparable<User>
{
   private static final long serialVersionUID = -7572241766772304908L;
   
   public static final String QUERY_FIND_BY_USERNAME = "User.findByUsername";
   
   @Id @GeneratedValue(strategy = GenerationType.TABLE, generator = "gen_user")
   @TableGenerator(name = "gen_user", table = "sequence_list", pkColumnName = "name",
            valueColumnName = "next_value", allocationSize = 1, initialValue = 100,
            pkColumnValue = "express_user")
   @Column(name="user_id")
   private Long id;
   
   @Version @Column(name="version_no")
   private Long version;
   
   @Column(name="created_date") @Temporal(value = TemporalType.TIMESTAMP)
   private Calendar createdDate;
   
   @Column(name="email", unique = true, nullable = false)
   private String email;

   @Column(name = "f_name")
   private String firstName;
   
   @Column(name = "l_name")
   private String lastName;

   @Column(name = "password")
   private String password;
   
   @Column(name = "passwd_hint")
   private String passwordHint;
   
   @Column(name = "phone1")
   private String phone1;
   
   @Column(name = "phone2")
   private String phone2;
   
   @Column(name="active")
   private Boolean active = false;
   
   @Column(name="colour")
   private Integer colour;

   @OneToMany(mappedBy = "requestor")
   @OptimisticLock(excluded = true)
   private Set<AccessRequest> accessRequests;

   public User() {
      this.accessRequests = new HashSet<AccessRequest>();
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Calendar getCreatedDate() {
      return createdDate;
   }

   public void setCreatedDate(Calendar createdDate) {
      this.createdDate = createdDate;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPasswordHint() {
      return passwordHint;
   }

   public void setPasswordHint(String passwordHint) {
      this.passwordHint = passwordHint;
   }

   public String getPhone1() {
      return phone1;
   }

   public void setPhone1(String phone1) {
      this.phone1 = phone1;
   }

   public String getPhone2() {
      return phone2;
   }

   public void setPhone2(String phone2) {
      this.phone2 = phone2;
   }

   public void setActive(Boolean active) {
      this.active = active;
   }

   public Boolean getActive() {
      return active;
   }
   
   
   public Integer getColour() {
      return colour;
   }

   
   public void setColour(Integer colour) {
      this.colour = colour;
   }

   public List<AccessRequest> getAccessRequests() {
      List<AccessRequest> requestList = new ArrayList<AccessRequest>(accessRequests);
      Collections.sort(requestList);
      return requestList;
   }

   public void setAccessRequests(List<AccessRequest> requestList) {
      this.accessRequests.clear();
      if(requestList != null) {
         this.accessRequests.addAll(requestList);
      }
   }

   public void addAccessRequest(AccessRequest accessRequest) {
      this.accessRequests.add(accessRequest);
      accessRequest.setRequestor(this);
   }

   public void removeAccessRequest(AccessRequest accessRequest) {
      this.accessRequests.remove(accessRequest);
      accessRequest.setRequestor(null);
   }

   public String getFullName() {
      if(StringUtils.hasText(firstName) || StringUtils.hasText(lastName)) {
         return firstName + " " + lastName;
      }
      return email.substring(0, email.indexOf('@'));
   }

   public boolean hasPendingRequest(Project project) {
      for(AccessRequest request : accessRequests) {
         if(project.equals(request.getProject()) && request.getStatus().equals(AccessRequest.UNRESOLVED)) {
            return true;
         }
      }
      return false;
   }

   public int compareTo(User user) {
      return this.getFullName().compareTo(user.getFullName());
   }

   @Override
   public boolean equals(Object obj) {
      if (obj == this)
         return true;
      if (this.id == null || !(obj instanceof User))
         return false;
      User user = (User) obj;
      return this.id.equals(user.getId());
   }

   @Override
   public int hashCode() {
      return this.id == null ? super.hashCode() : this.id.hashCode();
   }

   @Override
   public String toString() {
      StringBuilder output = new StringBuilder();
      output.append(User.class.getName());
      output.append("[");
      output.append("id=").append(id).append(",");
      output.append("version=").append(version).append(",");
      output.append("email=").append(email).append(",");
      output.append("firstName=").append(firstName).append(",");
      output.append("lastName=").append(lastName).append(",");
      output.append("phone1=").append(phone1).append(",");
      output.append("phone2=").append(phone2).append(",");
      output.append("createdDate=").append(createdDate).append("]");
      return output.toString();
   }
   
   public void setGrantedAuthorities() {
      
   }
   
   //UserDetails implementation

   public GrantedAuthority[] getAuthorities() {
      GrantedAuthority authority = new GrantedAuthorityImpl(Role.USER.getCode());
      return new GrantedAuthority[]{authority};
   }

   public String getUsername() {
      return email;
   }

   public boolean isAccountNonExpired() {
      return true;
   }

   public boolean isAccountNonLocked() {
      return true;
   }

   public boolean isCredentialsNonExpired() {
      return active;
   }

   public boolean isEnabled() {
      return active;
   }
}
