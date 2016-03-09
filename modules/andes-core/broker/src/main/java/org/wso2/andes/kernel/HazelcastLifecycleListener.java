/*
 * Copyright (c) 2016, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.andes.kernel;

import com.hazelcast.core.LifecycleEvent;
import com.hazelcast.core.LifecycleListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.andes.server.cluster.coordination.hazelcast.HazelcastAgent;

/**
 * Hazelcast Lifecycle events are monitored through this listener. MB state and Hazelcast data structures are updated
 * appropriately depending on the {LifecycleEvent}
 */
public class HazelcastLifecycleListener implements LifecycleListener {

    private static Log log = LogFactory.getLog(HazelcastLifecycleListener.class);

    /**
     * On {MERGED} event all the topic listeners for the local node is added back. Since the data structures except for
     * IMaps are not merged after a split brain scenario within Hazelcast (data structures from MERGED nodes are
     * discarded)
     * @param lifecycleEvent {LifecycleEvent}
     */
    @Override
    public void stateChanged(LifecycleEvent lifecycleEvent) {
        log.info("Hazelcast instance lifecycle changed state to " + lifecycleEvent.getState());
        if (lifecycleEvent.getState() == LifecycleEvent.LifecycleState.MERGED) {
            log.info("Hazelcast cluster merge detected after a split brain. Updating unmerged data structures");
            HazelcastAgent.getInstance().addTopicListeners();
        }
    }
}
