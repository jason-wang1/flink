/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.table.planner.plan.nodes.exec.common;

import org.apache.flink.api.dag.Transformation;
import org.apache.flink.streaming.api.transformations.UnionTransformation;
import org.apache.flink.table.data.RowData;
import org.apache.flink.table.planner.delegation.PlannerBase;
import org.apache.flink.table.planner.plan.nodes.exec.ExecEdge;
import org.apache.flink.table.planner.plan.nodes.exec.ExecNode;
import org.apache.flink.table.planner.plan.nodes.exec.ExecNodeBase;
import org.apache.flink.table.types.logical.RowType;

import java.util.ArrayList;
import java.util.List;

/**
 * Base {@link ExecNode} that is not a physical node and just union the inputs' records into one
 * node.
 */
public abstract class CommonExecUnion extends ExecNodeBase<RowData> {

    public CommonExecUnion(List<ExecEdge> inputEdges, RowType outputType, String description) {
        super(inputEdges, outputType, description);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Transformation<RowData> translateToPlanInternal(PlannerBase planner) {
        final List<Transformation<RowData>> inputTransforms = new ArrayList<>();
        for (ExecNode<?> input : getInputNodes()) {
            inputTransforms.add((Transformation<RowData>) input.translateToPlan(planner));
        }
        return new UnionTransformation(inputTransforms);
    }
}
