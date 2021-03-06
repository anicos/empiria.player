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

package eu.ydp.empiria.player.client.module.expression;

import com.google.inject.Inject;
import eu.ydp.empiria.player.client.controller.item.ItemResponseManager;
import eu.ydp.empiria.player.client.controller.variables.objects.response.Response;
import eu.ydp.empiria.player.client.module.expression.model.ExpressionBean;
import eu.ydp.gwtutil.client.debug.gwtlogger.Logger;

import java.util.List;

public class ExpressionToResponseConnector {

    private static final Logger LOGGER = new Logger();
    private IdentifiersFromExpressionExtractor identifiersFromExpressionExtractor;

    @Inject
    public ExpressionToResponseConnector(IdentifiersFromExpressionExtractor identifiersFromExpressionExtractor) {
        this.identifiersFromExpressionExtractor = identifiersFromExpressionExtractor;
    }

    public void connectResponsesToExpression(ExpressionBean expressionBean, ItemResponseManager responseManager) {
        String template = expressionBean.getTemplate();
        List<String> identifiers = identifiersFromExpressionExtractor.extractResponseIdentifiersFromTemplate(template);

        for (String responseId : identifiers) {
            Response response = responseManager.getVariable(responseId);

            if (response == null) {
                String message = "Expression: " + template + " is using identifier: " + responseId + " that is not existing in responsesMap!";
                LOGGER.info(message);
            } else {
                expressionBean.getResponses().add(response);
                response.setExpression(expressionBean);
            }
        }
    }

}
