/**
 * Copyright (C) 2010.
 * Olaf Bergner.
 * Hamburg, Germany. olaf.bergner@gmx.de
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package net.camelpe.extension.camel.typeconverter;

import javax.inject.Inject;

import org.apache.camel.Converter;

/**
 * <p>
 * TODO: Insert short summary for
 * InstanceMethodTypeConverterHavingOneInjectionPoint
 * </p>
 * 
 * @author <a href="mailto:olaf.bergner@saxsys.de">Olaf Bergner</a>
 * 
 */
@Converter
public class InstanceMethodTypeConverterHavingOneInjectionPoint {

    @Inject
    private StringToByteArray stringToByteArray;

    @Converter
    public byte[] convertToByteArray(final String stringToConvert) {
        return this.stringToByteArray.toByteArray(stringToConvert);
    }
}
