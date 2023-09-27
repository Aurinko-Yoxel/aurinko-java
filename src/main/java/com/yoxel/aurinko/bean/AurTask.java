package com.yoxel.aurinko.bean;

import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

import com.yoxel.aurinko.bean.sub.TaskLink;

import java.util.List;

import lombok.Data;

import static org.apache.commons.lang3.BooleanUtils.isTrue;

@Data
public class AurTask extends AurLiveIdEntity {

  @Key // ("folderId")
  private String tasklistId;

  @Key
  private String parentId;

  @Key
  private String previousId;

  @Key
  private String position;

  @Key
  private String owner;

  @Key
  private String title;

  @Key
  private String notes;

  @Key
  private String status;

  @Key
  private String importance;

  @Key
  private DateTime startDateTime;

  @Key("due")
  private DateTime dueDateTime;

  @Key
  private DateTime completed;

  @Key
  private List<String> categories;

  @Key
  private List<TaskLink> links;

}
