package com.yoxel.aurinko.extuser;

import com.yoxel.aurinko.AurinkoService;
import com.yoxel.commons.xstream.XStream;

import java.io.IOException;
import java.util.Date;

/**
 *
 */
public interface AurExtUserInfoProvider {

  interface Factory {

    AurExtUserInfoProvider createProvider(AurinkoService aurSvc, String extOrgId, String extUserId);
  }

  AurExtUser getMyInfo() throws IOException;

  AurExtUser lookupUser(String xid) throws IOException;

  XStream<AurExtUser, IOException> loadUsers(Date modifiedSince) throws IOException;
}
