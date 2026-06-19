package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.EntitySupport_TokenBased;
import com.yoxel.aurinko.apis.SyncSupport;
import com.yoxel.aurinko.bean.AurContact;
import com.yoxel.aurinko.bean.AurContactSaveResult;
import com.yoxel.aurinko.bean.AurContactsPage;
import com.yoxel.aurinko.http.HttpImpl;

/**
 * Contact API: /contacts
 */
public class Contacts
        extends EntitySupport_TokenBased<AurContact, String, AurContactsPage, AurContactSaveResult>
        implements SyncSupport<AurContact, AurContactsPage> {

    public Contacts(HttpImpl httpImpl) {
        super("/contacts", AurContact.class, AurContactsPage.class, AurContactSaveResult.class, httpImpl);
    }

    @Override
    public String syncRootPath() {
        return "/contacts";
    }
}
