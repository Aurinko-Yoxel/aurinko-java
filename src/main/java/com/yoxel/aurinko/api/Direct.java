package com.yoxel.aurinko.api;

import com.yoxel.aurinko.http.HttpApiSupport;
import com.yoxel.aurinko.http.HttpImpl;

public class Direct extends HttpApiSupport {

    public Direct(HttpImpl httpImpl) {
        super(httpImpl);
    }

    @Override
    protected String basePath() {
        return "/direct";
    }
}
