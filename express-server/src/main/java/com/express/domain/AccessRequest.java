package com.express.domain;

import javax.persistence.*;
import java.util.Calendar;

/**
 * An AccessRequest models a user's request to join a Project. It effectively has 2 statuses. If it is approved
 * then the request is removed and a ProjectWorker recod is written. If it is rejected, however, we keep it to
 * stop Users making multiple requests to access a project.
 */
@Entity
@Table(name = "ACCESS_REQUEST")
public class AccessRequest implements Persistable, Comparable<AccessRequest> {
   private static final long serialVersionUID = 5229139735357304608L;

   public static final Integer UNRESOLVED = 0;
   public static final Integer APPROVED = 1;
   public static final Integer REJECTED = 2;

   @Id
   @GeneratedValue(strategy = GenerationType.TABLE, generator = "GEN_REQUEST")
   @TableGenerator(name = "GEN_REQUEST", table = "SEQUENCE_LIST", pkColumnName = "NAME",
            valueColumnName = "NEXT_VALUE", allocationSize = 1, initialValue = 100,
            pkColumnValue = "BACKLOG")
   @Column(name="REQUEST_ID")
   private Long id;

   @Version @Column(name="VERSION_NO")
   private Long version;

   @ManyToOne @JoinColumn(name = "USER_ID")
   private User requestor;

   @Column(name = "REQUEST_DATE")
   @Temporal(value = TemporalType.TIMESTAMP)
   private Calendar requestDate;

   @Column(name = "RESOLVED_DATE")
   @Temporal(value = TemporalType.TIMESTAMP)
   private Calendar resolvedtDate;

   @Column(name = "STATUS")
   private Integer status;

   @Column(name = "REASON")
   private String reason;

   @ManyToOne @JoinColumn(name = "PROJECT_ID")
   private Project project;

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public User getRequestor() {
      return requestor;
   }

   public void setRequestor(User requestor) {
      this.requestor = requestor;
   }

   public Calendar getRequestDate() {
      return requestDate;
   }

   public void setRequestDate(Calendar requestDate) {
      this.requestDate = requestDate;
   }

   public Calendar getResolvedtDate() {
      return resolvedtDate;
   }

   public void setResolvedtDate(Calendar resolvedtDate) {
      this.resolvedtDate = resolvedtDate;
   }

   public Integer getStatus() {
      return status;
   }

   public void setStatus(Integer status) {
      this.status = status;
   }

   public String getReason() {
      return reason;
   }

   public void setReason(String reason) {
      this.reason = reason;
   }

   public Project getProject() {
      return project;
   }

   public void setProject(Project project) {
      this.project = project;
   }

   public int compareTo(AccessRequest accessRequest) {
      return this.requestDate.compareTo(accessRequest.getRequestDate());
   }
}
