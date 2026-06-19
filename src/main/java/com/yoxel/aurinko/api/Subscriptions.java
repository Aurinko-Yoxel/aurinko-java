package com.yoxel.aurinko.api;

import com.yoxel.aurinko.apis.EntitySupport_OffsetBased;
import com.yoxel.aurinko.bean.AurSubscription;
import com.yoxel.aurinko.bean.AurSubscriptionsPage;
import com.yoxel.aurinko.http.HttpImpl;

public class Subscriptions
        extends EntitySupport_OffsetBased<AurSubscription, Long, AurSubscriptionsPage, AurSubscription> {

    public Subscriptions(HttpImpl httpImpl) {
        super(
                "/subscriptions",
                AurSubscription.class,
                AurSubscriptionsPage.class,
                AurSubscription.class,
                httpImpl
        );
    }
}
