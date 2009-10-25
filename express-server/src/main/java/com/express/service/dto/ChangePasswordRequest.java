package com.express.service.dto;

import java.io.Serializable;


public class ChangePasswordRequest implements Serializable {
   private static final long serialVersionUID = -1042427196078479892L;

   private Long userId;
   
   private String oldPassword;
   
   private String newPassword;

   
   public Long getUserId() {
      return userId;
   }

   
   public void setUserId(Long userId) {
      this.userId = userId;
   }

   
   public String getOldPassword() {
      return oldPassword;
   }

   
   public void setOldPassword(String oldPassword) {
      this.oldPassword = oldPassword;
   }

   
   public String getNewPassword() {
      return newPassword;
   }

   
   public void setNewPassword(String newPassword) {
      this.newPassword = newPassword;
   }

}
