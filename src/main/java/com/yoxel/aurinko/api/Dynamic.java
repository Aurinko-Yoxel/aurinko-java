package com.yoxel.aurinko.api;

import com.yoxel.aurinko.AurinkoClass;
import com.yoxel.aurinko.apis.EntitySupport_TokenBased;
import com.yoxel.aurinko.apis.QueryParams;
import com.yoxel.aurinko.bean.AurLiveIdEntity;
import com.yoxel.aurinko.bean.AurTokenPage;
import com.yoxel.aurinko.http.HttpImpl;
import com.yoxel.commons.xstream.IOXStream;
import com.yoxel.commons.xstream.XStream;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.function.Consumer;

public class Dynamic<Entity extends AurLiveIdEntity, Page extends AurTokenPage<Entity>>
        extends EntitySupport_TokenBased<Entity, String, Page, Entity> {

    public Dynamic(AurinkoClass<Entity, Page> aurClass, Integer apiConfId, HttpImpl httpImpl) {
        super(
                "/dynamic/" + (apiConfId == null ? "default" : apiConfId) +
                        "/objects/" + aurClass.name, aurClass.entityClass, aurClass.pageClass, aurClass.entityClass, httpImpl);
    }

    public String entityFunctionPath(String pathFunction) {
        if (StringUtils.isBlank(pathFunction)) {
            return entityPath();
        }

        int pos = entityPath().indexOf("/objects/");
        return entityPath().substring(0, pos) + pathFunction + entityPath().substring(pos + 8);
    }

    public Page loadFunctionPage(String pathFunction, QueryParams query, String pageToken) throws IOException {

        return httpGet(
                entityFunctionPath(pathFunction),
                query.add("pageToken", pageToken)
        ).parseAs(entityPageClass());
    }

    public XStream<Entity, IOException> streamFunctionPaged(String pathFunction, QueryParams queryParams)
            throws IOException {
        return streamFunctionPaged(pathFunction, queryParams, null);
    }

    public XStream<Entity, IOException> streamFunctionPaged(String pathFunction, Consumer<? super Page> onPage)
            throws IOException {
        return streamFunctionPaged(pathFunction, QueryParams.EMPTY, onPage);
    }

    public XStream<Entity, IOException> streamFunctionPaged(String pathFunction, QueryParams queryParams,
                                                            Consumer<? super Page> onPage) throws IOException {

        if (onPage == null) {
            onPage = v -> {
            };
        }

        // query pages, until we get a page with done=true | totalSize=0
        return IOXStream
                .iterateUntil(
                        loadFunctionPage(pathFunction, queryParams, null),
                        qr -> loadFunctionPage(pathFunction, queryParams, qr.getNextPageToken()),
                        qr -> qr.getNextPageToken() == null
                )
                .peek(onPage)
                .map(Page::getRecords)
                .flatMap(IOXStream::of);
    }

}
