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

package eu.ydp.empiria.player.client.module.expression.adapters;

import com.google.inject.Inject;

import java.util.Map;

public class DefaultExpressionCharactersAdapter {

    private final ExpressionCharacterMappingProvider replacementsProvider;

    @Inject
    public DefaultExpressionCharactersAdapter(ExpressionCharacterMappingProvider replacementsProvider) {
        this.replacementsProvider = replacementsProvider;
    }

    public String process(String expression) {
        expression = fixDivide(expression);
        expression = fixComma(expression);
        expression = fixReplacements(expression);
        return expression;
    }

    private String fixReplacements(String expression) {
        Map<String, String> replacements = replacementsProvider.getMapping();
        for (String from : replacements.keySet()) {
            String to = replacements.get(from);
            expression = expression.replace(from, to);
        }
        return expression;
    }

    private String fixDivide(String expression) {
        expression = expression.replaceAll(":", "/");
        return expression;
    }

    private String fixComma(String expression) {
        expression = expression.replaceAll(",", ".");
        return expression;
    }

}