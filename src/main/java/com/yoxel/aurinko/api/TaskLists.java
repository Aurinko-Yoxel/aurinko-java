package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.EntitySupport_TokenBased;
import com.yoxel.aurinko.bean.AurTasklist;
import com.yoxel.aurinko.bean.AurTasklistsPage;
import com.yoxel.aurinko.http.HttpImpl;

/**
 * TaskList API: /tasklists
 */
public class TaskLists extends EntitySupport_TokenBased<AurTasklist, String, AurTasklistsPage, AurTasklist> {

    private final HttpImpl httpImpl;

    public TaskLists(HttpImpl httpImpl) {
        super("/tasklists", AurTasklist.class, AurTasklistsPage.class, AurTasklist.class, httpImpl);
        this.httpImpl = httpImpl;
    }

    @Override
    public String normalizeId(String id) {
        return id == null ? "default" : id;
    }

    public TasklistEntries tasklistEntries(String tasklistId) {
        return new TasklistEntries(normalizeId(tasklistId), httpImpl);
    }
}
