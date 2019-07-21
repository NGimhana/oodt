/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership.  The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.oodt.cas.wmservices.resources;


import org.apache.oodt.cas.workflow.structs.WorkflowInstance;

import javax.xml.bind.annotation.*;


/**
 * A JAX-RS resource representing a {@link WorkflowInstance}.
 *
 * @author ngimhana (Nadeeshan Gimhana)
 */
@XmlRootElement(name = "workflowInstance")
@XmlType(propOrder = {"workflowInstanceId", "currentTaskId", "startDate",
        "endDate", "timesBlocked"})
@XmlAccessorType(XmlAccessType.NONE)
public class WorkflowInstanceResource {
    private WorkflowInstance workflowInstance;
    private String workflowInstanceId;
    private String currentTaskId;
    private String startDate;
    private String endDate;
    private int timesBlocked;

    /**
     * Default constructor required by JAXB.
     */
    public WorkflowInstanceResource() {
    }


    /**
     * Constructor that sets the workflowInstance to JAXRS resource.
     *
     * @param workflowInstance the workflowInstance associated with the resource
     */
    public WorkflowInstanceResource(WorkflowInstance workflowInstance) {
        this.workflowInstance = workflowInstance;
        this.workflowInstanceId = workflowInstance.getId();
        this.currentTaskId = workflowInstance.getCurrentTaskId();
        this.startDate = workflowInstance.getStartDate().toString();
        this.endDate = workflowInstance.getEndDate().toString();
        this.timesBlocked = workflowInstance.getTimesBlocked();
    }


    @XmlElement(name = "workflowInstanceId")
    public String getWorkflowInstanceId() {
        return workflowInstanceId;
    }

    @XmlElement(name = "currentTaskId")
    public String getCurrentTaskId() {
        return currentTaskId;
    }

    @XmlElement(name = "startDate")
    public String getStartDate() {
        return startDate;
    }

    @XmlElement(name = "endDate")
    public String getEndDate() {
        return endDate;
    }

    @XmlElement(name = "timesBlocked")
    public int getTimesBlocked() {
        return timesBlocked;
    }
}
