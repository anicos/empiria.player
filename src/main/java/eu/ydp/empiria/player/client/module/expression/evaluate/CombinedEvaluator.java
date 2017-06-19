/*
 * Copyright 2017 Young Digital Planet S.A.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package eu.ydp.empiria.player.client.module.expression.evaluate;

import com.google.inject.Inject;
import eu.ydp.empiria.player.client.module.expression.model.ExpressionBean;

public class CombinedEvaluator implements Evaluator {

    private ExpressionEvaluator expressionEvaluator;
    private CommutationEvaluator commutationEvaluator;

    @Inject
    public CombinedEvaluator(ExpressionEvaluator expressionEvaluator, CommutationEvaluator commutationEvaluator) {
        this.expressionEvaluator = expressionEvaluator;
        this.commutationEvaluator = commutationEvaluator;
    }

    @Override
    public boolean evaluate(ExpressionBean bean) {
        return expressionEvaluator.evaluate(bean) && commutationEvaluator.evaluate(bean);
    }

}