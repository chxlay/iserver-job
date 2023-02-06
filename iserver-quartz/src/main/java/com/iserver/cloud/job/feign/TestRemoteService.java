package com.iserver.cloud.job.feign;

import com.iserver.common.core.result.R;

/**
 * @author Alay
 * @date 2023-02-05 09:13
 */
public interface TestRemoteService {

    /**
     * 示例feign调用
     *
     * @return
     */
    R remoteTestCall();

}
