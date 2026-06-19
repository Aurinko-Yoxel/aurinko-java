package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.EntitySupport_TokenBased;
import com.yoxel.aurinko.apis.SyncSupport;
import com.yoxel.aurinko.bean.AurTask;
import com.yoxel.aurinko.bean.AurTaskSaveResult;
import com.yoxel.aurinko.bean.AurTasksPage;
import com.yoxel.aurinko.http.HttpImpl;
import com.yoxel.commons.xstream.XStream;

import java.io.IOException;

/**
 * Tasks API: /tasklists/:id/tasks
 */
public class TasklistEntries
        extends EntitySupport_TokenBased<AurTask, String, AurTasksPage, AurTaskSaveResult>
        implements SyncSupport<AurTask, AurTasksPage> {

    private final String tasklistId;
    private final HttpImpl httpImpl;

    TasklistEntries(String tasklistId, HttpImpl httpImpl) {
        this(tasklistId, "", httpImpl);
    }

    private TasklistEntries(String tasklistId, String postfix, HttpImpl httpImpl) {
        super("/tasklists/" + tasklistId + "/tasks" + postfix,
                AurTask.class, AurTasksPage.class, AurTaskSaveResult.class, httpImpl);
        this.tasklistId = tasklistId;
        this.httpImpl = httpImpl;
    }

    @Override
    public String syncRootPath() {
        return "/tasklists/" + tasklistId;
    }

    public XStream<AurTask, IOException> streamTasks()
            throws IOException {

        return new TasklistEntries(tasklistId, "", httpImpl).streamPaged();
    }
}
