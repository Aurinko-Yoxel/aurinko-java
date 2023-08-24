package com.yoxel.aurinko.bean.sub;

/**
 *
 */
public interface EventParticipant {

  String getId();

  void setId(String id);

  EmailAddress getEmailAddress();

  void setEmailAddress(EmailAddress emailAddress);

  default boolean sameParticipant(EventParticipant other) {
    return
        this.getId() != null && other.getId() != null && this.getId().equals(other.getId()) ||

            this.getEmailAddress() != null && this.getEmailAddress().getAddress() != null &&
                other.getEmailAddress() != null && other.getEmailAddress().getAddress() != null &&
                this.getEmailAddress().getAddress().equalsIgnoreCase(other.getEmailAddress().getAddress());
  }
}
