package com.yoxel.aurinko;

import com.yoxel.aurinko.bean.AurCompaniesPage;
import com.yoxel.aurinko.bean.AurCompany;
import com.yoxel.aurinko.bean.AurContact;
import com.yoxel.aurinko.bean.AurContactsPage;
import com.yoxel.aurinko.bean.AurLiveIdEntity;
import com.yoxel.aurinko.bean.AurQueryResult;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 *
 */
@EqualsAndHashCode
@ToString
@RequiredArgsConstructor
public final class AurinkoClass<Entity extends AurLiveIdEntity, Page extends AurQueryResult<Entity>> {

  public final String name;
  public final Class<Entity> entityClass;
  public final Class<Page> pageClass;

  public static final AurinkoClass<AurCompany, AurCompaniesPage> COMPANY =
      new AurinkoClass<>("company", AurCompany.class, AurCompaniesPage.class);

  public static final AurinkoClass<AurContact, AurContactsPage> CONTACT =
      new AurinkoClass<>("contact", AurContact.class, AurContactsPage.class);

}
