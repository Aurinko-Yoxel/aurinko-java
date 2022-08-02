package com.yoxel.aurinko.bean;

import com.google.api.client.util.Key;

import com.yoxel.aurinko.bean.sub.PhoneNumber;
import com.yoxel.aurinko.bean.sub.PostalAddress;
import com.yoxel.aurinko.bean.sub.Url;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class AurCompany extends AurLiveIdEntity {

  @Key
  private String name;

  @Key
  private String type;

  @Key
  private String industry;

  @Key
  private String aboutUs;

  @Key
  private String description;

  @Key
  private String domain;

  @Key
  private PostalAddress address;

  @Key
  private Url website;

  @Key
  private PhoneNumber phone;

  @Key
  private String timezone;

  @Key
  private Integer numberOfEmployees;
}
